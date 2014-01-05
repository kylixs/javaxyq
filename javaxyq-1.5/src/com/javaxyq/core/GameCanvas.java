/*
 * JavaXYQ Source Code
 * by kylixs
 * at 2010-5-29
 * please visit http://javaxyq.googlecode.com
 * or mail to kylixs@qq.com
 */
package com.javaxyq.core;

import java.awt.Point;

import javax.swing.JComponent;

import com.javaxyq.util.MP3Player;
import com.javaxyq.widget.Animation;
import com.javaxyq.widget.Cursor;
import com.javaxyq.widget.Player;

/**
 * @author gongdewei
 * @date 2010-5-29 create
 */
public interface GameCanvas {
	
	JComponent getComponent();
	
	void setWindow(GameWindow window);
	GameWindow getWindow();

	Point localToView(Point p);
	
	Point viewToLocal(Point p);

	Point localToScene(Point p);
	
	Point sceneToLocal(Point p);
	
	Point sceneToView(Point p);

	Point viewToScene(Point p);
	
	boolean isHover(Player player);
	
	void setMovingObject(Animation anim, Point offset);
	void removeMovingObject();
	void setGameCursor(Cursor cursor);
	Cursor getGameCursor();
	
	void playMusic();
	void stopMusic();
}
