package com.javaxyq.resources;

public class MapUnit {

	private int[] maskIndexs;
	private byte[] cellData;
	
	public MapUnit(int[] masklist, byte[] mCell) {
		this.maskIndexs = masklist;
		this.cellData = mCell;
	}

	public int[] getMaskIndexs() {
		return maskIndexs;
	}

	public byte[] getCellData() {
		return cellData;
	}
	
}
