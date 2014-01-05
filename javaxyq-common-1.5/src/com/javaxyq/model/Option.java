/*
 * JavaXYQ Source Code
 * by kylixs
 * at 2010-4-29
 * please visit http://javaxyq.googlecode.com
 * or mail to kylixs@qq.com
 */
package com.javaxyq.model;

/**
 * 对话的选项
 * @author gongdewei
 * @date 2010-4-29 create
 */
public class Option {

	private String text;
	private String action;
	private Object value;
	private boolean selected;
	public Option(String text, String action) {
		this(text,action,null);
	}
	public Option(String text, String action, Object value) {
		super();
		this.text = text;
		this.action = action;
		this.value = value;
	}
	public boolean isSelected() {
		return selected;
	}
	public void setSelected(boolean selected) {
		this.selected = selected;
	}
	public String getText() {
		return text;
	}
	public Object getValue() {
		return value;
	}
	
	public String getAction() {
		return action;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((action == null) ? 0 : action.hashCode());
		result = prime * result + ((text == null) ? 0 : text.hashCode());
		result = prime * result + ((value == null) ? 0 : value.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Option other = (Option) obj;
		if (action == null) {
			if (other.action != null)
				return false;
		} else if (!action.equals(other.action))
			return false;
		if (text == null) {
			if (other.text != null)
				return false;
		} else if (!text.equals(other.text))
			return false;
		if (value == null) {
			if (other.value != null)
				return false;
		} else if (!value.equals(other.value))
			return false;
		return true;
	}
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Option [action=").append(action).append(", selected=").append(selected).append(", text=")
			.append(text).append(", value=").append(value).append("]");
		return builder.toString();
	}
	
}
