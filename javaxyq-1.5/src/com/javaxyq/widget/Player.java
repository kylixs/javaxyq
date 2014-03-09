package com.javaxyq.widget;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.EventObject;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import javax.swing.event.EventListenerList;

import com.javaxyq.core.Application;
import com.javaxyq.core.ApplicationHelper;
import com.javaxyq.core.SpriteFactory;
import com.javaxyq.data.CharacterUtils;
import com.javaxyq.data.WeaponItem;
import com.javaxyq.event.EventDispatcher;
import com.javaxyq.event.EventException;
import com.javaxyq.event.EventTarget;
import com.javaxyq.event.PlayerEvent;
import com.javaxyq.event.PlayerListener;
import com.javaxyq.model.PlayerVO;
import com.javaxyq.search.Searcher;
import com.javaxyq.ui.FloatPanel;
import com.javaxyq.util.Cheat;
import com.javaxyq.util.MP3Player;
import com.javaxyq.util.UIUtils;
import com.javaxyq.util.WASDecoder;

/**
 * 游戏人物
 * 
 * @author 龚德伟
 * @history 2008-5-14 龚德伟 完善人物键盘行走的处理
 *          2013-12-31 wpaul modify
 */
public class Player extends AbstractWidget implements EventTarget {

	private static final long serialVersionUID = 4030203990139411828L;

	public static final String STATE_STAND = "stand";

	public static final String STATE_WALK = "walk";
	
	private Object MOVE_LOCK = new Object();
	private Object UPDATE_LOCK = new Object();

	private String id;

	/** 角色的类型(如逍遥生/侠客/车夫) */
	private String character;

	/** 历史冒泡对话 */
	private List<FloatPanel> chatPanels;

	private Sprite person;

	private Sprite weapon;

	private Sprite shadow;

	/** 路径队列 */
	private Queue<Point> path = new ConcurrentLinkedQueue<Point>();

	private String name;

	private String state = STATE_STAND;

	private int x;

	private int y;

	private int direction;

	private Color nameForeground = UIUtils.COLOR_NAME;

	private Color nameBackground = UIUtils.COLOR_NAME_BACKGROUND;

	private Color highlightColor = UIUtils.COLOR_NAME_HIGHLIGHT;

	private List<String> chatHistory = new ArrayList<String>();

	private Font nameFont;

	private boolean visible = true;

	private boolean moving = false;

	private boolean stepping = false;

	private EventListenerList listenerList = new EventListenerList();

	/** 场景X坐标 */
	private int sceneX;

	/** 场景Y坐标 */
	private int sceneY;

	/** 当前的移动量[x,y] */
	private Point nextStep;

	/** 继续当前方向移动 */
	private boolean movingOn = false;

	/** 染色方案 */
	private String profile;

	private int[] profileData;

	private int[] colorations = null;// new int[3];

	/**
	 * 人物数据
	 */
	private PlayerVO data;

	/**
	 * 状态效果动画
	 */
	private Map<String, Animation> stateEffects = new HashMap<String, Animation>();

	/**
	 * 单次效果动画
	 */
	private Animation onceEffect = null;

	private boolean directionMoving;

	private boolean isHover;
	
	private Searcher searcher;

	private int delay;

	private WeaponItem weaponItem;

	public Player(String id, String name, String character) {
		this.id = id;
		this.name = name;
		this.character = character;
		shadow = SpriteFactory.loadShadow();
		nameFont = UIUtils.TEXT_NAME_FONT;
		chatPanels = new ArrayList<FloatPanel>();
	}

	public String getId() {
		return id;
	}

	public int getHeight() {
		return this.person.getHeight();
	}

	public int getWidth() {
		return this.person.getWidth();
	}

	public boolean isHover() {
		return isHover;
	}

	public void setHover(boolean isHover) {
		this.isHover = isHover;
	}

	/**
	 * 取出下一步的移动量
	 * 
	 * @return
	 */
	private Point popPath() {
		// System.out.println("path count:" + path.size());
		if (this.path != null && !this.path.isEmpty()) {
			Point step = this.path.poll();
			while (step != null && step.x == this.sceneX && step.y == this.sceneY) {
				step = this.path.poll();
			}
			return step;
		}
		return null;
	}

	/**
	 * 根据路径的步进量计算出移动方向
	 * 
	 * @param step
	 * @return
	 */
	private int calculateStepDirection(Point step) {
		System.out.println("dx,dy is:"+step.x+","+step.y);
		System.out.println("scenex,y is:"+sceneX+","+sceneY);
		
		int dx = step.x - this.sceneX;
		int dy = step.y - this.sceneY;
		double r = Math.sqrt(dx*dx+dy*dy);
	
		double cos=dx/r;
		int angle=(int) Math.floor(Math.acos(cos)*180/Math.PI);
		if(dy>0){
		    angle=360-angle;
		}

		int dir = 0;
		System.out.println("angle is:"+angle);
        if(angle>337 || angle<=22){
        	dir = Sprite.DIR_RIGHT;
		}else if(angle>22 && angle<=67){
			dir = Sprite.DIR_DOWN_RIGHT;
		}else if(angle>67 && angle<=112){
			dir = Sprite.DIR_DOWN;
		}else if(angle>112 && angle<=157){
			dir = Sprite.DIR_DOWN_LEFT;
		}else if(angle>157 && angle<=202){
			dir = Sprite.DIR_LEFT;
		}else if(angle>202 && angle<=247){
			dir = Sprite.DIR_UP_LEFT;
		}else if(angle>247 && angle<=292){
			dir = Sprite.DIR_UP;
		}else if(angle>292 && angle<=337){
			dir = Sprite.DIR_UP_RIGHT;
		}
		
		
		/*if (dx < 0) {
			if (dy < 0) {
				dir = Sprite.DIR_DOWN_LEFT;
			} else if (dy > 0) {
				dir = Sprite.DIR_UP_LEFT;
			} else {
				dir = Sprite.DIR_LEFT;
			}
		} else if (dx > 0) {
			if (dy < 0) {
				dir = Sprite.DIR_DOWN_RIGHT;
			} else if (dy > 0) {
				dir = Sprite.DIR_UP_RIGHT;
			} else {
				dir = Sprite.DIR_RIGHT;
			}
		} else {// x=0
			if (dy < 0) {
				dir = Sprite.DIR_DOWN;
			} else if (dy > 0) {
				dir = Sprite.DIR_UP;
			} else {
				// no move
				dir = -1;
			}
		}*/

		return dir;
	}

	public void changeDirection(Point mouse) {
		// FIXME 人物转向
		int direction = computeDirection(getLocation(), mouse);
		setDirection(direction);
	}

	public boolean contains(int x, int y) {
		boolean b = person.contains(x, y) || shadow.contains(x, y);
		if (weapon != null && !b) {
			b = weapon.contains(x, y);
		}
		return b;
	}

	public Point getLocation() {
		return new Point(x, y);
	}

	public String getName() {
		return name;
	}

	/*synchronized*/ public void say(String chatText) {
		if(Cheat.process(chatText)) {
			return ;
		}
		this.chatHistory.add(chatText);
		this.chatPanels.add(new FloatPanel(chatText));
		if (this.chatPanels.size() > 3) {
			this.chatPanels.remove(0);
		}
		System.out.println(name + " 说: " + chatText);
	}

	public List<String> getChatHistory() {
		return chatHistory;
	}

	public void move() {
		// TODO
		synchronized(MOVE_LOCK) {
			this.prepareStep();
		}
	}

	/**
	 * 向某方向移动一步
	 * 
	 * @param direction
	 */
	public void stepTo(int direction) {
		this.clearPath();
		int dx = 0;
		int dy = 0;
		switch (direction) {
		case Sprite.DIR_LEFT:
			dx = -1;
			break;
		case Sprite.DIR_UP:
			dy = 1;
			break;
		case Sprite.DIR_RIGHT:
			dx = 1;
			break;
		case Sprite.DIR_DOWN:
			dy = -1;
			break;
		case Sprite.DIR_DOWN_LEFT:
			dx = -1;
			dy = -1;
			break;
		case Sprite.DIR_UP_LEFT:
			dx = -1;
			dy = 1;
			break;
		case Sprite.DIR_UP_RIGHT:
			dx = -1;
			dy = 1;
			break;
		case Sprite.DIR_DOWN_RIGHT:
			dx = 1;
			dy = -1;
			break;
		default:
			break;
		}
		Point next = new Point(this.sceneX + dx, this.sceneY + dy);
		this.addStep(next);
		// System.out.printf("step to:%s, (%s,%s)\n", direction, next.x,
		// next.y);
		this.prepareStep();
	}

	public void moveBy(int dx, int dy) {
		this.x += dx;
		this.y += dy;
	}

	public void setDirection(int direction) {
		if (this.direction != direction) {
			this.direction = direction;
			System.out.printf("[debug]player.direction=%s\n", this.direction);
			//delay = 5;
			if(person!=null) {
				person.setDirection(direction);
				person.resetFrames();
			}
			if (weapon != null) {
				weapon.setDirection(direction);
				weapon.resetFrames();
			}
		} else {
			amendDirection();
		}
	}

	public void amendDirection() {
		if(person != null) {			
			person.setDirection(direction);
			int index = person.getCurrAnimation().getIndex();
			if (weapon != null) {
				weapon.setDirection(direction);
				weapon.getCurrAnimation().setIndex(index);
			}
		}
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setState(String state) {
		if(state == null) {
			state = STATE_STAND;
		}
		if (this.state != state) {
			System.out.println("[debug] setState: "+state);
			this.state = state;
			this.person = createPerson(state);
			this.weapon = createWeapon(state);
			this.person.setDirection(this.direction);
			this.person.resetFrames();
			if (this.weapon != null) {
				this.weapon.setDirection(this.direction);
				this.weapon.resetFrames();
			}
		}
	}

	private Sprite createPerson(String state) {
		int charNo = Integer.parseInt(this.character);
		if(weaponItem != null){
			if(!CharacterUtils.isFirstWeapon(this.character, weaponItem.getType())) {
				if(!CharacterUtils.isNormalState(state)) {
					charNo +=12;
				}
			}
		}		
		String filename = String.format("/shape/char/%04d/%s.tcp", charNo, state);
		Sprite sprite = SpriteFactory.loadSprite(filename);
		return sprite;
	}

	private Sprite createWeapon(String state) {
		if(weaponItem != null){
			int charNo = Integer.parseInt(this.character);
			int resNo = Integer.parseInt(weaponItem.getResNo());
			if(!CharacterUtils.isFirstWeapon(this.character, weaponItem.getType())) {
				if(CharacterUtils.isNormalState(state)) {
					resNo +=50;
				}else {
					charNo +=12;
				}
			}
			String filename = String.format("/shape/char/%04d/%02d/%s.tcp", charNo, resNo, state);
			Sprite sprite = SpriteFactory.loadSprite(filename);
	        return sprite;
		}
		return null;
	}

	public String getState() {
		return state;
	}

	public void stop(boolean force) {
		synchronized(MOVE_LOCK) {			
			if (force) {
				stopAction();
			} else {
				this.movingOn = false;
			}
			this.directionMoving = false;
			// this.setState(STATE_STAND);
			// System.out.println("stop");
		}
	}

	private void stopAction() {
		synchronized(MOVE_LOCK) {			
			this.moving = false;
			this.movingOn = false;
			this.setState(STATE_STAND);
			// System.out.println("stop action!");
		}
	}

	public void update(long elapsedTime) {
		shadow.update(elapsedTime);
		person.update(elapsedTime);
		if (weapon != null) {
			weapon.update(elapsedTime);
		}
		// effect
		Collection<Animation> stateEffs = stateEffects.values();
		for (Animation effect : stateEffs) {
			effect.update(elapsedTime);
		}
		if (this.onceEffect != null) {
			this.onceEffect.update(elapsedTime);
			if (this.onceEffect.getRepeat() == 0) {
				// 播放完毕，移除动画
				this.onceEffect = null;
			}
		}
	}

	public /*synchronized*/ void updateMovement(long elapsedTime) {
		// 根据状态改变player的sprite（character可能改变）
		this.setState(this.isMoving() ? STATE_WALK: this.state);
		if (this.isMoving()) {
			// 如果移动完成,则发送STEP_OVER消息
			if (this.isStepOver()) {
				fireEvent(new PlayerEvent(this, PlayerEvent.STEP_OVER));
				prepareStep();
			} else {// 计算移动量
				if(this.delay > 0) {
					this.delay --;
					return;
				}
				Point d = this.calculateIncrement(elapsedTime);
				if (d.x != 0 || d.y != 0) {
					x += d.x;
					y += d.y;
					PlayerEvent evt = new PlayerEvent(this, PlayerEvent.MOVE);
					evt.setAttribute(PlayerEvent.MOVE_INCREMENT, d);
					fireEvent(evt);
//					 System.out.printf("pos:(%s,%s)\tmove->:(%s,%s)\n", x, y,
//					 d.x, d.y);
				}
			}
		}

	}

	private Point calculateIncrement(long elapsedTime) {
		int dx = 0, dy = 0;
		// 如果该坐标可以到达移动
		if (searcher.pass(this.nextStep.x, this.nextStep.y)) {
			// 计算起点与目标点的弧度角
			double radian = Math.atan(1.0 * (nextStep.y - sceneY) / (nextStep.x - sceneX));
			// 计算移动量
			int distance = (int) (Application.NORMAL_SPEED * elapsedTime);
			dx = (int) (distance * Math.cos(radian));
			dy = (int) (distance * Math.sin(radian));
			// 修正移动方向
			if (nextStep.x > sceneX) {
				dx = Math.abs(dx);
			} else {
				dx = -Math.abs(dx);
			}
			if (nextStep.y > sceneY) {
				dy = -Math.abs(dy);
			} else {
				dy = Math.abs(dy);
			}
		} else if (this.directionMoving) {// 遇到障碍物时，按住方向键移动
			// TODO 修正移动的方向
			// switch (this.direction) {
			// case Sprite.DIRECTION_BOTTOM:
			//				
			// break;
			//
			// default:
			// break;
			// }
		} else if (!this.directionMoving) {// 遇到障碍物时，松开方向键(没有继续移动)
			stopAction();
		}
		return new Point(dx, dy);
	}

	/**
	 * 是否完成一步的移动<br>
	 * 如果水平或者垂直方向移动大于等于步长,则认为移动完成
	 * 
	 * @return
	 */
	private boolean isStepOver() {
		return getSceneLocation().equals(nextStep);
	}

	/**
	 * 处理事件
	 * 
	 * @param event
	 */
	public void handleEvent(PlayerEvent event) {
		final PlayerListener[] listeners = listenerList.getListeners(PlayerListener.class);
		switch (event.getId()) {
		case PlayerEvent.STEP_OVER:
			for (PlayerListener listener : listeners) {
				listener.stepOver(this);
			}
			break;
		case PlayerEvent.WALK:
			for (PlayerListener listener : listeners) {
				listener.walk(event);
			}
			break;
		case PlayerEvent.MOVE:
			for (PlayerListener listener : listeners) {
				listener.move(this, (Point) event.getAttribute(PlayerEvent.MOVE_INCREMENT));
			}
			break;
		case PlayerEvent.CLICK:
			for (PlayerListener listener : listeners) {
				listener.click(event);
			}
			break;
		case PlayerEvent.TALK:
			for (PlayerListener listener : listeners) {
				listener.talk(event);
			}
			break;
		default:
			break;
		}
	}

	public void removePlayerListener(PlayerListener l) {
		listenerList.remove(PlayerListener.class, l);
	}

	/**
	 * 是否到达目的点
	 * 
	 * @return
	 */
	public boolean isArrived() {
		return false;
	}

	public Color getNameBackground() {
		return nameBackground;
	}

	public void setNameBackground(Color textBackground) {
		this.nameBackground = textBackground;
	}

	public Color getNameForeground() {
		return nameForeground;
	}

	public void setNameForeground(Color textForeground) {
		this.nameForeground = textForeground;
	}

	public void setLocation(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public boolean isMoving() {
		return moving;
	}

	public boolean isDirectionMoving() {
		return directionMoving;
	}

	public void setDirectionMoving(boolean directionMoving) {
		this.directionMoving = directionMoving;
	}

	public void addPlayerListener(PlayerListener l) {
		listenerList.add(PlayerListener.class, l);
	}

	public void clearPath() {
		this.path.clear();
	}

	public void addStep(Point p) {
		this.path.add(p);
	}

	public void setPath(Collection<Point> path) {
		this.path.clear();
		this.path.addAll(path);
		if (path == null || path.isEmpty()) {
			System.out.println("path is empty.");
		} else {
			// System.out.println("new path:");
			 //for (Point p : path) {
			 //System.out.printf("(%s,%s)\n", p.x, p.y);
			 //}
			// System.out.println();
		}
	}

	public /*synchronized*/ void draw(Graphics g, int x, int y) {
		shadow.draw(g, x, y);
		person.draw(g, x, y);
		if (weapon != null)
			weapon.draw(g, x, y);
		// draw name
		g.setFont(nameFont);
		int textY = y + 30;
		int textX = x - g.getFontMetrics().stringWidth(name) / 2;
		Graphics2D g2d = (Graphics2D) g.create();
		g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		g2d.setColor(nameBackground);
		g2d.drawString(name, textX + 1, textY + 1);
		g2d.setColor(isHover ? highlightColor : nameForeground);
		g2d.drawString(name, textX, textY);
		g2d.dispose();

		// 人物冒泡对话内容
		int chatY = y - person.getRefPixelY() - 10;
		for (int i = chatPanels.size() - 1; i >= 0; i--) {
			FloatPanel chatPanel = chatPanels.get(i);
			if (shouldDisplay(chatPanel)) {
				int chatX = x - chatPanel.getWidth() / 2;
				chatY -= chatPanel.getHeight() + 2;
				Graphics g2 = g.create(chatX, chatY, chatPanel.getWidth(), chatPanel.getHeight());
				chatPanel.paint(g2);
				g2.dispose();
			} else {
				chatPanels.remove(i);
			}
		}
		// effect
		Collection<Animation> stateEffs = stateEffects.values();
		for (Animation effect : stateEffs) {
			effect.draw(g, x, y);
		}
		if (this.onceEffect != null)
			onceEffect.draw(g, x, y);
		if(ApplicationHelper.getApplication().isDebug()) {
			g.drawLine(x-10, y, x+10, y);
			g.drawLine(x, y-10, x, y+10);
		}
	}

	private boolean shouldDisplay(FloatPanel chatPanel) {
		return System.currentTimeMillis() - chatPanel.getCreateTime() < Application.CHAT_REMAIND_TIME;
	}

	@Override
	public void dispose() {
		// TODO Player: dispose

	}

	public Sprite getWeapon() {
		return weapon;
	}

	public void setWeapon(Sprite weapon) {
		this.weapon = weapon;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public boolean isVisible() {
		return visible;
	}

	public void setVisible(boolean visible) {
		this.visible = visible;
	}

	public int getDirection() {
		return direction;
	}

	public boolean isStepping() {
		return stepping;
	}

	/**
	 * 准备下一步
	 */
	private void prepareStep() {
		synchronized(MOVE_LOCK) {
			this.nextStep = this.popPath();
			// 路径已经为空,停止移动
			if (this.nextStep == null) {
				if (this.movingOn) {
					this.stepTo(direction);
				} else {
					this.stopAction();
				}
			}
			this.stepAction();
		}
	}

	private void stepAction() {
		if (this.nextStep != null) {
			this.moving = true;
			// 计算下一步的方向
			int dir = calculateStepDirection(this.nextStep);
			if (dir != -1) {
				/*switch(this.direction) {
					case Sprite.DIR_DOWN_RIGHT:
						if(dir == Sprite.DIR_RIGHT || dir == Sprite.DIR_DOWN) {
							return;
						}
						break;
					case Sprite.DIR_DOWN_LEFT:
						if(dir == Sprite.DIR_LEFT || dir == Sprite.DIR_DOWN) {
							return;
						}
						break;
					case Sprite.DIR_UP_LEFT:
						if(dir == Sprite.DIR_LEFT || dir == Sprite.DIR_UP) {
							return;
						}
						break;
					case Sprite.DIR_UP_RIGHT:
						if(dir == Sprite.DIR_RIGHT || dir == Sprite.DIR_UP) {
							return;
						}
						break;
					case Sprite.DIR_DOWN:
						if(dir == Sprite.DIR_DOWN_LEFT || dir == Sprite.DIR_DOWN_RIGHT) {
							return;
						}
						break;
					case Sprite.DIR_LEFT:
						if(dir == Sprite.DIR_DOWN_LEFT || dir == Sprite.DIR_UP_LEFT) {
							return;
						}
						break;
					case Sprite.DIR_UP:
						if(dir == Sprite.DIR_UP_RIGHT || dir == Sprite.DIR_UP_LEFT) {
							return;
						}
						break;
					case Sprite.DIR_RIGHT:
						if(dir == Sprite.DIR_DOWN_RIGHT || dir == Sprite.DIR_UP_RIGHT) {
							return;
						}
						break;
				}*/
				setDirection(dir);
			}
		}
	}

	public int getSceneX() {
		return sceneX;
	}

	public int getSceneY() {
		return sceneY;
	}

	public Point getSceneLocation() {
		return new Point(sceneX, sceneY);
	}

	public void setSceneLocation(int x, int y) {
		this.sceneX = x;
		this.sceneY = y;
	}

	public void setSceneLocation(Point p) {
		setSceneLocation(p.x, p.y);
	}

	public void moveOn() {
		this.movingOn = true;
	}

	@Override
	public String toString() {
		return "[name=" + this.name + ",x=" + this.x + ",y=" + this.y + ",sceenX=" + this.sceneX + ",sceneY="
				+ this.sceneY + "]";
	}

	public List<Point> getPath() {
		Point[] paths = new Point[path.size()];
		path.toArray(paths);
		return Arrays.asList(paths);
	}

	public String getCharacter() {
		return character;
	}

	public boolean handleEvent(EventObject evt) throws EventException {
		if (evt instanceof PlayerEvent) {
			PlayerEvent playerEvt = (PlayerEvent) evt;
			handleEvent(playerEvt);
		}
		return false;
	}

	public void fireEvent(PlayerEvent e) {
		EventDispatcher.getInstance().dispatchEvent(e);
	}

	public Sprite getPerson() {
		return person;
	}

	public Sprite getShadow() {
		return shadow;
	}

	public int[] getColorations() {
		return colorations;
	}

	public void setColorations(int[] colorations, boolean recreate) {
		this.colorations = colorations;
		if (recreate) {
			this.coloring(colorations);
		}
	}

	public void coloring(int[] colorations) {
		// 更新改变颜色后的精灵
		this.person = createPerson(state);
		this.weapon = createWeapon(state);
		this.person.setDirection(this.direction);
		this.person.resetFrames();
		if (this.weapon != null) {
			this.weapon.setDirection(this.direction);
			this.weapon.resetFrames();
		}
	}

	public String getProfile() {
		return profile;
	}

	public void setProfile(String profile) {
		this.profile = profile;
	}

	public int getColorationCount(int part) {
		if (this.profileData == null) {
			// 解析着色方案
			WASDecoder decoder = new WASDecoder();
			decoder.loadColorationProfile("shape/char/" + this.character + "/00.pp");
			int partCount = decoder.getSectionCount();
			this.profileData = new int[partCount];
			for (int i = 0; i < partCount; i++) {
				this.profileData[i] = decoder.getSchemeCount(i);
			}
		}
		return this.profileData[part];
	}

	/**
	 * 单次播放效果动画
	 * 
	 * @param name
	 * @param sound TODO
	 */
	public void playEffect(String name, boolean sound) {
		Animation s = SpriteFactory.loadAnimation("/magic/" + name + ".tcp");
		s.setRepeat(1);
		this.onceEffect = s;
		if(sound) {
			try {
				MP3Player.play("sound/magic/" + name + ".mp3");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 添加状态效果
	 * 
	 * @param name
	 */
	public void addStateEffect(String name) {
		Animation s = SpriteFactory.loadAnimation("/magic/" + name + ".tcp");
		this.stateEffects.put(name, s);
	}

	/**
	 * 取消状态效果
	 * 
	 * @param name
	 */
	public void removeStateEffect(String name) {
		this.stateEffects.remove(name);
	}

	/**
	 * 播放某个动作的动画
	 * 
	 * @param state
	 */
	public void playOnce(String state) {
		this.setState(state);
		this.person.setRepeat(1);
		if (this.weapon != null) {
			this.weapon.setRepeat(1);
		}
		MP3Player.play("sound/char/" + this.character + "/" + state + ".mp3");
	}

	/**
	 * 等待当前动作结束
	 */
	public void waitFor() {
		this.person.getCurrAnimation().waitFor();
	}

	/**
	 * 等待效果动画结束
	 * 
	 * @param name
	 */
	public void waitForEffect(String name) {
		if (this.onceEffect != null)
			this.onceEffect.waitFor();
	}

	public int getTop() {
		return y - this.person.getRefPixelY();
	}

	public int getLeft() {
		return x - this.person.getRefPixelX();
	}

	public PlayerVO getData() {
		data.direction = this.direction;
		data.state = this.state;
		data.colorations = this.colorations;
		data.sceneLocation = this.getSceneLocation();
		return data;
	}

	public void setData(PlayerVO data) {
		this.data = data;
		this.setDirection(data.direction);
		//加载着色精灵
		this.setColorations(data.colorations, true);
		this.setState(data.state);
		this.setSceneLocation(data.getSceneLocation());
	}

	/**
	 * 清楚全部监听器
	 */
	public void removeAllListeners() {
		PlayerListener[] listeners = listenerList.getListeners(PlayerListener.class);
		for (int i = 0; i < listeners.length; i++) {
			this.removePlayerListener(listeners[i]);
		}
	}
	
	@Override
	public void setAlpha(float alpha) {
		super.setAlpha(alpha);
		shadow.setAlpha(alpha);
		person.setAlpha(alpha);
		if(this.weapon!=null) {
			this.weapon.setAlpha(alpha);
		}
	}

	@Override
	protected void doDraw(Graphics2D g2, int x, int y, int width, int height) {
	}

	public Searcher getSearcher() {
		return searcher;
	}

	public void setSearcher(Searcher searcher) {
		this.searcher = searcher;
	}

    /**
     * 计算目标点相对中心点的角度
     * 
     * @param src
     * @param mouse
     * @return 8个方向之一
     */
    public static int computeDirection(Point src, Point mouse) {
        double dy, dx, k;
        int direction = Sprite.DIR_DOWN_RIGHT;
        dy = mouse.y - src.y;
        dx = mouse.x - src.x;
        if (dx == 0) {
            return (dy >= 0) ? Sprite.DIR_DOWN : Sprite.DIR_UP;
        } else if (dy == 0) {
            return (dx >= 0) ? Sprite.DIR_RIGHT : Sprite.DIR_LEFT;
        }
        k = Math.abs(dy / dx);
        if (k >= k2) {
            if (dy > 0)
                direction = Sprite.DIR_DOWN;
            else
                direction = Sprite.DIR_UP;
        } else if (k <= k1) {
            if (dx > 0)
                direction = Sprite.DIR_RIGHT;
            else
                direction = Sprite.DIR_LEFT;
        } else if (dy > 0) {
            if (dx > 0)
                direction = Sprite.DIR_DOWN_RIGHT;
            else
                direction = Sprite.DIR_DOWN_LEFT;
        } else {
            if (dx > 0)
                direction = Sprite.DIR_UP_RIGHT;
            else
                direction = Sprite.DIR_UP_LEFT;
        }
        return direction;
    }
    private static double k1 = Math.tan(Math.PI / 8);

    private static double k2 = 3 * k1;

	public int getRefPixelX() {
		return person.getRefPixelX();
	}
	public int getRefPixelY() {
		return person.getRefPixelY();
	}

	/**
	 * 拿起武器
	 * @param item
	 */
	public void takeupWeapon(WeaponItem item) {
		if(item == null){
			takeoffWeapon();
			return;
		}
		this.weaponItem = item;
		this.weapon = createWeapon(state);
		if (this.weapon != null) {
			this.weapon.setDirection(this.direction);
			this.weapon.resetFrames();
			this.person.resetFrames();
		}

	}
	
	/**
	 * 放下武器
	 */
	public void takeoffWeapon() {
		this.weaponItem = null;
		this.weapon = null;
	}
}
