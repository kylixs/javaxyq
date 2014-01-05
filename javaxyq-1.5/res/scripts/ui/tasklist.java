/*
 * JavaXYQ Source Code
 * by kylixs
 * at 2009-11-27
 * please visit http://javaxyq.googlecode.com
 * or mail to kylixs@qq.com
 */

package ui;

import java.awt.Color;
import java.util.List;

import com.javaxyq.event.ActionEvent;
import com.javaxyq.event.PanelEvent;
import com.javaxyq.event.PanelHandler;
import com.javaxyq.model.Task;
import com.javaxyq.ui.Label;
import com.javaxyq.ui.RichLabel;

/**
 * 任务提示对话框脚本
 * @author dewitt
 * @date 2009-11-27 create
 */
public class tasklist extends PanelHandler {
	
	private String taskItem;
	
	@Override
	public void initial(PanelEvent evt) {
		super.initial(evt);
		taskItem ="school";
		update(evt);
		setAutoUpdate(true);
	}
	public void show_storyTask(ActionEvent evt) {
		this.taskItem = "story";		RichLabel lblDetail =  (RichLabel) panel.findCompByName("lblTaskDetail");
		lblDetail.setText("#B当前没有剧情任务。");
		Label lblSchoolTask = (Label) panel.findCompByName("lblSchoolTask");
		Label lblStoryTask = (Label) panel.findCompByName("lblStoryTask");
		lblSchoolTask.setForeground(Color.BLACK);
		lblStoryTask.setForeground(Color.BLUE);
	}
	public void show_schoolTask(ActionEvent evt) {
		this.taskItem = "school";
		List<Task> tasks = application.getTaskManager().getTasksOfType(this.taskItem);
		RichLabel lblDetail = (RichLabel) panel.findCompByName("lblTaskDetail");
		if(tasks!=null && tasks.size()>0) {
			Task task = tasks.get(0);
			int times = task.getInt("times");
			int rounds = task.getInt("rounds");
			String status = task.isFinished()?"，#R已完成#B":"";
			String taskdesc = application.getTaskManager().desc(task).replaceAll("#n","#B");
			lblDetail.setText("#B"+taskdesc+"#r(当前#R"+rounds+"#B轮#R"+times+"#B次"+status+")");
		}else {
			lblDetail.setText("#B当前没有师门任务，请找师傅领取。");
		}
		Label lblSchoolTask = (Label) panel.findCompByName("lblSchoolTask");
		Label lblStoryTask = (Label) panel.findCompByName("lblStoryTask");		lblSchoolTask.setForeground(Color.BLUE);
		lblStoryTask.setForeground(Color.BLACK);	}

	public void update(PanelEvent evt) {
		try {
			this.invokeMethod0("show_"+taskItem+"Task", new ActionEvent(this,"show"));
		} catch (Exception e) {
			System.err.println("更新任务列表失败！");
			e.printStackTrace();
		}
	}
}
