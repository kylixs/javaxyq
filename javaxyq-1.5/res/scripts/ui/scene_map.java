/*
 * JavaXYQ Source Code
 * by kylixs
 * at 2009-11-27
 * please visit http://javaxyq.googlecode.com
 * or mail to kylixs@qq.com
 */

package ui;


import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;

import com.javaxyq.core.SceneCanvas;
import com.javaxyq.core.SpriteFactory;
import com.javaxyq.event.PanelEvent;
import com.javaxyq.event.PanelHandler;
import com.javaxyq.ui.Label;
import com.javaxyq.ui.UIHelper;
import com.javaxyq.widget.Animation;
import com.javaxyq.widget.Player;
import com.javaxyq.widget.Sprite;
import com.javaxyq.widget.SpriteImage;
/**
 * 场景小地图对话框脚本
 * @author dewitt
 * @date 2009-11-27 create
 */
public class scene_map extends PanelHandler implements MouseListener{
	
	private int marginX = 18;
	private int marginY = 12;
	private Animation animPoint = SpriteFactory.loadAnimation("wzife/scene/point.tcp");
	private Animation animWpoint = SpriteFactory.loadAnimation("wzife/scene/wpoint.tcp");
	private Animation animTarget = SpriteFactory.loadAnimation("wzife/scene/target.tcp");
	
	private Label lblPoint = new Label(animPoint);
	private Label lblTarget = new Label(animTarget);
	private List<Label> steps = new ArrayList<Label>();
	
	/** 缩略图宽度 */
	private int navWidth;
	/** 缩略图高度 */
	private int navHeight;
	/** 缩小比例 */
	private double rateX;
	private double rateY;
	private SceneCanvas canvas;
	public void initial(PanelEvent evt) {
		super.initial(evt);
		this.canvas = (SceneCanvas) super.canvas;
		String path = canvas.getMap().getConfig().getPath();
		path = path.replace(".map",".tcp").replace("scene/","smap/");
		Sprite sprite = SpriteFactory.loadSprite(path);
		panel.setBgImage(new SpriteImage(sprite));
		panel.setSize(sprite.getWidth(),sprite.getHeight());
		int x = (640-panel.getWidth())/2;
		int y = (480 - panel.getHeight())/2;
		panel.setLocation(x, y);
		panel.remove(lblPoint);
		panel.remove(lblTarget);
		navWidth = panel.getWidth()-2*marginX;
		navHeight = panel.getHeight() - 2*marginY;
		rateX = 1.0*navWidth /canvas.getSceneWidth();
		rateY = 1.0*navHeight /canvas.getSceneHeight();
		
		UIHelper.removeAllMouseListeners(panel);
		panel.addMouseListener(this);
		setAutoUpdate(true);
	}

	@Override
	synchronized public void update(PanelEvent evt) {
		Player player = context.getPlayer();
		Point playerLoc = player.getSceneLocation();
		Point p0 = sceneToLocal(playerLoc);
		//FIXME 修复单帧偏移位置问题
		p0.translate(-animPoint.getRefPixelX(),-animPoint.getRefPixelY());
		lblPoint.setLocation(p0);
		panel.add(lblPoint);
		//移除路线的点
		//TODO 解决闪烁问题
		for (Label l : steps) {			
			panel.remove(l);
		}
		
		List<Point> path = player.getPath();
		if(path!=null && path.size()>1) {
			steps.clear();
			Point targetPoint = path.get(path.size()-1);
			Point p = sceneToLocal(targetPoint);
			p.translate(-animTarget.getRefPixelX(),-animTarget.getRefPixelY());
			lblTarget.setLocation(p);
			panel.add(lblTarget);
			
			for(int i=1;i<path.size()-2;i+=5) {
				Label lblWpoint = new Label(animWpoint);
				lblWpoint.setLocation(sceneToLocal(path.get(i)));
				steps.add(lblWpoint);
				panel.add(lblWpoint);
			}
		}else if(!steps.isEmpty()){//完成移动
			steps.clear();
			panel.remove(lblTarget);
			panel.close();
		}
	}
	
	private Point sceneToLocal(Point p) {
		return new Point(marginX+(int)(p.x*rateX), marginY+navHeight-(int)(p.y*rateY));
	}

	private Point localToScene(Point p) {
		p.translate(-marginX,-marginY);
		return new Point((int)(p.x/rateX),(int)((navHeight-p.y)/rateY));
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		Point target = localToScene(e.getPoint());
		canvas.walkTo(target.x,target.y);
	}

	@Override
	public void mouseEntered(MouseEvent e) {
	}

	@Override
	public void mouseExited(MouseEvent e) {
	}

	@Override
	public void mousePressed(MouseEvent e) {
	}

	@Override
	public void mouseReleased(MouseEvent e) {
	}
}
