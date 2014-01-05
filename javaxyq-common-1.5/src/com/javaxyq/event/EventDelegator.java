/*
 * JavaXYQ Source Code
 * by kylixs
 * at 2009-11-26
 * please visit http://javaxyq.googlecode.com
 * or mail to kylixs@qq.com
 */
package com.javaxyq.event;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.util.EventObject;

import com.javaxyq.ui.Panel;

/**
 * @author dewitt
 * @date 2009-11-26 create
 */
public class EventDelegator implements EventTarget {

	private static final EventDelegator instance = new EventDelegator();
	private final static EventDispatcher<EventDelegator, EventObject> dispatcher = EventDispatcher.getInstance();

	private EventDelegator() {
	}

	public static EventDelegator getInstance() {
		return instance;
	}
	public boolean isDelegatedThread() {
		return Thread.currentThread() == dispatcher;
	}
	private static class DelegateEvent extends EventObject {
		private EventObject event;

		public DelegateEvent(EventDelegator delegator, EventObject evt) {
			super(delegator);
			this.event = evt;
		}

		public EventObject getEvent() {
			return event;
		}
	}

	@Override
	public boolean handleEvent(EventObject evt) throws EventException {
		if (evt instanceof DelegateEvent) {
			EventObject event = ((DelegateEvent) evt).getEvent();
			try {
				Component comp = (Component) event.getSource();
				Panel panel = findPanel(comp);
				if(panel!=null) {
					return panel.handleEvent(event);
				}
			} catch (Exception e) {
				System.err.println("处理事件失败！" + event);
				e.printStackTrace();
			}
		}
		return false;
	}

	public void delegateEvent(EventObject event) {
		if (event instanceof ActionEvent) {
			ActionEvent ae = (ActionEvent) event;
			String cmd = ae.getActionCommand();
			// 如果没有设置actionId，则不处理
			if (cmd == null || cmd.trim().length() == 0) {
				return;
			}
		}
		dispatcher.dispatchEvent(new DelegateEvent(instance, event));
	}

	public void pumpEvents(Conditional cond) {
		dispatcher.pumpEvents(cond);
	}
	
	/**
	 * 查找最近的面板
	 * @param c
	 * @return
	 */
	private static Panel findPanel(Component c) {
		for(;c!=null;) {
			if (c instanceof Panel) {
				break;
			}
			c=c.getParent();
		} 
		return (Panel) c;
	}	
}
