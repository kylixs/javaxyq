/*
 * JavaXYQ Source Code
 * by kylixs
 * at 2009-11-27
 * please visit http://javaxyq.googlecode.com
 * or mail to kylixs@qq.com
 */

package ui;

import java.awt.Desktop;
import java.net.URI;

import com.javaxyq.core.GameMain;
import com.javaxyq.event.ActionEvent;
import com.javaxyq.event.PanelEvent;
import com.javaxyq.event.PanelHandler;
import com.javaxyq.profile.ProfileException;
import com.javaxyq.ui.Button;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 退出游戏对话框脚本
 * @author dewitt
 * @date 2009-11-27 create
 */
public class game_exit extends PanelHandler {
	Logger log = LoggerFactory.getLogger(this.getClass());
	public void initial(PanelEvent evt) {
		super.initial(evt);
	}	

	public void dispose(PanelEvent evt) {
		log.info("dispose: system.mainwin ");
	}
	
	public void exit_game(ActionEvent evt) {
		//application.shutdown();
		application.endGame();
	}
	public void visit_homepage(ActionEvent evt) {
		try {
			Desktop.getDesktop().browse(new URI(GameMain.getHomeURL()));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public void contributors(ActionEvent evt) {
		helper.showDialog("contributors");
	}
	
	public void toggle_debug(ActionEvent evt) {
		application.setDebug(!application.isDebug());
		application.getScriptEngine().setDebug(application.isDebug());
		Button btn = (Button) panel.findCompByName("debugbtn");
		if(application.isDebug()) {
			log.info("已打开游戏调试");
			btn.setText("关闭调试");
		}else {
			log.info("已关闭游戏调试");
			btn.setText("打开调试");
		}
	}
	
	public void toggle_music(ActionEvent evt) {
		GameMain.setPlayingMusic(!GameMain.isPlayingMusic());
		Button btn = (Button) panel.findCompByName("musicbtn");
		if(GameMain.isPlayingMusic()) {
			btn.setText("关闭音乐");
			log.info("打开游戏背景音乐");
		}else {
			btn.setText("打开音乐");
			log.info("关闭游戏背景音乐");
		}
	}
	
	public void saveProfile(ActionEvent evt) {
		try {
			application.saveProfile();
		} catch (ProfileException e) {
			e.printStackTrace();
			application.getUIHelper().prompt("保存游戏失败！", 3000);
		}
	}
}
