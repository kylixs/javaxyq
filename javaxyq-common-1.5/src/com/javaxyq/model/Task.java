package com.javaxyq.model;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 任务实体类
 * @author dewitt
 * @date 2009-11-23 create
 */
public class Task implements Serializable{
	private static final long serialVersionUID = 2745458583606730756L;
	private String sender;
	private String receiver;
	private Date createDate;
	private String type;
	private String subtype;
	private String id;
	private int exp;
	private int money;
	/** 任务是否已完成 */
	private boolean finished;
	/** 自动触发、响应任务（任务完成时） */
	private boolean autoSpark;  
	private Map params;
	
	public Task() {
		params = new HashMap();
		createDate = new Date();
	}
	
	public Task(String type,String subtype,String sender,String receiver) {
		params = new HashMap();
		createDate = new Date();
		this.type = type;
		this.subtype = subtype;
		this.sender = sender;
		this.receiver = receiver;
	}
	
	/**
	 * 设置某个参数的值
	 * @param paramName
	 * @param value
	 */
	public void set(String paramName,Object value) {
		params.put(paramName, value);
	}
	
	/**
	 * 增加某个参数的值！注意，与set不一样
	 * @param paramName
	 * @param value
	 */
	public void add(String paramName,Object value) {
		//params[paramName] += value;
		Integer val = (Integer) params.get(paramName);
		if(val == null) {
			val = new Integer(0);
		}
		params.put(paramName, val + (Integer)value);
	}
	
	public void remove(String param) {
		params.remove(param);
	}
	public Object get(String param) {
		return params.get(param);
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
	 * @return the autoSpark
	 */
	public boolean isAutoSpark() {
		return autoSpark;
	}
	
	/**
	 * @return the finished
	 */
	public boolean isFinished() {
		return finished;
	}
	
	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}
	
	/**
	 * @return the subtype
	 */
	public String getSubtype() {
		return subtype;
	}
	
	
	public String getSender() {
		return sender;
	}

	public String getReceiver() {
		return receiver;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public String getId() {
		return id;
	}

	public int getExp() {
		return exp;
	}

	public int getMoney() {
		return money;
	}

	public Map getParams() {
		return params;
	}

	public void setFinished(boolean finished) {
		this.finished = finished;
	}

	public void setAutoSpark(boolean autoSpark) {
		this.autoSpark = autoSpark;
	}

	/**
	 * set value of exp
	 * @param exp 
	 */
	public void setExp(int exp) {
		this.exp = exp;
	}

	public void setMoney(int money) {
		this.money = money;
	}

	protected void writeObject(ObjectOutputStream s)
	throws IOException
	{
		s.writeBoolean(autoSpark);
		s.writeObject(createDate);
		s.writeInt(getExp());
		s.writeBoolean(finished);
		s.writeUTF(id);
		s.writeInt(money);
		//params
		s.writeObject(params);
		s.writeUTF(receiver);
		s.writeUTF(sender);
		s.writeUTF(subtype);
		s.writeUTF(type);
	}
	
	/**
	 * Reconstitute this object from a stream (i.e., deserialize it).
	 */
	protected void readObject(ObjectInputStream s)
	throws IOException, ClassNotFoundException
	{
		autoSpark=s.readBoolean();
		createDate=(Date) s.readObject();
		setExp(s.readInt());
		finished=s.readBoolean();
		id=s.readUTF();
		money=s.readInt();
		//params
		params=(Map) s.readObject();
		receiver=s.readUTF();
		sender=s.readUTF();
		subtype=s.readUTF();
		type=s.readUTF();
				
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Task [autoSpark=");
		builder.append(autoSpark);
		builder.append(", createDate=");
		builder.append(createDate);
		builder.append(", exp=");
		builder.append(getExp());
		builder.append(", finished=");
		builder.append(finished);
		builder.append(", id=");
		builder.append(id);
		builder.append(", money=");
		builder.append(money);
		builder.append(", params=");
		builder.append(params);
		builder.append(", receiver=");
		builder.append(receiver);
		builder.append(", sender=");
		builder.append(sender);
		builder.append(", subtype=");
		builder.append(subtype);
		builder.append(", type=");
		builder.append(type);
		builder.append("]");
		return builder.toString();
	}
	
}
