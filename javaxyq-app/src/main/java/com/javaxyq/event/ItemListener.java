package com.javaxyq.event;


import java.util.EventListener;

import com.javaxyq.model.Item;

/**
 * 物品事件监听器
 *
 * @author dewitt
 */
public interface ItemListener extends EventListener {

    /**
     * 物品被初始化
     */
    default void onItemInit(ItemEvent evt) {
    }

    /**
     * 物品被使用
     */
    void onItemUsed(ItemEvent evt);

    /**
     * 物品被销毁
     */
    default void onItemDestroyed(ItemEvent evt) {
    }
}
