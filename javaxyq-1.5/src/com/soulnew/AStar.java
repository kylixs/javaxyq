/*
 * AStar.java
 *
 * Created on April 1, 2007, 9:48 PM
 *
 
 */

package com.soulnew;

/**
 *@author soulnew@soulnew.com
 */
import java.awt.Point;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import com.javaxyq.search.Searcher;


public class AStar implements Searcher{

	private int width;
	private int height;
	/** Creates a new instance of AStar */
	public AStar() {
	}

	public static class PriorityList extends LinkedList {

		public void add(Comparable object) {
			for (int i = 0; i < size(); i++) {
				if (object.compareTo(get(i)) <= 0) {
					add(i, object);
					return;
				}
			}
			addLast(object);
		}
	}

	/**
	 * Construct the path, not including the start node.
	 */
	protected List<AStarNode> constructPath(AStarNode node) {
		LinkedList<AStarNode> path = new LinkedList<AStarNode>();
		while (node.pathParent != null) {
			path.addFirst(node);
			node = node.pathParent;
		}
		return path;
	}

	/**
	 * 计算两点间的路径
	 * @param startNode
	 * @param goalNode
	 * @return
	 */
	public List<AStarNode> findPath(AStarNode startNode, AStarNode goalNode) {
		LinkedList openList = new LinkedList(); // 要访问的节点放到这个表
		//LinkedList closedList = new LinkedList(); // 已经访问过的节点放到这个表
		Set closedList = new HashSet(); // 已经访问过的节点放到这个表

		startNode.costFromStart = 0; // 设置起点到自己的距离是0
		startNode.estimatedCostToGoal = startNode.getEstimatedCost(goalNode); // 得到至终点的估计成本,赋值给起点
		startNode.pathParent = null; // 和广度优先一样pathParent用来记录,上一个节点,通过遍历就可以找到起点
		openList.add(startNode); // 把起点放入 即将用来搜索的表中

		while (!openList.isEmpty()) {
			AStarNode node = (AStarNode) openList.removeFirst(); // 从要访问的表中取处一个来
			// if (node == goalNode) {
			// construct the path from start to goal
			// 找到终点了,停止搜索,开始遍历路径
			// }

			List neighbors = node.getNeighbors();
			for (int i = 0; i < neighbors.size(); i++) { // 遍历所有的邻居节点
				AStarNode neighborNode = (AStarNode) neighbors.get(i);
				boolean isOpen = openList.contains(neighborNode);
				// isOpen 用来判断邻居节点在不在即将访问的表中
				boolean isClosed = closedList.contains(neighborNode);
				// isClosed 用来判断邻居节点在不在已经访问过的表中
				int costFromStart = node.costFromStart + node.getCost(neighborNode); // 获得该节点成本

				// check if the neighbor node has not been
				// traversed or if a shorter path to this
				// neighbor node is found.
				if ((!isOpen && !isClosed) || costFromStart < neighborNode.costFromStart)
				// 检查邻居节点是否还未遍历,或找到了这个邻居节点的更短路径
				{
					neighborNode.pathParent = node;
					neighborNode.costFromStart = costFromStart;
					// neighborNode.estimatedCostToGoal =
					// neighborNode.getEstimatedCost(goalNode);
					// 估计到重点的距离的方法,看使用A*的具体场合
					if (node != goalNode) {
						if (isClosed) {
							closedList.remove(neighborNode);
							// 找到该节点的更短路径,则该路径从已访问过的表中移走
						}
						if (!isOpen) {
							openList.add(neighborNode);
						}
					}
				}
			}
			closedList.add(node);
		}
		return constructPath(goalNode);

		// no path found
		// return null;
	}

	public static void main(String[] args) {
		AStarNode nodeA = new AStarNode("A", 0, 10);
		AStarNode nodeB = new AStarNode("B", 5, 15);
		AStarNode nodeC = new AStarNode("C", 10, 20);
		AStarNode nodeD = new AStarNode("D", 15, 15);
		AStarNode nodeE = new AStarNode("E", 20, 10);
		AStarNode nodeF = new AStarNode("F", 15, 5);
		AStarNode nodeG = new AStarNode("G", 10, 0);
		AStarNode nodeH = new AStarNode("H", 5, 5);

		nodeA.neighbors.add(nodeF);
		nodeA.neighbors.add(nodeC);
		nodeA.neighbors.add(nodeE);

		nodeC.neighbors.add(nodeA);
		nodeC.neighbors.add(nodeE);

		nodeE.neighbors.add(nodeA);
		nodeE.neighbors.add(nodeC);
		nodeE.neighbors.add(nodeF);

		nodeF.neighbors.add(nodeA);
		nodeF.neighbors.add(nodeE);

		AStar bfs = new AStar();
		System.out.println("From A to F: " + bfs.findPath(nodeA, nodeF).toString());
		System.out.println("From C to F: " + bfs.findPath(nodeC, nodeF).toString());
		System.out.println("From F to C: " + bfs.findPath(nodeF, nodeC));
		// System.out.println("From A to G: " +
		// bfs.findPath(nodeH, nodeG).toString());
		// System.out.println("From A to unknown: " +
		// bfs.findPath(nodeA, new AStarNode("unknown",0,0)).toString());

	}
	
	/**
	 * 初始化结点
	 * @param maskdata 地图掩码数据(width*height)
	 */
	public void init(int width,int height,byte[] maskdata) {
		this.width = width;
		this.height = height;
		nodes = new AStarNode[width*height];
		//初始化结点
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				//坐标系转换
				if(maskdata[x+y*width]>0) {
					nodes[x+(height-y-1)*width] = new AStarNode(x, height-y-1);
					//判断8方向的点是否到达
					
				}
			}
		}
		//判断8方向的点是否到达
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				AStarNode node = getNode(x, y);
				if(node!=null ) {
					//左   ←
					AStarNode n = getNode(x-1, y);
					if(n!=null) {
						node.neighbors.add(n);
					}
					//右  →
					n = getNode(x+1, y);
					if(n!=null) {
						node.neighbors.add(n);
					}
					//上  ↑
					n = getNode(x, y-1);
					if(n!=null) {
						node.neighbors.add(n);
					}
					//下 ↓
					n = getNode(x, y+1);
					if(n!=null) {
						node.neighbors.add(n);
					}
					//左上 ↖
					n = getNode(x-1, y-1);
					if(n!=null) {
						node.neighbors.add(n);
					}
					//右上  ↗
					n = getNode(x+1, y-1);
					if(n!=null) {
						node.neighbors.add(n);
					}
					//右下 ↘
					n = getNode(x+1, y+1);
					if(n!=null) {
						node.neighbors.add(n);
					}
					//左下 ↙
					n = getNode(x-1, y+1);
					if(n!=null) {
						node.neighbors.add(n);
					}
				}
			}
		}
	}
	
	private AStarNode[] nodes;
	/**
	 * 获取某个通行点
	 * @param x
	 * @param y
	 * @return
	 */
	public AStarNode getNode(int x,int y) {
		try {
			return nodes[x+y*width];
		}catch(Exception e) {
		}
		return null;
	}
	
	/**
	 * 获得距离该点最近的可通行点
	 * @param x
	 * @param y
	 * @return
	 */
	public AStarNode getNearstNode(int x,int y) {
		AStarNode node = null;
		try {
			node = nodes[x+y*width];
			while(node == null) {
				//TODO
			}
		}catch(Exception e) {
		}
		return node;
	}

	/**
	 * 是否可以通过该点
	 * @return
	 */
	public boolean pass(int x,int y) {
		return nodes[x+y*width] != null;
	}
	
	public List<Point> findPath(int x1,int y1,int x2,int y2){
		AStarNode startNode = getNode(x1,y1);
		AStarNode goalNode = getNode(x2,y2);
		if(goalNode ==null) {
			return null;
		}
		List<AStarNode> nodepath = this.findPath(startNode, goalNode);
		List<Point> path = new ArrayList<Point>(nodepath.size());
		for (AStarNode node : nodepath) {
			path.add(new Point(node.x, node.y));
		}
		return path;
		
	}
}
