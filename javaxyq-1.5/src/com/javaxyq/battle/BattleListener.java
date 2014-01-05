/*
 * JavaXYQ Source Code
 * by kylixs
 * at 2009-11-15
 * http://javaxyq.googlecode.com
 * kylixs@qq.com
 */
package com.javaxyq.battle;

import java.util.EventListener;

/**
 * @author dewitt
 * @date 2009-11-15 create
 */
public interface BattleListener extends EventListener {
	/**
	 * 战斗胜利
	 * @param e
	 */
	void battleWin(BattleEvent e);
	/**
	 * 战斗失败
	 * @param e
	 */
	void battleDefeated(BattleEvent e);
	/**
	 * 战斗超时
	 * @param e
	 */
	void battleTimeout(BattleEvent e);
	/**
	 * 战斗被打断
	 * @param e
	 */
	void battleBreak(BattleEvent e);
	
}
