/*
 * JavaXYQ Engine 
 * 
 * javaxyq@2008 all rights. 
 * http://www.javaxyq.com
 */

package com.javaxyq.ui;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import com.javaxyq.model.Option;

/**
 * @author 龚德伟
 * @history 2008-6-9 龚德伟 新建
 */
public class OptionLabel extends RichLabel {

	private static final long serialVersionUID = -2826555036505106535L;
	private Option option;
    private static EventHandler eventHandler = new EventHandler();

    public Option getOption() {
		return option;
	}

	public void setOption(Option option) {
		this.option = option;
	}

	public OptionLabel(Option option) {
        super("#R"+option.getText());
        this.option = option;
        this.addMouseListener(eventHandler);
        this.addMouseMotionListener(eventHandler);
    }

    private static class EventHandler implements MouseListener, MouseMotionListener {

        public void mouseClicked(MouseEvent e) {
        }

        public void mouseDragged(MouseEvent e) {
        }

        public void mousePressed(MouseEvent e) {
            RichLabel label = (RichLabel) e.getSource();
            label.setLocation(label.getX() + 1, label.getY() + 1);
        }

        public void mouseReleased(MouseEvent e) {
            RichLabel label = (RichLabel) e.getSource();
            label.setLocation(label.getX() - 1, label.getY() - 1);
        }

        public void mouseMoved(MouseEvent e) {
            //RichLabel label = (RichLabel) e.getSource();
            //String text = label.getText();
            //label.setText(text.replaceAll("#R", "#G"));
        }

        public void mouseExited(MouseEvent e) {
            RichLabel label = (RichLabel) e.getSource();
            String text = label.getText();
            label.setText(text.replaceAll("#G", "#R"));
        }

        public void mouseEntered(MouseEvent e) {
            RichLabel label = (RichLabel) e.getSource();
            String text = label.getText();
            label.setText(text.replaceAll("#R", "#G"));
        }

    }

	@Override
	public void paintImmediately(int x, int y, int w, int h) {
		//super.paintImmediately(x, y, w, h);
	}
}
