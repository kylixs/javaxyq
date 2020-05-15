
/*
 * JavaXYQ Source Code 
 * SimpleCanvas SimpleCanvas.groovy
 * by kylixs 2009-10
 * All Rights Reserved.
 * Please see also http://javaxyq.cn or http://javaxyq.googlecode.com.
 * Please email to  javaxyq@qq.com.
 */

package com.javaxyq.tools;

import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.JPanel;

/**
 * @author dewitt
 *
 */
public class SimpleCanvas extends JPanel {

	private static final long serialVersionUID = -2239429873915188963L;
	
	public SimpleCanvas() {
		super(null,false);
		setIgnoreRepaint(true);
		setFocusable(true);
		setPreferredSize(new Dimension(640,480));
		setSize(new Dimension(640,480));
	}
	
	public void paint(Graphics g) {
		Graphics co = g.create();
		paintComponent(co);
		paintBorder(co);
		paintChildren(co);
		co.dispose();
		//System.out.println("paint panel ...");
	}	
	
	public void repaint(long tm, int x, int y, int width, int height) {
		//println "repaint canvas ($x,$y,$width,$height)"
	}
	
	public void paintImmediately(int x,int y,int w, int h) {
		
	}
	
	public void paintAll(Graphics g) {
		
	}
	public void paintComponents(Graphics g) {};	
	
}
