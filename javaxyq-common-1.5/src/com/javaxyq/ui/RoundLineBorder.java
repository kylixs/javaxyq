package com.javaxyq.ui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Insets;

import javax.swing.border.AbstractBorder;

public class RoundLineBorder extends AbstractBorder{
	private static final long serialVersionUID = -710038999584689482L;
	private Color color;
	private int thickness;
	private int arcWidth;
	private int arcHeight;
	public RoundLineBorder(Color color,int thickness,int arcWidth, int arcHeight) {
		this.color = color;
		this.thickness = thickness;
		this.arcWidth = arcWidth;
		this.arcHeight = arcHeight;
	}
	
	public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
		//Graphics2D g2d = (Graphics2D) g.create();
		//g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f));
		//g2d.setColor(Color.BLACK);
		//g2d.fillRoundRect(x+1, y+1, width-2, height-2, arcWidth, arcHeight);
		//g2d.dispose();
		//g.setColor(background);
		//g.fillRoundRect(x+1, y+1, width-2, height-2, arcWidth, arcHeight);
		g.setColor(this.color);
		for(int i=0;i<thickness;i++) {
			g.drawRoundRect(x+i, y+i, width-1-i-i, height-1-i-i, arcWidth, arcHeight);
		}
	}
	
	public Insets getBorderInsets(Component c)       { 
		return new Insets(0, 0, 0, 0);
	}
	
}
