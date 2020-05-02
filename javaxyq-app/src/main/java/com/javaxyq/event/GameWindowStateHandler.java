package com.javaxyq.event;

import lombok.extern.slf4j.Slf4j;

import java.awt.event.WindowEvent;
import java.awt.event.WindowStateListener;

import javax.swing.JFrame;

@Slf4j
public class GameWindowStateHandler implements WindowStateListener {

	public void windowStateChanged(WindowEvent e) {
		// if(e.getNewState()==JFrame.MAXIMIZED_BOTH) {
		// String cmd="com.javaxyq.action.全屏切换";
		// GameMain.actionPerformed(e.getSource(), cmd);
		// }else
	    switch (e.getNewState()) {
        case JFrame.ICONIFIED:
            log.info("游戏窗口被最小化。");
            break;
        case JFrame.NORMAL:
            log.info("游戏窗口恢复正常。");
            break;
        case JFrame.MAXIMIZED_BOTH:
            log.info("游戏处理全屏状态。");
            break;

        default:
            break;
        }
	}

}
