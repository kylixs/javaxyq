/**
 * 
 */
package com.javaxyq.widget.impl;

import java.awt.Graphics;
import java.awt.Point;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.javaxyq.core.SpriteFactory;
import com.javaxyq.widget.Character;
import com.javaxyq.widget.CharacterActions;
import com.javaxyq.widget.Directions;
import com.javaxyq.widget.Sprite;

/**
 * @author gongdewei
 * @date 2011-7-24 create
 */
public class CharacterImpl implements Character {

	private static final Logger log = Logger.getLogger(CharacterImpl.class.getName());
	private Object LocationLock = new Object();
	private Object UpdateLock = new Object();
	
	private String id;
	
	private Sprite sprite;
	
	private int direction;
	
	private int x;
	private int y;
	/** 是否在移动  */
	private boolean moving;
	/** 移动速度  */
	private int speed;
	/** 角色动作  */
	private String action = CharacterActions.STAND;
	/** 是否继续移动 */
	private boolean moveon;
	
	private LinkedList<Point> footmark;
	private LinkedList<Point> track;
	private long elapsedTime;
	private int lastFrameIndex;
	
	public CharacterImpl(String id) {
		this.id = id;
		footmark = new LinkedList<Point>();
		track = new LinkedList<Point>();
	}
	
	@Override
	public void initialize() {
		//checkCharacter(id);
		refresh();
	}
	
	synchronized public void refresh() {
		String resId = id+"-"+action;
		if(sprite == null || !sprite.getResId().equals(resId)) {
			sprite = SpriteFactory.getSprite(id, action);
		}
		if(sprite != null && sprite.getDirection() != direction) {
			sprite.setDirection(direction);
			sprite.resetFrames();
		}
	}
	
	@Override
	public String getId() {
		return id;
	}
	
	@Override
	public boolean isReady() {
		return (sprite!=null);
	}

	@Override
	public Point getLocation() {
		synchronized (LocationLock) {
			return new Point(x, y);
		}
	}

	@Override
	public void moveBy(int x, int y) {
		synchronized (LocationLock) {
			this.x += x;
			this.y += y;
			saveTrack();
		}
	}

	@Override
	public void moveTo(int x, int y) {
		synchronized (LocationLock) {
			this.x = x;
			this.y = y;
			saveFootmark();
		}
	}
	
	@Override
	synchronized public void action(String key) {
		// checkAction(key);
		this.action = key;
		this.moving = false;
		this.speed = 0;
		refresh();
	}

	@Override
	synchronized public void rush() {
		this.action = CharacterActions.RUSHA;
		this.moving = true;
		this.speed = 2;
		refresh();
		saveFootmark();
	}

	@Override
	synchronized public void stand() {
		this.action = CharacterActions.STAND;
		this.moving = false;
		this.speed = 0;
		refresh();
		saveFootmark();
	}

	@Override
	synchronized public void turn(int direction) {
		if(direction > 7) {direction %= 8; }
		if(direction <0 ) {direction += 8; }
		this.direction = direction;
		refresh();
	}
	
	@Override
	public void turn() {
		if(this.direction < 4) {
			turn(this.direction + 4);
		}else if(this.direction == 7){
			turn(0);
		}else {
			turn(this.direction - 3);
		}
	}
	
	public int getDirection() {
		return direction;
	}

	@Override
	synchronized public void walk() {
		this.action = CharacterActions.WALK;
		this.moving = true;
		this.speed = 1;
		refresh();
		saveFootmark();
	}

	@Override
	synchronized public void draw(Graphics g) {
		if(isReady()) {
			this.sprite.draw(g, x, y);
		}
	}

	@Override
	synchronized public void update(long elapsedTime) {
		updateAnimation(elapsedTime);
		updateMovement(elapsedTime);
	}

	private void updateMovement(long elapsedTime) {
		checkMovement();
		if(moving && speed>0) {
			int dx=0, dy=0;
			double unit = 2;
			double q = 0.7;
			switch (this.direction) {
			case Directions.LEFT:
				dx = - (int) (unit * this.speed);
				break;
			case Directions.UP_LEFT:
				dx = - (int) (unit * this.speed * q);
				dy = (int) (unit * this.speed * q);
				break;
			case Directions.UP:
				dy = (int) (unit * this.speed);
				break;
			case Directions.UP_RIGHT:
				dx = (int) (unit * this.speed * q);
				dy = (int) (unit * this.speed * q);
				break;
			case Directions.RIGHT:
				dx = (int) (unit * this.speed);
				break;
			case Directions.DOWN_RIGHT:
				dx = (int) (unit * this.speed * q);
				dy = - (int) (unit * this.speed * q);
				break;
			case Directions.DOWN:
				dy = - (int) (unit * this.speed * q);
				break;
			case Directions.DOWN_LEFT:
				dx = - (int) (unit * this.speed * q);
				dy = - (int) (unit * this.speed * q);
				break;
			default:
				break;
			}
			this.moveBy(dx, -dy);
		}
	}

	private void checkMovement() {
		Point last = getLastFootmark();
		double distance = last.distance(getLocation());
		if(distance >= 20) {
			moveArrived();
		}
	}

	private void moveArrived() {
		saveFootmark();
		if(!isMoveOn()) {
			moving = false;
			stand();
		}
	}

	public boolean isMoveOn() {
		return moveon;
	}
	
	public void setMoveon(boolean moveon) {
		this.moveon = moveon;
	}

	private void updateAnimation(long elapsedTime) {
		if(isReady()) {
			this.sprite.update(elapsedTime);
			if(moving && log.isLoggable(Level.INFO)) {
				int findex = this.sprite.getCurrAnimation().getIndex();
				if(lastFrameIndex != findex) {
					//log.info("frame: "+this.direction+ " - "+findex);
				}
				lastFrameIndex = findex;
			}
		}
	}

	private void saveTrack() {
		Point last = track.size()>0? track.getLast(): null;
		Point p = new Point(x, y);
		if(last==null || !last.equals(p)) {
			track.add(new Point(x, y));
			//log.info("Track: ("+x+","+y+")");
		}
	}
	
	private void saveFootmark() {
		Point last = getLastFootmark();
		Point p = new Point(x, y);
		if(last==null || !last.equals(p)) {
			track.add(p);
			footmark.add(p);
			log.info("Footmark: ("+x+","+y+")");
		}
	}
	
	private Point getLastFootmark() {
		return footmark.size()>0? footmark.getLast(): null;
	}
}
