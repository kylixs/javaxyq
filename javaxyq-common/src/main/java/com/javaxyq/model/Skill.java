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
 * 
 */
public interface Skill extends Serializable {

	Long getId();

	String getName();

	String getDescription();

	String getEffection();
	
	String getConditions();
	
	String getConsumption();


}