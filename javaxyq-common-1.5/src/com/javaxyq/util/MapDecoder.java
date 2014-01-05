package com.javaxyq.util;

import java.io.ByteArrayOutputStream;
import java.io.EOFException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

public class MapDecoder {

	private int width;

	private int height;

	private int[][] segmentsOffset;

	private Object[][] jpegDatas;

	private String filename;

	private MyRandomAccessFile mapFile;

	private int horSegmentCount;

	private int verSegmentCount;

	public MapDecoder(String filename) throws Exception {
		this(new File(filename));
	}

	public MapDecoder(File file) throws Exception {
		this.filename = file.getName();
		mapFile = new MyRandomAccessFile(file, "r");
		loadHeader();
	}

	/**
	 * 从流加载MAP
	 * 
	 * @param is
	 */
	private void loadHeader() {
		if (!isValidMapFile()) { throw new IllegalArgumentException("非梦幻地图格式文件!"); }
		try {
			// start decoding
			width = mapFile.readInt2();
			height = mapFile.readInt2();
			horSegmentCount = (int) Math.ceil(width / 320.0);
			verSegmentCount = (int) Math.ceil(height / 240.0);

			// System.out.println("size: " + width + "*" + height);
			// System.out.println("segment: " + horSegmentCount + "*" + horSegmentCount);

			segmentsOffset = new int[horSegmentCount][verSegmentCount];
			jpegDatas = new Object[horSegmentCount][verSegmentCount];
			for (int v = 0; v < verSegmentCount; v++) {
				for (int h = 0; h < horSegmentCount; h++) {
					segmentsOffset[h][v] = mapFile.readInt2();
				}
			}
			// int headerSize = sis.readInt2();// where need it?
		} catch (Exception e) {
			throw new IllegalArgumentException("地图解码失败:" + e.getMessage());
			// e.printStackTrace();
		}
	}

	/**
	 * 获取指定的JPEG数据块
	 * 
	 * @param h
	 *            行
	 * @param v
	 *            列
	 * @return
	 */
	public byte[] getJpegData(int h, int v) {
		try {
			// read jpeg data
			int len;
			byte jpegBuf[] = null;
			mapFile.seek(segmentsOffset[h][v]);// XXX offset
			if (isJPEGData()) {
				len = mapFile.readInt2();
				jpegBuf = new byte[len];
				mapFile.readFully(jpegBuf);
				jpegDatas[h][v] = jpegBuf;
			}

			// modify jpeg data
			ByteArrayOutputStream bos = new ByteArrayOutputStream(4096);
			boolean isFilled = false;// 是否0xFF->0xFF 0x00
			bos.reset();
			jpegBuf = (byte[]) jpegDatas[h][v];
			bos.write(jpegBuf, 0, 2);
			// skip 2 bytes: FF A0
			int p, start;
			isFilled = false;
			for (p = 4, start = 4; p < jpegBuf.length - 2; p++) {
				if (!isFilled && jpegBuf[p] == (byte) 0xFF && jpegBuf[++p] == (byte) 0xDA) {
					isFilled = true;
					// 0xFF 0xDA ; SOS: Start Of Scan
					// ch=jpegBuf[p+3];
					// suppose always like this: FF DA 00 09 03...
					jpegBuf[p + 2] = 12;
					bos.write(jpegBuf, start, p + 10 - start);
					// filled 00 3F 00
					bos.write(0);
					bos.write(0x3F);
					bos.write(0);
					start = p + 10;
					p += 9;
				}
				if (isFilled && jpegBuf[p] == (byte) 0xFF) {
					bos.write(jpegBuf, start, p + 1 - start);
					bos.write(0);
					start = p + 1;
				}
			}
			bos.write(jpegBuf, start, jpegBuf.length - start);
			jpegDatas[h][v] = bos.toByteArray();
		} catch (Exception e) {
			System.err.println("获取JPEG 数据块失败：" + e.getMessage());
		}
		return (byte[]) jpegDatas[h][v];

	}

	private boolean isJPEGData() {
		byte[] buf = new byte[4];
		try {
			int len = mapFile.read();
			mapFile.skipBytes(3 + len * 4);
			mapFile.read(buf);// 47 45 50 4A; GEPJ
			String str = new String(buf);
			return str.equals("GEPJ");
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		return false;
	}

	private boolean isValidMapFile() {
		byte[] buf = new byte[4];
		try {
			mapFile.read(buf);
			String str = new String(buf);
			return str.equals("0.1M");
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		return false;
	}

	public String getFilename() {
		return filename;
	}

	public int getHeight() {
		return height;
	}

	public int getWidth() {
		return width;
	}

	class MyRandomAccessFile extends RandomAccessFile {

		public MyRandomAccessFile(String name, String mode) throws FileNotFoundException {
			super(name, mode);
		}

		public MyRandomAccessFile(File file, String mode) throws FileNotFoundException {
			super(file, mode);
		}

		public int readInt2() throws IOException {
			int ch1 = this.read();
			int ch2 = this.read();
			int ch3 = this.read();
			int ch4 = this.read();
			if ((ch1 | ch2 | ch3 | ch4) < 0)
				throw new EOFException();
			return ((ch1 << 0) + (ch2 << 8) + (ch3 << 16) + (ch4 << 24));
		}
	}

	public int getHorSegmentCount() {
		return horSegmentCount;
	}

	public int getVerSegmentCount() {
		return verSegmentCount;
	}

}
