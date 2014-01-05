/*
 * JavaXYQ Engine 
 * 
 * javaxyq@2008 all rights. 
 * http://www.javaxyq.com
 */

package com.javaxyq.config;

/**
 * @author 龚德伟
 * @history 2008-5-21 龚德伟 新建
 */
public class StateConfig implements Config {

	private String id;

	private String character;

	private String weapon;

	public StateConfig(String id, String character, String weapon) {
		this.id = id;
		this.character = character;
		this.weapon = weapon;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCharacter() {
		return character;
	}

	public void setCharacter(String character) {
		this.character = character;
	}

	public String getWeapon() {
		return weapon;
	}

	public void setWeapon(String weapon) {
		this.weapon = weapon;
	}

	public String getType() {
		return "state";
	}

}
