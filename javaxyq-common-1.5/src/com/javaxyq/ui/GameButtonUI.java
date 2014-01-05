/*
 * JavaXYQ Source Code 
 * GameButtonUI GameButtonUI.java
 * by kylixs 2009-10
 * All Rights Reserved.
 * Please see also http://javaxyq.cn or http://javaxyq.googlecode.com.
 * Please email to  javaxyq@qq.com.
 */
package com.javaxyq.ui;

import java.awt.Dimension;

import javax.swing.JComponent;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicButtonUI;

/**
 * @author dewitt
 * 
 */
public class GameButtonUI extends BasicButtonUI {
	private static final GameButtonUI instance = new GameButtonUI();

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
