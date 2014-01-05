/*
 * JavaXYQ Source Code
 * by kylixs
 * at 2009-11-23
 * http://javaxyq.googlecode.com
 * kylixs@qq.com
 */
package com.javaxyq.task;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.javaxyq.action.Actions;
import com.javaxyq.battle.BattleEvent;
import com.javaxyq.battle.BattleListener;
import com.javaxyq.core.ApplicationHelper;
import com.javaxyq.core.Context;
import com.javaxyq.core.DataManager;
import com.javaxyq.core.Environment;
import com.javaxyq.core.GameWindow;
import com.javaxyq.data.ItemInstance;
import com.javaxyq.model.Task;
import com.javaxyq.ui.UIHelper;
import com.javaxyq.util.StringUtils;
import com.javaxyq.widget.Player;

/**
 * "门派任务"处理单元
 * @author dewitt
 * @date 2009-11-23 create
 */
public class SchoolTaskCoolie extends TaskCoolie {
	
	/** 每10次为一轮任务 */
	private static final int ROUND = 10;
	
	private Random rand = new Random();
	/** 任务次数 */
	int times;
	int rounds = 1;

	private Context context;

	private DataManager dataManager;

	private UIHelper helper;

	private GameWindow window;
	
	public SchoolTaskCoolie() {
		dataManager = ApplicationHelper.getApplication().getDataManager();
		context = ApplicationHelper.getApplication().getContext();
		window = context.getWindow();
		helper = window.getHelper();
	}
	
	/**
	 * 送信
	 * @param task
	 * @return
	 */
	public boolean deliver(Task task) {
		//System.out.println("deliver $task");
		if(!task.isFinished()) {
			Player player =context.getPlayer();
			Player target = (Player) task.get("target");
			task.setFinished(true);
			rounds = task.getInt("rounds");
			times = task.getInt("times");
			ApplicationHelper.getApplication().doTalk(target,"我已收到你师傅的来信，赶快回去禀报吧。", null);
			return true;
		}else {
			System.out.println("任务已完成？"+task);
		}
		return false;
	}
	
	/**
	 * 寻物
	 * @param task
	 * @return
	 */
	public boolean lookfor(Task task) {
		//System.out.println("lookfor $task");
		if(!task.isFinished()) {
			Player player =context.getPlayer();
			Player target = (Player) task.get("target");
			String required = (String) task.get("item");
			ItemInstance[] items = dataManager.getItems(player);
			for (ItemInstance item : items) {
				if(item!=null && StringUtils.equals(required, item.getName())) {
					item.alterAmount(-1);
					if(item.getAmount() == 0) {//TODO 物品销毁？
						dataManager.removePlayerItem(player,item);
					}
					task.setFinished(true);
					rounds = (Integer)task.get("rounds");
					times = (Integer)task.get("times");
					return true;
				}
			}
		}
		return false;
	}
	
	/**
	 * 巡逻
	 * @param task
	 * @return
	 */
	public boolean patrol(final Task task) {
		//System.out.println("patrol $task");
		final Player player = context.getPlayer();
		player.stop(true);
		//初始化小怪队伍
		int level = context.getPlayer().getData().getLevel();
		List<Player> t1 = new ArrayList<Player>();
		List<Player> t2 = new ArrayList<Player>();
		String[] elfs = {"2036","2037","2009","2010","2011","2012"};
		String[] elfNames = {"大海龟","巨蛙","芙蓉仙子","树怪","蝴蝶仙子","花妖"};
		Random random = new Random();
		final int elfCount = random.nextInt(3)+1;
		for(int i=0;i<elfCount;i++) {		
			int elflevel = Math.max(0,level+random.nextInt(4)-2);
			int elfIndex = random.nextInt(elfs.length); 
			t2.add(dataManager.createElf(elfs[elfIndex], elfNames[elfIndex],elflevel));
		}
		t1.add(player);
		//进入战斗
		ApplicationHelper.getApplication().doAction(this, Actions.ENTER_BATTLE,new Object[] {t1,t2});
		helper.prompt("一群小贼真正破坏门派，被你抓个正着。",3000);
		window.addBattleListener(new BattleListener() {
			//战斗胜利处理
			public void battleWin(BattleEvent e) {
				System.out.println("战斗胜利");
				task.add("battle",1);
				int exp = player.getData().level*(150 + rand.nextInt(20))*elfCount/10;
				player.getData().exp += exp;
				helper.prompt("获得"+exp+"点经验。",3000);
				if((Integer)task.get("battle") == 2) {
					task.setFinished(true);
					rounds = (Integer) task.get("rounds");
					times = (Integer)task.get("times");
					helper.prompt("师门巡逻任务完成，快去禀报师傅吧。",3000);
				}else {
					helper.prompt("小贼们趁你不注意，又不知道溜到哪里。",3000);
				}
				Environment.set(Environment.LAST_PATROL_TIME,System.currentTimeMillis());
			}
			public void battleTimeout(BattleEvent e) {
			}
			//战斗失败处理
			public void battleDefeated(BattleEvent e) {
				System.out.println("战斗失败");
				//气血为0的人物恢复一点气血
				if(player.getData().hp ==0) {
					player.getData().hp = 1;
				}
				Environment.set(Environment.LAST_PATROL_TIME,System.currentTimeMillis());
				helper.prompt("想不到小贼们也武艺高强~~!",3000);
				//helper.prompt("师门巡逻任务失败，请找师傅取消任务。",3000);
			}
			public void battleBreak(BattleEvent e) {
			}
		});
		return true;
	}
	
	/**
	 * 创建送信任务
	 * @param sender
	 * @return
	 */
	public Task create_deliver(String sender) {
		Task task = new Task("school","deliver",sender,randomNpc());
		task.setAutoSpark(true);
		this.times ++;
		if(this.times >ROUND) {
			this.rounds ++;
			this.times %= ROUND;
		}
		task.set("times",times);
		task.set("rounds",rounds);
		int level = context.getPlayer().getData().level;
		task.setExp(this.times*780*level);
		task.setMoney(this.times * 1150*level);
		return task;
	}
	
	/**
	 * 创建寻物任务
	 * @param sender
	 * @return
	 */
	public Task create_lookfor(String sender) {
		Task task = new Task("school","lookfor",sender,sender);
		task.set("item",randomItem());
		this.times ++;
		if(this.times >ROUND) {
			this.rounds ++;
			this.times %= ROUND;
		}
		task.set("times",times);
		task.set("rounds",rounds);
		int level = context.getPlayer().getData().level;
		task.setExp(this.times*980*level);
		task.setMoney(this.times * 2150*level);
		
		return task;
	}
	
	/**
	 * 创建巡逻任务
	 * @param sender
	 * @return
	 */
	public Task create_patrol(String sender) {
		Task task = new Task("school","patrol",sender,sender);
		task.setAutoSpark(false);
		//FIXME 根据门派设置巡逻的场景id
		if("镇元大仙".equals(sender)) {
			task.set("sceneId","1146");
		}
		task.set("battle",0);
		
		this.times ++;
		if(this.times >ROUND) {
			this.rounds ++;
			this.times %= ROUND;
		}
		task.set("times",times);
		task.set("rounds",rounds);
		int level = context.getPlayer().getData().level;
		task.setExp(this.times*1080*level);
		task.setMoney(this.times * 1550*level);
				
		return task;
	}
	
	/**生成送信任务的描述
	 * @param task
	 * @return
	 */
	public String desc_deliver(Task task) {
		return "师傅有事与#R"+task.getReceiver()+"#n商议，你将信件送过去。";
	}
	
	public String desc_lookfor(Task task) {
		return "门派建设需要#R"+task.get("item")+"#n，你下山去寻找一个回来。";
	}
	
	public String desc_patrol(Task task) {
		return "近日听闻有异动，你到外面到处走走看看，发现有不轨之徒，教训一下。";
	}
	
	private String[] npclist = {"船夫","配色师","药店掌柜"};//FIXME
	/**
	 * 随机选择一个NPC
	 * @return
	 */
	public String randomNpc() {
		//FIXME 完善随机NPC 
		int index = rand.nextInt(npclist.length);
		return npclist[index];
	}
	
	String[] items = {"四叶花","七叶莲","天青地白","草果","九香虫","水黄莲","紫丹罗","佛手","旋复花","百色花",
	             "香叶","龙须草","灵脂","白玉骨头","鬼切草","曼佗罗花","山药","八角莲叶","人参","月见草"};
	/**
	 * 随机一个物品
	 * @return
	 */
	public String randomItem() {
		return items[rand.nextInt(items.length)];
	}
}
