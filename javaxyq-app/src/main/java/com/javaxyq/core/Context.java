/**
 * 
 */
package com.javaxyq.core;

import com.javaxyq.widget.Player;

/**
 * @author Administrator
 *
 */
public class Context {

	private GameWindow window;
	private Player player;
	private String scene;
	private Player talker;
	
	public Context() {
		super();
	}

	public Player getPlayer() {
		return player;
	}

	public String getScene() {
		return scene;
	}
	
	public GameWindow getWindow() {
		return window;
	}

	void setWindow(GameWindow window) {
		this.window = window;
	}
	void setPlayer(Player player) {
		this.player = player;
	}
	void setScene(String sceneId) {
		this.scene = sceneId;
	}
	void setTalker(Player talker) {
		this.talker = talker;
	}
	public Player getTalker() {
		return this.talker;
	}
}
