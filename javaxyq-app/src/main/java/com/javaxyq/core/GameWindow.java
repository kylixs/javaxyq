/*
 * JavaXYQ Source Code
 * by kylixs
 * at 2010-2-25
 * please visit http://javaxyq.googlecode.com
 * or mail to kylixs@qq.com
 */
package com.javaxyq.core;

import com.javaxyq.battle.BattleListener;
import com.javaxyq.ui.UIHelper;
import com.javaxyq.widget.Cursor;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowListener;
import java.awt.event.WindowStateListener;
import java.beans.PropertyChangeListener;

/**
 * @author dewitt
 * @date 2010-2-25 create
 */
public interface GameWindow {

    void init(Context context);

    Dimension getSize();

    int getContentWidth();

    int getContentHeight();

    void show();

    void hide();

    void setFullScreen();

    void restoreScreen();

    boolean isFullScreen();

    GameCanvas getCanvas();

    void setCanvas(GameCanvas gameCanvas);

    void setGameCursor(String cursor);

    Cursor getGameCursor();

    void addWindowListener(WindowListener handler);

    void removeWindowListener(WindowListener handler);

    void addWindowStateListener(WindowStateListener handler);

    void removeWindowStateListener(WindowStateListener handler);

    void addPropertyChangeListener(PropertyChangeListener listener);

    void addPropertyChangeListener(String propertyName, PropertyChangeListener listener);

    void removePropertyChangeListener(PropertyChangeListener listener);

    void removePropertyChangeListener(String propertyName, PropertyChangeListener listener);

    Point getMousePosition();

    JLayeredPane getLayeredPane();

    UIHelper getHelper();

    void prepareUI();

    void installUI();

    void installListeners();

    ActionMap getActionMap();

    InputMap getInputMap();

    void addBattleListener(BattleListener listener);

    void removeBattleListener(BattleListener listener);

    void addListener(String type, String className);

    void installWindowListeners();
}
