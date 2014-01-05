/*
 * JavaXYQ Source Code
 * by kylixs
 * at 2009-11-26
 * please visit http://javaxyq.googlecode.com
 * or mail to kylixs@qq.com
 */
package com.javaxyq.event;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Timer;
import java.util.TimerTask;

import com.javaxyq.core.Application;
import com.javaxyq.core.ApplicationHelper;
import com.javaxyq.core.Context;
import com.javaxyq.core.DataManager;
import com.javaxyq.core.GameCanvas;
import com.javaxyq.core.GameWindow;
import com.javaxyq.model.Option;
import com.javaxyq.ui.Panel;
import com.javaxyq.ui.UIHelper;
import com.javaxyq.widget.Player;

/**
 * 面板事件处理器基类
 * @author dewitt
 * @date 2009-11-26 create
 */
public abstract class PanelHandler implements PanelListener{
	
	protected Panel panel;
	private boolean autoUpdate; 
	private Timer timer;
	protected long period = 1000;
	protected Application application;
	protected Context context;
	protected UIHelper helper;
	protected DataManager dataManager;
	protected GameCanvas canvas;
	protected GameWindow window;
	
	public void actionPerformed(ActionEvent evt) {
		try {
			String cmd = evt.getCommand();
			//cmd的第一段为函数名，后面可以有参数
			this.invokeMethod0(cmd.split(" ")[0],evt);
			evt.consume();
		} catch (NoSuchMethodException e) {
			System.err.println("\n[PanelHandler]找不到事件的处理方法："+e.getMessage());
		} catch (Exception e) {
			System.err.println("\n[PanelHandler]执行事件时发生异常："+evt);
			e.printStackTrace();
		}
	}
	
	synchronized public void dispose(PanelEvent evt) {
		System.out.println("dispose: "+this.getClass().getName());
		if(this.timer!=null) {
			this.timer.cancel();
			this.timer = null;
		}
	}
	
	public void initial(PanelEvent evt) {
		System.out.println("initial: "+this.getClass().getName());
		panel = (Panel) evt.getSource();
		application = ApplicationHelper.getApplication();
		context = application.getContext();
		window = context.getWindow();
		helper = window.getHelper();
		dataManager = application.getDataManager();
		canvas = window.getCanvas();
	}	
	synchronized public void update(PanelEvent evt) {
		
	}
	
	public void close(ActionEvent evt) {
		ApplicationHelper.getApplication().getContext().getWindow().getHelper().hideDialog(panel);
	}
	
	public void help(ActionEvent evt) {
		System.out.println("help: "+this.getClass().getName());
	}

	synchronized public void setAutoUpdate(boolean b) {
		if(b) {
			if(timer == null) {
				timer = new Timer("update-"+this.getClass().getName(), true) ;
				timer.schedule(new TimerTask() {
					@Override
					public void run() {
						update(null);
					}
					}, 100, period );
			}
		}else {
			if(timer!=null) {
				timer.cancel();
				timer = null;
			}
		}
		this.autoUpdate = b;
	}
	
	public boolean isAutoUpdate() {
		return autoUpdate;
	}	
	public void doAction(Object source, String actionId, Object[] args) {
		application.doAction(source, actionId, args);
	}

	public void doAction(Object source, String actionId) {
		application.doAction(source, actionId);
	}

	public Option doTalk(Player talker, String chat, Option[] options) {
		return application.doTalk(talker, chat, options);
	}

	public void doTalk(Player p, String chat) {
		application.doTalk(p, chat);
	}

	/**
	 * invoke a  method
	 * @param mName method name
	 * @param arg argument 
	 * @return
	 * @throws InvocationTargetException 
	 * @throws IllegalAccessException 
	 * @throws IllegalArgumentException 
	 * @throws NoSuchMethodException 
	 * @throws SecurityException 
	 */
	protected Object invokeMethod0(String mName, Object arg) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException, SecurityException, NoSuchMethodException {
		Method m = this.getClass().getMethod(mName, arg.getClass());
		return m.invoke(this, arg);
	}
}