/*
 * JavaXYQ Source Code
 * by kylixs
 * at 2009-11-26
 * please visit http://javaxyq.googlecode.com
 * or mail to kylixs@qq.com
 */
package com.javaxyq.event;

import com.javaxyq.ui.Panel;


/**
 * @author dewitt
 * @date 2009-11-26 create
 */
public class PanelEvent extends ActionEvent{
	
	public static final String INITIAL = "initial";
	
	public static final String UPDATE= "update";
	
	public static final String DISPOSE = "dispose";
	public static final String TALK = "talk";

	private Panel panel;
	
	public PanelEvent(Object source, String command, Object[] args) {
		super(source, command, args);
		consumed = false;
		if (source instanceof Panel) {
			this.panel = (Panel) source;
		}
	}
	
	public PanelEvent(Object source, String command) {
		super(source, command);
		consumed = false;
		if (source instanceof Panel) {
			this.panel = (Panel) source;
		}
	}
	
	public PanelEvent(Object source, String command,Panel panel) {
		this(source, command,panel,null);
	}
	
	public PanelEvent(Object source, String command,Panel panel, Object[] args) {
		super(source, command,args);
		consumed = false;
		this.panel = panel;
	}
	
	public Panel getPanel() {
		return panel;
	}
}
