/*
 * JavaXYQ Source Code
 * by kylixs
 * at 2010-5-29
 * please visit http://javaxyq.googlecode.com
 * or mail to kylixs@qq.com
 */
package com.javaxyq.event;

import com.javaxyq.core.ApplicationHelper;
import com.javaxyq.core.GameCanvas;
import com.javaxyq.core.GameWindow;
import com.javaxyq.core.SceneCanvas;
import com.javaxyq.ui.Panel;

/**
 * 简单对话动作处理器
 * 
 * @author gongdewei
 * @date 2010-5-29 create
 */
public class SimpleTalkHandler implements PanelListener {

	@Override
	public void actionPerformed(ActionEvent evt) {
		String action = evt.getActionCommand();
		Panel panel = (Panel) evt.getSource();
		if ("open".equals(action)) {
			ApplicationHelper.getApplication().getContext().getWindow().getHelper().showDialog(evt.getArgumentAsString(0));
			panel.close();
		} else if ("transport".equals(action)) {
			panel.close();
			String sceneId = evt.getArgumentAsString(0);
			int x = evt.getArgumentAsInt(1);
			int y = evt.getArgumentAsInt(2);
			GameWindow window = ApplicationHelper.getApplication().getContext().getWindow();
			GameCanvas canvas = window.getCanvas();
			if (canvas instanceof SceneCanvas) {
				SceneCanvas sceneCanvas = (SceneCanvas) canvas;
				sceneCanvas.changeScene(sceneId, x, y);
				window.getHelper().hideDialog(window.getHelper().getTalkPanel());
			}
		}
	}

	@Override
	public void close(ActionEvent evt) {
	}

	@Override
	public void dispose(PanelEvent evt) {
	}

	@Override
	public void help(ActionEvent evt) {
	}

	@Override
	public void initial(PanelEvent evt) {
	}

	@Override
	public void update(PanelEvent evt) {
	}

}
