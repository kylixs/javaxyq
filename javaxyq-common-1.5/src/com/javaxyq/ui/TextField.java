package com.javaxyq.ui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import com.javaxyq.event.EventDelegator;
import com.javaxyq.util.UIUtils;

public class TextField extends JTextField {

	private static final long serialVersionUID = 5663220885858934210L;
	private static final String uiClassID = "GameTextFieldUI";
	static {
		UIManager.put("GameTextFieldUI", GameTextFieldUI.class.getName());
	}
	private List<String> historyInputs;

	private int historyIndex = -1;

	private int maxHistoryCount = 15;

	/** 是否保留历史记录 */
	private boolean history = true;

	private class Handler extends MouseAdapter implements KeyListener, ActionListener {
		public void mouseEntered(MouseEvent e) {
			//TODO set text cursor
//			try {
//				GameMain.setCursor(com.javaxyq.widget.Cursor.TEXT_CURSOR);
//			} catch (Exception e1) {
//			}
		}

		public void mouseExited(MouseEvent e) {
//			try {
//				GameMain.restoreCursor();
//			} catch (Exception e1) {
//			}
		}

		public void keyPressed(KeyEvent e) {
			int keyCode = e.getKeyCode();
			switch (keyCode) {
			case KeyEvent.VK_ENTER:
				historyIndex = -1;
				addHistoryInput(getText());
				return;
			case KeyEvent.VK_ESCAPE:
				historyIndex = -1;
				setText("");
				return;
			}

			int historyCount = historyInputs.size();
			if (historyCount == 0) {
				return;
			}
			switch (keyCode) {
			case KeyEvent.VK_UP:
				if (historyIndex < historyCount - 1) {
					historyIndex++;
					setText(historyInputs.get(historyIndex));
				}
				break;
			case KeyEvent.VK_DOWN:
				if (historyIndex > 0) {
					historyIndex--;
					setText(historyInputs.get(historyIndex));
				} else {
					historyIndex = -1;
					setText("");
				}
				break;

			default:
				break;
			}
		}

		public void actionPerformed(final ActionEvent e) {
			SwingUtilities.invokeLater(new Runnable() {
				public void run() {
					EventDelegator.getInstance().delegateEvent(e);
				}});
		}

		public void keyReleased(KeyEvent e) {
		}

		public void keyTyped(KeyEvent e) {
		}
	}

	private Handler handler = new Handler();

	public TextField() {
		this("");
	}

	public TextField(String text) {
		super(text);
		this.historyInputs = new ArrayList<String>();
		setFont(UIUtils.TEXT_FONT);
		setForeground(Color.WHITE);
		setCaretColor(Color.WHITE);
		setBorder(null);
		setOpaque(false);
		//setBackground(Color.LIGHT_GRAY);
//		try {
//			setCursor(Cursor.getSystemCustomCursor("BLANK_CURSOR"));
//		} catch (HeadlessException e) {
//			e.printStackTrace();
//		} catch (AWTException e) {
//			e.printStackTrace();
//		}
		//this.addMouseListener(handler);
		//this.addKeyListener(handler);
		//this.addActionListener(handler);
	}

	@Override
	public String getUIClassID() {
		return uiClassID;
	}
	
	public boolean isHistory() {
		return history;
	}

	public void setHistory(boolean history) {
		this.history = history;
	}

	public void addHistoryInput(String text) {
		this.historyInputs.add(0, text);
		if (this.historyInputs.size() > maxHistoryCount) {
			this.historyInputs.remove(this.historyInputs.size() - 1);
		}
	}

	public int getMaxHistoryCount() {
		return maxHistoryCount;
	}

	public void setMaxHistoryCount(int maxHistoryCount) {
		this.maxHistoryCount = maxHistoryCount;
	}

	public List<String> getHistoryInputs() {
		return historyInputs;
	}

	@Override
	public void paintImmediately(int x, int y, int w, int h) {
		// super.paintImmediately(x, y, w, h);
	}

	@Override
	public void invalidate() {
		//super.invalidate();
	}
	
	@Override
	public void validate() {
		//super.validate();
	}
	
	@Override
	public void revalidate() {
		//super.revalidate();
	}
	@Override
	public void updateUI() {
		super.updateUI();
	}
	@Override
	public void update(Graphics g) {
		super.update(g);
	}
}
