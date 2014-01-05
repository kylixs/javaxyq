/**
 * 
 */
package com.javaxyq.ui;

import java.util.HashMap;
import java.util.Map;

/**
 * @author gongdewei
 * @date 2011-4-30 create
 */
public class UIFactory {

	public static final String TOOLTIP_TEMPLATE= "tooltipTemplate";
	
	private static final Map<String, Object>uiDefaults = new HashMap<String, Object>();
	/**
	 * 注册UI相关组件
	 * @param key
	 * @param value
	 */
	public static void put(String key, Object value) {
		uiDefaults.put(key, value);
	}
	
	public static Object get(String key) {
		return uiDefaults.get(key);
	}
}
