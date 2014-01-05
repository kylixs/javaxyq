/*
 * JavaXYQ Source Code
 * by kylixs
 * at 2009-11-27
 * please visit http://javaxyq.googlecode.com
 * or mail to kylixs@qq.com
 */

package ui;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import com.javaxyq.core.GameMain;
import com.javaxyq.event.ActionEvent;
import com.javaxyq.event.PanelEvent;
import com.javaxyq.event.PanelHandler;
import com.javaxyq.profile.ProfileException;
import com.javaxyq.ui.Button;

/**
 * 退出游戏对话框脚本
 * @author dewitt
 * @date 2009-11-27 create
 */
public class game_exit extends PanelHandler {
	
	private static final String blogURL = "http://blog.csdn.net/Kylixs";
	
	public void initial(PanelEvent evt) {
		super.initial(evt);
	}	

	public void dispose(PanelEvent evt) {
		System.out.println("dispose: system.mainwin ");
	}
	
	public void exit_game(ActionEvent evt) {
		application.shutdown();
	}
	public void visit_homepage(ActionEvent evt) {
		try {
			Desktop.getDesktop().browse(new URI(GameMain.getHomeURL()));
		} catch (IOException e) {
			e.printStackTrace();
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
	}
	public void visit_blog(ActionEvent evt) {
		try {
			Desktop.getDesktop().browse(new URI(blogURL));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
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
			System.out.println("已打开游戏调试");
			btn.setText("关闭调试");
		}else {
			System.out.println("已关闭游戏调试");
			btn.setText("打开调试");
		}
	}
	
	public void toggle_music(ActionEvent evt) {
		GameMain.setPlayingMusic(!GameMain.isPlayingMusic());
		Button btn = (Button) panel.findCompByName("musicbtn");
		if(GameMain.isPlayingMusic()) {
			btn.setText("关闭音乐");
			System.out.println("打开游戏背景音乐");
		}else {
			btn.setText("打开音乐");
			System.out.println("关闭游戏背景音乐");
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
