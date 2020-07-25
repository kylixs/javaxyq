package com.javaxyq.core;

import com.javaxyq.data.ItemInstance;
import com.javaxyq.event.ItemListener;
import com.javaxyq.widget.Player;

public interface ItemManager {

    void registerItem(int type, ItemListener l);

    boolean useItem(Player player, ItemInstance item);
}