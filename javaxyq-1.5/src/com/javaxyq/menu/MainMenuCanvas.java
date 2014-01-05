/**
 * 
 */
package com.javaxyq.menu;

import java.awt.Image;
import java.util.Random;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

import com.javaxyq.core.Canvas;
import com.javaxyq.ui.UIHelper;

/**
 * Ö÷²Ëµ¥
 * @author gongdewei
 * @date 2011-5-1 create
 */
public class MainMenuCanvas extends Canvas {

	private static final long serialVersionUID = -8251534211862320637L;

	/**
	 * @param width
	 * @param height
	 */
	public MainMenuCanvas(Image content, int width, int height) {
		super(width, height);
		JLabel label = new JLabel(new ImageIcon(content));
		label.setSize(content.getWidth(label), content.getHeight(label));
		add(label);
	}

	protected String getMusic() {
		Random rand = new Random();
		String[] files = new String[] {"1091","1514","1070","1193"};
		return ("music/"+files[rand.nextInt(100)%files.length]+".mp3");
	}
}
