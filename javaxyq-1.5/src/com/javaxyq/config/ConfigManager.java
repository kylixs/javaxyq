/**
 * 
 */
package com.javaxyq.config;

import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * ≈‰÷√π‹¿Ì
 * @author gongdewei
 * @date 2011-5-3 create
 */
public interface ConfigManager {

	public String get(String key);
	
	public int getInt(String key);
	
	public void put(String key, Object value);
	
	public void loadConfigs() throws FileNotFoundException, IOException;
	
	public void saveConfigs() throws IOException;
}
