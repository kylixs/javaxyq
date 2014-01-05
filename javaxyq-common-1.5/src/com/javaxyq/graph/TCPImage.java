/*
 * JavaXYQ Source Code
 * by kylixs
 * at 2009-12-5
 * please visit http://javaxyq.googlecode.com
 * or mail to kylixs@qq.com
 */
package com.javaxyq.graph;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.ImageObserver;
import java.awt.image.ImageProducer;

/**
 * @author dewitt
 * @date 2009-12-5 create
 */
public class TCPImage extends Image {

	static final int TYPE_ALPHA = 0x00;// 前2位

	static final int TYPE_ALPHA_PIXEL = 0x20;// 前3位 0010 0000

	static final int TYPE_ALPHA_REPEAT = 0x00;// 前3位

	static final int TYPE_FLAG = 0xC0;// 2进制前2位 1100 0000

	static final int TYPE_PIXELS = 0x40;// 以下前2位 0100 0000

	static final int TYPE_REPEAT = 0x80;// 1000 0000

	static final int TYPE_SKIP = 0xC0; // 1100 0000

	/** 文件头标记 */
	static final String WAS_FILE_TAG = "SP";

	static final int TCP_HEADER_SIZE = 12;

	/** 文件名 */
	private String filename;
	
	/** Reference Pixel X (悬挂点X)	 */
	private int refPixelX;

	/** Reference Pixel Y (悬挂点Y) */
	private int refPixelY;

	/** 包含动画个数 */
	private int animCount;

	/** 动画的帧数 */
	private int frameCount;

	/** 文件头大小 */
	private int headerSize;

	/** 原始调色板 */
	private short[] originPalette;

	/** 当前调色板 */
	private short[] palette;

	/** 精灵宽度 */
	private int width;
	/** 精灵高度 */
	private int height;

	private TCPImageSource source;

	/**
	 * 创建一个TCP图片
	 * @param filename tcp文件名
	 * @param index 动画索引号
	 */
	public TCPImage(String filename,int index) {
		this.filename = filename;
	}

	public Graphics getGraphics() {
		return null;
	}

	public int getHeight(ImageObserver observer) {
		return height;
	}

	public Object getProperty(String name, ImageObserver observer) {
		return null;
	}

	public ImageProducer getSource() {
//		if (source == null) {
//			source = new TCPImageSource(filename);
//		}
		return source;
	}

	public int getWidth(ImageObserver observer) {
		return width;
	}

}
