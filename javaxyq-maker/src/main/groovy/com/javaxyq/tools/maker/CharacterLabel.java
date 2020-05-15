/*
 * JavaXYQ Source Code
 * by kylixs
 * at 2009-12-7
 * please visit http://javaxyq.googlecode.com
 * or mail to kylixs@qq.com
 */
package com.javaxyq.tools.maker;

import java.awt.Graphics;

import javax.swing.JLabel;

/**
 * 人物角色label
 * @author dewitt
 * @date 2009-12-7 create
 */
public class CharacterLabel extends JLabel {

	private String character;
	public CharacterLabel() {
	}
	public CharacterLabel(String id) {
		setCharacter(id);
	}
	/**
	 * @param id
	 */
	public void setCharacter(String id) {
		this.character = id;
		init();
	}
	
	/**
	 * @return the character
	 */
	public String getCharacter() {
		return character;
	}
	
	private void init() {
		
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
	}
}
