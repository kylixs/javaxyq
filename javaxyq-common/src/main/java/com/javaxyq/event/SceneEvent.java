package com.javaxyq.event;



import java.util.EventObject;

/**
 * @author dewitt
 * 
 */
public class SceneEvent extends EventObject {

	private String scene;
	private int x;
	private int y;

	public SceneEvent(String source, int x, int y) {
		super(source);
		this.scene = source;
		this.x = x;
		this.y = y;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public String getScene() {
		return scene;
	}

}
