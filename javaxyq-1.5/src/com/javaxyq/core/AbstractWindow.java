/*
 * JavaXYQ Source Code
 * by kylixs
 * at 2010-6-17
 * please visit http://javaxyq.googlecode.com
 * or mail to kylixs@qq.com
 */
package com.javaxyq.core;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.WindowListener;
import java.awt.event.WindowStateListener;
import java.beans.PropertyChangeListener;

import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JLayeredPane;

import com.javaxyq.battle.BattleListener;
import com.javaxyq.ui.UIHelper;
import com.javaxyq.widget.Cursor;

/**
 * @author gongdewei
 * @date 2010-6-17 create
 */
public class AbstractWindow implements GameWindow {

	private Cursor cursor;

	@Override
	public void addPropertyChangeListener(PropertyChangeListener listener) {
	}

	@Override
	public void addPropertyChangeListener(String propertyName, PropertyChangeListener listener) {
	}

	@Override
	public void addWindowListener(WindowListener handler) {
	}

	@Override
	public void addWindowStateListener(WindowStateListener handler) {
	}

	@Override
	public GameCanvas getCanvas() {
		return null;
	}
	@Override
	public void setGameCursor(String name) {
		Cursor cursor = UIHelper.getCursor(name);
		if(cursor!=null) {
			this.cursor = cursor;
		}
	}
	
	public Cursor getGameCursor() {
		return this.cursor;
	}
	@Override
	public JLayeredPane getLayeredPane() {
		return null;
	}

	@Override
	public Point getMousePosition() {
		return null;
	}

	@Override
	public void init(Context context) {
		context.setWindow(this);
		
	}

	@Override
	public boolean isFullScreen() {
		return false;
	}

	@Override
	public void removePropertyChangeListener(PropertyChangeListener listener) {
	}

	@Override
	public void removePropertyChangeListener(String propertyName, PropertyChangeListener listener) {
	}

	@Override
	public void removeWindowListener(WindowListener handler) {
	}

	@Override
	public void removeWindowStateListener(WindowStateListener handler) {
	}

	@Override
	public void restoreScreen() {
	}

	@Override
	public void setCanvas(GameCanvas gameCanvas) {
	}

	@Override
	public void setFullScreen() {
	}

	@Override
	public Dimension getSize() {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public void hide() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void show() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addBattleListener(BattleListener listener) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public ActionMap getActionMap() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public UIHelper getHelper() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public InputMap getInputMap() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void installListeners() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void prepareUI() {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void installUI() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void removeBattleListener(BattleListener listener) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addListener(String type, String className) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int getContentHeight() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getContentWidth() {
		// TODO Auto-generated method stub
		return 0;
	}

}
