package com.javaxyq.io;

import java.io.ByteArrayInputStream;
import java.io.IOException;

/**
 * 可跳到指定位置的ByteArrayInputStrem<br>
 * seek(int pos)
 * 
 * @author Langlauf
 * @date
 */
public class RandomAcessInputStream extends ByteArrayInputStream {
    
	public RandomAcessInputStream(byte[] buf) {
		super(buf);
	}

	public RandomAcessInputStream(byte[] buf, int offset, int length) {
		super(buf, offset, length);
	}

	public void seek(int pos) {
		if (pos < 0 || pos > this.count) { throw new IndexOutOfBoundsException("" + pos + ":" + this.count); }
		this.pos = pos;
	}

	public long getPosition() {
		return this.pos;
	}

	public void close() {
	    this.buf = null;
	    this.count = 0;
		System.gc();
	}

	public int readInt() throws IOException {
		int ch1 = read();
		int ch2 = read();
		int ch3 = read();
		int ch4 = read();
		return (ch1 + (ch2 << 8) + (ch3 << 16) + (ch4 << 24));
	}

	public short readUnsignedShort() throws IOException {
		int ch1 = read();
		int ch2 = read();
		return (short) ((ch2 << 8) + ch1);
	}
	
	public boolean readFully(byte[]buf) throws IOException {
		read(buf);
		return false;
	}
}
