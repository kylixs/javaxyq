/**
 * 
 */
package ui;

import java.awt.Desktop;
import java.net.URI;

import com.javaxyq.core.GameMain;
import com.javaxyq.event.ActionEvent;
import com.javaxyq.event.PanelEvent;
import com.javaxyq.event.PanelHandler;
import com.javaxyq.profile.ProfileException;
import com.javaxyq.ui.Panel;
import com.javaxyq.util.BrowserLauncher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 游戏主菜单
 * @author gongdewei
 * @date 2011-5-2 create
 */
public class mainmenu extends PanelHandler {
	Logger log = LoggerFactory.getLogger(this.getClass());

	public void initial(PanelEvent evt) {
		super.initial(evt);
	}	

	public void dispose(PanelEvent evt) {
		System.out.println("dispose: "+this.getClass().getName());
	}	
	
	public void load_last(ActionEvent evt) {
		String profileName = "0";
		try {
			application.loadProfile(profileName);
			application.enterScene();
		} catch (ProfileException e) {
			System.err.println("加载游戏存档失败!");
			e.printStackTrace();
			application.getUIHelper().prompt("加载游戏存档失败!", 3000);
		}
	}
	
	public void enter_game(ActionEvent evt) {
		Panel dlg = helper.getDialog("select_role");
		helper.hideDialog(panel);
		helper.showDialog(dlg);
	}
	
	public void newgame(ActionEvent evt) {
		
	}
	
	public void work(ActionEvent evt) {
		
	}
	
	public void home(ActionEvent evt) {
		BrowserLauncher.openURL(GameMain.getHomeURL());
	}
	
	public void exit(ActionEvent evt) {
		application.shutdown();
	}

		
}
