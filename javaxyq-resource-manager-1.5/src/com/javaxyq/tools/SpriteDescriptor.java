/**
 * 
 */
package com.javaxyq.tools;

import java.awt.Point;
import java.io.InputStream;

/**
 * æ´¡È√Ë ˆ
 * @author gongdewei
 * @date 2011-8-13 create
 */
public interface SpriteDescriptor {

	int getAnimCount();
	
	int getFrameCount();
	
	int getFrameWidth(int animIndex, int frameIndex);
	
	int getFrameHeight(int animIndex, int frameIndex);
	
	int getFrameLeft(int animIndex, int frameIndex);
	
	int getFrameTop(int animIndex, int frameIndex);
	
	Point getFrameRefPixel(int animIndex, int frameIndex);
	
	int getTotalWidth();
	
	int getTotalHeight();
	
	String encode();
	
	void decode(String descriptor);

	public void setFrameWidth(int animIndex, int frameIndex, int width);

	public void setFrameHeight(int animIndex, int frameIndex, int height);

	public void setFrameRefPixel(int animIndex, int frameIndex, int refPixelX, int refPixelY);

	public void decode(InputStream source);
}
