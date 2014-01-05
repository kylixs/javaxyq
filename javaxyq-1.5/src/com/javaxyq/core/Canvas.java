package com.javaxyq.core;

import java.awt.AWTKeyStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.KeyboardFocusManager;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.swing.JComponent;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.KeyStroke;

import com.javaxyq.event.DownloadEvent;
import com.javaxyq.event.DownloadListener;
import com.javaxyq.ui.UIHelper;
import com.javaxyq.util.MP3Player;
import com.javaxyq.util.UIUtils;
import com.javaxyq.widget.Animation;
import com.javaxyq.widget.Cursor;
import com.javaxyq.widget.Player;

//DONE 添加动画的播放次数，如使用技能动画（只播放１次），鼠标点击地面效果

public class Canvas extends JPanel implements GameCanvas, DownloadListener{

	private static final long serialVersionUID = -4190257004358562807L;
	private class AnimatorThread extends Thread {
		/** 动画总时间 */
		private long duration;

		private boolean increased;

		/**
		 * 动画刷新的间隔
		 */
		private long interval;

		private long passTime;

		/**
		 * @param duration
		 * @param interval
		 * @param increased
		 *            true是增加alpha,false 则是减少alpha
		 */
		public AnimatorThread(long duration, long interval, boolean increased) {
			this.duration = duration;
			this.interval = interval;
			this.increased = increased;
			setName("animator");
		}

		public void run() {
			synchronized (FADE_LOCK) {
				while (passTime < duration) {
					// System.out.println(this.getId()+" "+this.getName());
					synchronized (UPDATE_LOCK) {
						passTime += interval;
						alpha = (int) (255.0 * passTime / duration);
						if (!increased) {
							alpha = 255 - alpha;
						}
						if (alpha < 0) {
							alpha = 0;
						}
						if (alpha > 255) {
							alpha = 255;
						}
					}
					try {
						Thread.sleep(interval);
					} catch (InterruptedException e) {
					}
				}
				Canvas.this.fading = false;
				// System.out.println("faded");
			}
		}
	}

	private int drawCount = 0;

	protected long updateInterval = 20;
	
	private final class DrawThread extends Thread {
		{
			this.setName("DrawThread");
			this.setDaemon(true);
		}

		public void run() {
			while (canvasValid) {
				// System.out.println(this.getId()+" "+this.getName());
				synchronized (Canvas.UPDATE_LOCK) {
					if (isShowing() && isVisible()) {
						drawCanvas();
						drawCount ++;
					}
					try {
						UPDATE_LOCK.wait(updateInterval );
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}

	public static final Object FADE_LOCK = new Object();

	public static final Object UPDATE_LOCK = new Object();

	public static final Object MOVEMENT_LOCK = new Object();
	private boolean canvasValid = true;

	protected int alpha = 0;

	private Thread drawThread = new DrawThread();

	private boolean fading;

	private Cursor gameCursor;// 鼠标指针

	private long lastTime;

	/**
	 * 鼠标跟随物
	 */
	private Animation movingObject = null;

	private Point movingOffset = new Point();

	private List<Player> npcs;

	private Graphics2D offscreenGraphics;

	private BufferedImage offscreenImage;
	private Player player;// 角色

	private int maxWidth;
	private int maxHeight;

	private int viewportY;

	private int viewportX;
	
	public Canvas(int width,int height) {
		setIgnoreRepaint(true);
		setFocusable(true);
		requestFocus(true);
		setLayout(null);
		setSize(width, height);
		setPreferredSize(new Dimension(width,height));
		
		offscreenImage = new BufferedImage(width, height, BufferedImage.TYPE_USHORT_565_RGB);
		offscreenGraphics = (Graphics2D) offscreenImage.getGraphics();
		setBackground(Color.BLACK);
		setForeground(Color.WHITE);

		// 禁止tab键切换焦点
		Set<AWTKeyStroke> keystrokes = new HashSet<AWTKeyStroke>(
				getFocusTraversalKeys(KeyboardFocusManager.FORWARD_TRAVERSAL_KEYS));
		keystrokes.remove(KeyStroke.getKeyStroke(KeyEvent.VK_TAB, 0));
		setFocusTraversalKeys(KeyboardFocusManager.FORWARD_TRAVERSAL_KEYS, keystrokes);

		npcs = new ArrayList<Player>();
		drawThread.start();
	}

	protected DataManager getDataManager() {
		return ApplicationHelper.getApplication().getDataManager();
	}

	
	public synchronized void addNPC(Player npc) {
		npcs.add(npc);
	}

	/**
	 * 在(x,y)增加一个NPC
	 * 
	 * @param npc
	 * @param x
	 * @param y
	 */
	public void addNPC(Player npc, int x, int y) {
		npc.setSceneLocation(x, y);
		this.addNPC(npc);
	}
	
	public void removeNPC(Player npc) {
		npcs.remove(npc);
	}

	protected void clearNPCs() {
		this.npcs.clear();

	}

	/**
	 * 绘制游戏画面
	 * @param g
	 * @param elapsedTime
	 */
	public synchronized void draw(Graphics g, long elapsedTime) {
		if (g == null) {
			return;
		}
		try {
			g.setColor(Color.BLACK);
			// npcs
			drawNPC(g, elapsedTime);
			// update comps on the canvas
			drawComponents(g, elapsedTime);
			// draw fade
			drawFade(g);
			drawDebug(g);
			drawDownloading(g);
		} catch (Exception e) {
			System.out.printf("更新Canvas时失败！\n");
			e.printStackTrace();
		}
	}

	private void drawFade(Graphics g) {
		if(alpha > 0) {
			g.setColor(new Color(0, 0, 0, alpha));
			g.fillRect(0, 0, getWidth(), getHeight());
		}
	}

	private synchronized void drawCanvas() {
		//System.out.println("start drawCanvas: "+new java.util.Date());
		Graphics g = this.getGraphics();
		long currTime = System.currentTimeMillis();
		if (lastTime == 0)
			lastTime = currTime;
		long elapsedTime = currTime - lastTime;
		lastTime = currTime;
		if (g != null && offscreenGraphics!=null) {
			this.draw(offscreenGraphics, elapsedTime);
			// draw to real graphics
			g.drawImage(offscreenImage, 0, 0, null);
			g.dispose();
		}
		//System.out.println("drawCanvas cost: "+(System.currentTimeMillis()-currTime));
	}

	protected void drawComponents(Graphics g, long elapsedTime) {
		Component[] comps = getComponents();
		for (int i = comps.length - 1; i >= 0; i--) {// reverse the z-order
			Component c = comps[i];
			Graphics g2 = g.create(c.getX(), c.getY(), c.getWidth(), c.getHeight());
			c.paint(g2);
			g2.dispose();
//			g.setClip(c.getX(), c.getY(), c.getWidth(), c.getHeight());
//			g.translate(c.getX(), c.getY());
//			c.paint(g);
//			g.translate(-c.getX(), -c.getY());
		}
//		g.setClip(0, 0, getWidth(), getHeight());
		drawTooltips(g);
		// update cursor
		drawCursor(g, elapsedTime);

	}

	protected void drawCursor(Graphics g, long elapsedTime) {
		Point p = getWindow().getMousePosition();
		if (movingObject != null && p != null) {
			movingObject.update(elapsedTime);
			movingObject.draw(g, p.x + movingOffset.x, p.y + movingOffset.y);
		}
		if (gameCursor != null) {
			if (p != null) {
				gameCursor.setLocation(p.x, p.y);
			}
			gameCursor.update(elapsedTime);
			gameCursor.draw(g);
		}
	}

	protected void drawNPC(Graphics g, long elapsedTime) {
		for (Player npc : npcs) {
			npc.setHover(isHover(npc));
			npc.update(elapsedTime);
			Point p = npc.getLocation();
			// p = localToView(p);
			p.translate(-viewportX, -viewportY);
			npc.draw(g, p.x, p.y);
		}
	}

	protected void drawPlayer(Graphics g, long elapsedTime) {
		Player player = getPlayer();
		if (player != null) {
			player.setHover(isHover(player));
			// long s1 = System.currentTimeMillis();
			player.update(elapsedTime);
			Point p = player.getLocation();
			p = localToView(p);
			player.draw(g, p.x, p.y);
			// long s2 = System.currentTimeMillis();
			// if(s2-s1>0) {
			// System.out.printf("update player uses: %sms\n",s2-s1);
			// }
		}
	}

	private void drawTooltips(Graphics g) {
		Component[] comps = getWindow().getLayeredPane().getComponentsInLayer(JLayeredPane.POPUP_LAYER);
		for (Component comp : comps) {
			if(comp.isShowing()) {
				Graphics g1 = g.create(comp.getX(), comp.getY(), comp.getWidth(), comp.getHeight());
				try {
					comp.paint(g1);
				} catch (Exception e) {
					//e.printStackTrace();
				}
				g1.dispose();
			}
		}
	}

	public void fadeIn(long t) {
		this.fading = true;
		long duration = t;
		long interval = t / 10;
		AnimatorThread thread = new AnimatorThread(duration, interval, false);
		thread.start();
	}

	public void fadeOut(long t) {
		this.fading = true;
		long duration = t;
		long interval = t / 10;
		AnimatorThread thread = new AnimatorThread(duration, interval, true);
		thread.start();
	}

	private Component findCompByName(Component parent, String name) {
		if (!(parent instanceof Container)) {
			return null;
		}
		Container container = (Container) parent;
		Component[] comps = container.getComponents();
		for (Component c : comps) {
			if (name.equals(c.getName())) {
				return c;
			}
		}
		for (Component c : comps) {
			Component result = this.findCompByName(c, name);
			if (result != null) {
				return result;
			}
		}
		return null;
	}

	protected Component findCompByName(String name) {
		return this.findCompByName(this, name);
	}

	/**
	 * find npc by name
	 * 
	 * @param name
	 * @return
	 */
	public Player findNpcByName(String name) {
		for (Player p : this.npcs) {
			if (p.getName().equals(name)) {
				return p;
			}
		}
		return null;
	}

	public Cursor getGameCursor() {
		return gameCursor;
	}

	public List<Player> getNpcs() {
		return npcs;
	}

	public Graphics getOffscreenGraphics() {
		return offscreenGraphics;
	}

	public void removeMovingObject() {
		this.movingObject = null;
	}

	public void setGameCursor(String type) {
		Cursor cursor = UIHelper.getCursor(type);
		if(cursor!=null) {
			this.gameCursor = cursor;
		}
	}

	public void setMovingObject(Animation anim, Point offset) {
		this.movingObject = anim;
		this.movingOffset = offset;
	}

	public void waitForFading() {
		while (this.fading) {
			try {
				Thread.sleep(40);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	public void setViewPosition(int x, int y) {
		this.viewportX = x;
		this.viewportY = y;
		reviseViewport();
	}

	private void reviseViewport() {
		int canvasWidth = getWidth();
		int canvasHeight = getHeight();
		// int mapWidth = map.getWidth();
		// int mapHeight = map.getHeight();
		if (viewportX + canvasWidth > maxWidth) {
			viewportX = maxWidth - canvasWidth;
		} else if (viewportX < 0) {
			viewportX = 0;
		}
		if (viewportY + canvasHeight > maxHeight) {
			viewportY = maxHeight - canvasHeight;
		} else if (viewportY < 0) {
			viewportY = 0;
		}
	}

	protected int getMaxWidth() {
		return maxWidth;
	}

	protected void setMaxWidth(int maxWidth) {
		this.maxWidth = maxWidth;
	}

	protected int getMaxHeight() {
		return maxHeight;
	}

	protected void setMaxHeight(int maxHeight) {
		this.maxHeight = maxHeight;
	}

	protected int getViewportY() {
		return viewportY;
	}

	protected int getViewportX() {
		return viewportX;
	}

	public Point getViewPosition() {
		return new Point(viewportX, viewportY);
	}

	public Player getPlayer() {
		return player;
	}

	protected void setPlayer(Player player) {
		this.player = player;
	}

	/**
	 * 
	 */
	public void dispose() {
		canvasValid = false;
	}

	private long lastFPSTime;
	private int lastDrawCount;
	private double fps;

	private DownloadEvent downloadEvt;

	private long downloadUpdate;
	private long downloadMsgDelay = 1000;

	protected GameWindow window;
	protected void drawDebug(Graphics g) {
		if (g != null) {
			g.setFont(UIUtils.TEXT_FONT);
			double mb = 1024 * 1024;
			double maxMem = Runtime.getRuntime().maxMemory() / mb;
			double freeMem = Runtime.getRuntime().freeMemory() / mb;
			int x = 100, y = 20;
			g.setColor(Color.GREEN);
			g.drawString(String.format("内存：%.2f/%.2f MB", freeMem, maxMem), x, y);
			long nowtime = System.currentTimeMillis();
			if(lastFPSTime !=0 ) {
				if(nowtime >= lastFPSTime+1000) {
					fps = (drawCount-lastDrawCount)*1000.0/(nowtime-lastFPSTime);
					lastFPSTime = nowtime;
					lastDrawCount = drawCount; 
				}
			}else {
				lastFPSTime = nowtime;
				lastDrawCount = drawCount; 
			}
			g.drawString(String.format("FPS: %.2f",fps), x+200, y);
		}
	}
	
	protected void drawDownloading(Graphics g) {
		if(this.downloadEvt!=null && System.currentTimeMillis()-downloadUpdate<downloadMsgDelay) {
			String msg = "";
			String resourceName = this.downloadEvt.getResource();
			int size = this.downloadEvt.getSize();
			int received = this.downloadEvt.getReceived();
			//if(size==0)size = -1;//保证除数不为0
			//int percent = this.downloadEvt.getReceived()*100/size;
			switch (this.downloadEvt.getId()) {
			case DownloadEvent.DOWNLOAD_UPDATE:
				msg = String.format("正在下载  %s， 共%.2fKB，已下载%.2fKB ...",resourceName,size/1024.0, received/1024.0);
				break;
			case DownloadEvent.DOWNLOAD_STARTED:
				msg = String.format("开始下载 %s ...", resourceName);
				break;
			case DownloadEvent.DOWNLOAD_COMPLETED:
				//percent = 100;
				msg = String.format("下载完毕 %s .", resourceName);
				break;
			case DownloadEvent.DOWNLOAD_INTERRUPTED:
				msg = String.format("下载失败 %s .", resourceName);				
				break;

			default:
				break;
			}
//			int rw = 400;
//			int rh = 30;
//			int rx = (getWidth()-rw)/2;
//			int ry = (getHeight()-rh)/2;			
			//提示信息
			int x = 100, y = 40;
			g.setColor(Color.GREEN);
			g.drawString(msg ,x , y);
			//FontMetrics fm = g.getFontMetrics();
			//g.drawString(msg ,rx +(rw-fm.stringWidth(msg))/2, ry-10);
			//外框
			//g.setColor(Color.DARK_GRAY);
			//g.drawRect(rx, ry, rw, rh);
			//进度
			//g.setColor(Color.GREEN);
			//g.fillRect(rx+2, ry+2, (int) ((rw-4)*percent/100.0), rh-4);
			
		}
	}

	protected String getMusic() {
		return null;
	}

	public void playMusic() {
		if(GameMain.isPlayingMusic()) {
			final String filename = getMusic();
			if (filename != null) {
				Thread th = new Thread(new Runnable() {
					@Override
					public void run() {
						MP3Player.loop(filename);
					}
				});
				th.start();
			}
		}
	}

	public void stopMusic() {
		MP3Player.stopLoop();
	}

	@Override
	public void downloadCompleted(DownloadEvent e) {
		this.downloadEvt = e;
		this.downloadUpdate = System.currentTimeMillis();
	}

	@Override
	public void downloadInterrupted(DownloadEvent e) {
		this.downloadEvt = e;
		this.downloadUpdate = System.currentTimeMillis();
	}

	@Override
	public void downloadStarted(DownloadEvent e) {
		this.downloadEvt = e;
		this.downloadUpdate = System.currentTimeMillis();
	}

	@Override
	public void downloadUpdate(DownloadEvent e) {
		this.downloadEvt = e;
		this.downloadUpdate = System.currentTimeMillis();
	}

	@Override
	public JComponent getComponent() {
		return this;
	}

	@Override
	public GameWindow getWindow() {
		return window;
	}

	@Override
	public void setWindow(GameWindow window) {
		this.window = window;
	}
	@Override
	public boolean isHover(Player player) {
		Point p = getMousePosition();
		if (p == null) {
			return false;
		}
		Point vp = localToView(player.getLocation());
		boolean hover = player.contains(p.x - vp.x, p.y - vp.y);
		return hover;
	}

	public Point localToView(Point p) {
		return new Point(p.x - viewportX, p.y - viewportY);
	}

	public Point viewToLocal(Point p) {
		return new Point(p.x + getViewportX(), p.y + getViewportY());
	}
	@Override
	public Point localToScene(Point p) {
		return null;
	}

	@Override
	public Point sceneToLocal(Point p) {
		return null;
	}

	@Override
	public Point sceneToView(Point p) {
		return null;
	}

	@Override
	public Point viewToScene(Point p) {
		return null;
	}

	@Override
	public void setGameCursor(Cursor cursor) {
		this.gameCursor = cursor;
	}


	
}
