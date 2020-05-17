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
	 * @param maskData 地图掩码数据(width*height)
	 */
	void init(int width, int height, byte[] maskData);

	/**
	 * 是否可以通过该点
	 */
	boolean pass(int x, int y);

	/**
	 * 搜索两点的路径
	 */
	List<Point> findPath(int x1, int y1, int x2, int y2);

}