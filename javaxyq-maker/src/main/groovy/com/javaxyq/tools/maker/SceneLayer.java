/*
 * JavaXYQ Source Code
 * by kylixs
 * at 2009-12-7
 * please visit http://javaxyq.googlecode.com
 * or mail to kylixs@qq.com
 */
package com.javaxyq.tools.maker;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.JPanel;

/**
 * 场景编辑器的图层
 * @author dewitt
 * @date 2009-12-7 create
 */
public class SceneLayer extends JPanel implements MouseListener,MouseMotionListener {
	
	private int cellWidth = 20;
	private int cellHeight = 20;
	private Point selectedCell ;
	private Point selectingCell;
	private Color selectedColor = Color.RED;
	private Color selectingColor = Color.GREEN;
	
	public SceneLayer() {
		super(null);
		setOpaque(false);
		addMouseListener(this);
		addMouseMotionListener(this);
	}
	
	public void clearStatus() {
		this.selectedCell = null;
	}
	
	private Point localToScene(int x,int y) {
		return new Point(x/cellWidth, (getHeight()-y)/cellHeight);
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		if(selectedCell!=null) {
			g.setColor(selectedColor );
			int x0 = selectedCell.x*cellWidth;
			int y0 = getHeight()-cellHeight-selectedCell.y*cellHeight;
			g.drawRect(x0, y0, cellWidth, cellHeight);
			g.drawRect(x0+1, y0+1, cellWidth-2, cellHeight-2);
		}
		if(selectingCell!=null) {
			g.setColor(selectingColor );
			int x0 = selectingCell.x*cellWidth;
			int y0 = getHeight()-cellHeight- selectingCell.y*cellHeight;
			g.drawRect(x0, y0, cellWidth, cellHeight);
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		selectingCell = null;
		repaint();
	}

	@Override
	public void mousePressed(MouseEvent e) {
		selectedCell = localToScene(e.getX(),e.getY());
		repaint();
	}

	@Override
	public void mouseReleased(MouseEvent e) {
	}

	@Override
	public void mouseDragged(MouseEvent e) {
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		selectingCell = localToScene(e.getX(),e.getY());
		repaint();
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

	public Point getSelectedCell() {
		return selectedCell;
	}

	public void setSelectedCell(Point selectedCell) {
		this.selectedCell = selectedCell;
	}

	public Point getSelectingCell() {
		return selectingCell;
	}

	public void setSelectingCell(Point selectingCell) {
		this.selectingCell = selectingCell;
	}

	public Color getSelectedColor() {
		return selectedColor;
	}

	public void setSelectedColor(Color selectedColor) {
		this.selectedColor = selectedColor;
	}

	public Color getSelectingColor() {
		return selectingColor;
	}

	public void setSelectingColor(Color selectingColor) {
		this.selectingColor = selectingColor;
	}
	
	
}
