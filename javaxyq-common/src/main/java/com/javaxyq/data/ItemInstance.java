/*
 * JavaXYQ Source Code
 * by kylixs
 * at 2010-4-17
 * please visit http://javaxyq.googlecode.com
 * or mail to kylixs@qq.com
 */
package com.javaxyq.data;

import java.io.Serializable;

import com.javaxyq.model.Item;
import com.javaxyq.util.StringUtils;

/**
 * 物品实例对象
 * @author gongdewei
 * @date 2010-4-17 create
 */


public class ItemInstance implements Serializable{
	
	private static final long serialVersionUID = -4833399832791836608L;
	transient private Item item;
	private int amount;
	private long itemId = -1;
	private String name;
	private String type;

	public ItemInstance(Item item, int amount) {
		this.setItem(item);
		this.setAmount(amount);
	}
	public Item getItem() {
		return item;
	}
	public void setItem(Item item) {
		this.item = item;
		this.itemId = item.getId();
		this.name = item.getName();
		this.type = item.getType();
	}
	public int getAmount() {
		return amount;
	}
	public void setAmount(int amount) {
		this.amount = amount;
	}
	public long getItemId() {
		return itemId;
	}
	/**
	 * 改变物品数量
	 * @param n  改变量
	 */
	public int alterAmount(int n) {
		if(n <0 && this.amount + n < 0) {
			n = - this.amount;
		}
		this.amount += n;
		return n;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(this == obj) {
			return true;
		}
		if (obj instanceof ItemInstance) {
			ItemInstance it = (ItemInstance) obj;
			return getItemId() == it.getItemId() && StringUtils.equals(it.getName(), getName());
		}
		return false;
	}
	
	//---------- delegated methods ---------------//
	
	public Long getId() {
		return itemId;
	}
	public String getName() {
		return name;
	}
	public String getType() {
		return type;
	}
	public String getDescription() {
		return item.getDescription();
	}
	public short getLevel() {
		return item.getLevel();
	}
	public long getPrice() {
		return item.getPrice();
	}
	
	
}
