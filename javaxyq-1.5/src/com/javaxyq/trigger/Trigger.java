/*
 * JavaXYQ Engine 
 * 
 * javaxyq@2008 all rights. 
 * http://www.javaxyq.com
 */

package com.javaxyq.trigger;

import java.awt.Point;
import java.awt.Rectangle;

import com.javaxyq.widget.Widget;

/**
 * @author 龚德伟
 * @history 2008-5-31 龚德伟 新建
 */
public interface Trigger {
    boolean isEnable();

    void setEnable(boolean b);

    void doAction();

    void dispose();

    Rectangle getBounds();
    
    boolean hit(Point p);
    
    Widget getWidget();
}
