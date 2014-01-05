/*
 * JavaXYQ Source Code 
 * GameLabelUI GameLabelUI.java
 * by kylixs 2009-10
 * All Rights Reserved.
 * Please see also http://javaxyq.cn or http://javaxyq.googlecode.com.
 * Please email to  javaxyq@qq.com.
 */
package com.javaxyq.ui;

import java.awt.Dimension;

import javax.swing.JComponent;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicLabelUI;

/**
 * @author dewitt
 * 
 */
public class GameLabelUI extends BasicLabelUI {
	private static final GameLabelUI instance = new GameLabelUI();

	public static ComponentUI createUI(JComponent c) {
		return instance;
	}

	@Override
	public Dimension getMaximumSize(JComponent c) {
		return null;
	}

	@Override
	public Dimension getMinimumSize(JComponent c) {
		return null;
	}
}
