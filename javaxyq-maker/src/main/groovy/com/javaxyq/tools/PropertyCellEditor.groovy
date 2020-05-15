/*
 * JavaXYQ Source Code 
 * PropertyCellEditor PropertyCellEditor.groovy
 * by kylixs 2009-10
 * All Rights Reserved.
 * Please see also http://javaxyq.cn or http://javaxyq.googlecode.com.
 * Please email to  javaxyq@qq.com.
 */
package com.javaxyq.tools;

import javax.swing.DefaultCellEditor;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.DefaultCellEditor.EditorDelegate;
import javax.swing.event.CellEditorListener;
import javax.swing.plaf.basic.BasicComboBoxRenderer;
import javax.swing.table.TableCellEditor;
import java.awt.event.MouseEvent;

import java.awt.event.ActionListener;

import javax.swing.JTable;
import java.awt.Color;
import java.awt.Component;
import java.io.File;
import java.util.EventObject;
import java.awt.event.ActionEvent;

import javax.swing.AbstractCellEditor;
import javax.swing.JFileChooser;
import javax.swing.JList;
import javax.swing.JButton;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JComponent;
import groovy.swing.SwingBuilder;

import javax.swing.JTextField;

/**
 * @author dewitt
 *
 */
public class PropertyCellEditor extends AbstractCellEditor implements TableCellEditor {
	
	private FileCellEditor fileEditor= null;
	private BooleanEditor booleanEditor = null;
	private ColorEditor colorEditor;
	private DefaultCellEditor defaultEditor;
	private TableCellEditor editor;
	
	public PropertyCellEditor() {
		//editors
		defaultEditor = new DefaultCellEditor(new JTextField());
		booleanEditor = new BooleanEditor();
		colorEditor = new ColorEditor();
		fileEditor= new FileCellEditor("ui");
		
	
	}
	
	public void addCellEditorListener(javax.swing.event.CellEditorListener l) {
		editor.addCellEditorListener(l);	
	}
	
	@Override
	public void cancelCellEditing() {
		editor.cancelCellEditing();
	}

	@Override
	protected void fireEditingCanceled() {
		editor.fireEditingCanceled();
	}

	@Override
	protected void fireEditingStopped() {
		editor.fireEditingStopped();
	}

	@Override
	public CellEditorListener[] getCellEditorListeners() {
		return editor.getCellEditorListeners();
	}

	@Override
	public void removeCellEditorListener(CellEditorListener l) {
		editor.removeCellEditorListener(l);
	}

	@Override
	public boolean shouldSelectCell(EventObject anEvent) {
		return editor.shouldSelectCell(anEvent);
	}

	@Override
	public boolean stopCellEditing() {
		return editor.stopCellEditing();
	}

	@Override
	public Object getCellEditorValue() {
		return editor.getCellEditorValue();
	}

	public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected,
		int row, int column) {
		this.editor = defaultEditor;
		String attrName = table.getValueAt(row, column-1);
		switch(attrName) {
			case 'path':
			case 'was':
			case 'background':
				editor = fileEditor;
				break;
			case 'closable':
			case 'movable':
			case 'talk':
			case 'toggle':
			case 'enable':
				editor = booleanEditor;
				break;
			case 'color':
				editor = colorEditor;
				break;
			case 'font':
				break;
		}
		
		println "edit at ($row,$column),name:$attrName, value:$value"
		//editor.setValue(value);
		return editor.getTableCellEditorComponent(table, value, isSelected, row, column);
	}
	
}


class FileCellEditor extends AbstractCellEditor implements TableCellEditor,ActionListener {
	protected JComponent editorComp;
	private JFileChooser fileChooser;
	private JTextField textField;
	public FileCellEditor(String dir) {
		fileChooser = new JFileChooser(dir);
		textField = new JTextField();
		editorComp = new JPanel();
		def layout = new BoxLayout(editorComp, BoxLayout.LINE_AXIS);
		editorComp.setLayout(layout);
		JButton btnBrowser = new JButton("..");
		editorComp.add(textField);
		editorComp.add(btnBrowser);
		//listeners
		textField.addActionListener(this);
		btnBrowser.actionPerformed ={ActionEvent e ->
			openFileChooser();
		};
	}
	
	private void openFileChooser() {
		int choice = fileChooser.showOpenDialog(editorComp);
		if(choice == JFileChooser.APPROVE_OPTION) {
			String path = fileChooser.getSelectedFile().getAbsolutePath();
			String base = new File('').getAbsolutePath();
			println "path:$path, base:$base"
			setValue(path.replace(base,'').replace('\\','/'));
		}
	}
	
	public void setValue(Object value) { 
		textField.setText((value != null) ? value.toString() : "");
	}
	public Object getCellEditorValue() {
		return textField.getText();
	}
    public void actionPerformed(ActionEvent e) {
		stopCellEditing();
	}
	public Component getTableCellEditorComponent(JTable table, Object value,boolean isSelected,
		int row, int column) {
		setValue(value);
		return editorComp;
	}
	public Component getComponent() {
		return editorComp;
	}
}

class BooleanEditor extends AbstractCellEditor implements TableCellEditor,ActionListener {
	protected JCheckBox checkBox;
	public BooleanEditor() {
		checkBox = new JCheckBox();
		checkBox.setHorizontalAlignment(JCheckBox.CENTER);
		checkBox.addActionListener(this);
		checkBox.setRequestFocusEnabled(false);
	}
	public void setValue(Object value) { 
		boolean selected = false; 
		if (value instanceof Boolean) {
			selected = ((Boolean)value).booleanValue();
		}
		else if (value instanceof String) {
			selected = value.equals("true");
		}
		checkBox.setSelected(selected);
	}
	public Object getCellEditorValue() {
		return Boolean.valueOf(checkBox.isSelected());
	}
	public void actionPerformed(ActionEvent e) {
		stopCellEditing();
	}
	public Component getTableCellEditorComponent(JTable table, Object value,boolean isSelected,
			int row, int column) {
		setValue(value);
		return checkBox;
	}
	public Component getComponent() {
		return checkBox;
	}
}

class ColorEditor extends AbstractCellEditor implements TableCellEditor,ActionListener{
	private static final String[] colors = ['white','red','green','blue','yellow','pink','black'];
	JComboBox comboBox ;
	public ColorEditor() {
		comboBox = new JComboBox(colors);
		comboBox.setRenderer(new ColorCellRenderer());
		comboBox.putClientProperty("JComboBox.isTableCellEditor", Boolean.TRUE);
		comboBox.addActionListener(this);
	}
	public void setValue(Object value) {
		comboBox.setSelectedItem(value);
	}
	public Object getCellEditorValue() {
		return comboBox.getSelectedItem();
	}
	public boolean shouldSelectCell(EventObject anEvent) { 
		if (anEvent instanceof MouseEvent) { 
			MouseEvent e = (MouseEvent)anEvent;
			return e.getID() != MouseEvent.MOUSE_DRAGGED;
		}
		return true;
	}
    public void actionPerformed(ActionEvent e) {
		stopCellEditing();
	}
	public Component getTableCellEditorComponent(JTable table, Object value,boolean isSelected,
		int row, int column) {
		setValue(value);
		return comboBox;
	}
	public Component getComponent() {
		return comboBox;
	}
		
}

class ColorCellRenderer extends BasicComboBoxRenderer{
	public Component getListCellRendererComponent( JList list,Object value, int index,  boolean isSelected,
	boolean cellHasFocus) {
		super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
		this.setBackground(Color[value]);
		return this;
	}
	
}