/*
 *TODO 窗口面板 与 系统的交互设计
 */
package com.javaxyq.core;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;
import java.util.Map.Entry;

import com.javaxyq.ui.Panel;
import com.javaxyq.ui.XmlDialogBuilder;

public class DialogFactory {

	private DialogFactory() {
		// cann't new an instance
	}

	/** Dialog索引表<dialog id,ui filename> */
	private static Map<String, String> dialogIndexTable = new HashMap<String, String>();

	/** Dialog实例表 <dialog id, dialog instance> */
	private static Map<String, Panel> dialogs = new HashMap<String, Panel>();
	/** 被关闭的对话框，可以自动回收 */
	private static Map<String, Panel> disposeDialogs = new WeakHashMap<String, Panel>();

	private static DialogBuilder builder = new XmlDialogBuilder();

	public static Panel getDialog(String id) {
		return getDialog(id, false);
	}
	/**
	 * 获得对话框实例
	 * @param id
	 * @param create 是否自动创建
	 * @return
	 */
	public static Panel getDialog(String id, boolean create) {
		Panel dialog = null;
		if (id == null || id.length() == 0)
			return null;
		// 检查缓存的实例
		dialog = dialogs.get(id);
		if(dialog == null) {
			dialog = disposeDialogs.get(id);
		}
		if (dialog == null && create) {
			return createDialog(id);
		}
		return dialog;
	}

	public static Panel createDialog(String id) {
		Panel dialog = null;
		if (id == null || id.length() == 0)
			return null;
		// 调试状态每次重新加载
		// FIXME 调试避免每次加载？只更新脚本
		try {
			String filename = dialogIndexTable.get(id);
			dialog = builder.createDialog(id, filename);
			dialogs.put(id, dialog);
		} catch (Exception e) {
			System.err.println("创建对话框失败！id=" + id);
			e.printStackTrace();
		}
		return dialog;
	}

	/**
	 * 改变Dialog生成器
	 * 
	 * @param builder
	 */
	public static void init(DialogBuilder builder) {
		DialogFactory.builder = builder;
	}

	/**
	 * 添加dialog关联到索引表
	 * 
	 * @param id
	 * @param filename
	 */
	public static void addDialog(String id, String filename) {
		if (id != null && filename != null)
			dialogIndexTable.put(id, filename);
	}
	
	/**
	 * @param dlg
	 */
	public static void dispose(String id,Panel dlg) {
		if(id==null && dlg!=null) {
			Set<Entry<String, Panel>> entres =  dialogs.entrySet();
			for (Entry<String, Panel> entry : entres) {
				if(dlg.equals(entry.getValue())){
					id = entry.getKey();
					break;
				}
			}
		}
		dialogs.remove(id);
		disposeDialogs.put(id, dlg);
	}
}
