/**
 * 
 */
package com.javaxyq.core;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.swing.JFrame;

import com.javaxyq.battle.BattleCanvas;
import com.javaxyq.data.XmlDataLoader;
import com.javaxyq.io.CacheManager;
import com.javaxyq.widget.Cursor;
import com.javaxyq.widget.Player;

/**
 * 回合制战斗播放器
 * 
 * @author gongdewei
 */
public class BattleApplication extends BaseApplication {

	private DesktopWindow window;
	private BattleCanvas canvas;
	/**
	 * 
	 */
	public BattleApplication() {
		// TODO Auto-generated constructor stub
	}
	
	@Override
	protected void loadResources() {
		promptMsg("loading resource ...");
		window.setGameCursor(Cursor.DEFAULT_CURSOR);
		
		promptMsg("loading actions ...");
		XmlDataLoader loader = new XmlDataLoader(window);
		loader.parseActions();
		promptMsg("loading ui ...");
		loadUIs(loader);
	}

	@Override
	protected void finish() {
		initCanvas();
		super.finish();
	}
	
	
	@Override
	protected GameWindow createWindow() {
		window = new DesktopWindow();
		window.init(context);
		canvas = new BattleCanvas(window.getContentWidth(),window.getContentHeight());
		window.setCanvas(canvas);
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		return window;
	}
	@Override
	public void quitBattle() {
		System.out.println("quitBattle");
	}

	private void initCanvas() {
		//canvas.setBattleBackground(bg);
		List<Player> team1 = new ArrayList<Player>();
		List<Player> team2 = new ArrayList<Player>();
		
		String[] elfs = {"2036","2037","2009","2010","2011","2012"};
		String[] elfNames = {"大海龟","巨蛙","芙蓉仙子","树怪","蝴蝶仙子","花妖"};
		Random random = new Random();
		int level = 10;
		int elfCount = random.nextInt(3)+3;
		for(int i=0;i<elfCount;i++) {		
			int elflevel = Math.max(0,level +random.nextInt(4)-2);
			int elfIndex = random.nextInt(elfs.length); 
			team1.add(getDataManager().createElf(elfs[elfIndex], elfNames[elfIndex],elflevel));
		}		
		elfCount = random.nextInt(3)+3;
		for(int i=0;i<elfCount;i++) {		
			int elflevel = Math.max(0,level +random.nextInt(4)-2);
			int elfIndex = random.nextInt(elfs.length); 
			team2.add(getDataManager().createElf(elfs[elfIndex], elfNames[elfIndex],elflevel));
		}		
		
		canvas.setOwnsideTeam(team1);
		canvas.setAdversaryTeam(team2);
		canvas.fadeIn(500);
		canvas.init();
		canvas.playMusic();
	}

	private void loadUIs(XmlDataLoader loader) {
		File file = CacheManager.getInstance().getFile("ui/list.txt");
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
			String str = null;
			while((str=br.readLine())!=null) {
				String uifile = "ui/"+str;
				System.out.println("find ui: "+uifile);
				loader.loadUI(uifile);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		new BattleApplication().startup();
	}

	public void playMusic() {
		canvas.playMusic();
	}

	public void stopMusic() {
		canvas.stopMusic();
	}

	@Override
	public void enterScene() {
		// TODO Auto-generated method stub
		
	}

}
