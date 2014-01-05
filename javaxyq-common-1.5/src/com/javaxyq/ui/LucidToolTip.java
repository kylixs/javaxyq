/**
 * 
 */
package com.javaxyq.ui;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JToolTip;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;

import com.javaxyq.util.UIUtils;

/**
 * 半透明提示组件
 * @author gongdewei
 * @date 2011-4-30 create
 */
public class LucidToolTip extends JToolTip {

	private static final long serialVersionUID = -3192193036242819590L;

	public LucidToolTip() {
		Color foreground = Color.WHITE;
		setFont(UIUtils.TEXT_FONT);
		setForeground(foreground);
		setOpaque(false);
		setBorder(new CompoundBorder(new RoundLineBorder(foreground,1, 8, 8),new EmptyBorder(2, 2, 2, 2)));
	}

	@Override
	public void paint(Graphics g) {
		Graphics2D g2d = (Graphics2D) g.create();
		g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f));
		g2d.setColor(Color.BLACK);
		g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
		g2d.dispose();
		super.paint(g);
	}
}
