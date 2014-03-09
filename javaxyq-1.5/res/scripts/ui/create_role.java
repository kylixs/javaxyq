/**
 * 
 */
package ui;

import java.awt.Point;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import com.javaxyq.core.DataManager;
import com.javaxyq.core.DataStore;
import com.javaxyq.core.SpriteFactory;
import com.javaxyq.data.CharacterUtils;
import com.javaxyq.data.ItemInstance;
import com.javaxyq.event.ActionEvent;
import com.javaxyq.event.PanelEvent;
import com.javaxyq.event.PanelHandler;
import com.javaxyq.model.PlayerVO;
import com.javaxyq.profile.Profile;
import com.javaxyq.profile.ProfileException;
import com.javaxyq.profile.ProfileManager;
import com.javaxyq.profile.impl.ProfileManagerImpl;
import com.javaxyq.ui.Label;
import com.javaxyq.ui.Panel;
import com.javaxyq.ui.TextField;
import com.javaxyq.widget.Animation;

/**
 * 游戏主菜单
 * @author gongdewei
 * @date 2011-5-2 create
 */
public class create_role extends PanelHandler {
	
	private String character = "0003";
	private String roleName;
	
	public void initial(PanelEvent evt) {
		super.initial(evt);
		displayRoleInfo();
	}	

	public void dispose(PanelEvent evt) {
		System.out.println("dispose: select_role ");
	}	
	
	public void selectRole(ActionEvent evt) {
		character = evt.getArgumentAsString(0);
		displayRoleInfo();
	}
	
	public void goback(ActionEvent evt) {
		Panel dlg = helper.getDialog("select_role");
		helper.hideDialog(panel);
		helper.showDialog(dlg);
	}
	
	public void gonext(ActionEvent evt) {
		//TODO create profile
		TextField field = (TextField) panel.findCompByName("role_name");
		roleName = field.getText();
		if(roleName.trim().length() == 0) {
			helper.prompt("请在左下角输入框内填写人物的姓名！", 3000);
			return;
		}
		try {
			ProfileManager profileManager = application.getProfileManager();
			DataManager dataManager = application.getDataManager();
			String filename = newProfileName();
			String sceneId = "1506";
			Profile profile = profileManager.newProfile(filename);
			PlayerVO playerVo = dataManager.createPlayerData(character);
			playerVo.setName(roleName);
			playerVo.setSceneLocation(new Point(38, 20));
			playerVo.setMoney(50000);
			
			ItemInstance[] items = new ItemInstance[26];
			ItemInstance item = dataManager.createItem("四叶花",99);
			addItem(items, item);
			item = dataManager.createItem("佛手",99);
			addItem(items, item);
			item = dataManager.createItem("血色茶花",99);
			addItem(items, item);
			item = dataManager.createItem("九香虫",99);
			addItem(items, item);
			createWeapon(character, items);
			
			
			profile.setPlayerData(playerVo);
			profile.setSceneId(sceneId);
			profile.setItems(items);
			profileManager.saveProfile(profile);
			
			helper.prompt("人物创建成功！", 3000);
			try {
				String profileName = profile.getName();
				application.loadProfile(profileName);
				application.enterScene();
			} catch (ProfileException e) {
				System.err.println("加载游戏存档失败!");
				e.printStackTrace();
				helper.prompt("加载游戏存档失败!", 3000);
			}
			
		} catch (ProfileException e) {
			e.printStackTrace();
		}
	}

	private void addItem(ItemInstance[] items, ItemInstance item) {
		for(int i=6;i<items.length;i++) {
			if(items[i] == null) {
				items[i] = item;
				break;
			}
		}
	}

	private String newProfileName() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssS");
		return sdf.format(new Date());
	}

	private void displayRoleInfo() {
		Label label = (Label) panel.findCompByName("role_head");
		String filename = "/wzife/login/photo/selected/"+character+".tcp";
		Animation anim = SpriteFactory.loadAnimation(filename);
		if(anim != null) {
			label.setAnim(anim);
		}else {
			System.err.println("displayRoleInfo failed: "+filename);
		}
		//TODO 更新其它信息
		
	}	
	
	private void createWeapon(String charName, ItemInstance[] items) {
		//分配武器
		String[] weaponNames = null;
		if(CharacterUtils.char_0001.equals(charName)) {
			weaponNames = new String[] {"青铜短剑","逍遥江湖"};
		}else if(CharacterUtils.char_0002.equals(charName)) {
			weaponNames = new String[] {"柳叶刀"};
		}else if(CharacterUtils.char_0003.equals(charName)) {
		}else if(CharacterUtils.char_0004.equals(charName)) {
		}else if(CharacterUtils.char_0005.equals(charName)) {
		}else if(CharacterUtils.char_0006.equals(charName)) {
		}else if(CharacterUtils.char_0007.equals(charName)) {
		}else if(CharacterUtils.char_0008.equals(charName)) {
		}else if(CharacterUtils.char_0009.equals(charName)) {
			weaponNames = new String[] {"五虎断魂"};
		}else if(CharacterUtils.char_0010.equals(charName)) {
			weaponNames = new String[] {"红缨枪","五虎断魂","逍遥江湖"};
		}else if(CharacterUtils.char_0011.equals(charName)) {
		}else if(CharacterUtils.char_0012.equals(charName)) {
		}
		
		for (int i = 0; (weaponNames != null) && (i<weaponNames.length); i++) {
			ItemInstance item = dataManager.createItem(weaponNames[i]);
			addItem(items, item);
		}
	}


}
