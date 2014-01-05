/*
 * JavaXYQ Engine 
 * 
 * javaxyq@2008 all rights. 
 * http://www.javaxyq.com
 */

package com.javaxyq.widget;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.lang.ref.SoftReference;

import com.javaxyq.config.MapConfig;
import com.javaxyq.resources.DefaultTileMapProvider;

/**
 * @author 龚德伟
 * @history 2008-5-22 龚德伟 新建
 */
public class TileMap extends AbstractWidget {

    /** 地图块像素宽度 */
    private static final int MAP_BLOCK_WIDTH = 320;

    /** 地图块像素高度 */
    private static final int MAP_BLOCK_HEIGHT = 240;

    private DefaultTileMapProvider provider;

    private SoftReference<Image>[][] blockTable;

    /** 地图X方向块数 */
    private int xBlockCount;

    /** 地图Y方向块数 */
    private int yBlockCount;

    private int width;

    private int height;

    private MapConfig config;

    private int lastCount;

    public TileMap(DefaultTileMapProvider provider, MapConfig cfg) {
        //水平方向w块，垂直方向h块
        this.config = cfg;
        this.xBlockCount = provider.getXBlockCount();
        this.yBlockCount = provider.getYBlockCount();
        this.width = provider.getWidth();
        this.height = provider.getHeight();
        blockTable = new SoftReference[this.xBlockCount][this.yBlockCount];
        this.provider = provider;
    }

    @Override
    protected void doDraw(Graphics2D g2, int x, int y, int width, int height) {
        // 1.计算Rect落在的图块 
        Point pFirstBlock = viewToBlock(x, y);
        // 2.计算第一块地图相对ViewRect的偏移量,并将Graphics偏移
        int dx = pFirstBlock.x * MAP_BLOCK_WIDTH - x;
        int dy = pFirstBlock.y * MAP_BLOCK_HEIGHT - y;
        g2.translate(dx, dy);
        //System.out.printf("x=%s,y=%s,dx=%s,dy=%s,block=%s\n", x, y, dx, dy, pFirstBlock);
        // 3.计算X轴,Y轴方向需要的地图块数量
        int xCount = 1 + (width - dx - 1) / MAP_BLOCK_WIDTH;
        int yCount = 1 + (height - dy - 1) / MAP_BLOCK_HEIGHT;
        //System.out.printf("xCount=%s,yCount=%s\n",xCount,yCount);
        // 4.从缓存获取地图块,画到Graphics上
        for (int i = 0; i < xCount; i++) {
            for (int j = 0; j < yCount; j++) {
                Image b = getBlock(i + pFirstBlock.x, j + pFirstBlock.y);
                g2.drawImage(b, i * MAP_BLOCK_WIDTH, j * MAP_BLOCK_HEIGHT, null);
            }
        }
    }

    /**
     * 预加载此区域的地图块
     * 
     * @param x
     * @param y
     * @param width
     * @param height
     */
    public void prepare(int x, int y, int width, int height) {
        // 1.计算Rect落在的图块 
        Point pFirstBlock = viewToBlock(x, y);
        // 2.计算第一块地图相对ViewRect的偏移量,并将Graphics偏移
        int dx = pFirstBlock.x * MAP_BLOCK_WIDTH - x;
        int dy = pFirstBlock.y * MAP_BLOCK_HEIGHT - y;
        //System.out.printf("x=%s,y=%s,dx=%s,dy=%s,block=%s\n", x, y, dx, dy, pFirstBlock);
        // 3.计算X轴,Y轴方向需要的地图块数量
        int xCount = 1 + (width - dx - 1) / MAP_BLOCK_WIDTH;
        int yCount = 1 + (height - dy - 1) / MAP_BLOCK_HEIGHT;
        //System.out.printf("xCount=%s,yCount=%s\n",xCount,yCount);
        // 4.缓存此区域的地图块
        Image[][] images = new Image[xCount][yCount];
        for (int i = 0; i < xCount; i++) {
            for (int j = 0; j < yCount; j++) {
                Image img = getBlock(i + pFirstBlock.x, j + pFirstBlock.y);
                images[i][j] = img;
            }
        }
    }

    private int checkTable() {
        int count = 0;
        int width = this.blockTable.length;
        int height = this.blockTable[0].length;
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                SoftReference<Image> reference = this.blockTable[i][j];
                if (reference != null && reference.get() != null) {
                    count++;
                }
            }
        }
//        if (count != lastCount) {
//            System.out.printf("map loaded block count: %s \n", count);
//        }
        lastCount = count;
        return count;
    }

    private Image getBlock(int x, int y) {
        SoftReference<Image> reference = this.blockTable[x][y];
        //如果此地图块还没加载,则取地图块数据并生成图像
        //如果GC由于低内存,已释放image,需要重新装载
        if (reference == null || reference.get() == null) {
            reference = new SoftReference<Image>(provider.getBlock(x, y));
            this.blockTable[x][y] = reference;
        }
        this.checkTable();
        return reference.get();
    }

    public int getXBlockCount() {
        return xBlockCount;
    }

    public void setXBlockCount(int blockCount) {
        xBlockCount = blockCount;
    }

    public int getYBlockCount() {
        return yBlockCount;
    }

    public void setYBlockCount(int blockCount) {
        yBlockCount = blockCount;
    }

    /**
     * 计算view坐标vp点对应的地图数据块位置 （即vp点落在哪个地图块上）
     * 
     * @param vp view's top left position
     * @return the map block index of the vp
     */
    private Point viewToBlock(int x, int y) {
        Point p = new Point();
        p.x = x / MAP_BLOCK_WIDTH;
        p.y = y / MAP_BLOCK_HEIGHT;
        if (p.x < 0)
            p.x = 0;
        if (p.y < 0)
            p.y = 0;
        return p;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public void dispose() {
        this.provider.dispose();
        this.provider = null;
        for (SoftReference<Image>[] refs : this.blockTable) {
            for (SoftReference<Image> ref : refs) {
                if (ref != null) {
                    ref.clear();
                }
            }
        }
        this.blockTable = null;
    }

    public MapConfig getConfig() {
        return config;
    }

    public void setConfig(MapConfig config) {
        this.config = config;
    }

    public boolean contains(int x, int y) {
        return true;
    }

}
