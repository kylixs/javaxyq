/*
 * JavaXYQ Engine 
 * 
 * javaxyq@2008 all rights. 
 * http://www.javaxyq.com
 */

package com.javaxyq.resources;

import java.awt.Component;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.Toolkit;
import java.io.ByteArrayOutputStream;
import java.io.EOFException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

import com.javaxyq.config.MapConfig;
import com.javaxyq.core.DataManager;
import com.javaxyq.core.DataStore;
import com.javaxyq.data.Scene;
import com.javaxyq.io.CacheManager;
import com.javaxyq.widget.TileMap;

/**
 * @author 龚德伟
 * @history 2008-5-29 龚德伟 新建
 */
public class DefaultTileMapProvider implements MapProvider {

    public class ImageLoadThread extends Thread {

        protected Component component = new Component() {
        };

        private Image image;

        private boolean isCompleted;

        private boolean isFinished;

        private int mediaTrackerID;

        protected MediaTracker tracker = new MediaTracker(component);

        public ImageLoadThread() {
            setDaemon(true);
            setName("ImageLoadThread");
        }

        /**
         * Returns an ID to use with the MediaTracker in loading an image.
         */
        private int getNextID() {
            return ++mediaTrackerID;
        }

        public boolean isCompleted() {
            return isCompleted;
        }

        public boolean isFinished() {
            return isFinished;
        }

        public void run() {
            while (true) {
            	//System.out.println(this.getId()+" "+this.getName());
                synchronized (this) {
                    if (image != null) {
                        // load image
                        isFinished = false;
                        isCompleted = false;
                        int id = getNextID();
                        tracker.addImage(image, id);
                        try {
                            tracker.waitForID(id, 0);
                            isCompleted = true;
                        } catch (InterruptedException e) {
                            System.out.println("INTERRUPTED while loading Image");
                        }
                        tracker.removeImage(image, id);
                        isFinished = true;
                        image = null;
                        notifyAll();
                    }
                    try {
                        this.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        public void loadImage(Image image) {
            this.image = image;
            synchronized (this) {
                notifyAll();
            }
        }
    }

    class MyRandomAccessFile extends RandomAccessFile {

        public MyRandomAccessFile(File file, String mode) throws FileNotFoundException {
            super(file, mode);
        }

        public MyRandomAccessFile(String name, String mode) throws FileNotFoundException {
            super(name, mode);
        }

        public int readInt2() throws IOException {
            int ch1 = this.read();
            int ch2 = this.read();
            int ch3 = this.read();
            int ch4 = this.read();
            if ((ch1 | ch2 | ch3 | ch4) < 0)
                throw new EOFException();
            return ((ch1 << 0) + (ch2 << 8) + (ch3 << 16) + (ch4 << 24));
        }
    }

    private MyRandomAccessFile mapFile;

    /** 地图块入口地址偏移表 */
    private int[][] blockOffsetTable;

    /** 地图宽度 */
    private int width;

    /** 地图高度 */
    private int height;

    /** 地图X方向块数 */
    private int xBlockCount;

    /** 地图Y方向块数 */
    private int yBlockCount;

    private ImageLoadThread imageLoader;

	private DataManager dataManager;

    public DefaultTileMapProvider(DataManager dataManager) {
        imageLoader = new ImageLoadThread();
        imageLoader.start();
        this.dataManager = dataManager;
    }

    public Image getBlock(int x, int y) {
        byte[] data = this.getJpegData(x, y);
        Image image = Toolkit.getDefaultToolkit().createImage(data);
        imageLoader.loadImage(image);
        return image;
    }

    public String getDescription() {
        return "default tile map provider for XYQ";
    }

    public int getHeight() {
        return height;
    }

    /**
     * 获取指定的JPEG数据块
     * 
     * @param x X轴索引
     * @param y Y轴索引
     * @return
     */
    public byte[] getJpegData(int x, int y) {
        byte jpegBuf[] = null;
        try {
            // read jpeg data
            int len = 0;
            mapFile.seek(blockOffsetTable[x][y]);// XXX offset
            if (isJPEGData()) {
                len = mapFile.readInt2();
                jpegBuf = new byte[len];
                mapFile.readFully(jpegBuf);
            }

            // modify jpeg data
            ByteArrayOutputStream bos = new ByteArrayOutputStream(4096);
            boolean isFilled = false;// 是否0xFF->0xFF 0x00
            bos.reset();
            bos.write(jpegBuf, 0, 2);
            // skip 2 bytes: FF A0
            int p, start;
            isFilled = false;
            for (p = 4, start = 4; p < jpegBuf.length - 2; p++) {
                if (!isFilled && jpegBuf[p] == (byte) 0xFF && jpegBuf[++p] == (byte) 0xDA) {
                    isFilled = true;
                    // 0xFF 0xDA ; SOS: Start Of Scan
                    // ch=jpegBuf[p+3];
                    // suppose always like this: FF DA 00 09 03...
                    jpegBuf[p + 2] = 12;
                    bos.write(jpegBuf, start, p + 10 - start);
                    // filled 00 3F 00
                    bos.write(0);
                    bos.write(0x3F);
                    bos.write(0);
                    start = p + 10;
                    p += 9;
                }
                if (isFilled && jpegBuf[p] == (byte) 0xFF) {
                    bos.write(jpegBuf, start, p + 1 - start);
                    bos.write(0);
                    start = p + 1;
                }
            }
            bos.write(jpegBuf, start, jpegBuf.length - start);
            jpegBuf = bos.toByteArray();
            bos.close();
        } catch (Exception e) {
            System.err.println("获取JPEG 数据块失败：" + e.getMessage());
        }
        return jpegBuf;

    }

    public TileMap getResource(String resId) {
        return loadMap(resId);
    }

    public String getVersion() {
        return "1.0";
    }

    public int getWidth() {
        return width;
    }

    public int getXBlockCount() {
        return xBlockCount;
    }

    public int getYBlockCount() {
        return yBlockCount;
    }

    private boolean isJPEGData() {
        byte[] buf = new byte[4];
        try {
            int len = mapFile.read();
            mapFile.skipBytes(3 + len * 4);
            mapFile.read(buf);// 47 45 50 4A; GEPJ
            String str = new String(buf);
            return str.equals("GEPJ");
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return false;
    }

    private boolean isValidMapFile() {
        byte[] buf = new byte[4];
        try {
            mapFile.read(buf);
            String str = new String(buf);
            return str.equals("0.1M");
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return false;
    }

    /**
     * 从流加载MAP
     * 
     * @param is
     */
    private void loadHeader() {
        if (!isValidMapFile()) {
            throw new IllegalArgumentException("非梦幻地图格式文件!");
        }
        try {
            // start decoding
            width = mapFile.readInt2();
            height = mapFile.readInt2();
            xBlockCount = (int) Math.ceil(width / 320.0);
            yBlockCount = (int) Math.ceil(height / 240.0);

            blockOffsetTable = new int[xBlockCount][yBlockCount];
            for (int y = 0; y < yBlockCount; y++) {
                for (int x = 0; x < xBlockCount; x++) {
                    blockOffsetTable[x][y] = mapFile.readInt2();
                }
            }
            // int headerSize = sis.readInt2();// where need it?
        } catch (Exception e) {
            throw new IllegalArgumentException("地图解码失败:" + e.getMessage());
            // e.printStackTrace();
        }
    }

    private TileMap loadMap(String sceneId) {
        if (mapFile != null) {
            try {
                mapFile.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            this.blockOffsetTable = null;
        }

        Scene scene = dataManager.findScene(Integer.valueOf(sceneId));
        if(scene!=null) {
            try {
            	String path = String.format("scene/%s.map", sceneId);
				String music = String.format("music/%s.mp3", sceneId);;
				MapConfig cfg = new MapConfig(sceneId, scene.getName(), path, music);
                File file = CacheManager.getInstance().getFile(cfg.getPath());
                mapFile = new MyRandomAccessFile(file, "r");
                loadHeader();
                return new TileMap(this, cfg);
            } catch (Exception e) {
                System.err.println("create map decoder failed!");
                e.printStackTrace();
            }
        }
        
//        MapConfig cfg = (MapConfig) ResourceStore.getInstance().findConfig(id);
//        if (cfg != null) {
//            try {
//                File file = CacheManager.getInstance().getFile(cfg.getPath());
//                mapFile = new MyRandomAccessFile(file, "r");
//                loadHeader();
//                return new TileMap(this, cfg);
//            } catch (Exception e) {
//                System.err.println("create map decoder failed!");
//                e.printStackTrace();
//            }
//        }
        return null;
    }

    public void dispose() {
        try {
            mapFile.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        blockOffsetTable = null;
        imageLoader.stop();
    }
}
