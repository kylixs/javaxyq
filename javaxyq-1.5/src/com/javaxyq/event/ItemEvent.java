package com.javaxyq.event;


import java.util.EventObject;

import com.javaxyq.data.ItemInstance;
import com.javaxyq.model.Item;
import com.javaxyq.widget.Player;

/**
 * @author dewitt
 * 
 */
public class ItemEvent extends EventObject {

	private Player player;
	private ItemInstance item;
	private String args;

	public ItemEvent(Player player, ItemInstance item, String args) {
		super(player);
		this.player = player;
		this.item = item;
		this.args = args;
	}

	public Player getPlayer() {
		return player;
	}

	public ItemInstance getItem() {
		return item;
	}

	public String getArgs() {
		return args;
	}

}
