package com.javaxyq.tools;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.io.File;
import java.util.LinkedList;
import java.util.Vector;

import javax.swing.JComponent;

import com.javaxyq.util.MapDecoder;
import lombok.extern.slf4j.Slf4j;

/**
 * 显示游戏地图的控件
 * //TODO 多线程加载,paint不等待
 *
 * @author Administrator
 */
@Slf4j
public class JMap extends JComponent {

    private static final long serialVersionUID = 1L;

    private final Rectangle maxVisibleRect = new Rectangle();

    private MapDecoder decoder;

    private LinkedList<MapImage> loadedImages = new LinkedList<>();

    private MapImage[][] visibleImages;

    public JMap(File file) {
        loadMap(file);
    }

    public JMap() {
    }

    @Override
    public void paint(Graphics g) {
        paintComponent(g);
    }

    protected void paintComponent(Graphics g) {
        if (decoder == null)
            return;
        Rectangle clipRect = getVisibleRect(); // g.getClipBounds();
        if (!maxVisibleRect.contains(clipRect)) {
            loadRectMapData(clipRect);
        }
        drawMap(g);
    }

    /**
     * 在g上画出当前可见区域对应的地图图像
     */
    protected void drawMap(Graphics g) {
        g = g.create();
        MapImage img0 = visibleImages[0][0];
        int xOffset = img0.h * 320;
        int yOffset = img0.v * 240;
        g.translate(xOffset, yOffset);
        for (int h = 0; h < visibleImages.length; h++) {
            for (int v = 0; v < visibleImages[0].length; v++) {
                g.drawImage(visibleImages[h][v].image, h * 320, v * 240, this);
            }
        }
        g.dispose();
    }

    protected void drawCell(Graphics g) {

    }

    /**
     * 绘制指定区域
     */
    public void drawRect(Graphics g, Rectangle clipRect) {
        if (decoder == null)
            return;
        if (!maxVisibleRect.contains(clipRect)) {
            loadRectMapData(clipRect);
        }
        g = g.create();
        MapImage img0 = visibleImages[0][0];
        int xOffset = img0.h * 320 - clipRect.x;
        int yOffset = img0.v * 240 - clipRect.y;
        g.translate(xOffset, yOffset);
        for (int h = 0; h < visibleImages.length; h++) {
            for (int v = 0; v < visibleImages[0].length; v++) {
                g.drawImage(visibleImages[h][v].image, h * 320, v * 240, this);
            }
        }
        g.dispose();

    }


    /**
     * 加载clipRect用到的地图块
     *
     * @param clipRect
     */
    private void loadRectMapData(Rectangle clipRect) {
        MapArea mapArea = getMapArea(clipRect);
        MapImage mapimage;
        int rows = mapArea.right - mapArea.left + 1;
        int cols = mapArea.bottom - mapArea.top + 1;
        visibleImages = new MapImage[rows][cols];
        // long begTime, endTime;
        // begTime = System.currentTimeMillis();

        Vector<ImageLoadThread> threads = new Vector<ImageLoadThread>();
        for (int h = mapArea.left; h <= mapArea.right; h++) {
            for (int v = mapArea.top; v <= mapArea.bottom; v++) {
                mapimage = removeMapImage(h, v);
                if (mapimage == null) {
                    byte[] imageData = decoder.getJpegData(h, v);
                    Image image = Toolkit.getDefaultToolkit().createImage(imageData);
                    mapimage = new MapImage(image, h, v);
                    ImageLoadThread thread = getLoadThread();
                    thread.setLoadingImage(image);
                    threads.add(thread);
                }
                loadedImages.add(0, mapimage);
                if (loadedImages.size() > 16) {
                    loadedImages.removeLast();
                }
                visibleImages[h - mapArea.left][v - mapArea.top] = mapimage;
            }
        }
        for (ImageLoadThread thread : threads) {
            synchronized (thread) {
                while (!thread.isFinished())
                    try {
                        thread.wait();
                    } catch (InterruptedException e) {
                        log.error("", e);
                    }
            }
            releaseLoadThread(thread);
        }
        // endTime = System.currentTimeMillis();
        // System.out.println("load image :" + (endTime - begTime));
        maxVisibleRect.x = mapArea.left * 320;
        maxVisibleRect.y = mapArea.top * 240;
        maxVisibleRect.width = (mapArea.right - mapArea.left + 1) * 320;
        maxVisibleRect.height = (mapArea.bottom - mapArea.top + 1) * 240;
    }

    private void releaseImage(MapImage img) {
        for (int w = 0; w < visibleImages.length; w++) {
            for (int h = 0; h < visibleImages[w].length; h++) {
                if (visibleImages[w][h] == img) {
                    visibleImages[w][h] = null;
                    break;
                }
            }
        }
        System.gc();
    }

    Vector<ImageLoadThread> threadBuffer = new Vector<ImageLoadThread>(10);

    private void releaseLoadThread(ImageLoadThread thread) {
        threadBuffer.add(thread);
    }

    private ImageLoadThread getLoadThread() {
        if (threadBuffer.size() > 0) {
            return threadBuffer.remove(threadBuffer.size() - 1);
        }
        ImageLoadThread thread = new ImageLoadThread();
        thread.start();
        return thread;
    }

    /**
     * 获得clipRect对应地图数据块区域
     *
     * @param clipRect
     * @return
     */
    private MapArea getMapArea(Rectangle clipRect) {
        MapArea mapArea = new MapArea();
        mapArea.left = (int) Math.ceil(clipRect.x / 320.0) - 1;
        mapArea.top = (int) Math.ceil(clipRect.y / 240.0) - 1;
        mapArea.right = (int) Math.ceil((clipRect.x + clipRect.width) / 320.0) - 1;
        mapArea.bottom = (int) Math.ceil((clipRect.y + clipRect.height) / 240.0) - 1;
        if (mapArea.left < 0)
            mapArea.left = 0;
        if (mapArea.top < 0)
            mapArea.top = 0;
        // System.out.println(mapArea);
        return mapArea;
    }

    /**
     * 从缓冲区获取某个图像块
     *
     * @param h
     * @param v
     * @return
     */
    private MapImage removeMapImage(int h, int v) {
        int length = loadedImages.size();
        MapImage mapImage = null;
        int index = -1;
        for (int i = 0; i < length; i++) {
            mapImage = loadedImages.get(i);
            if (mapImage.h == h && mapImage.v == v) {
                index = i;
                break;
            }
        }
        return index == -1 ? null : loadedImages.remove(index);
    }

    /**
     * 用于描述游戏地图数据块区域
     *
     * @author Langlauf
     * @date
     */
    class MapArea {
        public int left;

        public int right;

        public int top;

        public int bottom;

        @Override
        public String toString() {
            return "[" + left + "," + top + "," + right + "," + bottom + "]";
        }
    }

    /**
     * 用于描述地图的一片解码后的数据
     *
     * @author Langlauf
     * @date
     */
    class MapImage {
        public Image image;

        // public String mapname;
        public int h;

        public int v;

        public MapImage(Image image, int h, int v) {
            this.image = image;
            this.h = h;
            this.v = v;
        }
    }

    /**
     * 加载一个文件
     *
     * @param file
     * @return
     */
    public boolean loadMap(File file) {
        if (file == null)
            return false;
        try {
            decoder = new MapDecoder(file);
            setSize(decoder.getWidth(), decoder.getHeight());
            setPreferredSize(new Dimension(decoder.getWidth(), decoder.getHeight()));
            loadedImages = new LinkedList<MapImage>();
            maxVisibleRect.height = 0;
            maxVisibleRect.width = 0;
        } catch (Exception e) {
            System.err.println("打开地图文件出错:" + file.getName());
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public int getMapWidth() {
        return decoder.getWidth();
    }

    public int getMapHeight() {
        return decoder.getHeight();
    }

    public Dimension getMapSize() {
        return new Dimension(decoder.getWidth(), decoder.getHeight());
    }

    public Dimension getSegments() {
        return new Dimension(decoder.getHorSegmentCount(), decoder.getVerSegmentCount());
    }

    public class ImageLoadThread extends Thread {

        private Image image;

        protected Component component = new Component() {
        };

        protected MediaTracker tracker = new MediaTracker(component);

        private int mediaTrackerID;

        private boolean isFinished;

        private boolean isCompleted;

        /**
         * Returns an ID to use with the MediaTracker in loading an image.
         */
        private int getNextID() {
            return ++mediaTrackerID;
        }

        public ImageLoadThread() {
            setDaemon(true);
        }

        public void setLoadingImage(Image image) {
            this.image = image;
            synchronized (this) {
                notifyAll();
            }
        }

        public void run() {
            while (true) {
                synchronized (this) {
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
                    try {
                        this.wait();
                    } catch (InterruptedException e) {
                        // e.printStackTrace();
                    }
                }
            }
        }

        public boolean isCompleted() {
            return isCompleted;
        }

        public boolean isFinished() {
            return isFinished;
        }
    }

    public MapDecoder getDecoder() {
        return decoder;
    }
}
