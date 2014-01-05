/**
 * 
 */
package com.javaxyq.core;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Administrator
 *
 */
public class Environment {

	private static Map<String, Object> props = new HashMap<String, Object>();
	
	public static final String LAST_PATROL_TIME = "LastPatrolTime";

	public static final String PATROL_INTERVAL = "PatrolInterval";
	
	static {
		set(PATROL_INTERVAL, 10000L);
		set(LAST_PATROL_TIME, System.currentTimeMillis());
	}
	public static void set(String name,Object value) {
		props.put(name, value);
	}
	public static Object get(String name) {
		return props.get(name);
	}
}
