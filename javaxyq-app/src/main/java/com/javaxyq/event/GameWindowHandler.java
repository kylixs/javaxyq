package com.javaxyq.event;

import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import com.javaxyq.core.Application;
import com.javaxyq.core.ApplicationHelper;

public class GameWindowHandler implements WindowListener {

	public void windowActivated(WindowEvent e) {
	}

	public void windowClosed(WindowEvent e) {
	}

	public void windowClosing(WindowEvent e) {
		/*Application application = ApplicationHelper.getApplication();
		if(application.getSceneCanvas() != null) {
			application.getUIHelper().showDialog("game_exit");
		}else {
			application.shutdown();
		}*/
		System.exit(0);
	}

	public void windowDeactivated(WindowEvent e) {
	}

	public void windowDeiconified(WindowEvent e) {
	}

	public void windowIconified(WindowEvent e) {
	}

	public void windowOpened(WindowEvent e) {
	}

}
