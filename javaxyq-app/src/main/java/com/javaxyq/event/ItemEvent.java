package com.javaxyq.event;


import java.util.EventObject;

import com.javaxyq.data.ItemInstance;
import com.javaxyq.model.Item;
import com.javaxyq.widget.Player;
import lombok.Getter;

/**
 * @author dewitt
 * 
 */
@Getter
public class ItemEvent extends EventObject {

	private final Player player;
	private final ItemInstance item;
	private final String args;

	public ItemEvent(Player player, ItemInstance item, String args) {
		super(player);
		this.player = player;
		this.item = item;
		this.args = args;
	}
}
