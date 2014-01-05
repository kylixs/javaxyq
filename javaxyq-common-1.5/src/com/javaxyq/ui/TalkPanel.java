/*
 * JavaXYQ Engine 
 * 
 * javaxyq@2008 all rights. 
 * http://www.javaxyq.com
 */

package com.javaxyq.ui;

import java.awt.Graphics;
import java.awt.Shape;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import com.javaxyq.core.ResourceStore;
import com.javaxyq.core.Toolkit;
import com.javaxyq.event.PanelEvent;
import com.javaxyq.model.Option;
import com.javaxyq.util.StringUtils;
import com.javaxyq.widget.Sprite;

/**
 * 人物对话面板
 * 
 * @author 龚德伟
 * @history 2008-6-9 龚德伟 新建
 */
public class TalkPanel extends Panel {

	private static final long serialVersionUID = -1282924443598182483L;

	private static final int HEAD_OFFSET = 12;

	private String talker;

	private MouseListener optionHandler = new OptionHandler(this);

	private Option result;

	private static class OptionHandler implements MouseListener {
		private TalkPanel panel;

		public OptionHandler(TalkPanel panel) {
			this.panel = panel;
		}

		@Override
		public void mouseClicked(MouseEvent e) {
			OptionLabel label = (OptionLabel) e.getSource();
			label.getOption().setSelected(true);
			this.panel.setResult(label.getOption());
			this.panel.close();
		}

		@Override
		public void mouseEntered(MouseEvent e) {
		}

		@Override
		public void mouseExited(MouseEvent e) {
		}

		@Override
		public void mousePressed(MouseEvent e) {
		}

		@Override
		public void mouseReleased(MouseEvent e) {
		}

	}

	public TalkPanel(int width, int height) {
		this(width, height, true, true);
	}

	public TalkPanel(int width, int height, boolean closable, boolean movable) {
		super(width, height, closable, movable);
		setClickClosabled(true);
	}

	@Override
	public void paint(Graphics g) {
		super.paint(g);
		try {
			if (this.talker != null) {
				Sprite s = ResourceStore.getInstance().findPhoto(talker);
				Shape oldclip = g.getClip();
				g.translate(-getX(), -getY());
				g.setClip(0, 0, 640, 480);
				s.draw(g, getX() + HEAD_OFFSET, getY()- s.getHeight());
				g.translate(getX(), getY());
				g.setClip(oldclip);
			}
		} catch (Exception e) {
			System.err.println("绘制对话人物头像失败! character="+talker);
			e.printStackTrace();
		}
	}

	@Override
	public void paintImmediately(int x, int y, int w, int h) {
		// super.paintImmediately(x, y, w, h);
	}

	public String getTalker() {
		return talker;
	}

	public void setTalker(String talker) {
		this.talker = talker;
	}

	public void initTalk(String text, Option[] options) {
		this.result = null;
		this.removeAll();
		RichLabel lblText = Toolkit.getInstance().createRichLabel(16, 30, 450, 130, text);
		this.add(lblText);
		int y0 = lblText.getHeight() + lblText.getY() + 10;
		for (int i = 0; options != null && i < options.length; i++) {
			Option option = options[i];
			OptionLabel label = Toolkit.getInstance().createOptionLabel(20, y0 + i * 20, 450, 18, option);
			label.addMouseListener(optionHandler);
			this.add(label);
		}
	}
	/**
	 * @return
	 */
	public Option getResult() {
		return result;
	}

	/**
	 * @param option
	 */
	protected void setResult(Option result) {
		this.result = result;
		if (!processDefaultActions()) {
			PanelEvent evt = new PanelEvent(this, result.getAction(), StringUtils.split(String.valueOf(result
				.getValue()), " "));
			fireEvent(evt);
		}
	}

	/**
	 * @param panel
	 * @param result
	 */
	private boolean processDefaultActions() {
		System.out.println("processDefaultActions: " + result);
		String action = result.getAction();
		if (action != null) {
			if ("close".equals(action)) {
				//this.close();
				return true;
			} else if ("prev".equals(action)) {
				// TODO 上一段内容
				return true;
			} else if ("next".equals(action)) {
				// TODO 下一段内容
				return true;
			}
		}
		return false;
	}
}
