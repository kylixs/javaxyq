/*
 * JavaXYQ Source Code
 * by kylixs
 * at 2010-4-25
 * please visit http://javaxyq.googlecode.com
 * or mail to kylixs@qq.com
 */

package npc;

import java.util.List;
import java.util.Random;

import com.javaxyq.core.BaseApplication;
import com.javaxyq.core.Context;
import com.javaxyq.core.GameMain;
import com.javaxyq.event.PlayerAdapter;
import com.javaxyq.event.PlayerEvent;
import com.javaxyq.model.Option;
import com.javaxyq.model.Task;
import com.javaxyq.task.TaskManager;
import com.javaxyq.ui.UIHelper;
import com.javaxyq.widget.Player;


/**
 * @author gongdewei
 * @date 2010-4-25 create
 */
public class n1002 extends PlayerAdapter {
	
    public void talk(PlayerEvent evt) {
    	System.out.println("talk: "+this.getClass().getName());
    	String chat = "五庄观是地仙发源地，庄内的人参果与天地同寿，乃是三界之珍稀宝贝！#r你找为师何事？";
    	Option[] options = new Option[3];
    	options[0] = new Option("为师门做贡献","applyfor_task");
    	options[1] = new Option("学习技能","learn_skill");
    	options[2] = new Option("徒儿告退","close");
    	Option result = doTalk(evt.getPlayer(),chat,options);
		if(result!=null && "applyfor_task".equals(result.getAction())) {
			applyfor_task(evt);
		}else if(result!=null && "learn_skill".equals(result.getAction())) {
			learn_skill(evt);
		}
    	
    }
	
    public void applyfor_task(PlayerEvent evt) {
    	TaskManager taskManager = application.getTaskManager();
		List<Task> currTasks = taskManager.getTasksOfType("school");
		if(currTasks!=null && currTasks.size()>0) {
			Task currTask = currTasks.get(0);
			if(!currTask.isFinished() && !currTask.isAutoSpark() && !currTask.getSubtype().equals("patrol")) {//非自动触发完成的任务
				taskManager.process(currTask);
			}
			if(currTask.isFinished()) {//任务已完成
				taskManager.remove(currTask);
				Player player = context.getPlayer();
				dataManager.addMoney(player,currTask.getMoney());
				dataManager.addExp(player, currTask.getExp());
				String chat = "徒儿辛苦了，为师奖励你#R"+currTask.getExp()+"#n经验和#R"+currTask.getMoney()+"#n金钱，继续努力！";
				int times = currTask.getInt("times");
				if(times==10) {
					int rounds = currTask.getInt("rounds");
					//额外奖励
					String[] items = {"天不老","六道轮回","仙狐涎","白露为霜","麝香","熊胆"};
					String item = items[new Random().nextInt(items.length)];
					dataManager.addItemToPlayer(player,dataManager.createItem(item));
					chat += "#r完成了第"+rounds+"轮师门任务，额外奖励你一个#R"+item+"#n！";
				}
				doTalk(context.getTalker(),chat);				
			}else {//任务未完成
				String chat ="你还有任务在身，不能重复接任务。你要取消当前任务吗？";
		    	Option[] options = new Option[2];
		    	options[0] = new Option("是的，我要取消","cancel_task");
		    	options[1] = new Option("我去完成任务","close");
		    	Option result = doTalk(context.getTalker(),chat,options);
				if("cancel_task".equals(result.getAction())) {
					//取消未完成任务
					taskManager.remove(currTask);
					doTalk(context.getTalker(),"你的任务已取消。");
				}
			}
		}else {//没有未完成的师门任务
			Random rand = new Random();
			String[] subtypes = {"deliver","lookfor","patrol"};
			String subtype = subtypes[rand.nextInt(subtypes.length)];
			String sender = "镇元大仙";
			Task task = taskManager.create("school",subtype, sender);
			taskManager.add(task);
			String desc = taskManager.desc(task);
			doTalk(context.getTalker(),desc);
		}
    	
    }
    
    public void learn_skill(PlayerEvent evt) {
    	Player player = context.getPlayer();
    	if(player.getData().level < 10) {
    		doTalk(context.getTalker(),"你的根基尚浅，过些日子再来找为师吧！");
    	}else {
    		helper.showDialog("main_skill");
    	}
    }
}
