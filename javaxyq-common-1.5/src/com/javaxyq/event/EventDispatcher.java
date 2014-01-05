/*
 * JavaXYQ Engine 
 * 
 * javaxyq@2008 all rights. 
 * http://www.javaxyq.com
 */

package com.javaxyq.event;

import java.awt.Component;
import java.util.EventObject;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * @author 龚德伟
 * @history 2008-6-18 龚德伟 新建
 */
public class EventDispatcher<S extends EventTarget, E extends EventObject> extends Thread{

	/** 事件调度器实例表 */
	private static final Map<Object, EventDispatcher> instances = new HashMap<Object, EventDispatcher>();
	private static EventDispatcher dispatcher;

	private BlockingQueue<E> eventQueue = new LinkedBlockingQueue<E>();

	//private EventProcessor eventProcessor = null;
	private static int processorCount;

    private static final int ANY_EVENT = -1;

	private EventDispatcher() {
		//eventProcessor = new EventProcessor();
		//eventProcessor.start();
		processorCount++;
		setName("EventDispatcher-" + processorCount);
		setDaemon(true);
	}

	public void dispatchEvent(E evt) {
		eventQueue.offer(evt);
	}

	synchronized public static <T1 extends EventTarget, T2 extends EventObject> EventDispatcher<T1, T2> getInstance(
			Class<T1> clazz1, Class<T2> clazz2) {
		EventDispatcher<T1, T2> dispatcher = instances.get(clazz1);
		if (dispatcher == null) {
			dispatcher = new EventDispatcher<T1, T2>();
			instances.put(clazz1, dispatcher);
			dispatcher.start();
		}
		return dispatcher;
	}
	synchronized public static EventDispatcher getInstance() {
		if (dispatcher == null) {
			dispatcher = new EventDispatcher();
			dispatcher.start();
		}
		return dispatcher;
	}

    public void pumpEvents(Conditional cond) {
    	pumpEvents(ANY_EVENT, cond);
    }
    
	/**
	 * TODO filter events
	 * @param anyEvent
	 * @param cond
	 */
	public void pumpEvents(int id, Conditional cond) {
		while (cond.evaluate()) {
			// System.out.println(this.getId()+" "+this.getName());
			try {
				// 等待下一个事件
				E evt = eventQueue.take();
				if (evt != null) {
					//System.out.println("handle event: "+evt);
					((S) evt.getSource()).handleEvent(evt);
				}
			} catch (Exception e) {
				System.err.println("event process error!");
				e.printStackTrace();
			}
		}
		
	}

	public void run() {
		System.out.println(getName()+" starting...");
	    pumpEvents(new Conditional() {
			public boolean evaluate() {
			    return true;
			}
		});	    
	    System.out.println(getName()+" stopped.");
	}

	/**
	 * @param currentThread
	 * @param cond
	 */
	public static void pumpEvents(Thread currentThread, Conditional cond) {
		if (currentThread instanceof EventDispatcher) {
			EventDispatcher dispatcher = (EventDispatcher) currentThread;
			dispatcher.pumpEvents(cond);
		}
	}

	
/*	private class EventProcessor extends Thread {
		public EventProcessor() {
			processorCount++;
			setName("EventProcessor-" + processorCount);
			setDaemon(true);
		}

		@Override
		public void run() {
			while (true) {
				// System.out.println(this.getId()+" "+this.getName());
				try {
					// 等待下一个事件
					E evt = eventQueue.take();
					if (evt != null) {
						((S) evt.getSource()).handleEvent(evt);
					}
				} catch (Exception e) {
					System.err.println("event process error!");
					e.printStackTrace();
				}
			}
		}
	}
*/
}
