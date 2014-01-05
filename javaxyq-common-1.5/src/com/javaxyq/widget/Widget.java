/*
 * JavaXYQ Engine 
 * 
 * javaxyq@2008 all rights. 
 * http://www.javaxyq.com
 */

package com.javaxyq.widget;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.io.Serializable;

/**
 * 游戏中使用的UI构件接口
 * 
 * @author 龚德伟
 * @history 2008-5-29 龚德伟 新建
 */
public interface Widget extends Serializable {
    void draw(Graphics g, int x, int y);

    void draw(Graphics g, int x, int y, int width, int height);

    void draw(Graphics g, Rectangle rect);

    void fadeIn(long t);

    void fadeOut(long t);

    void dispose();

    int getWidth();

    int getHeight();
    
    boolean contains(int x, int y);

}
