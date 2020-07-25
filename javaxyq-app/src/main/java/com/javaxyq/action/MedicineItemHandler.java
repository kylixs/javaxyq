package com.javaxyq.action;

import com.javaxyq.core.ApplicationHelper;
import com.javaxyq.core.DataManager;
import com.javaxyq.data.ItemInstance;
import com.javaxyq.data.MedicineItem;
import com.javaxyq.event.ItemEvent;
import com.javaxyq.event.ItemListener;
import com.javaxyq.util.MP3Player;
import com.javaxyq.widget.Player;


public class MedicineItemHandler implements ItemListener {

    private DataManager dataManager;

    public MedicineItemHandler() {
        dataManager = ApplicationHelper.getApplication().getDataManager();
    }

    @Override
    public void onItemUsed(ItemEvent evt) {
        MP3Player.play("sound/addon/use_item.mp3");
        ItemInstance itemInstance = evt.getItem();
        if (itemInstance.inc(-1) == -1) { // 如果成功删除一个数量
            MedicineItem item = (MedicineItem) itemInstance.getItem();
            Player player = evt.getPlayer();
            // 疗伤、播放效果动画及声音
            if (item.getHp() != 0 && item.getMp() != 0) {
                player.playEffect("add_hpmp", false);
                dataManager.incHp(player, item.getHp());
                dataManager.incMp(player, item.getMp());
            } else if (item.getHp() != 0) {
                player.playEffect("add_hp", false);
                dataManager.incHp(player, item.getHp());
            } else if (item.getMp() != 0) {
                player.playEffect("add_mp", false);
                dataManager.incMp(player, item.getMp());
            }
        }
    }
}