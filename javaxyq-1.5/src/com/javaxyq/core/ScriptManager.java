package com.javaxyq.core;



import com.javaxyq.event.PlayerListener;

/**
 * Ω≈±æπ‹¿Ì∆˜
 * @author gongdewei
 * @date 2010-4-25 create
 */
public class ScriptManager {
	private static final ScriptManager instance = new ScriptManager();
	private ScriptEngine scriptEngine;
	private ScriptManager() {
		scriptEngine = DefaultScript.getInstance();
	}
	public static ScriptManager getInstance() {
		return instance;
	}
	
//	public PlayerListener findListener(String npcId) {
//		return scriptEngine.loadClass("scripts/npc/n"+npcId+".java", PlayerListener.class);
//	}
}
