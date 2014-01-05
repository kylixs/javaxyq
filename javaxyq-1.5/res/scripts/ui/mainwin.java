package ui;

import java.awt.Point;
import java.awt.event.ActionListener;
import java.util.Arrays;

import javax.swing.SwingUtilities;

import com.javaxyq.core.Application;
import com.javaxyq.core.DataStore;
import com.javaxyq.core.GameCanvas;
import com.javaxyq.core.SceneCanvas;
import com.javaxyq.core.SpriteFactory;
import com.javaxyq.data.ItemInstance;
import com.javaxyq.event.ActionEvent;
import com.javaxyq.event.PanelEvent;
import com.javaxyq.event.PanelHandler;
import com.javaxyq.model.ItemTypes;
import com.javaxyq.model.PlayerVO;
import com.javaxyq.ui.Button;
import com.javaxyq.ui.Label;
import com.javaxyq.ui.TextField;
import com.javaxyq.util.MedicineItemComparator;
import com.javaxyq.widget.Player;
import com.javaxyq.widget.Sprite;

public class mainwin extends PanelHandler implements ActionListener{
	
	private Label sceneLabel;
	private Label coordinateLabel;
	private Label hpTrough ;
	private Label mpTrough ;
	private Label spTrough ;
	private Label expTrough ;
	private TextField chatInput;

	public void initial(PanelEvent evt) {
		super.initial(evt);
		System.out.println("initial：system.mainwin ");
		String charactorId = context.getPlayer().getCharacter();
		Sprite sprite = SpriteFactory.loadSprite("wzife/photo/facesmall/"+charactorId+".tcp");
		Button btnHeader = (Button) panel.findCompByName("btn_player_header");
		btnHeader.init(sprite);
		coordinateLabel = (Label) panel.findCompByName("player_coordinate");
		sceneLabel = (Label) panel.findCompByName("scene_name");
		hpTrough = (Label) panel.findCompByName("player_hp");
		mpTrough = (Label) panel.findCompByName("player_mp");
		spTrough = (Label) panel.findCompByName("player_sp");
		expTrough = (Label) panel.findCompByName("player_exp");
		chatInput = (TextField) panel.findCompByName("chat_input");
		chatInput.addActionListener(this);
		
		updateCoords();
		setAutoUpdate(true);
	}

	public void dispose(PanelEvent evt) {
		System.out.println("dispose: ui.mainwin ");
	}
	
	public void update(PanelEvent evt) {
		if (context.getPlayer() == null)
			return;
		updateCoords();
		Player player = context.getPlayer();
		if(player == null)return;
		PlayerVO playerVO = player.getData();
		int maxLen = 50;
		// player_hp
		int len = playerVO.getHp() * maxLen / playerVO.getMaxHp();
		hpTrough.setSize(len, hpTrough.getHeight());
		// player_mp
		len = playerVO.getMp() * maxLen / playerVO.getMaxMp();
		mpTrough.setSize(len, mpTrough.getHeight());
		// player_sp
		len = playerVO.getSp() * maxLen / 150;
		spTrough.setSize(len, spTrough.getHeight());
		// player_exp
		len = (int) (playerVO.getExp() * maxLen / dataManager.getLevelExp(playerVO.getLevel()));
		expTrough.setSize(len, expTrough.getHeight());

		// TODO summon状态
		// summon气血
		// summon魔法
		// summon经验
	}

	/**
	 * 更新人物坐标
	 */
	private void updateCoords() {
		GameCanvas canvas = context.getWindow().getCanvas();
		if (canvas instanceof SceneCanvas) {
			SceneCanvas sceneCanvas = (SceneCanvas) canvas;
			Point pp = context.getPlayer().getSceneLocation();
			String strCoordinate = "X:" + pp.x + " Y:" + pp.y;
			coordinateLabel.setText(strCoordinate);
			sceneLabel.setText(sceneCanvas.getSceneName());
		}
		
	}
	
	/**
	 * 补充player_hp
	 */
	public void eke_player_hp(ActionEvent evt) {
		//如果当前在战斗则返回
		if(application.getState() == Application.STATE_BATTLE) {
			helper.prompt("[温馨提示]战斗中不能自动补充气血哦！",3000);
			return;
		}
		Player player = context.getPlayer();
		PlayerVO data = player.getData();
		int reqHp = data.maxHp - data.hp;//需要补充的气血量
		//查找可以补充气血的药品
		ItemInstance[] items = dataManager.findItems(player,ItemTypes.TYPE_MEDICINE_HP);
		Arrays.sort(items,new MedicineItemComparator(ItemTypes.TYPE_MEDICINE_HP));
		for (int i = 0; i < items.length; i++) {
			ItemInstance item = items[i];
			while(reqHp > 0 && item.getLevel() <3 && item.getAmount() > 0) {
				System.out.println("使用一个药品："+item.getName());
				application.getItemManager().useItem(player, item);
				reqHp = data.maxHp - data.hp;
			}
			if(reqHp == 0) {
				System.out.println("气血补充完毕！");
				break;
			}
		}
	}
	
	/**
	 * 补充player_mp值
	 * @param evt
	 */
	public void eke_player_mp(ActionEvent evt) {
		//如果当前在战斗则返回
		if(application.getState() == Application.STATE_BATTLE) {
			helper.prompt("[温馨提示]战斗中不能自动补充法力哦！",3000);
			return;
		}
		Player player = context.getPlayer();
		PlayerVO data = player.getData();
		int reqMp = data.maxMp - data.mp;//需要补充的气血量
		//查找可以补充气血的药品
		ItemInstance[] items = dataManager.findItems(player,ItemTypes.TYPE_MEDICINE_MP);
		Arrays.sort(items,new MedicineItemComparator(ItemTypes.TYPE_MEDICINE_MP));
		for (int i = 0; i < items.length; i++) {
			ItemInstance item = items[i];
			while(reqMp > 0 && item.getLevel() <3 && item.getAmount() > 0) {
				System.out.println("使用一个药品："+item.getName());
				application.getItemManager().useItem(player, item);
				reqMp = data.maxMp - data.mp;
			}
			if(reqMp == 0) {
				System.out.println("法力补充完毕！");
				break;
			}
		}
	}
	
	/**
	 * 补充summon气血
	 */
	public void eke_summon_hp(ActionEvent evt) {
		System.out.println("补充summon气血");
	}
	
	/**
	 * 补充summon魔法值
	 * @param evt
	 */
	public void eke_summoned_mp(ActionEvent evt) {
		System.out.println("补充summon魔法");
	}

	/**
	 * 全屏切换
	 * @param evt
	 */
	public void fullscreen(ActionEvent evt) {
		//GameMain.fullScreen();
	}
	public void world_map(ActionEvent evt) {
		helper.showHideDialog("world_map");
	}
	public void scene_map(ActionEvent evt) {		
		helper.showHideDialog("scene_map");
	}
	public void summon_status(ActionEvent evt) {
		helper.showHideDialog("summon_status");
	}
	public void player_status(ActionEvent evt) {
		helper.showHideDialog("player_status");
	}
	/**
	 * 输入聊天内容
	 * @param evt
	 */
	public void chat(ActionEvent evt) {
		final TextField editor = (TextField) evt.getSource();
		String text = editor.getText();
		if(text !=null && text.length()>0) {
			context.getPlayer().say(text);
		}
		editor.setText("");
	}
	public void attack(ActionEvent evt) {
	}
	public void open_item(ActionEvent evt) {
		helper.showHideDialog("item");		
	}
	public void giving(ActionEvent evt) {
		//helper.showDialog("giving");		
	}
	public void exchange(ActionEvent evt) {
		//helper.showDialog("exchange");		
	}
	public void team(ActionEvent evt) {
	}
	public void task_list(ActionEvent evt) {
		helper.showHideDialog("tasklist");		
	}
	/**
	 * 打开帮派
	 * @param evt
	 */
	public void open_org(ActionEvent evt) {
	}
	/**
	 * 法术快捷栏
	 * @param evt
	 */
	public void quick_magic(ActionEvent evt) {
	}
	public void friend_list(ActionEvent evt) {
	}
	/**
	 * 人物动作
	 * @param evt
	 */
	public void open_motion(ActionEvent evt) {
	}
	/**
	 * 系统设置
	 * @param evt
	 */
	public void system_setting(ActionEvent evt) {		
		helper.showHideDialog("game_exit");		
	}
	/**
	 * 频道选择
	 * @param evt
	 */
	public void change_channel(ActionEvent evt) {
	}

	@Override
	public void actionPerformed(java.awt.event.ActionEvent e) {
		chat(new ActionEvent(e.getSource(), "chat"));
	}
}
