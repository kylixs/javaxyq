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
import java.util.stream.Collectors;

import com.javaxyq.model.Task;
import com.javaxyq.util.StringUtils;
import lombok.extern.slf4j.Slf4j;

/**
 * 任务管理器
 *
 * @author dewitt
 * @date 2009-11-23 create
 */
@Slf4j
public class TaskManager {
    private final List<Task> taskList = new ArrayList<>();
    private final Map<String, TaskCoolie> coolies = new HashMap<>();

    /**
     * 注册任务处理单元
     */
    public void register(String type, Object coolie) {
        try {
            if (coolie instanceof String) {
                coolie = Class.forName((String) coolie).newInstance();
            }
            coolies.put(type, (TaskCoolie) coolie);
        } catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * 读取某个npc处理的任务列表
     */
    public List<Task> getTasksFor(String npcId) {
        return taskList.stream()
                .filter(task -> StringUtils.equals(npcId, task.getReceiver()))
                .collect(Collectors.toList());
    }

    /**
     * 是否有某类型的任务
     */
    public boolean hasTaskOfType(String type) {
        return taskList.stream()
                .anyMatch(task -> StringUtils.equals(type, task.getType()));
    }

    /**
     * 读取某类型的任务列表
     */
    public List<Task> getTasksOfType(String type) {
        return taskList.stream()
                .filter(task -> StringUtils.equals(type, task.getType()))
                .collect(Collectors.toList());
    }

    /**
     * 读取某类型的任务
     */
    public Task getTaskOfType(String type, String subtype) {
        return taskList.stream()
                .filter(task -> StringUtils.equals(type, task.getType()) && StringUtils.equals(subtype, task.getSubtype()))
                .findFirst()
                .orElse(null);
    }

    /**
     * 返回任务列表
     */
    public List<Task> getTaskList() {
        return taskList;
    }

    /**
     * 删除任务
     */
    public void remove(Task task) {
        taskList.remove(task);
    }

    /**
     * 添加任务
     */
    public void add(Task task) {
        taskList.add(task);
    }

    /**
     * 处理任务
     */
    public boolean process(Task task) {
        TaskCoolie coolie = coolies.get(task.getType());
        if (coolie == null) {
            log.info("任务类型:{} 未注册，处理失败！", task.getType());
            return false;
        }
        return coolie.process(task);
    }

    /**
     * 创建任务
     */
    public Task create(String type, String subtype, String sender) {
        TaskCoolie coolie = coolies.get(type);
        if (coolie == null) {
            log.info("任务类型:{} 未注册，处理失败！", type);
            return null;
        }
        return coolie.create(subtype, sender);
    }

    /**
     * 生成任务描述
     */
    public String desc(Task task) {
        TaskCoolie coolie = coolies.get(task.getType());
        if (coolie == null) {
            log.info("任务类型:{} 未注册，处理失败！", task.getType());
            return "";
        }
        return coolie.desc(task);
    }
}
