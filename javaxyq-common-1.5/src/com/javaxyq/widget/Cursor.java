/******************************************************************************
 * Copyright (C) 2007 ShenZhen ComTop Information Technology Co.,Ltd
 * All Rights Reserved.
 * 本软件为深圳康拓普开发研制。未经本公司正式书面同意，其他任何个人、团体不得使用、复制、
 * 修改或发布本软件.
 *****************************************************************************/

package com.javaxyq.widget;

import java.awt.Graphics;
import java.awt.Point;

import com.javaxyq.core.SpriteFactory;

/**
 * 游戏指针
 * 
 * @author 龚德伟
 * @history 2008-6-8 龚德伟 新建
 */
public class Cursor {
    public static final String DEFAULT_CURSOR = "01";

    public static final String ATTACK_CURSOR = "15";

    public static final String EXCHANGE_CURSOR = "exchange";

    public static final String GIVE_CURSOR = "13";

    public static final String TALK_CURSOR = "22";

    public static final String PICKUP_CURSOR = "03";

    public static final String FRIEND_CURSOR = "friend";

    public static final String SELECT_CURSOR = "11";

    public static final String CATCH_CURSOR = "21";

    public static final String FORBID_CURSOR = "04";

    public static final String TEXT_CURSOR = "02";

    public static final String PROTECT_CURSOR = "17";
    private SpriteImage pointer;

    private SpriteImage effect;

    private int x;

    private int y;

    /** 点击的场景坐标 */
    private int clickX;

    private int clickY;

    /** x偏移量(相对于clickX,为了精确显示点击的效果) */
    private int offsetX;

    private int offsetY;

    public Cursor(String type, boolean effect) {
        this.pointer = new SpriteImage(SpriteFactory.loadSprite("/wzife/cursor/"+type+".tcp"));
        if(effect) {        	
        	this.effect = new SpriteImage(SpriteFactory.loadSprite("/addon/wave.tcp"));;
        	this.effect.setVisible(false);
        }
    }

    public SpriteImage getPointer() {
        return pointer;
    }

    public SpriteImage getEffect() {
        return effect;
    }

    public void setClick(int x, int y) {
        this.clickX = x;
        this.clickY = y;
        this.effect.setVisible(true);
    }

    public int getClickX() {
        return clickX;
    }

    public int getClickY() {
        return clickY;
    }

    public Point getClickPosition() {
        return new Point(this.clickX, this.clickY);
    }

    public void setOffset(int x, int y) {
        this.offsetX = x;
        this.offsetY = y;
    }

    public int getOffsetX() {
        return offsetX;
    }

    public void setOffsetX(int offsetX) {
        this.offsetX = offsetX;
    }

    public int getOffsetY() {
        return offsetY;
    }

    public void setOffsetY(int offsetY) {
        this.offsetY = offsetY;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    /**
     * 设置指针在屏幕的位置
     * @param x
     * @param y
     */
    public void setLocation(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void draw(Graphics g) {
        this.pointer.draw(g, x, y);
    }

    public void update(long elapsedTime) {
        this.pointer.update(elapsedTime);
    }

}