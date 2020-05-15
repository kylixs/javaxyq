package com.javaxyq.tools;

import com.jidesoft.swing.Resizable;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import javax.swing.JLayeredPane;

import java.awt.image.BufferedImage;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.JComponent;
import javax.swing.JPanel;



//图层排序事件
class LayerOrderChangeListener implements PropertyChangeListener {
	private UIMaker maker;
	public LayerOrderChangeListener(UIMaker maker) {
		this.maker = maker;
	}
	void propertyChange(PropertyChangeEvent evt) {
		println "change: ${evt.oldValue} ${evt.newValue}"
		def layers = maker.builder.layerlist.model.elements();
		for(LayerObject layerObj in layers) {
			//改变dom顺序
			Node node = layerObj.node
			if(node.name() == 'Dialog') {
				continue;
			}
			maker.dialogNode.remove(node)
			maker.dialogNode.append(node);
			
			//改变控件顺序
			Component comp = maker.node2comp[node]; 
			if(comp) {//有些结点没有对应的控件
				maker.dialog.remove(comp);
				maker.dialog.add(comp);
			}
		}
		
	}
}

//绘制线程
class PaintTask extends TimerTask {
	UIMaker maker;
	JPanel canvas;
	private BufferedImage offscreenImage;
	private Graphics2D offscreenGraphics;
	public PaintTask(UIMaker maker) {
		this.maker = maker;
		this.canvas = maker.builder.canvas;
		this.offscreenImage = maker.offscreenImage;
		this.offscreenGraphics = maker.offscreenGraphics;
	}
	void run () {
		try {
			//offscreen painting!
			if(canvas.isShowing()) {
				canvas.paint(this.offscreenGraphics);
				paintMenu()
				canvas.getGraphics().drawImage(this.offscreenImage, 0, 0, null);
			}
		}catch(e) {
			e.printStackTrace()
		}
	}
	//绘制canvas上的LayeredPane内容，如弹出菜单等
	//因为Canvas是自描绘的，影响了原来的菜单绘制
	//FIXME 与canvas绘制有冲突
	private paintMenu() {
		try {
			def pane = maker.frame.getLayeredPane();
			def comps = pane.getComponentsInLayer(JLayeredPane.POPUP_LAYER);
			//println "popup layer: $comps"
			Graphics g = this.offscreenGraphics.create(0,0,300,150);
			Point p0 = pane.getLocationOnScreen();
			Point p1 = canvas.getLocationOnScreen();
			int dx = p1.x - p0.x;
			int dy = p1.y - p0.y
			g.translate(-dx,-dy);
			//pane.paint(g);
			for(def c in comps) {
				if(c.visible) {
					Graphics g2 = g.create(c.x,c.y,c.width,c.height);
					c.paint(g2);
					g2.dispose();
				}
			}
			g.dispose();
		}catch(e) {}
	}
}


//custom resizable 
class MyResizable extends Resizable {
	private UIMaker maker;
	private JComponent component;
	public MyResizable(JComponent component,UIMaker maker) {
		super(component);
		this.maker = maker;
		this.component = component;
	}

	public void resizing(int resizeCorner, int newX, int newY, int newW, int newH) {
        Dimension minimumSize = component.getMinimumSize();
        Dimension maximumSize = component.getMaximumSize();
        if (newW < minimumSize.width) {
            newW = minimumSize.width;
        }
        if (newH < minimumSize.height) {
            newW = minimumSize.height;
        }
        if (newW > maximumSize.width) {
            newW = maximumSize.width;
        }
        if (newH > maximumSize.height) {
            newH = maximumSize.height;
        }
        component.setPreferredSize(new Dimension(newW, newH));
		maker.resizeComp(component,new Dimension(newW, newH))
	};
}