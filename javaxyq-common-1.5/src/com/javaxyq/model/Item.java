/*
 * JavaXYQ Source Code 
 * Item Item.groovy
 * by kylixs 2009-10
 * All Rights Reserved.
 * Please see also http://javaxyq.cn or http://javaxyq.googlecode.com.
 * Please email to  javaxyq@qq.com.
 */
package com.javaxyq.model;

import java.io.Serializable;

/**
 * 游戏物品对象接口
 * @author gongdewei
 * @date 2010-4-17 create
 */
public interface Item extends Serializable {

	public abstract Long getId();

	public abstract String getName();

	public abstract String getType();

	public abstract String getDescription();

	public abstract short getLevel();

	public abstract long getPrice();

}