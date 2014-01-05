/*
 * JavaXYQ Source Code 
 * TranslucentTooltipUI TranslucentTooltipUI.groovy
 * by kylixs 2009-10
 * All Rights Reserved.
 * Please see also http://javaxyq.cn or http://javaxyq.googlecode.com.
 * Please email to  javaxyq@qq.com.
 */
package com.javaxyq.ui;


import java.awt.*;
import javax.swing.JComponent;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.metal.MetalToolTipUI;


/**
 * @author dewitt
 *
 */
public class TranslucentTooltipUI extends MetalToolTipUI {
	private static TranslucentTooltipUI sharedInstance = new TranslucentTooltipUI();
	/**
	 * 
	 */
	public TranslucentTooltipUI() {
		// TODO Auto-generated constructor stub
		System.out.println("init tooltip ui");
	}
	
	public static ComponentUI createUI(JComponent c) {
		return sharedInstance;
	}
		
	public void paint(Graphics g, JComponent c) {
		c.setOpaque(false);
		Graphics2D g2d = (Graphics2D) g.create();
		g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f));
		g2d.setColor(Color.BLACK);
		g2d.fillRoundRect(0, 0, c.getWidth(), c.getHeight(), 10, 10);
		g2d.dispose();
		super.paint(g,c);
	}
	
}
