/**
 * 
 */
package com.javaxyq.tools;

import java.awt.Point;
import java.io.InputStream;
import java.util.Scanner;

public class SpriteDescriptorImpl implements SpriteDescriptor {

	private int animCount;
	private int frameCount;
	private int[][] widths;
	private int[][] heights;
	private Point [][] refPixels;
	
	public SpriteDescriptorImpl(int animCount, int frameCount) {
		super();
		initialize(animCount, frameCount);
	}

	public SpriteDescriptorImpl() {
	}
	
	private void initialize(int animCount, int frameCount) {
		this.animCount = animCount;
		this.frameCount = frameCount;
		this.widths = new int[animCount][frameCount];
		this.heights = new int[animCount][frameCount];
		refPixels = new Point[animCount][frameCount];
	}
	
	@Override
	public int getAnimCount() {
		return animCount;
	}

	@Override
	public int getFrameCount() {
		return frameCount;
	}
	
	public void setFrameWidth(int animIndex, int frameIndex, int width) {
		int[] framewidths = this.widths[animIndex];
		framewidths[frameIndex] = width;
	}

	public void setFrameHeight(int animIndex, int frameIndex, int height) {
		int[] frameheights = this.heights[animIndex];
		frameheights[frameIndex] = height;
	}
	
	@Override
	public int getFrameHeight(int animIndex, int frameIndex) {
		return heights[animIndex][frameIndex];
	}

	@Override
	public int getFrameWidth(int animIndex, int frameIndex) {
		return widths[animIndex][frameIndex];
	}

	@Override
	public int getFrameLeft(int animIndex, int frameIndex) {
		int xx = 0;
		int[] framewidths = widths[animIndex];
		for (int i = 0; i< frameIndex && i < framewidths.length; i++) {
			xx += framewidths[i];
		}
		return xx;
	}

	@Override
	public int getFrameTop(int animIndex, int frameIndex) {
		int yy = 0;
		int[] frameheights = heights[animIndex];
		for (int i = 0; i< frameIndex && i < frameheights.length; i++) {
			yy += frameheights[i];
		}
		return yy;
	}

	@Override
	public int getTotalWidth() {
		int total = 0;
		for (int i = 0; i < animCount; i++) {
			total = Math.max(total, getFrameTotalWidth(i));
		}
		return total;
	}
	
	@Override
	public int getTotalHeight() {
		int total = 0;
		for (int i = 0; i < animCount; i++) {
			total = Math.max(total, getFrameTotalHeight(i));
		}
		return total;
	}

	private int getFrameTotalWidth(int animIndex) {
		int sum = 0;
		int[] framewidths = widths[animIndex];
		for (int i = 0; i < framewidths.length; i++) {
			sum += framewidths[i];
		}
		return sum;
	}

	private int getFrameTotalHeight(int animIndex) {
		int sum = 0;
		int[] frameheights = heights[animIndex];
		for (int i = 0; i < frameheights.length; i++) {
			sum += frameheights[i];
		}
		return sum;
	}

	public void setFrameRefPixel(int animIndex, int frameIndex, int refPixelX, int refPixelY) {
		refPixels[animIndex][frameIndex] = new Point(refPixelX, refPixelY);
	}
	
	@Override
	public Point getFrameRefPixel(int animIndex, int frameIndex) {
		return refPixels[animIndex][frameIndex];
	}

	@Override
	public void decode(String source) {
		Scanner scanner = new Scanner(source);
		decode(scanner);
	}
	
	public void decode(InputStream source) {
		Scanner scanner = new Scanner(source);
		decode(scanner);
	}
	
	private void decode(Scanner scanner) {
		scanner.useDelimiter("[\\s\\,]");
		String first = scanner.nextLine();
		if(!first.startsWith("sprite_descriptor")) {
			// ¸ñÊ½´íÎó
			return ;
		}
		//animCount frameCount
		animCount = scanner.nextInt();
		frameCount = scanner.nextInt();
		initialize(animCount, frameCount);
		//anim lines
		for (int i = 0; i < animCount; i++) {
			//frames(refPixelX, refPixelY, width, height)
			for (int f = 0; f < frameCount; f++) {
				scanner.skip("[\\s\\(]*");
				int refPixelX = scanner.nextInt();
				int refPixelY = scanner.nextInt();
				int width = scanner.nextInt();
				int height = scanner.nextInt();
				this.setFrameRefPixel(i, f, refPixelX, refPixelY);
				this.setFrameWidth(i, f, width);
				this.setFrameHeight(i, f, height);
				scanner.skip("[\\s\\)]*");
			}
		}
	}

	@Override
	public String encode() {
		StringBuilder sb = new StringBuilder();
		sb.append("sprite_descriptor{\r\n");
		sb.append(animCount).append(" ").append(frameCount).append("\r\n");
		for (int i = 0; i < animCount; i++) {
			for(int f=0; f<frameCount; f++) {
				Point p = getFrameRefPixel(i, f);
				sb.append("( ");
				sb.append(p.x).append(",");
				sb.append(p.y).append(",");
				sb.append(getFrameWidth(i, f)).append(",");
				sb.append(getFrameHeight(i, f));
				sb.append(" )");
				if(f < frameCount-1) {
					sb.append(" ");
				}
			}
			sb.append("\r\n");
		}
		sb.append("}");
		return sb.toString();
	}

//	public static void main(String[] args) throws FileNotFoundException {
//		File file = new File("C:\\Documents and Settings\\Administrator\\×ÀÃæ\\test3\\des.txt");
//		SpriteDescriptorImpl descriptor = new SpriteDescriptorImpl();
//		descriptor.decode(new FileInputStream(file));
//		System.out.println("decode descriptor: "+descriptor.encode());
//	}
}
