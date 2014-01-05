package com.javaxyq.action;

import com.javaxyq.core.ApplicationHelper;
import com.javaxyq.core.GameCanvas;
import com.javaxyq.core.GameWindow;
import com.javaxyq.core.SceneCanvas;
import com.javaxyq.event.ActionEvent;

/**
 * 默认的传送事件处理器
 * @author dewitt
 */
public class DefaultTransportAction extends BaseAction{
	
	public void doAction(ActionEvent e) {
		String sceneId = e.getArgumentAsString(0);
		int x = e.getArgumentAsInt(1);
		int y = e.getArgumentAsInt(2);
		GameWindow window = ApplicationHelper.getApplication().getContext().getWindow();
		GameCanvas canvas = window.getCanvas();
		if (canvas instanceof SceneCanvas) {
			window.getHelper().hideDialog(window.getHelper().getTalkPanel());
			SceneCanvas sceneCanvas = (SceneCanvas) canvas;
			sceneCanvas.changeScene(sceneId, x, y);
		}
				
	}
}