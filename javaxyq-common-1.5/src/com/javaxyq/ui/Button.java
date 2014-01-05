package com.javaxyq.ui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.util.List;

import javax.swing.AbstractButton;
import javax.swing.Action;
import javax.swing.ButtonModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JToolTip;
import javax.swing.UIManager;
import javax.swing.plaf.basic.BasicButtonUI;

import com.javaxyq.event.EventDelegator;
import com.javaxyq.util.UIUtils;
import com.javaxyq.widget.Frame;
import com.javaxyq.widget.Sprite;

/**
 * 自定义的Button类<br>
 * 使用游戏资源来显示
 * 
 * @author gdw
 * @date
 */
public class Button extends JButton {

	private static final long serialVersionUID = -1440948304007652916L;
	private static final String uiClassID = "GameButtonUI";
	static {
		UIManager.put("GameButtonUI", "com.javaxyq.ui.GameButtonUI");
	}

	/**
	 * 按钮按下时是否自动向右下偏移
	 */
	private boolean autoOffset = false;
	private String tooltipTpl;
	private TooltipTemplate template;

	public Button() {

	}

	public Button(Action action) {
		super(action);
	}

	/**
	 * 设置一个包括4帧图片的数组作为按钮的4种状态,0-3依次为normal,pressed,rollover,disabled
	 * 
	 * @param frames
	 */
	public Button(int width, int height, List<Frame> frames) {
		setSize(width, height);
		init(frames);
	}

	public Button(Sprite sprite) {
		init(sprite);
	}

	@Override
	public String getUIClassID() {
		return uiClassID;
	}

	private void init(List<Frame> frames) {
		// changed ui
		setUI(new CustomButtonUI());
		setFont(UIUtils.TEXT_FONT);
		setForeground(Color.WHITE);
		setHorizontalTextPosition(JButton.CENTER);
		setVerticalTextPosition(JButton.CENTER);
		setHorizontalAlignment(JButton.CENTER);
		setVerticalAlignment(JButton.CENTER);
		// changed viewer properties
		setIgnoreRepaint(true);
		setBorder(null);
		// setOpaque(false);
		setFocusable(false);
		setContentAreaFilled(false);
		// set icons
		try {
			int frameCount = frames.size();
			this.setAutoOffset(frameCount < 3);
			if (frameCount > 0) {
				setIcon(new ImageIcon(frames.get(0).getImage()));
			}
			if (frameCount > 1) {
				setPressedIcon(new ImageIcon(frames.get(1).getImage()));
			}
			if (frameCount > 2) {
				setRolloverIcon(new ImageIcon(frames.get(2).getImage()));
			}
			if (frameCount > 3) {
				setDisabledIcon(new ImageIcon(frames.get(3).getImage()));
			}
		} catch (Exception e) {
			System.err.println("创建Button失败!");
			e.printStackTrace();
		}
	}

	public void init(Sprite sprite) {
		setSize(sprite.getWidth(), sprite.getHeight());
		List<Frame> frames = sprite.getAnimation(0).getFrames();
		init(frames);
	}

	public boolean isAutoOffset() {
		return autoOffset;
	}

	// 碰撞检测
	// public boolean contains(int x, int y) {
	// return super.contains(x, y);
	// }

	/**
	 * 按钮按下时是否自动向右下偏移
	 */
	public void setAutoOffset(boolean autoOffset) {
		this.autoOffset = autoOffset;
	}

	static class CustomButtonUI extends BasicButtonUI {
		@Override
		protected void paintIcon(Graphics g, JComponent c, Rectangle iconRect) {
			Button btn = (Button) c;
			ButtonModel model = btn.getModel();
			if (btn.isAutoOffset() && model.isArmed() && model.isPressed()) {
				defaultTextShiftOffset = 1; // down
			} else {
				defaultTextShiftOffset = 0;
			}
			setTextShiftOffset();
			super.paintIcon(g, c, iconRect);
		}

		protected void paintText(Graphics g, JComponent c, Rectangle textRect, String text) {
			AbstractButton b = (AbstractButton) c;
			ButtonModel model = b.getModel();
			if (model.isArmed() && model.isPressed()) {
				defaultTextShiftOffset = 1; // down
			} else {
				defaultTextShiftOffset = 0;
			}
			setTextShiftOffset();
			super.paintText(g, c, textRect, text);
		}

	}

	public JToolTip createToolTip() {
		LucidToolTip tip = new LucidToolTip();
        tip.setComponent(this);
		return tip;
	}

	@Override
	public String getToolTipText() {
		if(tooltipTpl !=null && tooltipTpl.trim().length()>0 && template!=null) {
			return template.getTooltipText(tooltipTpl);
		}
		return super.getToolTipText();
	}

	public String getTooltipTpl() {
		return tooltipTpl;
	}

	public void setTooltipTpl(String tooltipTpl) {
		this.tooltipTpl = tooltipTpl;
		LightweightToolTipManager toolTipManager = LightweightToolTipManager.sharedInstance();
		if (tooltipTpl != null) {
			toolTipManager.registerComponent(this);
		} else {
			toolTipManager.unregisterComponent(this);
		}
	}

	public void setToolTipText(String text) {
		putClientProperty(TOOL_TIP_TEXT_KEY, text);
	}

	public void setTemplate(TooltipTemplate template) {
		this.template = template;
	}

	@Override
	public void paintImmediately(int x, int y, int w, int h) {
		// super.paintImmediately(x, y, w, h);
	}

	@Override
	protected void fireActionPerformed(ActionEvent event) {
		super.fireActionPerformed(event);
		EventDelegator.getInstance().delegateEvent(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, getActionCommand()));
	}

}
