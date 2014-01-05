/**
 * 
 */
package ui;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.javaxyq.core.SpriteFactory;
import com.javaxyq.event.ActionEvent;
import com.javaxyq.event.PanelEvent;
import com.javaxyq.event.PanelHandler;
import com.javaxyq.model.PlayerVO;
import com.javaxyq.profile.Profile;
import com.javaxyq.profile.ProfileException;
import com.javaxyq.ui.Label;
import com.javaxyq.ui.Panel;
import com.javaxyq.ui.TextField;
import com.javaxyq.widget.Animation;

/**
 * ÓÎÏ·Ö÷²Ëµ¥
 * @author gongdewei
 * @date 2011-5-2 create
 */
public class select_role extends PanelHandler implements MouseListener {
	
	private int profileIndex;
	
	String prefix = "icon_role";

	private HashMap<String, String> charNames;
	
	private List<Label> profileLabels;

	private List<Profile> profiles;
	
	public void initial(PanelEvent evt) {
		super.initial(evt);
		profileIndex = 0;
		profileLabels = new ArrayList<Label>(6);
		for(int i=1;i<=6;i++) {
			Label label = (Label) panel.findCompByName(prefix+i);
			label.setAnim(null);
			label.addMouseListener(this);
			profileLabels.add(label);
		}
		preloadProfiles();
		displayProflieInfo();
	}	

	public void dispose(PanelEvent evt) {
		System.out.println("dispose: select_role ");
	}	
	
//	public void selectRole(ActionEvent evt) {
//		try {
//			profileName = evt.getArgumentAsString(0);
//			application.loadProfile(profileName);
//			loaded = true;
//		} catch (ProfileException e) {
//			System.err.println("¼ÓÔØÓÎÏ·´æµµÊ§°Ü!"+profileName);
//			e.printStackTrace();
//			application.getUIHelper().prompt("¼ÓÔØÓÎÏ·´æµµÊ§°Ü!", 3000);
//		}
//	}
	
	public void createRole(ActionEvent evt) {
		Panel dlg = helper.getDialog("create_role");
		if(dlg != null) {
			helper.hideDialog(panel);
			helper.showDialog(dlg);
		}
	}
	
	public void goback(ActionEvent evt) {
		Panel dlg = helper.getDialog("mainmenu");
		if(dlg != null) {
			helper.hideDialog(panel);
			helper.showDialog(dlg);
		}
	}
	
	public void gonext(ActionEvent evt) {
		try {
			Profile profile = profiles.get(profileIndex);
			String profileName = profile.getName();
			application.loadProfile(profileName);
			application.enterScene();
		} catch (ProfileException e) {
			System.err.println("¼ÓÔØÓÎÏ·´æµµÊ§°Ü!");
			e.printStackTrace();
			application.getUIHelper().prompt("¼ÓÔØÓÎÏ·´æµµÊ§°Ü!", 3000);
		}
	}

	private void preloadProfiles(){
		try {
			profiles = application.getProfileManager().listProfiles();
			for (int i = 0; i < profileLabels.size() && i<profiles.size(); i++) {
				PlayerVO data = profiles.get(i).getPlayerData();
				Animation anim = SpriteFactory.loadAnimation("/shape/char/"+data.character+"/stand.tcp");
				profileLabels.get(i).setAnim(anim);
			}
		} catch (ProfileException e) {
			e.printStackTrace();
		}
	}
	
	private void displayProflieInfo() {
		if(profileIndex>=profiles.size()) {
			return;
		}
		Profile profile = profiles.get(profileIndex);
		PlayerVO data = profile.getPlayerData();
		Label label = (Label) panel.findCompByName("role_name");
		label.setText(data.getName());
		label = (Label) panel.findCompByName("role_level");
		label.setText(data.getLevel()+"");
		label = (Label) panel.findCompByName("role_faction");
		label.setText(data.getSchool());
		label = (Label) panel.findCompByName("role_appellation");
		label.setText(data.getTitle());
		label = (Label) panel.findCompByName("role_head");
		Animation anim = SpriteFactory.loadAnimation("/wzife/login/photo/selected/"+getCharacterName(data.character)+".tcp");
		label.setAnim(anim);
		System.out.println("select£º"+profile);
	}

	private String getCharacterName(String character) {
		if(charNames == null) {
			charNames= new HashMap<String, String>();
			charNames.put("0001", "åÐÒ£Éú");
			charNames.put("0002", "½£ÏÀ¿Í");
			charNames.put("0003", "·ÉÑàÅ®");
			charNames.put("0004", "Ó¢Å®ÏÀ");
			charNames.put("0005", "¾ÞÄ§Íõ");
			charNames.put("0006", "»¢Í·¹Ö");
			charNames.put("0007", "ºüÃÀÈË");
			charNames.put("0008", "¹Ç¾«Áé");
			charNames.put("0009", "ÉñÌì±ø");
			charNames.put("0010", "ÁúÌ«×Ó");
			charNames.put("0011", "ÎèÌì¼§");
			charNames.put("0012", "Ðþ²Ê¶ð");
		}
		return charNames.get(character);
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		if(e.getClickCount() == 2) {
			gonext(null);
		}else {
			Label label = (Label) e.getSource();
			String str = label.getName().substring(prefix.length());
			profileIndex = Integer.parseInt(str)-1;
			displayProflieInfo();
		}
	}

	@Override
	public void mouseEntered(MouseEvent e) {
	}

	@Override
	public void mouseExited(MouseEvent e) {
	}

	@Override
	public void mousePressed(MouseEvent e) {
	}

	@Override
	public void mouseReleased(MouseEvent e) {
	}

		
}
