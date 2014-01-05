package com.javaxyq.util;

import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.lang.ref.WeakReference;

public class WASFrame {
    /** 帧数据偏移 */
    private int frameOffset;

    /** 行数据偏移 */
    private int[] lineOffsets;

    private int delay = 1;// 延时帧数

    private int height;

    private int width;

    private int x;// 图像偏移

    private int y;

    /** Image格式图像 */
    private WeakReference<BufferedImage> image;

    /**
     * 图像原始数据<br>
     * 0-15位RGB颜色(565)<br>
     * 16-20为alpha值<br>
     * pixels[x+y*width]
     */
    private int[] pixels;

    public WASFrame(int x, int y, int width, int height, int delay,int frameOffset,int []lineOffset) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.delay = delay;
        this.frameOffset = frameOffset;
        this.lineOffsets = lineOffset;
    }

    public WASFrame() {
        // TODO Auto-generated constructor stub
    }

//    /**
//     * 将图像数据画到Image上
//     */
//    public void draw(WritableRaster raster, int x, int y) {
//        int iArray[] = new int[4];
//        for (int y1 = 0; y1 < height; y1++) {
//            for (int x1 = 0; x1 < width; x1++) {
//                iArray[0] = ((pixels[y1][x1] >>> 11) & 0x1F) << 3;
//                iArray[1] = ((pixels[y1][x1] >>> 5) & 0x3f) << 2;
//                iArray[2] = (pixels[y1][x1] & 0x1F) << 3;
//                iArray[3] = ((pixels[y1][x1] >>> 16) & 0x1f) << 3;
//                raster.setPixel(x1 + x, y1 + y, iArray);
//            }
//        }
//    }
//
//    public BufferedImage getImage() {
//        if (image == null || image.get() == null) {
//            BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
//            draw(img.getRaster(), 0, 0);
//            image = new WeakReference<BufferedImage>(img);
//        }
//        return image.get();
//    }

    public int getFrameOffset() {
        return frameOffset;
    }

    public void setFrameOffset(int addrOffset) {
        this.frameOffset = addrOffset;
    }

    public int[] getLineOffsets() {
        return lineOffsets;
    }

    public void setLineOffsets(int[] lineOffsets) {
        this.lineOffsets = lineOffsets;
    }

    public int getDelay() {
        return delay;
    }

    public void setDelay(int delay) {
        this.delay = delay;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int[] getPixels() {
        return pixels;
    }

    public void setPixels(int[] pixels) {
        this.pixels = pixels;
    }

}
