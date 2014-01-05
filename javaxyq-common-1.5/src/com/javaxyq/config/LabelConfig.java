/*
 * JavaXYQ Engine 
 * 
 * javaxyq@2008 all rights. 
 * http://www.javaxyq.com
 */

package com.javaxyq.config;

/**
 * @author 龚德伟
 * @history 2008-6-9 龚德伟 新建
 */
public class LabelConfig implements Config {
    private int x;

    private int y;

    private int width;

    private int height;

    private String name;

    private String text;

    private String color;

    private String align;

    public LabelConfig(int x, int y, int width, int height, String text) {
        this.setLocation(x, y);
        this.setSize(width, height);
        this.text = text;
    }

    public LabelConfig() {
    }

    public String getType() {
        return "label";
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getAlign() {
        return align;
    }

    public void setAlign(String align) {
        this.align = align;
    }

    public void setLocation(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void setSize(int width, int height) {
        this.width = width;
        this.height = height;
    }

}
