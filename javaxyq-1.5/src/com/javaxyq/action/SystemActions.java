package com.javaxyq.action;

import com.javaxyq.core.ApplicationHelper;
import com.javaxyq.core.GameCanvas;
import com.javaxyq.core.GameWindow;
import com.javaxyq.core.SceneCanvas;
import com.javaxyq.event.ActionEvent;

/**
 * 系统事件处理代码
 */
public class SystemActions extends BaseAction {

	private int dlgIdIndex = "com.javaxyq.action.dialog.".length();
    public void doAction(ActionEvent e) {
        String cmd = e.getCommand();
        Object source = e.getSource();
        System.out.println("action: "+cmd);
        if (cmd.startsWith("com.javaxyq.action.dialog.")) {
    		GameWindow window = ApplicationHelper.getApplication().getContext().getWindow();
    		window.getHelper().showHideDialog(cmd.substring(dlgIdIndex));
        } else if (cmd.startsWith("com.javaxyq.action.transport")) {
        	ApplicationHelper.getApplication().getContext().getPlayer().stop(true);
            String sceneId = e.getArgumentAsString(0);
            int x = e.getArgumentAsInt(1);
            int y = e.getArgumentAsInt(2);
    		GameWindow window = ApplicationHelper.getApplication().getContext().getWindow();
    		GameCanvas canvas = window.getCanvas();
    		if (canvas instanceof SceneCanvas) {
    			SceneCanvas sceneCanvas = (SceneCanvas) canvas;
    			sceneCanvas.changeScene(sceneId, x, y);
    			window.getHelper().hideDialog(window.getHelper().getTalkPanel());
    		}
        } else {

        }
    }

}
