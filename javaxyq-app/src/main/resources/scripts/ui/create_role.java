/**
 *
 */
package ui;

import com.javaxyq.core.DataManager;
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
import com.javaxyq.ui.Label;
import com.javaxyq.ui.Panel;
import com.javaxyq.ui.TextField;
import com.javaxyq.widget.Animation;
import org.apache.commons.beanutils.PropertyUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.lang.reflect.InvocationTargetException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 游戏主菜单
 * @author gongdewei
 * @date 2011-5-2 create
 */
public class create_role extends PanelHandler {
    Logger log = LoggerFactory.getLogger(this.getClass());

    private String character = "0003";
    private String roleName;
    private static String[] attr_points = {"physique", "magic", "strength", "durability", "agility"};
    private static String[] 人族 = {"0001", "0002", "0003", "0004"};
    private static String[] 魔族 = {"0005", "0006", "0007", "0008"};
    private static String[] 仙族 = {"0009", "0010", "0011", "0012"};
    private static int[] 人族初始属性点 = {10, 10, 10, 10, 10};
    private static int[] 魔族初始属性点 = {12, 11, 11, 8, 8};
    private static int[] 仙族初始属性点 = {12, 5, 11, 12, 10};

    public void initial(PanelEvent evt) {
        super.initial(evt);
        displayRoleInfo();
    }

    /**
     * 判断数组array是否包含值value
     * @return
     */
    private static boolean inArray(String[] array, String value) {
        for (int i = 0; i < array.length; i++) {
            if (array[i].equals(value)) return true;
        }
        return false;
    }

    private static void init_属性点(PlayerVO playerVo, int[] 初始属性点) {
        for (int i = 0; i < attr_points.length; i++) {
            try {
                PropertyUtils.setProperty(playerVo, attr_points[i], 初始属性点[i]);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }

        }
    }

    public void dispose(PanelEvent evt) {
        log.info("dispose: select_role ");
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
        TextField field = (TextField) panel.findCompByName("role_name");
        roleName = field.getText();
        if (roleName.trim().length() == 0) {
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
            playerVo.setMoney(1500000);
            if (inArray(人族, playerVo.character)) {
                init_属性点(playerVo, 人族初始属性点);
            } else if (inArray(魔族, playerVo.character)) {
                init_属性点(playerVo, 魔族初始属性点);
            } else if (inArray(仙族, playerVo.character)) {
                init_属性点(playerVo, 仙族初始属性点);
            }
            dataManager.recalcProperties(playerVo);

            ItemInstance[] items = new ItemInstance[26];
            ItemInstance item = dataManager.createItem("四叶花", 99);
            addItem(items, item);
            item = dataManager.createItem("佛手", 99);
            addItem(items, item);
            item = dataManager.createItem("血色茶花", 99);
            addItem(items, item);
            item = dataManager.createItem("九香虫", 99);
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
                log.error("加载游戏存档失败!");
                e.printStackTrace();
                helper.prompt("加载游戏存档失败!", 3000);
            }

        } catch (ProfileException e) {
            e.printStackTrace();
        }
    }

    private void addItem(ItemInstance[] items, ItemInstance item) {
        for (int i = 6; i < items.length; i++) {
            if (items[i] == null) {
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
        String filename = "/wzife/login/photo/selected/" + character + ".tcp";
        Animation anim = SpriteFactory.loadAnimation(filename);
        if (anim != null) {
            label.setAnim(anim);
        } else {
            log.warn("displayRoleInfo failed: " + filename);
        }
        //TODO 更新其它信息

    }

    private void createWeapon(String charName, ItemInstance[] items) {
        //分配武器
        String[] weaponNames = null;
        if (CharacterUtils.char_0001.equals(charName)) {
            weaponNames = new String[]{"青铜短剑", "逍遥江湖"};
        } else if (CharacterUtils.char_0002.equals(charName)) {
            weaponNames = new String[]{"柳叶刀"};
        } else if (CharacterUtils.char_0003.equals(charName)) {
        } else if (CharacterUtils.char_0004.equals(charName)) {
        } else if (CharacterUtils.char_0005.equals(charName)) {
        } else if (CharacterUtils.char_0006.equals(charName)) {
        } else if (CharacterUtils.char_0007.equals(charName)) {
        } else if (CharacterUtils.char_0008.equals(charName)) {
        } else if (CharacterUtils.char_0009.equals(charName)) {
            weaponNames = new String[]{"五虎断魂"};
        } else if (CharacterUtils.char_0010.equals(charName)) {
            weaponNames = new String[]{"红缨枪", "五虎断魂", "逍遥江湖"};
        } else if (CharacterUtils.char_0011.equals(charName)) {
        } else if (CharacterUtils.char_0012.equals(charName)) {
        }

        for (int i = 0; (weaponNames != null) && (i < weaponNames.length); i++) {
            ItemInstance item = dataManager.createItem(weaponNames[i]);
            addItem(items, item);
        }
    }


}
