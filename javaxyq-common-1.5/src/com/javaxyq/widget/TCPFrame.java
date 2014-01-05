/*
 * JavaXYQ Source Code
 * by kylixs
 * at 2009-12-8
 * please visit http://javaxyq.googlecode.com
 * or mail to kylixs@qq.com
 */
package com.javaxyq.widget;

import java.awt.Graphics2D;
import java.awt.Image;

/**
 * TCP动画的帧对象
 * 
 * @author dewitt
 * @date 2009-12-8 create
 */
public class TCPFrame extends AbstractWidget {

	private static final long serialVersionUID = 1L;
	private int x;
	private int y;
	private int width;
	private int height;
	private Image image;
	// Reference Pixel(悬挂点)
	private int refPixelX;
	private int refPixelY;

	public TCPFrame(Image image, int x, int y, int width, int height) {
		this(image, x, y, width, height, 0, 0);
	}

	public TCPFrame(Image image, int x, int y, int width, int height, int refPixelX, int refPixelY) {
		super();
		this.image = image;
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.refPixelX = refPixelX;
		this.refPixelY = refPixelY;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public int getRefPixelX() {
		return refPixelX;
	}

	public int getRefPixelY() {
		return refPixelY;
	}

	public Image getImage() {
		return image;
	}

	public void dispose() {
		image = null;
	}

	protected void doDraw(Graphics2D g2, int x, int y, int width, int height) {
		g2.drawImage(image, this.refPixelX + x - this.x, this.refPixelY + y - this.y, null);
	}

	public boolean contains(int x, int y) {
		// TODO]
		// image.getSource().
		return x >= this.x && y >= this.y && x <= this.x + width && y <= this.y + height;
	}

}
