package com.javaxyq.ui;

import javax.swing.JComponent;
import javax.swing.RepaintManager;

public class NullRepaintManager extends RepaintManager {

	public static void install(){
		RepaintManager repaintManager=new NullRepaintManager();
		repaintManager.setDoubleBufferingEnabled(false);
		RepaintManager.setCurrentManager(repaintManager);
	}
	@Override
	public synchronized void addInvalidComponent(JComponent invalidComponent) {
		// do nothing
	}
	@Override
	public void addDirtyRegion(JComponent c, int x, int y, int w, int h) {
		// do nothing
	}
	@Override
	public void markCompletelyDirty(JComponent aComponent) {
		// do nothing
	}
	@Override
	public void paintDirtyRegions() {
		//do nothing
	}
}
