/**
 * 
 */
package com.javaxyq.battle;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

import com.javaxyq.model.PlayerVO;
import com.javaxyq.widget.Player;

/**
 * 战斗命令
 * @author dewitt
 */
public class Command implements Comparable{
	private String cmd;
	private Player source;
	private Player target;
	private Map params = new HashMap();
	public Command(String cmd,Player source) {
		this(cmd,source,null,null);
	}
	public Command(String cmd,Player source,Player target) {
		this.cmd = cmd;
		this.source = source;
		this.target = target;
	}
	public Command(String cmd,Player source,Player target,Map params) {
		this(cmd,source,target);
		this.params = params;
	}
	public String getCmd() {
		return this.cmd;
	}
	/**
	 * 添加一个参数
	 * @param name
	 * @param value
	 */
	public void add(String name,Object value) {
		//this.params[name] = value;
		this.params.put(name, value);
	}
	public Object get(String name) {
		return this.params.get(name);
	}
	public int getInt(String name) {
		Integer val = (Integer) this.params.get(name);
		return val!=null?val.intValue():0;
	}
	public boolean getBool(String name) {
		Boolean val = (Boolean) this.params.get(name);
		return val!=null?val.booleanValue():false;
	}
	
	/**
	 * @return the source
	 */
	public Player getSource() {
		return source;
	}
	/**
	 * @return the target
	 */
	public Player getTarget() {
		return target;
	}
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Command [cmd=");
		builder.append(cmd);
		builder.append(", params=");
		builder.append(params);
		builder.append(", source=");
		builder.append(source);
		builder.append(", target=");
		builder.append(target);
		builder.append("]");
		return builder.toString();
	}
	@Override
	public int compareTo(Object o) {
		if (o instanceof Command) {
			Command cmd2 = (Command) o;
			PlayerVO data1 = this.source.getData();
			PlayerVO data2 = cmd2.getSource().getData();
			return (data1.speed +data1.tmpSpeed) - (data2.speed +data2.tmpSpeed);
		}
		return 0;
	}
	
}
