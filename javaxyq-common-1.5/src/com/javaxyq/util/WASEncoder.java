//TODO 生成256索引色??

package com.javaxyq.util;

import java.awt.Image;
import java.io.OutputStream;
import java.util.Vector;

/**
 * 游戏资源文件-WAS格式编码器
 * 
 * @author Langlauf
 * @date
 */
public class WASEncoder {
	short imageHeaderSize;

	short[] palette;

	// size
	int width;

	int height;

	// 中心点(x,y)
	int centerX;

	int centerY;

	int frameCount;

	int spriteCount;

	Vector<Image> images;

	/** 帧延时 */
	Vector<Integer> delays;

	/**
	 * 
	 * @param x
	 * @param y
	 * @param width
	 * @param height
	 * @param spriteCount
	 *            精灵动画个数
	 * @param frameCount
	 *            每个精灵动画帧数
	 */
	public WASEncoder(int x, int y, int width, int height, int spriteCount, int frameCount) {
		this.centerX = x;
		this.centerY = y;
		this.width = width;
		this.height = height;
		this.spriteCount = spriteCount;
		this.frameCount = frameCount;
	}

	/**
	 * 精灵个数为1,动画帧数不确定
	 * 
	 * @param x
	 * @param y
	 * @param width
	 * @param height
	 */
	public WASEncoder(int x, int y, int width, int height) {
		this(x, y, width, height, 1, -1);
	}

	/**
	 * 精灵个数为1,动画帧数不确定<br>
	 * x,y为0
	 * 
	 * @param width
	 * @param height
	 */
	public WASEncoder(int width, int height) {
		this(0, 0, width, height, 1, -1);
	}

	public void addFrame(Image image) {
		addFrame(image, 1);
	}

	public void addFrames(java.util.List<Image> images) {
		for (Image image : images) {
			addFrame(image, 1);
		}
	}

	public void addFrame(Image image, int delay) {
		images.add(image);
		delays.add(delay);
	}

	/**
	 * 将图像编码输出到指定的流
	 * 
	 * @param out
	 */
	public void encode(OutputStream out) {

	}
}
