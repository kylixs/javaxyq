/*
 * JavaXYQ Source Code
 * by kylixs
 * at 2009-11-25
 * please visit http://javaxyq.googlecode.com
 * or mail to kylixs@qq.com
 */
package com.javaxyq.search;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.geom.Line2D;
import java.util.ArrayList;
import java.util.List;

/**
 * @author dewitt
 * @date 2009-11-25 create
 */
public class SearchUtils {

	/**
	 * 计算两点间的直线路径
	 * 
	 * @param x1
	 * @param y1
	 * @param x2
	 * @param y2
	 * @return
	 */
	public static List<Point> getLinePath(int x1, int y1, int x2, int y2) {
		List<Point> path = new ArrayList<Point>();
		int x, y;
		int dx, dy;
		int incx, incy;
		int balance;
		int i = 0;
		if (x2 >= x1) {
			dx = x2 - x1;
			incx = 1;
		} else {
			dx = x1 - x2;
			incx = -1;
		}
		if (y2 >= y1) {
			dy = y2 - y1;
			incy = 1;
		} else {
			dy = y1 - y2;
			incy = -1;
		}
		x = x1;
		y = y1;
		if (dx >= dy) {
			dy <<= 1;
			balance = dy - dx;
			dx <<= 1;
			while (x != x2) {
				path.add(new Point(x, y));
				if (balance >= 0) {
					y += incy;
					balance -= dx;
				}
				balance += dy;
				x += incx;
				i++;
			}
			path.add(new Point(x, y));
		} else {
			dx <<= 1;
			balance = dx - dy;
			dy <<= 1;
			while (y != y2) {
				path.add(new Point(x, y));
				if (balance >= 0) {
					x += incx;
					balance -= dy;
				}
				balance += dx;
				y += incy;
				i++;
			}
			path.add(new Point(x, y));
		}
		return path;
	}

	/**
	 * 获取两点间的二次曲线
	 * 
	 * @param path
	 * @param p
	 * @return
	 */
	public static List<Point> getBezierPath(Point source, Point target) {
		Point[] vertexs = new Point[4];
		// 设置二次曲线的控制点
		int dx = (target.x - source.x) / 3;
		int dy = (target.y - source.y) / 3;
		vertexs[0] = source;
		vertexs[1] = new Point(source.x + dx, target.y - dy);
		vertexs[2] = new Point(target.x - dx , source.y + dy);
		vertexs[3] = target;
		List<Point> path = new ArrayList<Point>();
		getBezierPath(path, vertexs);
		return path;
	}

	private static List<Point> getBezierPath(List<Point> path, Point[] p) {
		Point[][] result = new Point[2][4];
		Point[] q = new Point[4];
		Point[] r = new Point[4];
		if (Math.max(distance(p[1], p[0], p[3]), distance(p[2], p[0], p[3])) <= 1.0d)
			// g.drawLine(p[0].x, p[0].y, p[3].x, p[3].y);
			path.addAll(getLinePath(p[0].x, p[0].y, p[3].x, p[3].y));
		else {
			result = curve_split(p);
			for (int i = 0; i < 4; i++) {
				q[i] = new Point(result[0][i].x, result[0][i].y);
				r[i] = new Point(result[1][i].x, result[1][i].y);
			}
			getBezierPath(path, q);
			getBezierPath(path, r);
		}
		return path;
	}

	/**
	 * 
	 * Bresenham Line Algorithm 　　
	 * 
	 * @author Turbo Chen 　　
	 * @version 1.0 copyright 2001 　　
	 * @email turbochen@163.com 　　
	 * @param dashedMask
	 *            设置线型的虚线的间隔，为0则画实线。 　　
	 * @param lineWidth
	 *            设置线宽。 　　
	 * @param x1
	 *            　　
	 * @param y1
	 * @param x2
	 *            　　
	 * @param y2
	 *            　　
	 */
	public static void bresenhamLine(java.awt.Graphics g, int dashedMask, int lineWidth, int x1, int y1, int x2, int y2) {
		int x, y;
		int dx, dy;
		int incx, incy;
		int balance;
		int i = 0;
		if (x2 >= x1) {
			dx = x2 - x1;
			incx = 1;
		} else {
			dx = x1 - x2;
			incx = -1;
		}
		if (y2 >= y1) {
			dy = y2 - y1;
			incy = 1;
		} else {
			dy = y1 - y2;
			incy = -1;
		}
		x = x1;
		y = y1;
		if (dx >= dy) {
			dy <<= 1;
			balance = dy - dx;
			dx <<= 1;
			while (x != x2) {
				if ((i & dashedMask) == 0)
					g.fillOval(x, y, lineWidth, lineWidth);
				if (balance >= 0) {
					y += incy;
					balance -= dx;
				}
				balance += dy;
				x += incx;
				i++;
			}
			if ((i & dashedMask) == 0)
				g.fillOval(x, y, lineWidth, lineWidth);
		} else {
			dx <<= 1;
			balance = dx - dy;
			dy <<= 1;
			while (y != y2) {
				if ((i & dashedMask) == 0)
					g.fillOval(x, y, lineWidth, lineWidth);
				if (balance >= 0) {
					x += incx;
					balance -= dy;
				}
				balance += dx;
				y += incy;
				i++;
			}
			if ((i & dashedMask) == 0)
				g.fillOval(x, y, lineWidth, lineWidth);
		}
	}

	void draw_Bezier(Graphics g, Point[] p) {
		Point[][] result = new Point[2][4];
		Point[] q = new Point[4];
		Point[] r = new Point[4];
		if (Math.max(distance(p[1], p[0], p[3]), distance(p[2], p[0], p[3])) <= 1.0d)
			g.drawLine(p[0].x, p[0].y, p[3].x, p[3].y);
		else {
			result = curve_split(p);
			for (int i = 0; i < 4; i++) {
				q[i] = new Point(result[0][i].x, result[0][i].y);
				r[i] = new Point(result[1][i].x, result[1][i].y);
			}
			draw_Bezier(g, q);
			draw_Bezier(g, r);
		}
	}

	static Point[][] curve_split(Point[] p) {
		Point[][] result = new Point[2][4];
		Point[] q = new Point[4];
		Point[] r = new Point[4];
		System.arraycopy(p, 0, q, 0, p.length);
		for (int i = 1; i < 4; i++) {
			r[4 - i] = new Point(q[3].x, q[3].y);
			for (int j = 3; j >= i; j--) {
				q[j].x = (q[j].x + q[j - 1].x) >> 1;
				q[j].y = (q[j].y + q[j - 1].y) >> 1;
			}

		}
		r[0] = new Point(q[3].x, q[3].y);
		for (int i = 0; i < 4; i++) {
			result[0][i] = new Point(q[i].x, q[i].y);
			result[1][i] = new Point(r[i].x, r[i].y);
		}
		return result;
	}

	static double distance(Point p1, Point p2, Point p3) {
		double d;
		Line2D.Float line = new Line2D.Float(p2.x, p2.y, p3.x, p3.y);
		d = line.ptLineDist(p1);
		return d;
	}

}
