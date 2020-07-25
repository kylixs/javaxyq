package com.javaxyq.algorithm;

import com.javaxyq.search.Searcher;

import java.awt.*;
import java.util.List;
import java.util.*;


public class BFS implements Searcher {

    private int width;
    private int height;

    /**
     * Construct the path, not including the start node. 当找到终点,依次返回其父节点,就是要搜索的路径
     */
    protected List<AStarNode> constructPath(AStarNode node) {
        LinkedList<AStarNode> path = new LinkedList<>();
        while (node.pathParent != null) {
            path.addFirst(node);
            node = node.pathParent;
        }
        return path;
    }

    public List<AStarNode> findPath(AStarNode startNode, AStarNode goalNode) {
        // list of visited nodes
        //LinkedList closedList = new LinkedList();// 存放已经访问过的节点,是(FIFO)表
        Set<AStarNode> closedList = new HashSet<>(); // 存放已经访问过的节点,是(FIFO)表

        // list of nodes to visit (sorted)
        LinkedList<AStarNode> openList = new LinkedList<>(); // 存放已经即将访的节点
        openList.add(startNode);
        startNode.pathParent = null;

        while (!openList.isEmpty()) {
            AStarNode node = (AStarNode) openList.removeFirst();
            if (node == goalNode) {
                // path found!
                return constructPath(goalNode);
            } else {
                closedList.add(node);

                for (AStarNode neighborNode : node.neighbors) {
                    if (!closedList.contains(neighborNode) && !openList.contains(neighborNode)) {
                        neighborNode.pathParent = node;
                        openList.add(neighborNode);
                    }
                }
            }
        }

        // no path found
        return null;
    }

    public void init(int width, int height, byte[] maskData) {
        this.width = width;
        this.height = height;
        nodes = new AStarNode[width * height];
        // 初始化结点
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                // 坐标系转换
                if (maskData[x + y * width] > 0) {
                    nodes[x + (height - y - 1) * width] = new AStarNode(x, height - y - 1);
                    // 判断8方向的点是否到达

                }
            }
        }
        // 判断8方向的点是否到达
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                AStarNode node = getNode(x, y);
                if (node != null) {
                    // 左 ←
                    AStarNode n = getNode(x - 1, y);
                    if (n != null) {
                        node.neighbors.add(n);
                    }
                    // 左上 ↖
                    n = getNode(x - 1, y - 1);
                    if (n != null) {
                        node.neighbors.add(n);
                    }
                    // 上 ↑
                    n = getNode(x, y - 1);
                    if (n != null) {
                        node.neighbors.add(n);
                    }
                    // 右上 ↗
                    n = getNode(x + 1, y - 1);
                    if (n != null) {
                        node.neighbors.add(n);
                    }
                    // 右 →
                    n = getNode(x + 1, y);
                    if (n != null) {
                        node.neighbors.add(n);
                    }
                    // 右下 ↘
                    n = getNode(x + 1, y + 1);
                    if (n != null) {
                        node.neighbors.add(n);
                    }
                    // 下 ↓
                    n = getNode(x, y + 1);
                    if (n != null) {
                        node.neighbors.add(n);
                    }
                    // 左下 ↙
                    n = getNode(x - 1, y + 1);
                    if (n != null) {
                        node.neighbors.add(n);
                    }
                }
            }
        }
    }

    private AStarNode[] nodes;

    public AStarNode getNode(int x, int y) {
        return nodes[x + y * width];
    }

    public AStarNode getNearestNode(int x, int y) {
        return nodes[x + y * width];
    }

    public boolean pass(int x, int y) {
        return nodes[x + y * width] != null;
    }

    public List<Point> findPath(int x1, int y1, int x2, int y2) {
        AStarNode startNode = getNode(x1, y1);
        AStarNode goalNode = getNode(x2, y2);
        if (goalNode == null) {
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
