/*
 * JavaXYQ Source Code
 * by kylixs
 * at 2009-11-23
 * please visit http://javaxyq.googlecode.com
 * mail to kylixs@qq.com
 */
package com.javaxyq.task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.javaxyq.model.Task;
import com.javaxyq.util.StringUtils;

/**
 * 任务管理器
 * @author dewitt
 * @date 2009-11-23 create
 */
public class TaskManager {
	private List<Task> tasklist = new ArrayList<Task>();
	private Map<String,TaskCoolie> coolies = new HashMap<String, TaskCoolie>();
	
	public TaskManager() {
	}
	
	/**
	 * 注册任务处理单元
	 * @param type
	 * @param coolie
	 */
	public void register(String type, Object coolie) {
		try {
			if (coolie instanceof String) {
				coolie = Class.forName((String) coolie).newInstance();
			}
			coolies.put(type, (TaskCoolie) coolie);
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 读取某个npc处理的任务列表
	 * @param npc
	 * @return
	 */
	public List<Task> getTasksFor(String npcid) {
		//return tasklist.findAll{task-> task.receiver==npcid};
		List<Task> tasks = new ArrayList<Task>();
		for (int i = 0; i < tasklist.size(); i++) {
			Task task = tasklist.get(i);
			if(StringUtils.equals(npcid,task.getReceiver())) {
				tasks.add(task);
			}
		}
		return tasks;
	}
	
	/**
	 * 是否有某类型的任务
	 * @param type
	 * @return
	 */
	public boolean hasTaskOfType(String type) {
		//return tasklist.any{task-> task.type==type};
		for (int i = 0; i < tasklist.size(); i++) {
			Task task = tasklist.get(i);
			if(StringUtils.equals(type,task.getType())) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * 读取某类型的任务列表
	 * @param type
	 * @return
	 */
	public List<Task> getTasksOfType(String type) {
		//return tasklist.findAll{task-> task.type==type};
		List<Task> tasks = new ArrayList<Task>();
		for (int i = 0; i < tasklist.size(); i++) {
			Task task = tasklist.get(i);
			if(StringUtils.equals(type,task.getType())) {
				tasks.add(task);
			}
		}
		return tasks;
	}
	
	/**
	 * 读取某类型的任务
	 * @param type
	 * @param subtype
	 * @return
	 */
	public Task getTaskOfType(String type, String subtype) {
		//return tasklist.find{task-> task.type==type && task.subtype == subtype};;
		for (int i = 0; i < tasklist.size(); i++) {
			Task task = tasklist.get(i);
			if(StringUtils.equals(type,task.getType()) && StringUtils.equals(subtype, task.getSubtype())) {
				return task;
			}
		}
		return null;
	}
	
	/**
	 * 返回任务列表
	 * @return
	 */
	public List<Task> getTaskList() {
		return tasklist;
	}
		
	/**
	 * 删除任务
	 * @param task
	 */
	public void remove(Task task) {
		tasklist.remove(task);
	}
	
	/**
	 * 添加任务
	 * @param task
	 */
	public void add(Task task) {
		tasklist.add(task);
	}
	
	/**
	 * 处理任务
	 * @param task
	 * @return
	 */
	public boolean process(Task task) {
		TaskCoolie coolie = coolies.get(task.getType());
		if(coolie == null) {
			System.out.printf("任务类型:%s 未注册，处理失败！\n",task.getType());
			return false;
		}
		return coolie.process(task);
	}

	/**
	 * 创建任务
	 * @param subtype
	 * @param sender
	 * @return
	 */
	public Task create(String type,String subtype,String sender) {
		TaskCoolie coolie = coolies.get(type);
		if(coolie == null) {
			System.out.printf("任务类型:%s 未注册，处理失败！\n",type);
			return null;
		}
		return coolie.create(subtype,sender);
	}

	/**
	 * 生成任务描述
	 * @param task
	 * @return
	 */
	public String desc(Task task) {
		TaskCoolie coolie = coolies.get(task.getType());
		if(coolie == null) {
			System.out.printf("任务类型:%s 未注册，处理失败！\n",task.getType());
			return "";
		}
		return coolie.desc(task);
	}
}
