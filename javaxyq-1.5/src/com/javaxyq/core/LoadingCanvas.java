/**
 * 
 */
package com.javaxyq.core;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.util.Random;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.javaxyq.util.UIUtils;

/**
 * @author dewitt
 *
 */
public class LoadingCanvas extends Canvas {
	private static final long serialVersionUID = -2718674869979598034L;
	private String loadingText = "";
	private Image contentImage;

	public LoadingCanvas( Image content, int width, int height) {
		super(width, height);
		JLabel label = new JLabel(new ImageIcon(content));
		label.setSize(content.getWidth(label), content.getHeight(label));
		add(label);
		setContent(content);
		updateInterval = 50;
	}

	public void setContent(Image img) {
		this.contentImage = img;
	}

	public void setLoading(String text) {
		this.loadingText = text;
	}


	public void showImage(Image img, long t) {
		this.fadeOut(300);
		// wait
		waitForFading();
		this.loadingText = "";
		this.setContent(img);
		this.fadeIn(200);
		waitForFading();
		// wait ,delay
		synchronized (FADE_LOCK) {
			try {
				Thread.sleep(t);
			} catch (InterruptedException e) {
			}
		}

	}
	
	public void draw(Graphics g, long elapsedTime) {
		if(contentImage != null) {
			g.drawImage(contentImage, 0, 0, null);
		}else {
			//g.setColor(Color.LIGHT_GRAY);
			//g.fillRect(0, 0, getWidth(), getHeight());
			g.clearRect(0, 0, getWidth(), getHeight());
		}
		// draw fade
//		g.setColor(new Color(0, 0, 0, alpha));
//		g.fillRect(0, 0, getWidth(), getHeight());

//		g.setFont(UIUtils.TEXT_FONT);
//		drawCursor(g,elapsedTime);
//		drawDebug(g);
//		drawDownloading(g);

		g.setColor(Color.WHITE);
		g.setFont(new Font("»ªÎÄÐÂÎº", Font.PLAIN, 20));
		g.drawString(loadingText, 250, 420);
		//System.out.println("draw loading ...");
		
		super.draw(g, elapsedTime);
	}

	protected String getMusic() {
		Random rand = new Random();
		String[] files = new String[] {"1091","1514","1070","1193"};
		return ("music/"+files[rand.nextInt(files.length)]+".mp3");
	}

}
