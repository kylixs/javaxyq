package com.javaxyq.resources;


public class MaskUnit {

	private int x;
	private int y;
	private int width;
	private int height;
	private int[] data;
	
	public MaskUnit(int x, int y, int width, int height, int[] data) {
		super();
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.data = data;
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

	public int[] getData() {
		return data;
	}

}
