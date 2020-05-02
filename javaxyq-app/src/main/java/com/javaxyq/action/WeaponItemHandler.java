package com.javaxyq.action;

import com.javaxyq.core.ApplicationHelper;
import com.javaxyq.core.DataManager;
import com.javaxyq.data.ItemInstance;
import com.javaxyq.data.WeaponItem;
import com.javaxyq.event.ItemEvent;
import com.javaxyq.event.ItemListener;
import com.javaxyq.util.MP3Player;
import com.javaxyq.widget.Player;
//potion  
public class WeaponItemHandler implements ItemListener{

	private DataManager dataManager;

	public WeaponItemHandler() {
		dataManager = ApplicationHelper.getApplication().getDataManager();
	}
	@Override
	public void itemUsed(ItemEvent evt) {
		/*MP3Player.play("sound/addon/use_item.mp3");
		ItemInstance iteminst = evt.getItem();
		if(iteminst.alterAmount(-1) == -1) {//如果成功删除一个数量
			MedicineItem item = (MedicineItem) iteminst.getItem();
			Player player = evt.getPlayer();
			if(item.getHp()!=0 && item.getMp()!=0) {
				player.playEffect("add_hpmp", false);
				dataManager.addHp(player,item.getHp());		
				dataManager.addMp(player,item.getMp());		
			}else if(item.getHp() != 0) {
				player.playEffect("add_hp", false);
				dataManager.addHp(player,item.getHp());		
			}else if(item.getMp() != 0) {
				player.playEffect("add_mp", false);
				dataManager.addMp(player,item.getMp());
			}
			//疗伤
			//播放效果动画及声音		
		}*/
		
//		ItemInstance iteminst = evt.getItem();
//		WeaponItem item = (WeaponItem) iteminst.getItem();
//		Player player = evt.getPlayer();
//		player.takeupWeapon(item);
//		System.out.println("takeup weapon: "+item);
	}
	
	@Override
	public void itemDestroyed(ItemEvent evt) {
	}
	
	@Override
	public void itemInitialized(ItemEvent evt) {
	}
	
}