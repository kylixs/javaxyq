/*
 * JavaXYQ Engine 
 * 
 * javaxyq@2008 all rights. 
 * http://www.javaxyq.com
 */

package com.javaxyq.trigger;

import java.awt.Point;
import java.awt.Rectangle;

import com.javaxyq.core.ApplicationHelper;
import com.javaxyq.core.SpriteFactory;
import com.javaxyq.data.SceneTeleporter;
import com.javaxyq.widget.Player;
import com.javaxyq.widget.Sprite;
import com.javaxyq.widget.Widget;

/**
 * 场景跳转触发器
 * 
 * @author 龚德伟
 * @history 2008-5-31 龚德伟 新建
 * @history 2010-4-21 gongdewei 修改使用SceneTeleporter实体类
 */
public class JumpTrigger implements Trigger {
    
    private SceneTeleporter teleporter;

    private boolean enable;

    private Rectangle bounds;

    private Sprite s;
    
    private Point startPoint;

	private Point endPoint;

    public JumpTrigger(SceneTeleporter teleporter) {
		super();
		this.teleporter = teleporter;
		startPoint = parsePoint(teleporter.getStartPoint());
		endPoint = parsePoint(teleporter.getEndPoint());
		this.bounds = new Rectangle(startPoint.x-2, startPoint.y-2, 4, 4);
	}

    public Rectangle getBounds() {
        return this.bounds;
    }

    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean b) {
        this.enable = b;
    }

    public void doAction() {
        String sceneId = String.valueOf(this.teleporter.getEndId());
        Player player = ApplicationHelper.getApplication().getContext().getPlayer();
		ApplicationHelper.getApplication().doAction(player, "com.javaxyq.action.transport", new Object[] { sceneId, endPoint.x, endPoint.y });
    }

    public void dispose() {
        this.teleporter = null;
        this.bounds = null;
        this.startPoint = null;
        this.endPoint = null;
    }

    public boolean hit(Point p) {
        return this.bounds.contains(p);
    }

    public Widget getWidget() {
        return null;
    }

    public Sprite getSprite() {
        if (s == null) {
            s = SpriteFactory.loadSprite("/magic/jump.tcp");
        }
        return s;
    }

    public Point getLocation() {
        return new Point(this.bounds.x, this.bounds.y);
    }
    
    private static Point parsePoint(String str) {
    	String[] strs = str.split("[ ,]");
    	return new Point(Integer.parseInt(strs[0]), Integer.parseInt(strs[1]));
    }
}
