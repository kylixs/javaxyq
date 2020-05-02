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
import com.javaxyq.data.ItemInstance;
import com.javaxyq.data.WeaponItem;
import com.javaxyq.event.ActionEvent;
import com.javaxyq.event.PanelEvent;
import com.javaxyq.event.PanelHandler;
import com.javaxyq.model.ItemTypes;
import com.javaxyq.model.PlayerVO;
import com.javaxyq.profile.Profile;
import com.javaxyq.profile.ProfileException;
import com.javaxyq.ui.Label;
import com.javaxyq.ui.Panel;
import com.javaxyq.ui.TextField;
import com.javaxyq.ui.UIHelper;
import com.javaxyq.widget.Animation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 游戏主菜单
 * @author gongdewei
 * @date 2011-5-2 create
 */
public class select_role extends PanelHandler implements MouseListener {
	
	Logger log = LoggerFactory.getLogger(this.getClass());
	
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
			UIHelper.removeAllMouseListeners(label);
			label.addMouseListener(this);
			profileLabels.add(label);
		}
		preloadProfiles();
		displayProflieInfo();
	}	

	public void dispose(PanelEvent evt) {
		log.info("dispose: select_role ");
	}	
	
//	public void selectRole(ActionEvent evt) {
//		try {
//			profileName = evt.getArgumentAsString(0);
//			application.loadProfile(profileName);
//			loaded = true;
//		} catch (ProfileException e) {
//			System.err.println("加载游戏存档失败!"+profileName);
//			e.printStackTrace();
//			application.getUIHelper().prompt("加载游戏存档失败!", 3000);
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
		if(profileIndex>=profiles.size()) {
			return;
		}

		try {
			Profile profile = profiles.get(profileIndex);
			String profileName = profile.getName();
			application.loadProfile(profileName);
			application.enterScene();
		} catch (ProfileException e) {
			log.error("加载游戏存档失败!");
			e.printStackTrace();
			application.getUIHelper().prompt("加载游戏存档失败!", 3000);
		}
	}

	private void preloadProfiles(){
		try {
			profiles = application.getProfileManager().listProfiles();
			for (int i = 0; i < profileLabels.size() && i<profiles.size(); i++) {
				PlayerVO data = profiles.get(i).getPlayerData();
				Animation anim = SpriteFactory.loadAnimation("/shape/char/"+data.character+"/stand.tcp");
				profileLabels.get(i).setAnim(anim);
				profileLabels.get(i).setSize(90, 120);
				//装上武器
				ItemInstance weaponItem = profiles.get(i).getItems()[2];
				if(weaponItem != null) {
					//weaponItem.getType()
					//if(ItemTypes.isType(weaponItem.getItem(), ItemTypes.TYPE_WEAPON)){
						//TODO 装上武器
						//player.takeupWeapon((WeaponItem) weaponItem.getItem());
						//log.info("takeup weapon: "+weaponItem.getItem());
					//}			
				}
			}
		} catch (ProfileException e) {
			log.error("", e);
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
		Animation anim = SpriteFactory.loadAnimation("/wzife/login/photo/selected/"+data.character+".tcp");
		if(anim != null) {
			label.setAnim(anim);
		}
		log.info("select: {}", profile);
	}

	private String getCharacterName(String character) {
		if(charNames == null) {
			charNames= new HashMap<String, String>();
			charNames.put("0001", "逍遥生");
			charNames.put("0002", "剑侠客");
			charNames.put("0003", "飞燕女");
			charNames.put("0004", "英女侠");
			charNames.put("0005", "巨魔王");
			charNames.put("0006", "虎头怪");
			charNames.put("0007", "狐美人");
			charNames.put("0008", "骨精灵");
			charNames.put("0009", "神天兵");
			charNames.put("0010", "龙太子");
			charNames.put("0011", "舞天姬");
			charNames.put("0012", "玄彩娥");
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
