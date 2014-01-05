package com.javaxyq.event;

import java.awt.event.WindowEvent;
import java.awt.event.WindowStateListener;

import javax.swing.JFrame;

public class GameWindowStateHandler implements WindowStateListener {

	public void windowStateChanged(WindowEvent e) {
		// if(e.getNewState()==JFrame.MAXIMIZED_BOTH) {
		// String cmd="com.javaxyq.action.全屏切换";
		// GameMain.actionPerformed(e.getSource(), cmd);
		// }else
	    switch (e.getNewState()) {
        case JFrame.ICONIFIED:
            System.out.println("游戏窗口被最小化。");
            break;
        case JFrame.NORMAL:
            System.out.println("游戏窗口恢复正常。");
            break;
        case JFrame.MAXIMIZED_BOTH:
            System.out.println("游戏处理全屏状态。");
            break;

        default:
            break;
        }
	}

}
