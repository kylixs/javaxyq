/*
 * JavaXYQ Source Code
 * by kylixs
 * at 2009-12-6
 * please visit http://javaxyq.googlecode.com
 * or mail to kylixs@qq.com
 */
package com.javaxyq.tools.maker;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

import javax.swing.JPanel;

/**
 * 场景单元格面板
 * @author dewitt
 * @date 2009-12-6 create
 */
public class CellPanel extends JPanel {

	private static final long serialVersionUID = 1L;
	
	private int cellWidth = 20;
	private int cellHeight = 20;
	
	private Color lineColor = new Color(128, 128,128);
	private int lineWidth = 1;
	
	public CellPanel() {
		setOpaque(false);
	}

	@Override
	protected void paintComponent(Graphics g) {
		long t1 = System.currentTimeMillis();
		Rectangle rect = getVisibleRect();
		int startX = rect.x - rect.x%cellWidth;
		int startY = rect.y - rect.y%cellHeight;
		int maxX = rect.x+rect.width;
		int maxY = rect.y + rect.height;
		
		for (int x = startX; x < maxX; x+=cellWidth) {
			drawLine(g,x, rect.y, x, maxY);
		}
		for (int y = startY; y < maxY; y+=cellHeight) {
			drawLine(g,rect.x, y, maxX,y);
		}
		long t2 = System.currentTimeMillis();
		if(t2-t1>100) System.out.println("draw grid: "+(t2-t1));
	}
	
	private void drawLine(Graphics g, int x1,int y1,int x2,int y2) {
		for (int i = 0; i < lineWidth; i++) {
			//g.drawLine(x1, y1, x2, y2);
			g.setColor(lineColor);
			g.drawLine(x1+i, y1+i, x2+i, y2+i);
		}
	}

	public int getCellWidth() {
		return cellWidth;
	}

	public void setCellWidth(int cellWidth) {
		this.cellWidth = cellWidth;
	}

	public int getCellHeight() {
		return cellHeight;
	}

	public void setCellHeight(int cellHeight) {
		this.cellHeight = cellHeight;
	}

	public Color getLineColor() {
		return lineColor;
	}

	public void setLineColor(Color lineColor) {
		this.lineColor = lineColor;
	}

	public int getLineWidth() {
		return lineWidth;
	}

	public void setLineWidth(int lineWidth) {
		this.lineWidth = lineWidth;
	}
}
