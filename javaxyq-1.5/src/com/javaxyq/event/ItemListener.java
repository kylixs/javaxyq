package com.javaxyq.event;


import java.util.EventListener;

import com.javaxyq.model.Item;

/**
 * 物品事件监听器
 * @author dewitt
 *
 */
public interface ItemListener extends EventListener {

	/**
	 * 物品被初始化
	 * @param evt
	 */
	void itemInitialized(ItemEvent evt);
	
	/**
	 * 物品被使用
	 * @param evt
	 */
	void itemUsed(ItemEvent evt);
	
	/**
	 * 物品被销毁
	 * @param evt
	 */
	void itemDestroyed(ItemEvent evt);
}
