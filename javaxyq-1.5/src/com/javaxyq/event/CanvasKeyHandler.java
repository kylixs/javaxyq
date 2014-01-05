package com.javaxyq.event;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import com.javaxyq.core.ApplicationHelper;

/**
 * @author 龚德伟
 * @history 2008-5-11 龚德伟 新建
 */
public class CanvasKeyHandler implements KeyListener {

    public void keyPressed(KeyEvent e) {
        int keycode = e.getKeyCode();
        String actionId = null;
        switch (keycode) {
        case KeyEvent.VK_LEFT:
            actionId = "com.javaxyq.action.MoveLeft";
            break;
        case KeyEvent.VK_UP:
            actionId = "com.javaxyq.action.MoveUp";
            break;
        case KeyEvent.VK_RIGHT:
            actionId = "com.javaxyq.action.MoveRight";
            break;
        case KeyEvent.VK_DOWN:
            actionId = "com.javaxyq.action.MoveDown";
            break;
//        case KeyEvent.VK_TAB://打开小地图
//        	actionId = "com.javaxyq.action.dialog.scene_map";
//        	break;
        default:
            //actionId = (String) GameMain.getInputMap().get(KeyStroke.getKeyStroke(keycode, e.getModifiers()));
        }
        if (actionId == null) {
            return;
        }
        //System.out.println("key event: "+actionId);
        ApplicationHelper.getApplication().doAction(e.getSource(), actionId);
    }

    public void keyReleased(KeyEvent e) {
        String actionId = "com.javaxyq.action.Stop";
        ApplicationHelper.getApplication().doAction(e.getSource(), actionId);
    }

    public void keyTyped(KeyEvent e) {
    }

}
