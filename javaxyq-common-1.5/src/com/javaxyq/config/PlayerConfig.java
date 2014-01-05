/*
 * JavaXYQ 
 * 
 * javaxyq 2008 all rights. http://www.javaxyq.com
 */

package com.javaxyq.config;

/**
 * @author 龚德伟
 * @history 2008-5-21 龚德伟 新建
 */
public class PlayerConfig implements Config {
	public static final String STATE_STAND = "stand";

	public static final String STATE_WALK = "walk";

	private String id;

	private String character;

	private String name;

	private String state = STATE_STAND;

	private int direction;

	private int x;

	private int y;

	private String colorations;

	private String movement;

	private long period;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setDirection(int direction) {
		this.direction = direction;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public void setLocation(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public int getDirection() {
		return direction;
	}

	public void setColorations(String colorations) {
		this.colorations = colorations;
	}

	public String getColorations() {
		return colorations;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("PlayerConfig[");
		sb.append("id=");
		sb.append(this.id);
		sb.append("character=");
		sb.append(this.character);
		sb.append(",name=");
		sb.append(this.name);
		sb.append(",colorations=");
		sb.append(this.colorations);
		sb.append(",movement=");
		sb.append(this.movement);
		sb.append("]");
		return sb.toString();
	}

	public String getCharacter() {
		return character;
	}

	public void setCharacter(String character) {
		this.character = character;
	}

	@Override
	public String getType() {
		return "PlayerConfig";
	}

	public String getMovement() {
		return movement;
	}

	public void setMovement(String movement) {
		this.movement = movement;
	}

	public long getPeriod() {
		return this.period;
	}

	public void setPeriod(long period) {
		this.period = period;
	}
}
