/*
 * JavaXYQ Source Code 
 * MyTableModel MyTableModel.java
 * by kylixs 2009-10
 * All Rights Reserved.
 * Please see also http://javaxyq.cn or http://javaxyq.googlecode.com.
 * Please email to  javaxyq@qq.com.
 */
package com.javaxyq.tools;

import groovy.model.ValueModel;


//触发更新事件的TableModel
public class MyTableModel extends groovy.model.DefaultTableModel{
	
	private static final long serialVersionUID = -4194721387687243555L;

	public MyTableModel(ValueModel rowsModel, ValueModel rowModel) {
		super(rowsModel, rowModel);
	}
	
	public MyTableModel(ValueModel rowsModel) {
		super(rowsModel);
	}
	
	public void setValueAt(Object value, int rowIndex, int columnIndex) {
		super.setValueAt(value, rowIndex, columnIndex);
		fireTableCellUpdated(rowIndex, columnIndex);
	}
}



