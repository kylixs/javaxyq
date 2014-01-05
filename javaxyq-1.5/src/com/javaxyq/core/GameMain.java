/*
 * JavaXYQ Source Code
 * by kylixs
 * http://javaxyq.googlecode.com
 * kylixs@qq.com
 */

package com.javaxyq.core;

import java.io.File;
import java.io.IOException;

import com.javaxyq.io.CacheManager;
import com.javaxyq.ui.UIHelper;

/**
 * JavaXYQ 游戏入口类
 * 
 * @author 龚德伟
 * @history 2008-6-7 龚德伟 新建
 */
public final class GameMain {

	public static String applicationName = "JavaXYQ";

	private static String homeURL;

	public static final int APP_APPLET = 1;
	private static final int APP_DESKTOP = 0;

	public static String version = "";

//	private static boolean isDebug;

	private static boolean showCopyright = false;
	
	private static boolean playingMusic = true;

	//public static int appType = APP_DESKTOP;

//	private static void showCopyright() {
//		// copyright
//		if (showCopyright) {
//			LoadingCanvas loadingCanvas = UIHelper.getLoadingCanvas();
//			Image img = SpriteFactory.loadImage("/resources/loading/声明.jpg");
//			loadingCanvas.showImage(img, 3000);
//			img = SpriteFactory.loadImage("/resources/loading/资源版权.jpg");
//			loadingCanvas.showImage(img, 2000);
//
//			img = SpriteFactory.loadImage("/resources/loading/梦想.jpg");
//			loadingCanvas.showImage(img, 3000);
//			img = SpriteFactory.loadImage("/resources/loading/感谢.jpg");
//			loadingCanvas.showImage(img, 3000);
//			img = SpriteFactory.loadImage("/resources/loading/version1.3.jpg");
//			loadingCanvas.showImage(img, 3000);
//		}
//	}
	
//	public static void loadGame() {
//		startLoading();
//		setDebug(false);
//		setShowCopyright(false);
//		setApplicationName("JavaXYQ ");
//		setVersion("1.4 M2");
//		setHomeURL("http://javaxyq.googlecode.com/");
//		updateLoading("loading game ...");
//		setCursor(Cursor.DEFAULT_CURSOR);
//		
//		showCopyright();
//		UIHelper.init();
//		
//		//updateLoading("loading groovy ...");
//		
//		updateLoading("loading actions ...");
//		XmlDataLoader.defActions();
//		//updateLoading("loading scenes ...");
//		//XmlDataLoader.defScenes();
//		//updateLoading("loading talks ...");
//		//XmlDataLoader.defTalks();
//		updateLoading("loading ui ...");
//		loadUIs();
//		
//		registerAction("com.javaxyq.action.transport",new DefaultTransportAction());
//		MovementManager.addMovementAction("random", new RandomMovementAction());
//		
//		//task
//		TaskManager.instance.register("school", "com.javaxyq.task.SchoolTaskCoolie");
//		ItemManager.regItem(ItemTypes.TYPE_MEDICINE, new MedicineItemHandler());
//		
//		updateLoading("loading data ...");
//		DataStore.init();
//		DataStore.loadData();
//		updateLoading("starting game ...");
//		stopLoading();
		
//		DataStore.addHp(getPlayer(), -200);
//		DataStore.addMp(getPlayer(), -200);
//		ItemInstance item = DataStore.createItem("血色茶花");
//		item.setAmount(1);
//		DataStore.addItemToPlayer(getPlayer(), item);
//		item = DataStore.createItem("龙之心屑");
//		item.setAmount(1);
//		DataStore.addItemToPlayer(getPlayer(), item);
//		item = DataStore.createItem("金创药");
//		item.setAmount(1);
//		DataStore.addItemToPlayer(getPlayer(), item);
//		item = DataStore.createItem("金香玉");
//		item.setAmount(1);
//		DataStore.addItemToPlayer(getPlayer(), item);
//		item = DataStore.createItem("九转回魂丹");
//		item.setAmount(1);
//		DataStore.addItemToPlayer(getPlayer(), item);
		//setPlayingMusic(false);//debug
//	}

//	public static void setPlayer(Player p) {
//		player = p;
//		UIHelper.getSceneCanvas().setPlayer(p);
//	}

//	public static Player getPlayer() {
//		return player;
//	}

//	public static Point localToPlayer(Point point) {
//		Point pl = player.getLocation();
//		Point pl2 = new Point(point);
//		pl2.translate(-pl.x, -pl.y);
//		return pl2;
//	}

	public static String getApplicationName() {
		return applicationName;
	}

	public static void setApplicationName(String applicationName) {
		GameMain.applicationName = applicationName;
	}

	public static String getHomeURL() {
		return homeURL;
	}

	public static String getVersion() {
		return version;
	}

	public static void setHomeURL(String homeURL) {
		GameMain.homeURL = homeURL;
	}

	public static void setVersion(String version) {
		GameMain.version = version;
	}

//	private static void startLoading() {
//		LoadingCanvas loadingCanvas = UIHelper.getLoadingCanvas();
//		CacheManager.getInstance().addDownloadListener(loadingCanvas);
//		startTime = System.currentTimeMillis();
//		loadingCanvas.setLoading("start loading ...");
//		Image img = SpriteFactory.loadImage("/resources/loading/cover.jpg");
//		loadingCanvas.setContent(img);
//		loadingCanvas.playMusic();
//		loadingCanvas.fadeIn(200);
//	}
//
//	public static void stopLoading() {
//		loaded = true;
//		endTime = System.currentTimeMillis();
//		System.out.printf("total cost: %ss\n", (endTime - startTime) / 1000.0);
//		UIHelper.installUI();
//        UIHelper.getWindow().setCanvas(UIHelper.getSceneCanvas());
//        CacheManager.getInstance().removeDownloadListener(UIHelper.getLoadingCanvas());
//		CacheManager.getInstance().addDownloadListener(UIHelper.getGameCanvas());
//        UIHelper.getLoadingCanvas().dispose();
//        //loadingCanvas.stopMusic();
//        UIHelper.updateUI();
//	}


	public static void pause(long t) {
		try {
			Thread.sleep(t);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

//	public static boolean isDebug() {
//		return isDebug;
//	}

//	public static void setDebug(boolean isDebug) {
//		GameMain.isDebug = isDebug;
//		UIHelper.setDebug(isDebug);
//		GroovyScript.setDebug(isDebug);
//	}


//	public boolean pass(int x, int y) {
//		return UIHelper.getSceneCanvas().pass(x, y);
//	}

//
//	public static void setCursor(String cursorId) {
//		UIHelper.getGameCanvas().setGameCursor(cursorId);
//	}
//
//	public static void restoreCursor() {
//		UIHelper.getGameCanvas().setGameCursor(Cursor.DEFAULT_CURSOR);
//	}

//	public static void registerAction(String actionId, final ActionListener al) {
//		Action action = new AbstractAction() {
//			public void actionPerformed(java.awt.event.ActionEvent e) {
//				al.actionPerformed(e);
//			}
//		};
//		UIHelper.getActionMap().put(actionId, action);
//	}
//
//	public static void registerAction(String actionId, Action action) {
//		UIHelper.getActionMap().put(actionId, action);
//	}

	public static boolean isPlayingMusic() {
		return playingMusic;
	}

	public static void setPlayingMusic(boolean playingMusic) {
		GameMain.playingMusic = playingMusic;
		if(playingMusic) {
			ApplicationHelper.getApplication().playMusic();
		}else {
			ApplicationHelper.getApplication().stopMusic();
		}
	}

	public static boolean isShowCopyright() {
		return showCopyright;
	}

	public static void setShowCopyright(boolean showCopyright) {
		GameMain.showCopyright = showCopyright;
	}

	/**
	 * @deprecated Use {@link CacheManager#getFile(String)} instead
	 */
//	public static File getFile(String filename) {
//		return CacheManager.getInstance().getFile(filename);
//	}
	
	/**
	 * 创建文件
	 * @param filename
	 * @return
	 * @throws IOException 
	 * @deprecated Use {@link CacheManager#createFile(String)} instead
	 */
//	public static File createFile(String filename) throws IOException {
//		return CacheManager.getInstance().createFile(filename);
//	}
	

//	public static SceneListener findSceneAction(String id) {
//		Object action = GroovyScript.loadClass("scripts/scene/"+id+".groovy");
//		return (SceneListener)action;
//	}	
	
//	public static void main(String[] args) throws InterruptedException {
//		System.setProperty("javaxyq.title", GameMain.applicationName +" "+ GameMain.version);
//		UIHelper.initDesktop(args);
//		GameMain.loadGame();
//	}
}
