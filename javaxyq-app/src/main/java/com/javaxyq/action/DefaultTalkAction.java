/**
 *
 */
package com.javaxyq.action;

import java.util.List;

import com.javaxyq.core.ApplicationHelper;
import com.javaxyq.core.DataManager;
import com.javaxyq.event.PlayerAdapter;
import com.javaxyq.event.PlayerEvent;
import com.javaxyq.event.PlayerListener;
import com.javaxyq.model.Task;
import com.javaxyq.task.TaskManager;
import com.javaxyq.widget.Player;
import lombok.extern.slf4j.Slf4j;

/**
 * @author dewitt
 *
 */
@Slf4j
public class DefaultTalkAction extends PlayerAdapter {

    @Override
    public void talk(PlayerEvent evt) {
        Player player = evt.getPlayer();
        String npcId = player.getId();
        log.info("talk: " + evt);
        //任务处理
        TaskManager taskManager = ApplicationHelper.getApplication().getTaskManager();
        List<Task> tasks = taskManager.getTasksFor(player.getName());
        if (tasks != null && !tasks.isEmpty()) {
            //TODO 多个任务时生成任务选择列表
            if (tasks.size() > 1) {
                log.info("npc[{}]有{}个任务", player.getName(), tasks.size());
                for (Object task : tasks) {
                    log.info(task.toString());
                }
            }
            Task task = tasks.get(0);
            if (!task.isFinished() && task.isAutoSpark()) {
                task.set("target", player);
                //如果任务处理完成，则返回，否则继续常规对话
                if (taskManager.process(task))
                    return;
            }
        }
        //npc事件
        PlayerListener listener = (PlayerListener) ApplicationHelper.getApplication().getScriptEngine().loadNPCScript(npcId);
        if (listener != null) {//处理事件
            listener.talk(evt);
        } else {//没有事件触发默认对话
            DataManager dataManager = ApplicationHelper.getApplication().getDataManager();
            String chat = dataManager.findChatText(npcId);
            if (chat != null) {
                ApplicationHelper.getApplication().chat(player, chat);
            }
        }
    }
}
