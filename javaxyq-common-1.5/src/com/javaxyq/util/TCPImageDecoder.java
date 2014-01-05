package com.javaxyq.util;

import java.awt.Image;
import java.awt.image.ColorModel;
import java.awt.image.DirectColorModel;
import java.awt.image.ImageProducer;
import java.awt.image.MemoryImageSource;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import com.javaxyq.core.Toolkit;
import com.javaxyq.io.RandomAcessInputStream;
import com.javaxyq.widget.TCPFrame;

/**
 * tcp/tca动画解码器
 * 
 * @author 龚德伟
 * @date create 2009-12-08
 */
public class TCPImageDecoder {

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

	// Reference Pixel(悬挂点)
	private int refPixelX;

	private int refPixelY;

	/** 包含动画个数 */
	private int animCount;

	/** 动画的帧数 */
	private int frameCount;

	/** 原始调色板 */
	private short[] originPalette;

	/** 当前调色板 */
	private short[] palette;

	/** 精灵宽度 */
	private int width;
	/** 精灵高度 */
	private int height;

	private RandomAcessInputStream in;

	private int[] frameOffsets;

	private String filename;
	
	private List<MemoryImageSource>animSources = new ArrayList<MemoryImageSource>();
	private AnimationThread animator = new AnimationThread(animSources);

	private int[] animpixels;

	private int[] totalPixels;
	
	public TCPImageDecoder() {
		palette = new short[256];
		originPalette = new short[256];
		animator.start();
	}
	
	/**
	 * @param string
	 * @throws Exception
	 */
	public TCPImageDecoder(String filename) throws Exception {
		palette = new short[256];
		originPalette = new short[256];
		load(filename);
		animator.start();
	}
	
	public int getAnimCount() {
		return animCount;
	}
	
	public int getFrameCount() {
		return frameCount;
	}
	
	public int getHeight() {
		return height;
	}
	
	public short[] getOriginPalette() {
		return originPalette;
	}
	
	public short[] getPalette() {
		return palette;
	}
	
	public int getRefPixelX() {
		return refPixelX;
	}
	
	public int getRefPixelY() {
		return refPixelY;
	}
	
	public int getWidth() {
		return width;
	}
	
	public void load(File file) throws IllegalStateException, FileNotFoundException, IOException {
		load(new FileInputStream(file));
	}
	
	public void load(InputStream is) throws IllegalStateException, IOException {
		in = prepareInputStream(is);
		
		// tcp 信息
		short headerSize = in.readUnsignedShort();
		animCount = in.readUnsignedShort();
		frameCount = in.readUnsignedShort();
		width = in.readUnsignedShort();
		height = in.readUnsignedShort();
		refPixelX = in.readUnsignedShort();
		refPixelY = in.readUnsignedShort();
		
		// 读取帧延时信息
		int len = headerSize - TCP_HEADER_SIZE;
		if (len < 0) {
			throw new IllegalStateException("帧延时信息错误: " + len);
		}
		int[] delays = new int[len];
		for (int i = 0; i < len; i++) {
			delays[i] = in.read();
		}
		
		// 读取调色板
		in.seek(headerSize + 4);
		for (int i = 0; i < 256; i++) {
			originPalette[i] = in.readUnsignedShort();
		}
		// 复制调色板
		System.arraycopy(originPalette, 0, palette, 0, 256);
		
		// 帧偏移列表
		frameOffsets = new int[animCount * frameCount];
		in.seek(headerSize + 4 + 512);
		for (int i = 0; i < animCount; i++) {
			for (int n = 0; n < frameCount; n++) {
				frameOffsets[i * frameCount + n] = in.readInt() + headerSize + 4;
			}
		}
	}
	
	public void load(String filename) throws Exception {
		this.filename = filename;
		// InputStream fileIn = getClass().getResourceAsStream(filename);
		// File file = new File(filename);
		// InputStream fileIn = new FileInputStream(file);
		load(Toolkit.getInputStream(filename));
	}
	
	/**
	 * 读取某帧动画
	 * 
	 * @param animIndex
	 *            动画索引号
	 * @param frameIndex
	 *            帧索引号
	 * @return
	 */
	public TCPFrame readFrame(int animIndex, int frameIndex) {
		int offset = frameOffsets[animIndex * frameCount + frameIndex];
		if (offset == 0)
			return null;// blank frame
		try {
			in.seek(offset);
			int frameX = in.readInt();
			int frameY = in.readInt();
			int frameWidth = in.readInt();
			int frameHeight = in.readInt();
			// ColorModel cm = new DirectColorModel(32, 0xff0000, 0xff00, 0xff,
			// 0xff000000);
			ColorModel cm = new DirectColorModel(21, 0xf800, 0x07E0, 0x001F, 0x1f0000);
			int[] pixels = getPixels(offset, frameWidth, frameHeight);
			ImageProducer producer = new MemoryImageSource(frameWidth, frameHeight, cm, pixels, 0, frameWidth);
			Image image = java.awt.Toolkit.getDefaultToolkit().createImage(producer);
			return new TCPFrame(image, frameX, frameY, frameWidth, frameHeight);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public Image readAnimation(int index) {
		index %= animCount;
		int startFrame = index * frameCount;
		int endFrame = startFrame + frameCount;
		int frameSize = width * height;
		int[] pixels = new int[frameSize * frameCount];
		for (int i = startFrame; i < endFrame; i++) {
			try {
				int offset = frameOffsets[i];
				in.seek(offset);
				int frameX = in.readInt();
				int frameY = in.readInt();
				int frameWidth = in.readInt();
				int frameHeight = in.readInt();
				getPixels(offset, pixels, (i - startFrame) * frameSize, width, height);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		animpixels = new int[frameSize];
		totalPixels = pixels;
		ColorModel cm = new DirectColorModel(21, 0xf800, 0x07E0, 0x001F, 0x1f0000);
		System.arraycopy(pixels, 0, animpixels, 0, frameSize);
		MemoryImageSource producer = new MemoryImageSource(width, height, cm, animpixels, 0, width);
		producer.setAnimated(true);
		Image image = java.awt.Toolkit.getDefaultToolkit().createImage(producer);
		registerAnimation(producer);
		return image;
	}
	
	private int[] getPixels(int frameOffset, int width, int height) throws IOException {
		int[] pixels = new int[width * height];
		return getPixels(frameOffset, pixels, 0, width, height);
	}
	
	/**
	 * 获取动画某帧图片的像素数据
	 * 
	 * @param pixels
	 * 
	 * @param frameIndex
	 * @return
	 * @throws IOException
	 */
	private int[] getPixels(int frameOffset, int[] pixels, int offset, int width, int height) throws IOException {
		in.seek(frameOffset);
		int offx = in.readInt();
		int offy = in.readInt();
		int w = in.readInt();
		int h = in.readInt();
		// 行像素数据偏移
		int[] lineOffsets = new int[h];
		for (int l = 0; l < h; l++) {
			lineOffsets[l] = in.readInt();
		}
		int b, x, c;
		int index;
		int count;
		int x0 = refPixelX -offx;
		int y0 = refPixelY - offy;
		for (int y = 0; y < h; y++) {
			x = 0;
			in.seek(lineOffsets[y] + frameOffset);
			while (x < w) {
				b = in.read();
				switch ((b & TYPE_FLAG)) {
				case TYPE_ALPHA:
					if ((b & TYPE_ALPHA_PIXEL) > 0) {
						index = in.read();
						c = palette[index];
						pixels[offset + (y0 + y) * width + x0 + x++] = c + ((b & 0x1F) << 16);
					} else if (b != 0) {// ???
						count = b & 0x1F;// count
						b = in.read();// alpha
						index = in.read();
						c = palette[index];
						for (int i = 0; i < count; i++) {
							pixels[offset + (y0 + y) * width + x0 + x++] = c + ((b & 0x1F) << 16);
						}
					} else {// block end
						if (x > w) {
							System.err.println("block end error: [" + y + "][" + x + "/" + w + "]");
							continue;
						} else if (x == 0) {
							// System.err.println("x==0");
						} else {
							x = w;
						}
					}
					break;
				case TYPE_PIXELS:
					count = b & 0x3F;
					for (int i = 0; i < count; i++) {
						index = in.read();
						pixels[offset + (y0 + y) * width + x0 + x++] = palette[index] + (0x1F << 16);
					}
					break;
				case TYPE_REPEAT:
					count = b & 0x3F;
					index = in.read();
					c = palette[index];
					for (int i = 0; i < count; i++) {
						pixels[offset + (y0 + y) * width + x0 + x++] = c + (0x1F << 16);
					}
					break;
				case TYPE_SKIP:
					count = b & 0x3F;
					x += count;
					break;
				}
			}
			if (x > w)
				System.err.println("block end error: [" + y + "][" + x + "/" + w + "]");
		}
		return pixels;
	}
	
	private RandomAcessInputStream prepareInputStream(InputStream in) throws IOException, IllegalStateException {
		byte[] buf;
		RandomAcessInputStream randomIn;
		buf = new byte[2];
		in.mark(10);
		in.read(buf, 0, 2);
		String flag = new String(buf, 0, 2);
		if (!WAS_FILE_TAG.equals(flag)) {
			throw new IllegalStateException("文件头标志错误:" + print(buf));
		}
		if (in instanceof RandomAcessInputStream) {
			in.reset();
			randomIn = (RandomAcessInputStream) in;
		} else {
			byte[] buf2 = new byte[in.available() + buf.length];
			System.arraycopy(buf, 0, buf2, 0, buf.length);
			int a = 0, count = buf.length;
			while (in.available() > 0) {
				a = in.read(buf2, count, in.available());
				count += a;
			}
			// construct a new seekable stream
			randomIn = new RandomAcessInputStream(buf2);
		}
		// skip header
		randomIn.seek(2);
		return randomIn;
	}
	
	private String print(byte[] buf) {
		String output = "[";
		for (byte b : buf) {
			output += b;
			output += ",";
		}
		output += "]";
		return output;
	}
	
	public void resetPalette() {
		System.arraycopy(originPalette, 0, palette, 0, 256);
	}
	
	private void registerAnimation(MemoryImageSource is) {
		animSources.add(is);
	}
	private class AnimationThread extends Thread{
		private List<MemoryImageSource> animSources;

		public AnimationThread(List<MemoryImageSource>animSources) {
			this.animSources = animSources;
			setDaemon(true);
			setName("TCPImageAnimation");
		}
		
		public void run() {
			int frameIndex = 0;
			int frameSize = width*height;
			while(true) {
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				if(animSources.size()>0) {
					frameIndex ++;
					frameIndex %= frameCount;
					System.arraycopy(totalPixels, frameIndex*frameSize, animpixels, 0, animpixels.length);
					for (int i = 0; i < animSources.size(); i++) {
						MemoryImageSource imgprod = animSources.get(i);
						imgprod.newPixels();
					}
				}
			}
		}
	}
}
