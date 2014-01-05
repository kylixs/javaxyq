/**
 * 
 */
package com.javaxyq.config.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

import com.javaxyq.config.ConfigManager;
import com.javaxyq.io.CacheManager;

/**
 * @author gongdewei
 * @date 2011-5-3 create
 */
public class ConfigManagerImpl implements ConfigManager {

	private String configFile;
	private Properties configs;
	
	public ConfigManagerImpl(String configFile){
		super();
		this.configFile = configFile;
		configs = new Properties(System.getProperties());
	}

	@Override
	public String get(String key) {
		return configs.getProperty(key);
	}

	@Override
	public int getInt(String key) {
		String strval = configs.getProperty(key);
		return Integer.parseInt(strval);
	}


	@Override
	public void put(String key, Object value) {
		configs.setProperty(key, String.valueOf(value));
	}
	
	@Override
	public void loadConfigs() throws FileNotFoundException, IOException {
		File file = CacheManager.getInstance().getFile(configFile);
		if(!file.exists()) {
			file.createNewFile();
		}
		configs.load(new FileInputStream(file));
		
	}

	@Override
	public void saveConfigs() throws IOException {
		File file = CacheManager.getInstance().getFile(configFile);
		configs.store(new FileOutputStream(file), "Create by JavaXYQ (http://javaxyq.googlecode.com)");
	}

}
