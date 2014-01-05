package com.javaxyq.widget;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;

/**
 * 精灵图片控件(带状态的)
 * 
 * @author 龚德伟
 * @history 2008-6-7 龚德伟 新建
 */
public class SpriteImage extends AbstractWidget {

    private Sprite sprite;

    private int x;

    private int y;

    private boolean visible = true;

    public SpriteImage(Sprite sprite) {
        this(sprite, 0, 0, sprite.getWidth(), sprite.getHeight());
    }

    public SpriteImage(Sprite sprite, int x, int y) {
        this(sprite, x, y, sprite.getWidth(), sprite.getHeight());
    }

    public SpriteImage(Sprite sprite, int x, int y, int width, int height) {
        this.sprite = sprite;
        setWidth(width);
        setHeight(height);
        this.x = x;
        this.y = y;
        visible = true;
    }

    public Image getImage() {
        return sprite.getImage();
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setLocation(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    @Override
    public void dispose() {
        this.sprite.dispose();
    }

    @Override
    protected void doDraw(Graphics2D g2, int x, int y, int width, int height) {
        if (this.visible && this.sprite != null) {
            this.sprite.draw(g2, x, y, width, height);
        }
    }

    public void update(long elapsedTime) {
        this.sprite.update(elapsedTime);
    }

    public void setRepeat(int i) {
        this.sprite.setRepeat(i);
    }

    public boolean contains(int x, int y) {
        return this.sprite.contains(x,y);
    }

	public Sprite getSprite() {
		return sprite;
	}

	public void setSprite(Sprite sprite) {
		this.sprite = sprite;
	}

}
