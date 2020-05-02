package com.javaxyq.event;


import java.util.EventObject;

import com.javaxyq.widget.Player;

/**
 * @author dewitt
 * 
 */
public class PlayerStateEvent extends EventObject {

	private String property;
	private Object oldValue;
	private Object newValue;
	private Player player;

	/**
	 * @param source
	 */
	public PlayerStateEvent(Player player, String property, Object oldValue, Object newValue) {
		super(player);
		this.player = player;
		this.oldValue = oldValue;
		this.newValue = newValue;
		this.property = property;
	}

	public String getProperty() {
		return property;
	}

	public Object getOldValue() {
		return oldValue;
	}

	public Object getNewValue() {
		return newValue;
	}

	public Player getPlayer() {
		return player;
	}

}
