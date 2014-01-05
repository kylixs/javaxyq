/**
 * 
 */
package com.javaxyq.core;

import java.awt.Image;
import java.awt.Point;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

import com.javaxyq.action.MedicineItemHandler;
import com.javaxyq.data.XmlDataLoader;
import com.javaxyq.io.CacheManager;
import com.javaxyq.menu.MainMenuCanvas;
import com.javaxyq.model.ItemTypes;
import com.javaxyq.profile.ProfileException;
import com.javaxyq.task.TaskManager;
import com.javaxyq.ui.Panel;
import com.javaxyq.util.SuffixFilenameFilter;
import com.javaxyq.widget.Cursor;
import com.javaxyq.widget.Player;

/**
 * 桌面游戏客户端程序
 * @author gongdewei
 * @date 2010
 */
public class DesktopApplication extends BaseApplication {

	private DesktopWindow window;
	
	protected GameWindow createWindow() {
		window = new DesktopWindow();
		window.init(context);
		return window;
	}

	@Override
	protected void prepareShow() {
		super.prepareShow();
		Image img = SpriteFactory.loadImage("/resources/loading/cover.jpg");
		LoadingCanvas loadingCanvas = new LoadingCanvas(img, window.getContentWidth(), window.getContentHeight());
		loadingCanvas.setLoading("loading ...");
		window.setCanvas(loadingCanvas);
		//loadingCanvas.fadeIn(100);
//		loadingCanvas.playMusic();
		CacheManager.getInstance().addDownloadListener(loadingCanvas);
	}
	
	@Override
	protected void loadData() {
		super.loadData();
	}

	protected void loadScene() {
		System.out.println("loading scene: "+new java.util.Date());
		Player player = context.getPlayer();
		Point p = player.getSceneLocation();
		if(sceneCanvas == null) {
			sceneCanvas = new SceneCanvas(window.getContentWidth(), window.getContentHeight());
		}
		sceneCanvas.setPlayer(player);
		sceneCanvas.changeScene(context.getScene(), p.x, p.y);
		System.out.println("loaded scene: "+new java.util.Date());
	}
	
	@Override
	protected void loadResources() {
		
		//setDebug(false);
		//setShowCopyright(false);
		//setApplicationName("JavaXYQ ");
		//setVersion("1.4 M2");
		//setHomeURL("http://javaxyq.googlecode.com/");
		promptMsg("loading resource ...");
		window.setGameCursor(Cursor.DEFAULT_CURSOR);
		
		//showCopyright();
		//promptMsg("loading groovy ...");
		
		promptMsg("loading actions ...");
		XmlDataLoader loader = new XmlDataLoader(window);
		loader.parseActions();
		//promptMsg("loading scenes ...");
		//XmlDataLoader.defScenes();
		//promptMsg("loading talks ...");
		//XmlDataLoader.defTalks();
		promptMsg("loading ui ...");
		loadUIs(loader);
		
		//task
		getTaskManager().register("school", "com.javaxyq.task.SchoolTaskCoolie");
		ApplicationHelper.getApplication().getItemManager().regItem(ItemTypes.TYPE_MEDICINE, new MedicineItemHandler());
	}
	
	@Override
	protected void finish() {
		super.finish();
		Image img = SpriteFactory.loadImage("/wzife/login/background.jpg");
		MainMenuCanvas menuCanvas = new MainMenuCanvas(img, window.getContentWidth(), window.getContentHeight());
		Panel mainmenu = getUIHelper().getDialog("mainmenu");
		
		LoadingCanvas loadingCanvas = (LoadingCanvas) window.getCanvas(); 
		//loadingCanvas.stopMusic();
		loadingCanvas.dispose();
		CacheManager.getInstance().removeDownloadListener(loadingCanvas);
		
		//切换到主菜单
		window.setCanvas(menuCanvas);
		getUIHelper().showDialog(mainmenu);
		menuCanvas.playMusic();
		CacheManager.getInstance().addDownloadListener(menuCanvas);
		
		//后台预加载场景
		//preloadLastProfile();
	}

	private void preloadLastProfile() {
		new Thread(new Runnable() {
			public void run() {
				try {
					//读取最近的存档
					String profileName = getConfigManager().get("lastprofile");
					if(profileName != null) {
						loadProfile(profileName);
						loadScene();
					}
				} catch (ProfileException e) {
					e.printStackTrace();
				}
			}
		}).start();		
	}

	/**
	 * 进入游戏
	 */
	public void enterScene() {
		GameCanvas canvas = window.getCanvas();
		if(canvas != sceneCanvas) {
			//canvas.stopMusic();
			loadScene();
			window.setCanvas(sceneCanvas);
			window.installUI();
			window.installListeners();
			sceneCanvas.playMusic();
			CacheManager.getInstance().removeAllDownloadListeners();
			CacheManager.getInstance().addDownloadListener(sceneCanvas);
		}
	}
	
	private void loadUIs(XmlDataLoader loader) {
		File dir = CacheManager.getInstance().getFile("ui");
		String[] files = dir.list(new SuffixFilenameFilter(".xml"));
		for (int i = 0; i < files.length; i++) {
			loader.loadUI("ui/"+files[i]);
		}
		window.prepareUI();
		
	}
	
	public void playMusic() {
		window.getCanvas().playMusic();
	}

	public void stopMusic() {
		window.getCanvas().stopMusic();
	}	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		DesktopApplication app = new DesktopApplication();
		app.startup();
	}

}
