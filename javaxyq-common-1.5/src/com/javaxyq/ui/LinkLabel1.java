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


/**
 * @author 龚德伟
 * @history 2008-6-9 龚德伟 新建
 */
public class LinkLabel1 extends RichLabel {

	private static final long serialVersionUID = -923795088598815398L;

	private String action;

    private String arguments;

    public LinkLabel1(String text, String action, String args) {
        super(text);
        this.action = action;
        this.arguments = args;
        EventHandler eventHandler = new EventHandler();
        this.addMouseListener(eventHandler);
        this.addMouseMotionListener(eventHandler);
    }

    private class EventHandler implements MouseListener, MouseMotionListener {

        public void mouseClicked(MouseEvent e) {
//            if (action != null) {
//                Panel dlg = (Panel) getParent();
//                if ("close".equals(action)) {
//                    dlg.close();
//                } else if ("open".equals(action)) {
//                    //GameMain.doAction(LinkLabel.this, "com.javaxyq.action.dialog." + arguments, null);
//                	UIHelper.showDialog(arguments);
//                    dlg.close();
//                } else if ("talk".equals(action)) {
//                    //FIXME 改进对话事件
//                    //TalkPanel t = (TalkPanel) dlg;
//                    //System.out.println("fire talk: "+arguments);
//                    GameMain.getTalker().fireEvent(new PlayerEvent(GameMain.getTalker(), PlayerEvent.TALK, arguments));
//                    //dlg.close();
//                } else {
//                    GameMain.doAction(GameMain.getTalker(), "com.javaxyq.action." + action, arguments.split(" "));
//                    //dlg.close();
//                }
//            }
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

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getArguments() {
        return arguments;
    }

    public void setArguments(String arguments) {
        this.arguments = arguments;
    }
	@Override
	public void paintImmediately(int x, int y, int w, int h) {
		//super.paintImmediately(x, y, w, h);
	}
}
