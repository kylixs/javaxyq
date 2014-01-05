package com.javaxyq.ui;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JPanel;

public class FloatPanel extends JPanel {

    private static final long serialVersionUID = 1L;

    private RichLabel label;

    private long createTime;

    public FloatPanel(String text) {
        setBorder(null);
        setLayout(null);
        setOpaque(false);
        setIgnoreRepaint(true);
        setFocusable(false);
        label = new RichLabel(text);
        label.setLocation(4, 3);
        Dimension d = label.computeSize(14 * 7);
        label.setSize(d);
        setSize(8 + d.width, 6 + d.height);
        add(label);
        this.createTime = System.currentTimeMillis();
    }

    public FloatPanel() {
        this(null);
    }

    @Override
    public void paint(Graphics g) {
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f));
        g2d.setColor(Color.BLACK);
        g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
        g2d.dispose();
        Component[] comps = getComponents();
        for (Component c : comps) {
            Graphics g2 = g.create(c.getX(), c.getY(), c.getWidth(), c.getHeight());
            c.paint(g2);
            g2.dispose();
        }
    }

    public void setText(String chatText) {
        label.setText(chatText);
        Dimension d = label.computeSize(14 * 7);
        label.setSize(d);
        setSize(8 + d.width, 6 + d.height);
    }

    public long getCreateTime() {
        return createTime;
    }
	@Override
	public void paintImmediately(int x, int y, int w, int h) {
		//super.paintImmediately(x, y, w, h);
	}
}
