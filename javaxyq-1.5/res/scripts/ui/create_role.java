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
import com.javaxyq.data.CharacterConstants;
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
			ItemInstance item = dataManager.createItem("四叶花");
			item.setAmount(99);
			items[6] = item;
			item = dataManager.createItem("佛手");
			item.setAmount(99);
			items[7] = item;
			item = dataManager.createItem("血色茶花");
			item.setAmount(99);
			items[8] = item;
			item = dataManager.createItem("九香虫");
			item.setAmount(99);
			items[9] = item;
			items[10] = createWeapon(character);
			
			
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
	
	private ItemInstance createWeapon(String charName) {
		//随机分配指定级别武器
		String weaponName = null;
		if(CharacterConstants.char_0010.equals(charName)) {
			weaponName = "五虎断魂";//逍遥江湖
		}else if(CharacterConstants.char_0009.equals(charName)) {
		}
		
		if(weaponName != null) {
			return application.getDataManager().createItem(weaponName);
		}
		return null;
	}


}
