/**
 * 
 */
package com.javaxyq.core;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.javaxyq.action.DefaultTalkAction;
import com.javaxyq.config.MapConfig;
import com.javaxyq.data.SceneNpc;
import com.javaxyq.data.SceneTeleporter;
import com.javaxyq.event.PlayerAdapter;
import com.javaxyq.event.PlayerEvent;
import com.javaxyq.event.PlayerListener;
import com.javaxyq.event.SceneEvent;
import com.javaxyq.event.SceneListener;
import com.javaxyq.io.CacheManager;
import com.javaxyq.model.Task;
import com.javaxyq.resources.DefaultTileMapProvider;
import com.javaxyq.search.SearchUtils;
import com.javaxyq.task.TaskManager;
import com.javaxyq.trigger.JumpTrigger;
import com.javaxyq.trigger.Trigger;
import com.javaxyq.widget.Cursor;
import com.javaxyq.widget.Player;
import com.javaxyq.widget.Sprite;
import com.javaxyq.widget.SpriteImage;
import com.javaxyq.widget.TileMap;
import com.soulnew.AStar;
import lombok.extern.slf4j.Slf4j;

/**
 * @author dewitt
 * @history 2013-12-25 wpaul modify 
 * 
 */
@Slf4j
public class SceneCanvas extends Canvas {

	/** 游戏地图 */
	private TileMap map;

	private AStar searcher;

	private PlayerListener scenePlayerHandler = new ScenePlayerHandler();

	private Color trackColor = new Color(255, 0, 0, 200);

	private List<Trigger> triggerList;

	/** 当前场景名称 */
	private String sceneName;

	/** 当前场景id */
	private String sceneId;


	private int sceneWidth;

	private int sceneHeight;

	private List<Point> path;
	private List<Point> playerpath;

	private String musicfile;

	private ScriptEngine scriptEngine;
	
	/**
	 * 地图遮掩层及参数
	 */
	//private Image mapMask;
	//private int tempviewX;
	//private int tempviewY;
	public boolean isloop = true;
	public boolean maskupdateinit;
	
	/**
	 * 创建场景画布实例
	 */
	public SceneCanvas(int width, int height) {
		super(width, height);
		scriptEngine = DefaultScript.getInstance();
		searcher = new AStar();
		// searcher = new OptimizeAStar();
		// searcher = new Dijkstra();
		// searcher = new BreadthFirstSearcher();
		Thread th1 = new MovementThread();
		th1.start();
	}

	private void drawMap(Graphics g) {
		g.clearRect(0, 0, getWidth(), getHeight());
		if (map != null) {
			int viewX = getViewportX();
			int viewY = getViewportY();
			map.draw(g, viewX, viewY, getWidth(), getHeight());
		}
	}

	
	private void drawMask(Graphics g,long elapsedTime) {
		if (map != null ) {
			//获得Player的坐标
			Player player = getPlayer();
			if (player != null) {
				Point p = player.getLocation();
				p = localToView(p);
				
				int viewX = getViewportX();
				int viewY = getViewportY();
				int sx2 = viewX + getWidth();
				int sy2 = viewY + getHeight();
				try {
					long t1,t2;
					t1 = System.currentTimeMillis();
					map.drawMask(player, p.x+viewX,p.y+viewY, g, viewX, viewY, sx2, sy2);
					t2 = System.currentTimeMillis();
					if(t2-t1 > 5) {
						log.info("drawMask: "+(t2-t1));
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				
			}
	
		}
	}

	private void drawTrigger(Graphics g, long elapsedTime) {
		if (triggerList == null) {
			return;
		}
		for (int i = 0; i < triggerList.size(); i++) {
			JumpTrigger t = (JumpTrigger) triggerList.get(i);
			Sprite s = t.getSprite();
			s.update(elapsedTime);
			Point p = t.getLocation();
			p = sceneToView(p);
			s.draw(g, p.x, p.y - s.getHeight() / 2 + s.getRefPixelY());
			if(ApplicationHelper.getApplication().isDebug()) {
				g.drawLine(p.x-10, p.y, p.x+10, p.y);
				g.drawLine(p.x, p.y-10, p.x, p.y+10);
			}
		}
	}

	public TileMap getMap() {
		return map;
	}

	public Point getPlayerLocation() {
		return this.getPlayer().getLocation();
	}

	public Point getPlayerSceneLocation() {
		return this.getPlayer().getSceneLocation();
	}


	public Point localToScene(Point p) {
		return new Point(p.x / Application.STEP_DISTANCE, (map.getHeight() - p.y) / Application.STEP_DISTANCE);
	}

	/**
	 * 判断某点是否可以通行
	 * 
	 * @param x
	 * @param y
	 * @return
	 */
	public boolean pass(int x, int y) {
		return searcher.pass(x, y);
	}

	private void revisePlayer(Point p) {
		int canvasWidth = getWidth();
		int canvasHeight = getHeight();
		int viewX = getViewportX();
		int viewY = getViewportY();
		if (p.x > viewX + canvasWidth) {
			p.x = viewX + canvasWidth;
		}
		if (p.y > viewY + canvasHeight) {
			p.y = viewY + canvasHeight;
		}
		if (p.x < viewX) {
			p.x = viewX;
		}
		if (p.y < viewY) {
			p.y = viewY;
		}
	}

	public Point sceneToLocal(Point p) {
		return new Point(p.x * Application.STEP_DISTANCE, getMaxHeight() - p.y * Application.STEP_DISTANCE);
	}

	public Point sceneToView(Point p) {
		return this.localToView(this.sceneToLocal(p));
	}

	/**
	 * 搜索行走路径
	 * 
	 * @param x
	 * @param y
	 * @return
	 */
	public List<Point> findPath(int x, int y) {
		Point source = getPlayerSceneLocation();
		Point target = new Point(x, y);
		// 计算两点的直线
		List<Point> path = SearchUtils.getLinePath(source.x,source.y,target.x,target.y);
		// 计算两点间的二次曲线
		//path = SearchUtils.getBezierPath(source, target);
		// 修正终点为最近可以到达点
		for (int i = path.size() - 1; i >= 0; i--) {
			Point p = path.get(i);
			if (pass(p.x, p.y)) {
				target = p;
				break;
			}
			path.remove(i);
		}
		// 如果直线上全部点可通行，则返回
		boolean passed = true;
		for (int i = 0; i < path.size(); i++) {
			Point p = path.get(i);
			if (!pass(p.x, p.y)) {
				passed = false;
				break;
			}
		}
		if (passed)
			return path;
		// 否则计算两点间的路径
		path = searcher.findPath(source.x, source.y, target.x, target.y);
		return path;
	}

	public void setMap(TileMap map) {
		if (map == null) {
			return;
		}
		
		setMaxWidth(map.getWidth());
		setMaxHeight(map.getHeight());
		sceneWidth = map.getWidth() / Application.STEP_DISTANCE;
		sceneHeight = map.getHeight() / Application.STEP_DISTANCE;
		this.map = map;
		MapConfig cfg = map.getConfig();
		this.setSceneId(cfg.getId());
		this.setSceneName(cfg.getName());
		//场景跳转点
		this.triggerList = new ArrayList<Trigger>();
		Integer _sceneId = Integer.valueOf(sceneId);
		List<SceneTeleporter> teleporters = getDataManager().findTeleportersBySceneId(_sceneId);
		for (int i = 0; i < teleporters.size(); i++) {
			triggerList.add(new JumpTrigger(teleporters.get(i)));
		}
		//场景npc
		clearNPCs();
		List<SceneNpc> _npcs = getDataManager().findNpcsBySceneId(_sceneId);
		for (int i = 0; i < _npcs.size(); i++) {
			Player npc = getDataManager().createNPC(_npcs.get(i));
			Point p = sceneToLocal(npc.getSceneLocation());
			npc.setLocation(p.x, p.y);
			this.addNPC(npc);
		}
		
		// test! get barrier image
		//this.mapMask = new ImageIcon(cfg.getPath().replace(".map", "_bar.png")).getImage();
		//maskdata = loadMask(cfg.getPath().replace(".map", ".msk"));
		
		byte[] celldata = loadCellData();
		searcher.init(sceneWidth, sceneHeight, celldata);
		//play music
		String musicfile = cfg.getPath().replaceAll("\\.map", ".mp3").replaceAll("scene","music");
		try {
			File file = CacheManager.getInstance().getFile(musicfile);
			if(file!=null && file.exists() && !musicfile.equals(this.musicfile)) {
				this.musicfile = musicfile;
				//playMusic();
			}
		} catch (Exception e) {
			System.out.println("切换场景音乐失败："+musicfile+", error="+e.getMessage());
			//e.printStackTrace();
		}
	}

	/**
	 * 加载地图的阻挡信息
	 * 
	 * @param filename
	 * @return
	 */
	private byte[] loadCellData() {
		byte[][] mapCellData = map.getCellData();
		byte[] celldata = new byte[sceneWidth * sceneHeight];
		for(int ch=0;ch<sceneHeight;ch++){
			for(int cw=0;cw<sceneWidth;cw++){
				celldata[ch*sceneWidth+cw] = mapCellData[ch][cw];
			}
		}
		return celldata;
	}

	/** 默认人物对话事件 */
	private DefaultTalkAction defaultTalkAction = new DefaultTalkAction();

	/** 自动调节视窗 */
	private boolean adjustViewport;
	private int viewportAx = -5;
	private int viewportAy = -5;
	private int viewportVx = 10;
	private int viewportVy = 10;

	public void addNPC(Player npc) {
		super.addNPC(npc);
		npc.removeAllListeners();
		npc.addPlayerListener(defaultTalkAction);
	}

	@Override
	protected void clearNPCs() {
		List<Player> npcs = getNpcs();
        for(int i=0;i<npcs.size();i++){
        	Player npc = npcs.get(i);
			npc.removePlayerListener(defaultTalkAction);
		}
		super.clearNPCs();
	}

	public void setPlayer(Player player) {
		Player player0 = getPlayer();
		if (player0 != null) {
			player0.stop(false);
			player0.removePlayerListener(scenePlayerHandler);
		}

		player.stop(false);
		super.setPlayer(player);
		if (player != null) {
			player.addPlayerListener(scenePlayerHandler);
			setPlayerSceneLocation(player.getSceneLocation());
			player.setSearcher(searcher);
		}
	}

	public void setPlayer(Player player, int x, int y) {
		player.setSceneLocation(x, y);
		setPlayer(player);
	}

	public void setPlayerLocation(Point p) {
		this.revisePlayer(p);
		this.getPlayer().setLocation(p.x, p.y);
	}

	/**
	 * 将人物移到场景位置p，同时自动修正地图
	 * 
	 * @param p
	 */
	public void setPlayerSceneLocation(Point p) {
		if (this.map == null) {
			return;
		}
		Point vp = sceneToLocal(p);
		this.setViewPosition(vp.x - 320, vp.y - 240);
		this.setPlayerLocation(vp);
		this.revisePlayerSceneLocation();
	}
	/**
	 * 根据人物的屏幕坐标和地图的viewport位置计算人物在场景中的坐标
	 * 
	 * @param player
	 */
	public void revisePlayerSceneLocation() {
		Point p = getPlayer().getLocation();
		p = this.localToScene(p);
		getPlayer().setSceneLocation(p.x, p.y);
	}

	/**
	 * 移动视窗(viewport),跟随人物移动
	 * 
	 * @param increment
	 */
	private void syncSceneAndPlayer(Point increment) {
		synchronized (UPDATE_LOCK) {
			Point p = getPlayerLocation();
			p = this.localToView(p);
			
			//走出中心区才移动视窗
			int dx = increment.x;
			int dy = increment.y;
			int fx=0,fy=0;
			// System.out.printf("move: dx=%s,dy=%s\n", dx, dy);
			Point vp = getViewPosition();
			// 前进方向移动view
			if(p.x < 200) {
				fx = -1;
			}else if(p.x > 480) {
				fx = 1;
			}
			if(p.y < 150) {
				fy = -1;
			}else if(p.y > 330) {
				fy = 1;
			}
			//System.out.printf("player on view: (%s, %s) => m(%s, %s) \n", p.x, p.y, fx, fy);
			
			// 设置视窗(viewport)的位置
			if(fx!=0 || fy!=0) {
				//setViewPosition(vp.x+dx, vp.y+dy);
				// System.out.printf("view: (%s,%s)\n",vp.x,vp.y);
				adjustViewport = true;
				viewportVx = fx*160;
				viewportVy = fy*120;
				viewportAx = fx*-40;
				viewportAy = fy*-30;
				//System.out.printf("adjustView: vx=%s, vy=%s, ax=%s, ay=%s\n",viewportVx,viewportVy,viewportAx,viewportAy);
			}
		}
	}

	public Point viewToScene(Point p) {
		return localToScene(viewToLocal(p));
	}

	/**
	 * Auto Walk
	 * 
	 * @param x
	 * @param y
	 */
	public void walkTo(int x, int y) {
		if(x<=0 || y<=0 || x> sceneWidth || y>sceneHeight)return;
		Point p = this.getPlayerSceneLocation();
		System.out.printf("walk to:(%s,%s) -> (%s,%s)\n", p.x, p.y, x, y);
		this.path = this.findPath(x, y);
		if(this.path.isEmpty()) {
			System.out.println("no path");
			return;
		}
		
		System.out.print("path is: ");
		for (Point ap : path) {
		 System.out.printf("(%s,%s)\n", ap.x, ap.y);
		 }
		
		playerpath = new ArrayList<Point>();
		int start =0;
		playerpath.add((Point)path.get(start));
		for(int i=1;i<path.size();i++){
			List<Point> linepath = path.subList(start, i);
			int distance = searcher.distance(linepath);
			Point source = linepath.get(0);
			Point target = linepath.get(linepath.size()-1);
			//List<Point> fpath = SearchUtils.getLinePath(source.x,source.y,target.x,target.y);
			//int fdistance = searcher.linedistance(fpath);
			int temp = (target.x-source.x)*(target.x-source.x)+(target.y-source.y)*(target.y-source.y);
			int  fdistance = (int)Math.sqrt(temp*100);
			//System.out.println("distance is:"+distance);
			//System.out.println("fdistance is:"+fdistance);
			if(  fdistance<distance){
			    start =i-1;
			    playerpath.add((Point)path.get(start));
			}
		}
		playerpath.add((Point)path.get(path.size()-1));
		/*System.out.print("path is:");
		for (Point ap : playerpath) {
			 System.out.printf("(%s,%s)\n", ap.x, ap.y);
		}*/

		
		if (path != null) {
			getPlayer().setPath(playerpath);
			getPlayer().move();
		} else {
			window.getHelper().prompt("不能到达那里", 1000);
		}
		
		
		
		
	}

	public void walkToView(int x, int y) {
		Point p = this.viewToScene(new Point(x, y));
		this.walkTo(p.x, p.y);
	}

	private void drawPath(Graphics g) {
		Player player = getPlayer();
		if (player != null && path != null) {// XXX DEBUG
			// List<Point> path = player.getPath();
			Point p0 = null;
			for (Point p : path) {
				this.drawTrack(g, p0, p);
				p0 = p;
			}
			drawTrack(g, p0,null);
		}
	}

	private void drawTrack(Graphics g, Point p0, Point p) {
		g.setColor(trackColor);
		if (p0 == null) {//画起点
			Point vp = this.sceneToView(p);
			//g.fillOval(vp.x - 4, vp.y - 4, 14, 14);
			drawCell(g, vp);
		} else if(p==null){//画终点
			Point vp = this.sceneToView(p0);
			drawCell(g, vp);
//			g.fillOval(vp.x - 4, vp.y - 4, 12, 12);
//			g.drawOval(vp.x -6, vp.y -6, 16, 16);
//			g.drawOval(vp.x -7, vp.y -7, 18, 18);
		} else {
			p0 = sceneToView(p0);
			p = sceneToView(p);
			drawCell(g, p0);
//			g.drawLine(p0.x, p0.y, p.x, p.y);
//			g.drawLine(p0.x + 1, p0.y + 1, p.x + 1, p.y + 1);
		}
	}
	
	private void drawCell(Graphics g, Point p) {
		int px,py,w,h;
		px = p.x - Application.STEP_DISTANCE/2;
		py = p.y - Application.STEP_DISTANCE/2;
		w=h=Application.STEP_DISTANCE;
		g.drawRect(px, py, w, h);
	}
	
	
	private void drawfPath(Graphics g) {
		Player player = getPlayer();
		if (player != null && playerpath != null) {// XXX DEBUG
			// List<Point> path = player.getPath();
			Point p0 = null;
			for (Point p : playerpath) {
				this.drawfTrack(g, p0, p);
				p0 = p;
			}
			drawfTrack(g, p0,null);
		}
	}

	private void drawfTrack(Graphics g, Point p0, Point p) {
		g.setColor(Color.blue);
		if (p0 == null) {//画起点
			Point vp = this.sceneToView(p);
			//g.fillOval(vp.x - 4, vp.y - 4, 14, 14);
			drawCell(g, vp);
		} else if(p==null){//画终点
			Point vp = this.sceneToView(p0);
			drawCell(g, vp);
//			g.fillOval(vp.x - 4, vp.y - 4, 12, 12);
//			g.drawOval(vp.x -6, vp.y -6, 16, 16);
//			g.drawOval(vp.x -7, vp.y -7, 18, 18);
		} else {
			p0 = sceneToView(p0);
			p = sceneToView(p);
			drawfCell(g, p0);
//			g.drawLine(p0.x, p0.y, p.x, p.y);
//			g.drawLine(p0.x + 1, p0.y + 1, p.x + 1, p.y + 1);
		}
	}
	
	private void drawfCell(Graphics g, Point p) {
		int px,py,w,h;
		px = p.x - Application.STEP_DISTANCE/2;
		py = p.y - Application.STEP_DISTANCE/2;
		w=h=Application.STEP_DISTANCE;
		g.fillOval(px, py, w, h);
	}

	/**
	 * doclick the canvas
	 * 
	 * @param p
	 *            the click-point of view(canvas)
	 */
	public void click(Point p) {
		final SpriteImage effectSprite = this.getGameCursor().getEffect();
		effectSprite.setVisible(true);
		Point sp = this.viewToScene(p);
		Point vp = this.sceneToView(sp);
		this.getGameCursor().setClick(sp.x, sp.y);
		p.translate(-vp.x, -vp.y);
		this.getGameCursor().setOffset(p.x, p.y);
		if (effectSprite != null) {
			effectSprite.setRepeat(2);
			// 取消点击效果
			new Thread() {
				public void run() {
					while (effectSprite.isVisible()) {
						try {
							Thread.sleep(500);
						} catch (InterruptedException e) {
						}
						if (effectSprite.getSprite().getRepeat() == 0) {
							effectSprite.setVisible(false);
							break;
						}
					}
				};
			}.start();
		}
	}

	/**
	 * draw click effect
	 * 
	 * @param elapsedTime
	 */
	private void drawClick(Graphics g, long elapsedTime) {
		// update click effection
		Cursor gameCursor = getGameCursor();
		SpriteImage effectSprite = (gameCursor == null) ? null : gameCursor.getEffect();
		if (effectSprite != null) {
			effectSprite.update(elapsedTime);
			Point p = this.sceneToView(gameCursor.getClickPosition());
			effectSprite.draw(g, p.x + gameCursor.getOffsetX(), p.y + gameCursor.getOffsetY());
		}
	}

	private final class MovementThread extends Thread {
		private long lastTime;
		{
			this.setName("movementThread");
		}

		public void run() {
			while (true) {
				// System.out.println(this.getId()+" "+this.getName());
				synchronized (Canvas.MOVEMENT_LOCK) {
					long t1 = System.currentTimeMillis();
					long currTime = System.currentTimeMillis();
					if (lastTime == 0)
						lastTime = currTime;
					long elapsedTime = currTime - lastTime;
					// update movement
					updateMovements(elapsedTime);
					long t2 = System.currentTimeMillis();
					if (t2 - t1 > 20) {
						System.out.printf("update movement costs: %sms\n", t2 - t1);
					}
					lastTime = currTime;
				}
				try {
					Thread.sleep(40);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * 场景的人物事件监听器
	 */
	private final class ScenePlayerHandler extends PlayerAdapter {

		public void walk(PlayerEvent evt) {
			Point coords = evt.getCoords();
			walkTo(coords.x, coords.y);
		}

		public void move(Player player, Point increment) {
			// 1. 更新场景坐标
			revisePlayerSceneLocation();
			syncSceneAndPlayer(increment);

			// 2. 触发地图跳转
			Point p = getPlayerSceneLocation();
			for (int i = 0; triggerList!=null && i < triggerList.size(); i++) {
				Trigger t = triggerList.get(i);
				if (t.hit(p)) {
					t.doAction();
					return;
				}
			}

			// TODO 3. 师门巡逻任务
			TaskManager taskManager = ApplicationHelper.getApplication().getTaskManager();
			Task task = taskManager.getTaskOfType("school", "patrol");
			if (task != null && !task.isFinished() && sceneId.equals(task.get("sceneId"))) {
				long nowtime = System.currentTimeMillis();
				Long lastPatrolTime = (Long) Environment.get(Environment.LAST_PATROL_TIME);
				Long patrolInterval = (Long) Environment.get(Environment.PATROL_INTERVAL);
				if (lastPatrolTime!=null && nowtime - lastPatrolTime > patrolInterval) {
					// FIXME 改进巡逻触发战斗机率的判断
					Random rand = new Random();
					if (rand.nextInt(100) < 5) {
						taskManager.process(task);
					}
				}
			}
		}
		
		//TODO stop , adjusting viewport
	}
	
	/**
	 * @param elapsedTime
	 */
	private void updateMovements(long elapsedTime) {
		Player p = getPlayer();
		if (p != null) {
			p.updateMovement(elapsedTime);
		}
		//move view
		if(this.adjustViewport) {
			if(this.viewportVx != 0 || this.viewportVy != 0) {
				Point vp = getViewPosition();
				double t = elapsedTime*1.0/1000;
				int vx = (int) (viewportVx + viewportAx*t);
				int vy = (int) (viewportVy + viewportAy*t);
				int dx=0, dy=0;
				if(viewportVx * vx >= 0) {
					dx = (int) (viewportVx*t + viewportAx*t*t/2);
					viewportVx = vx;
				}else {
					viewportVx = 0;
				}
				if(viewportVy * vy >= 0) {
					dy = (int) (viewportVy*t + viewportAy*t*t/2);
					viewportVy = vy;
				}else {
					viewportVy = 0;
				}
				//System.out.printf("move view: v(%s,%s) \n",viewportVx, viewportVy);
				if(viewportVx == 0 && viewportVy == 0) {
					adjustViewport = false;
				}
				setViewPosition(vp.x+dx, vp.y+dy);
				
//				Point newvp = getViewPosition();
//				map.setArgtoMask(newvp.x, newvp.y);
			}else {
				this.adjustViewport = false;
			}
		}
	}

	public void draw(Graphics g, long elapsedTime) {
		if (g == null) {
			return;
		}
		long t0,t1,t2,t3,t4,t5,t6,t7,tx;
		t0 = System.currentTimeMillis();
		try {
			g.setColor(Color.BLACK);
			// 场景地图
			drawMap(g);
//			t1 = System.currentTimeMillis();
//			System.out.printf("drawMap cost: %s\n", (t1-t0));
			
			// 人物行走路线
			if (ApplicationHelper.getApplication().isDebug()) {
				this.drawPath(g);
				this.drawfPath(g);
			}
			// 场景跳转
			drawTrigger(g, elapsedTime);
//			t2 = System.currentTimeMillis();
//			System.out.printf("drawTrigger cost: %s\n", (t2-t1));

			// npcs
			drawNPC(g, elapsedTime);
//			t3 = System.currentTimeMillis();
//			System.out.printf("drawNPC cost: %s\n", (t3-t2));
			
			// 人物
			drawPlayer(g, elapsedTime);
//			t4 = System.currentTimeMillis();
//			System.out.printf("drawPlayer cost: %s\n", (t4-t3));

			// 地图掩码(mask)
			drawMask(g,elapsedTime);
//			t5 = System.currentTimeMillis();
//			System.out.printf("drawMask cost: %s\n", (t5-t4));
			
			// 鼠标点击效果
			this.drawClick(g, elapsedTime);
//			t6 = System.currentTimeMillis();
//			System.out.printf("drawClick cost: %s\n", (t6-t5));

			// 游戏UI控件
			drawComponents(g, elapsedTime);
//			t7 = System.currentTimeMillis();
//			System.out.printf("drawComponents cost: %s\n", (t7-t6));

			// 过渡效果：谈出淡入
			if (alpha > 0) {
				g.setColor(new Color(0, 0, 0, alpha));
				g.fillRect(0, 0, getWidth(), getHeight());
			}
			// 内存使用量
			drawDebug(g);
			drawDownloading(g);
			
			tx = System.currentTimeMillis();
			//System.out.printf("SceneCanvas draw cost: %s\n", (tx-t0));
			
		} catch (Exception e) {
			log.warn("更新Canvas时失败! ", e);
		}
	}

	public String getSceneName() {
		return sceneName;
	}

	public void setSceneName(String sceneName) {
		this.sceneName = sceneName;
	}

	public String getSceneId() {
		return sceneId;
	}

	public void setSceneId(String sceneId) {
		this.sceneId = sceneId;
	}
	
	public void changeScene(String id, int x, int y){
		getPlayer().stop(true);
		if(id == null || id=="null") {
			throw new IllegalArgumentException("跳转场景失败，sceneId不能为空！");
		}
		SceneListener action = null;
		try {
			String currentScene = this.sceneId;
			if(currentScene!=null) {
				action = findSceneAction(currentScene);
				if(action!=null)action.onUnload(new SceneEvent(currentScene,-1,-1));
			}
		}catch(Exception e) {e.printStackTrace();}
		log.info("切换场景："+id+" ("+x+","+y+")");
		try {
			action =  findSceneAction(id);
			if(action!=null)action.onInit(new SceneEvent(id,x,y));
		}catch(Exception e) {e.printStackTrace();};
	
		fadeToMap(id,x,y);
		//TODO Fire SceneChangedEvent
		ApplicationHelper.getApplication().getContext().setScene(id);
		try {
			if(action!=null)action.onLoad(new SceneEvent(id,x,y));
		}catch(Exception e) {e.printStackTrace();};
	}

	public List<Point> getPath() {
		return path;
	}

	public int getSceneWidth() {
		return sceneWidth;
	}

	public int getSceneHeight() {
		return sceneHeight;
	}

	protected String getMusic() {
		return musicfile;		
	}
	private void fadeToMap(String sceneId, int x, int y) {
		this.fadeToMap(sceneId, new Point(x, y));
	}

	private void fadeToMap(String sceneId, Point p) {
		// 渐隐
		this.fadeOut(400);
		this.isloop = false;
		// prepare map
		TileMap newmap = this.getMap(sceneId);
		Point vp = reviseViewport(newmap, p,this.getWidth(),this.getHeight());
		newmap.prepare(vp.x, vp.y, this.getWidth(), this.getHeight());
		newmap.loadMask(vp.x, vp.y);
		// 等待动画结束
		synchronized (Canvas.FADE_LOCK) {
			// 更换地图
			synchronized (Canvas.UPDATE_LOCK) {
				this.setMap(newmap);
				this.setPlayerSceneLocation(p);
				this.isloop = true;
				this.maskupdateinit = true;
			}
		}
		// 渐现
		this.fadeIn(300);
		this.sceneId = sceneId;
	}
	public TileMap getMap(String id) {
//		TileMap m = maps.get(id);
//		if (m == null) {
			DefaultTileMapProvider tileMapProvider = new DefaultTileMapProvider(getDataManager());
			TileMap m = tileMapProvider.getResource(id);
//			maps.put(id, m);
//		}
		m.setAlpha(1.0f);
		return m;
	}
	
	/**
	 * 
	 * 修正地图显示的区域
	 * 
	 * @param canvas
	 * @param map
	 * @param p
	 *            player's scene coordinate
	 * @param canvasWidth 
	 * @param canvasHeight 
	 */
	private Point reviseViewport(TileMap map, Point p, int canvasWidth, int canvasHeight) {
		Point vp = new Point(p.x * Application.STEP_DISTANCE, map.getHeight() - p.y * Application.STEP_DISTANCE);
		Point viewPosition = new Point(vp.x - 320, vp.y - 240);

		int mapWidth = map.getWidth();
		int mapHeight = map.getHeight();
		if (viewPosition.x + canvasWidth > mapWidth) {
			viewPosition.x = mapWidth - canvasWidth;
		} else if (viewPosition.x < 0) {
			viewPosition.x = 0;
		}
		if (viewPosition.y + canvasHeight > mapHeight) {
			viewPosition.y = mapHeight - canvasHeight;
		} else if (viewPosition.y < 0) {
			viewPosition.y = 0;
		}
		return viewPosition;
	}
	
	private SceneListener findSceneAction(String id) {
		Object action = scriptEngine.loadSceneScript(id);
		return (SceneListener)action;
	}	
}
