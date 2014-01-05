/*
 * JavaXYQ Engine 
 * 
 * javaxyq@2008 all rights. 
 * http://www.javaxyq.com
 */

package com.javaxyq.widget;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

/**
 * 精灵
 * @author gongdewei
 * @history 2008-5-29 gongdewei 新建
 */
public class Sprite extends AbstractWidget {

	private static final long serialVersionUID = -4953815363295255637L;

	public static final int DIR_DOWN = 0x4;

    public static final int DIR_DOWN_LEFT = 0x1;

    public static final int DIR_DOWN_RIGHT = 0x0;

    public static final int DIR_LEFT = 0x5;

    public static final int DIR_RIGHT = 0x7;

    public static final int DIR_UP = 0x6;

    public static final int DIR_UP_LEFT = 0x2;

    public static final int DIR_UP_RIGHT = 0x3;

    private Vector<Animation> animations;

    /** 自动(循环)播放 */
    private boolean autoPlay = true;

    // 中心点
    private int refPixelX;

    private int refPixelY;

    /** 精灵着色 */
    private List<Integer> colorations;

    private Animation currAnimation;

    // 当前是第几个动画（哪个方向）
    private int direction;

    private String resId;

    private int repeat = -1;

    public Sprite(int width, int height, int centerX, int centerY) {
        this.colorations = new ArrayList<Integer>(3);
        this.colorations.add(0);
        this.colorations.add(0);
        this.colorations.add(0);

        this.animations = new Vector<Animation>();
        setWidth(width);
        setHeight(height);
        this.refPixelX = centerX;
        this.refPixelY = centerY;
    }

    @SuppressWarnings("unchecked")
	public Sprite(Sprite s) {
        this(s.getWidth(), s.getHeight(), s.getRefPixelX(), s.getRefPixelY());
        this.animations = (Vector<Animation>) s.animations.clone();
        this.colorations = new ArrayList<Integer>(s.colorations);
        this.autoPlay = s.autoPlay;
        this.resId = s.resId;
    }

    public void addAnimation(Animation anim) {
        anim.reset();
        animations.add(anim);
    }

    public void clearAnimations() {
        this.animations.clear();
    }

    public boolean contains(int x, int y) {
        try {
            BufferedImage bi = (BufferedImage) this.currAnimation.getImage();
            if(this.animations.size()>1) {
            	return bi.getRGB(x + refPixelX, y + refPixelY) != 0;
            }
            return bi.getRGB(x, y)!= 0;
        } catch (Exception e) {
        }
        return false;
    }

    public void dispose() {
        for (Animation anim : this.animations) {
            anim.dispose();
        }
        this.animations.clear();
        this.currAnimation = null;
    }

    @Override
    protected void doDraw(Graphics2D g, int x, int y, int width, int height) {
        x -= currAnimation.getRefPixelX();
        y -= currAnimation.getRefPixelY();
        g.drawImage(currAnimation.getImage(), x, y, (x + width), (y + height), 0, 0, width, height, null);
    }

    public Animation getAnimation(int index) {
        return animations.get(index);
    }

    public int getAnimationCount() {
        return animations.size();
    }

    public int getRefPixelX() {
        return refPixelX;
    }

    public int getRefPixelY() {
        return refPixelY;
    }

    public int getColoration(int part) {
        return this.colorations.get(part);
    }

    public Animation getCurrAnimation() {
        if(currAnimation == null) {
            setDirection(this.direction);
        }
        return currAnimation;
    }

    public int getDirection() {
        return direction;
    }

    public Image getImage() {
        return currAnimation.getImage();
    }

    public int getRepeat() {
        return currAnimation.getRepeat();
    }

    public String getResId() {
        return this.resId;
    }

    public boolean isAutoPlay() {
        return autoPlay;
    }

    public void reset() {
        direction = 0;
        currAnimation = animations.get(0);
        resetFrames();
    }

    public synchronized void resetFrames() {
        this.currAnimation.setIndex(0);
    }

    public void setAutoPlay(boolean autoPlay) {
        this.autoPlay = autoPlay;
        this.setRepeat(autoPlay ? -1 : 1);
    }

    public void setRefPixelX(int centerX) {
        this.refPixelX = centerX;
    }

    public void setRefPixelY(int centerY) {
        this.refPixelY = centerY;
    }

    public void setColoration(int part, int coloration) {
        this.colorations.set(part, coloration);
    }

    public void setColorations(int[] colorations) {
        for (int i = 0; i < colorations.length; i++) {
            this.colorations.set(i, colorations[i]);
        }
    }

    public synchronized void setDirection(int index) {
        index %= animations.size();
        this.direction = index;
        currAnimation = animations.get(this.direction);
        this.currAnimation.setRepeat(this.repeat);
    }

    public void setRepeat(int repeat) {
        currAnimation.setRepeat(repeat);
        this.repeat = repeat;
    }

    public void setResId(String resId) {
        this.resId = resId;
    }

    public void update(long elapsedTime) {
        // update animation
        currAnimation.update(elapsedTime);
    }

    public int[] getColorations() {
        int[] colorations = new int[this.colorations.size()];
        for (int i = 0; i < colorations.length; i++) {
            colorations[i] = this.colorations.get(i);
        }
        return colorations;
    }

}
