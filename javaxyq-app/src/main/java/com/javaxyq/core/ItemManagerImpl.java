package com.javaxyq.core;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.javaxyq.data.ItemInstance;
import com.javaxyq.event.ItemEvent;
import com.javaxyq.event.ItemListener;
import com.javaxyq.model.Item;
import com.javaxyq.model.ItemTypes;
import com.javaxyq.widget.Player;
import lombok.extern.slf4j.Slf4j;

/**
 * @author dewitt
 */
@Slf4j
public class ItemManagerImpl implements ItemManager {

    private static final ItemListener DEFAULT_HANDLER = evt -> log.error("Unknown Item. {}", evt);

    private final Map<Integer, ItemListener> itemHandlers = new HashMap<>();

    private final DataManager dataManager;

    public ItemManagerImpl(DataManager dataManager) {
        super();
        this.dataManager = dataManager;
    }

    public void registerItem(int type, ItemListener l) {
        itemHandlers.put(type, l);
    }

    public boolean useItem(Player player, ItemInstance item) {
        if (item.getCount() <= 0)
            return false;

        ItemListener handler = findItemListener(item.getItem());
        handler.onItemUsed(new ItemEvent(player, item, ""));
        if (item.getCount() <= 0) { //如果消耗完，则销毁物品
            dataManager.removeItemFromPlayer(player, item);
        }
        return true;
    }

    private ItemListener findItemListener(Item item) {
        Set<Integer> keys = itemHandlers.keySet();
        for (Integer type : keys) {
            if (ItemTypes.isType(item, type)) {
                return itemHandlers.get(type);
            }
        }
        return DEFAULT_HANDLER;
    }
}
