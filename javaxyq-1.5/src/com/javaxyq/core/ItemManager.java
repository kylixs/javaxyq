package com.javaxyq.core;

import com.javaxyq.data.ItemInstance;
import com.javaxyq.event.ItemListener;
import com.javaxyq.widget.Player;

public interface ItemManager {

	public abstract void regItem(int type, ItemListener l);

	public abstract boolean useItem(Player player, ItemInstance item);

}