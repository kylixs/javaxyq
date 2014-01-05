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
	private HashMap<String, String> charNames;
	
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
			String name = newProfileName();
			String sceneId = "1506";
			ItemInstance[] items = null;
			Profile profile = profileManager.newProfile(name);
			PlayerVO data = dataManager.createPlayerData(character);
			data.setName(roleName);
			data.setSceneLocation(new Point(38, 20));
			profile.setPlayerData(data);
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
		Animation anim = SpriteFactory.loadAnimation("/wzife/login/photo/selected/"+getCharacterName(character)+".tcp");
		label.setAnim(anim);
		//TODO 更新其它信息
		
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
}
