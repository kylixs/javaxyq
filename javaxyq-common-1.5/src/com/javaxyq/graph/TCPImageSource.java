/*
 * JavaXYQ Source Code
 * by kylixs
 * at 2009-12-8
 * please visit http://javaxyq.googlecode.com
 * or mail to kylixs@qq.com
 */
package com.javaxyq.graph;

import java.awt.image.ColorModel;
import java.awt.image.MemoryImageSource;
import java.util.Hashtable;


/**
 * @author dewitt
 * @date 2009-12-8 create
 */
public class TCPImageSource extends MemoryImageSource {
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
	private byte[]pixels;

	public TCPImageSource(int w, int h, ColorModel cm, byte[] pix, int off, int scan, Hashtable<?, ?> props) {
		super(w, h, cm, pix, off, scan, props);
		this.pixels = pix;
	}

	public TCPImageSource(int w, int h, ColorModel cm, byte[] pix, int off, int scan) {
		super(w, h, cm, pix, off, scan);
		this.pixels = pix;
	}
	
	public synchronized void newPixels(int x, int y, int w, int h, boolean framenotify) {
		super.newPixels(x, y, w, h, framenotify);
		
	}

	/**
	 * 
	 */
	public int getPixel(int x,int y) {
		return 1;
	}
}
