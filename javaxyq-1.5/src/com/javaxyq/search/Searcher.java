/*
 * JavaXYQ Source Code
 * by kylixs
 * at 2009-11-25
 * please visit http://javaxyq.googlecode.com
 * or mail to kylixs@qq.com
 */
package com.javaxyq.search;

import java.awt.Point;
import java.util.List;

import com.soulnew.AStarNode;


/**
 * @author dewitt
 * @date 2009-11-25 create
 */
public interface Searcher {

	/**
	 * 初始化结点
	 * 
	 * @param maskdata
	 *            地图掩码数据(width*height)
	 */
	void init(int width, int height, byte[] maskdata);

	/**
	 * 是否可以通过该点
	 * 
	 * @return
	 */
	boolean pass(int x, int y);

	/**
	 * 搜索两点的路径
	 * @param x1
	 * @param y1
	 * @param x2
	 * @param y2
	 * @return
	 */
	List<Point> findPath(int x1, int y1, int x2, int y2);

}