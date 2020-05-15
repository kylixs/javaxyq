/*
 * JavaXYQ Source Code
 * by kylixs
 * at 2009-12-5
 * please visit http://javaxyq.googlecode.com
 * or mail to kylixs@qq.com
 */
package com.javaxyq.tools.maker;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

import lombok.extern.slf4j.Slf4j;
import org.jdesktop.application.SingleFrameApplication;

import com.javaxyq.io.CacheManager;

/**
 * @author dewitt
 * @date 2009-12-5 create
 */
@Slf4j
public class GameMaker extends SingleFrameApplication{

	public static void main(String[] args) {
		launch(GameMaker.class, args);

	}

	private String lastOpenDir="." ;
	private String lastSaveDir="." ;
	
	private DataFacade dataFacade;

	@Override
	protected void startup() {
		System.setProperty("derby.system.home",CacheManager.getInstance().getCacheBase());
		loadProperties();
		String basePath = "";
		dataFacade = new DataFacade(basePath);
		GameMakerView view = new GameMakerView(this);
		//mainFrame.setLocationRelativeTo(null);
		show(view);
	}
	
	@Override
	protected void shutdown() {
		//super.shutdown();
		saveProperties();
	}
	
	/**
	 * 加载配置
	 */
	private void loadProperties() {
		Properties props = new Properties();
		try {
			props.load(new FileInputStream("GameMaker.properties"));
			lastOpenDir = props.getProperty("LastOpenDir",lastOpenDir);
			lastSaveDir = props.getProperty("LastSaveDir",lastSaveDir);
		} catch (FileNotFoundException e) {
			//e.printStackTrace();
			log.info("没有找到配置文件：GameMaker.properties");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void saveProperties() {
		Properties props = new Properties();
		try {
			props.setProperty("LastOpenDir", lastOpenDir);
			props.setProperty("LastSaveDir", lastSaveDir);
			props.store(new FileOutputStream("GameMaker.properties"),"Create by JavaXYQ Game Maker at "+new java.util.Date());
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public DataFacade getDataFacade() {
		return dataFacade;
	}


}
