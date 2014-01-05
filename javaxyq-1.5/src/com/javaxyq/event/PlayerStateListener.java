package com.javaxyq.event;


import java.util.EventListener;

/**
 * @author dewitt
 * 
 */
public interface PlayerStateListener extends EventListener {

	/**
	 * 人物状态改变，如气血、魔法、经验值、等级
	 * 
	 * @param evt
	 */
	void stateChanged(PlayerStateEvent evt);

}
