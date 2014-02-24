package com.javaxyq.data;

import java.io.Serializable;

import com.javaxyq.model.Item;


public abstract class Items implements Item, Serializable {

	private static final long serialVersionUID = 1L;
	
	
	public Items(){
	}
	
	
	
	public abstract String toString();
	
	public abstract int hashCode();

}
