package com.javaxyq.ui;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.awt.LayoutManager2;

/**
 * 居中布局
 * @author dewitt
 * @date 2009-12-5 实现LayoutManager2接口
 */
public class CenterLayout implements LayoutManager,LayoutManager2 {

	private Component comp;
	
	public void addLayoutComponent(String name, Component comp) {
		synchronized (comp.getTreeLock()) {
			this.comp = comp;
		}
	}

	public void layoutContainer(Container target) {
		if(comp==null)return;
		synchronized (target.getTreeLock()) {
			Insets insets = target.getInsets();
			int maxwidth = target.getWidth() - (insets.left + insets.right );
			int maxheight = target.getHeight() - (insets.top + insets.bottom );
			Dimension d = comp.getPreferredSize();
			if(d.width>maxwidth)d.width=maxwidth;
			if(d.height>maxheight)d.height=maxheight;
			int x = 0, y = 0 ;
			x=(maxwidth-d.width)/2+insets.left;
			y=(maxheight-d.height)/2+insets.top;
			comp.setLocation(x, y);
			comp.setSize(d);
		}
	}

	public Dimension minimumLayoutSize(Container target) {
		synchronized (target.getTreeLock()) {
			Component c = target.getComponent(0);
			return c == null ? new Dimension() : c.getMinimumSize();
		}
	}

	public Dimension preferredLayoutSize(Container target) {
		synchronized (target.getTreeLock()) {
			Component c = target.getComponent(0);
			return c == null ? new Dimension() : c.getPreferredSize();
		}
	}

	public void removeLayoutComponent(Component comp) {
		synchronized (comp.getTreeLock()) {
			if(this.comp == comp) {			
				this.comp = null;
			}
		}
	}

	@Override
	public void addLayoutComponent(Component comp, Object constraints) {
		this.comp = comp;
	}

	@Override
	public float getLayoutAlignmentX(Container target) {
		// TODO getLayoutAlignmentX
		return 0;
	}

	@Override
	public float getLayoutAlignmentY(Container target) {
		// TODO getLayoutAlignmentY
		return 0;
	}

	@Override
	public void invalidateLayout(Container target) {
		this.layoutContainer(target);
	}

	@Override
	public Dimension maximumLayoutSize(Container target) {
		synchronized (target.getTreeLock()) {
			Component c = target.getComponent(0);
			return c == null ? new Dimension() : c.getMaximumSize();
		}
	}

}
