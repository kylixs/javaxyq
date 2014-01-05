/**
 * 
 */
package com.javaxyq.widget;

import java.awt.Graphics;
import java.awt.Point;

/**
 * 角色
 * @author gongdewei
 * @date 2011-7-24 create
 */
public interface Character {

	String getId();
	
	boolean isReady();
	
	void initialize();
	
	/**
	 * 更新动画
	 * @param elapsedTime
	 */
	void update(long elapsedTime);
	
	/**
	 * 绘制到画布上
	 * @param g
	 * @param x
	 * @param y
	 */
	void draw(Graphics g);
	
	/**
	 * UI绘制坐标位置
	 * @return
	 */
	Point getLocation();
	
	/**
	 * 将角色移动到指定坐标
	 * @param x
	 * @param y
	 */
	void moveTo(int x, int y);
	
	/**
	 * 移动增量
	 * @param x
	 * @param y
	 */
	void moveBy(int x, int y);
	
	/**
	 * 行走
	 */
	void walk();
	
	/**
	 * 奔跑
	 */
	void rush();
	
	/**
	 * 站立
	 */
	void stand();
	
	/**
	 * 转向
	 * @param direction
	 */
	void turn(int direction);

	void turn();

	int getDirection();
	
	/**
	 * 设置人物动作
	 * @param key
	 */
	void action(String key);

	/**
	 * 是否继续移动
	 * @return
	 */
	boolean isMoveOn();

	/**
	 * 设置是否连续移动
	 * @param moveon
	 */
	void setMoveon(boolean moveon);

}
