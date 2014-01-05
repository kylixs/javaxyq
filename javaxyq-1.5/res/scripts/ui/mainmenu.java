/**
 * 
 */
package ui;

import java.awt.Desktop;
import java.net.URI;

import com.javaxyq.event.ActionEvent;
import com.javaxyq.event.PanelEvent;
import com.javaxyq.event.PanelHandler;
import com.javaxyq.profile.ProfileException;
import com.javaxyq.ui.Panel;

/**
 * ”Œœ∑÷˜≤Àµ•
 * @author gongdewei
 * @date 2011-5-2 create
 */
public class mainmenu extends PanelHandler {

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
			System.err.println("º”‘ÿ”Œœ∑¥Êµµ ß∞‹!");
			e.printStackTrace();
			application.getUIHelper().prompt("º”‘ÿ”Œœ∑¥Êµµ ß∞‹!", 3000);
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
		try {
			Desktop.getDesktop().browse(new URI("http://javaxyq.googlecode.com"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void exit(ActionEvent evt) {
		application.shutdown();
	}

		
}
