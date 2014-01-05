/*
 * JavaXYQ Source Code
 * by kylixs
 * at 2009-11-23
 * http://javaxyq.googlecode.com
 * kylixs@qq.com
 */
package com.javaxyq.task;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import com.javaxyq.model.Task;

/**
 * 任务处理单元类
 * @author dewitt
 * @date 2009-11-23 create
 */
public class TaskCoolie {
	
	/**
	 * 处理任务
	 * @param task
	 */
	public boolean process(Task task) {
		return (Boolean) this.invokeMethod(task.getSubtype(),task); 
	}
	
	/**
	 * 创建任务
	 * @param subtype
	 * @param sender
	 */
	public Task create(String subtype,String sender) {
		return (Task) this.invokeMethod("create_"+subtype,sender);
	}
	
	/**
	 * 生成任务的描述
	 * @param task
	 * @return
	 */
	public String desc(Task task) {
		return (String) this.invokeMethod("desc_"+task.getSubtype(),task);
	}

	/**
	 * invoke a  method
	 * @param mName method name
	 * @param arg argument 
	 * @return
	 */
	private Object invokeMethod(String mName, Object arg) {
		try {
			Method m = this.getClass().getMethod(mName, arg.getClass());
			return m.invoke(this, arg);
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		return null;
	}
}
