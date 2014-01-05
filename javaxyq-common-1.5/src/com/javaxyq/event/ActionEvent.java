/*
 * JavaXYQ Engine 
 * 
 * javaxyq@2008 all rights. 
 * http://www.javaxyq.com
 */

package com.javaxyq.event;

/**
 * @author 龚德伟
 * @history 2008-6-9 龚德伟 新建
 */
public class ActionEvent extends java.awt.event.ActionEvent {

	// private long when;

	private static final long serialVersionUID = 3465242220868670205L;
	private Object[] arguments;
	private String command;

	public ActionEvent(Object source, String command, Object[] args) {
		super(source, java.awt.event.ActionEvent.ACTION_PERFORMED, command, 0, 0);
		if (args == null) {
			args = new Object[] {};
		}
		this.command = command;
		this.arguments = args;
	}

	// public ActionEvent(Object source,Object ... args) {
	//        
	// }

	public ActionEvent(Object source, String command) {
		super(source, java.awt.event.ActionEvent.ACTION_PERFORMED, command, 0, 0);
		String[] strs = command.split(" ");
		this.command = strs[0];
		if(strs.length > 1) {
			this.arguments = new String[strs.length-1];
			for (int i = 0; i < strs.length-1; i++) {
				this.arguments[i] = strs[i+1];
			}
		}
	}

	/**
	 * @param evt
	 */
	public ActionEvent(java.awt.event.ActionEvent evt) {
		super(evt.getSource(), java.awt.event.ActionEvent.ACTION_PERFORMED, evt.getActionCommand());
		String command = evt.getActionCommand();
		String[] strs = command.split(" ");
		this.command = strs[0];
		if(strs.length > 1) {
			this.arguments = new String[strs.length-1];
			for (int i = 0; i < strs.length-1; i++) {
				this.arguments[i] = strs[i+1];
			}
		}
	}

	public String getCommand() {
		return this.command;
	}

	public Object[] getArguments() {
		return arguments;
	}

	public Object getArgument(int i) {
		if (arguments == null || i < 0 || arguments.length < i) {
			return null;
		}
		return arguments[i];
	}

	public String getArgumentAsString(int i) {
		if (arguments == null || i < 0 || arguments.length < i) {
			return null;
		}
		return (String) arguments[i];
	}

	public int getArgumentAsInt(int i) {
		if (arguments == null || i < 0 || arguments.length <= i) {
			return 0;
		}
		if (arguments[i] instanceof Integer) {
			Integer val = (Integer) arguments[i];
			return val;
		}
		try {
			return Integer.parseInt((String) arguments[i]);
		} catch (Exception e) {
			return 0;
		}
	}

	@Override
	public boolean isConsumed() {
		return super.isConsumed();
	}
	@Override
	public void consume() {
		super.consume();
	}
}
