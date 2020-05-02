/*
 * JavaXYQ Source Code
 * by kylixs
 * at 2009-11-27
 * please visit http://javaxyq.googlecode.com
 * or mail to kylixs@qq.com
 */

package ui;

import com.javaxyq.core.ApplicationHelper;
import com.javaxyq.core.GameCanvas;
import com.javaxyq.core.GameWindow;
import com.javaxyq.core.SceneCanvas;
import com.javaxyq.event.PanelEvent;
import com.javaxyq.event.PanelHandler;
import com.javaxyq.ui.UIHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 退出游戏对话框脚本
 * @author dewitt
 * @date 2009-11-27 create
 */
public class npctalk extends PanelHandler {
	
	Logger log = LoggerFactory.getLogger(this.getClass());

	public void initial(PanelEvent evt) {
		super.initial(evt);
	}	

	public void dispose(PanelEvent evt) {
		log.info("dispose: npctalk ");
	}
	

	public void open(PanelEvent evt) {
		log.info("open: "+evt);
		ApplicationHelper.getApplication().getUIHelper().showDialog(evt.getArgumentAsString(0));
	}
	public void transport(PanelEvent evt) {
		log.info("transport: "+evt);
    	ApplicationHelper.getApplication().getContext().getPlayer().stop(true);
        String sceneId = evt.getArgumentAsString(0);
        int x = evt.getArgumentAsInt(1);
        int y = evt.getArgumentAsInt(2);
		GameWindow window = ApplicationHelper.getApplication().getContext().getWindow();
		GameCanvas canvas = window.getCanvas();
		if (canvas instanceof SceneCanvas) {
			SceneCanvas sceneCanvas = (SceneCanvas) canvas;
			sceneCanvas.changeScene(sceneId, x, y);
			sceneCanvas.playMusic();
			window.getHelper().hideDialog(window.getHelper().getTalkPanel());
		}
	}

}
