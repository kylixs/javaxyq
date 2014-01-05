/*
 * JavaXYQ Source Code
 * by kylixs
 * http://javaxyq.googlecode.com
 * kylixs@qq.com
 */
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

/**
 * @author dewitt
 *
 */
public class ItemManagerImpl implements ItemManager {
	
	private static ItemListener DEFAULT_HANDLER = new ItemListener() {
		@Override
		public void itemUsed(ItemEvent evt) {
			System.err.println("itemUsed: Unknown Item !"+evt);
		}
		
		@Override
		public void itemInitialized(ItemEvent evt) {
			System.err.println("itemInitialized: Unknown Item !"+evt);
		}
		
		@Override
		public void itemDestroyed(ItemEvent evt) {
			System.err.println("itemDestroyed: Unknown Item !"+evt);
		}
	};
	private Map<Integer,ItemListener> itemHandlers = new HashMap<Integer, ItemListener>();

	private DataManager dataManager;
	public ItemManagerImpl(DataManager dataManager) {
		super();
		this.dataManager = dataManager;
	}

	public void regItem(int type,ItemListener l) {
		itemHandlers.put(type, l);
	}
	
	public boolean useItem(Player player,ItemInstance item) {
		if(item.getAmount() > 0) {
			ItemListener handler = findItemAction(item.getItem());
			if(handler != null) {
				handler.itemUsed(new ItemEvent(player,item,""));
				if(item.getAmount() <= 0) {//如果消耗完，则销毁物品
					dataManager.removePlayerItem(player,item);
				}
				return true;
			}
		}
		return false;
	}
	
	private ItemListener findItemAction(Item item) {
		Set<Integer> keys = itemHandlers.keySet();
		for (Integer type : keys) {
			if(ItemTypes.isType(item, type)) {
				return itemHandlers.get(type);
			}
		}
		return DEFAULT_HANDLER;
	}
	
	
}
