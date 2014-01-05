/*
 * JavaXYQ Source Code
 * by kylixs
 * at 2009-11-15
 * http://javaxyq.googlecode.com
 * kylixs@qq.com
 */
package com.javaxyq.battle;

import java.util.EventObject;

/**
 * @author dewitt
 * @date 2009-11-15 create
 */
public class BattleEvent extends EventObject {

	public static final int BATTLE_WIN = 1;
	public static final int BATTLE_DEFEATED = 2;
	public static final int BATTLE_TIMEOUT = 3;
	public static final int BATTLE_BREAK = 4;
	private BattleCanvas canvas;

	private int id;

	public BattleEvent(BattleCanvas source, int id) {
		super(source);
		this.canvas = source;
		this.id = id;
	}

	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	/**
	 * @return the canvas
	 */
	public BattleCanvas getCanvas() {
		return canvas;
	}
}
