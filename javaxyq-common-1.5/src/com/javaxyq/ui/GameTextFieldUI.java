package com.javaxyq.ui;

import java.awt.Dimension;

import javax.swing.JComponent;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicTextFieldUI;

public class GameTextFieldUI extends BasicTextFieldUI {
	private static final GameTextFieldUI instance = new GameTextFieldUI();

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
