/*
 * JavaXYQ NPC Scripts
 * home page: http://javaxyq.googlecode.com
 */

package npc;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.javaxyq.action.Actions;
import com.javaxyq.battle.BattleEvent;
import com.javaxyq.battle.BattleListener;
import com.javaxyq.core.ApplicationHelper;
import com.javaxyq.event.PlayerAdapter;
import com.javaxyq.event.PlayerEvent;
import com.javaxyq.model.Option;
import com.javaxyq.model.Task;
import com.javaxyq.widget.Player;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * @author gongdewei
 * @date 2014-03-02 create
 */
public class n1008 extends PlayerAdapter {

	Logger log = LoggerFactory.getLogger(this.getClass());

	private Random rand = new Random();
	
    public void talk(PlayerEvent evt) {
    	System.out.println("talk: "+this.getClass().getName());
    	
    	String chat="身为五庄首席弟子，我一定不负师门使命。我可以帮助你练习武艺，还可以购买门派特特产额！";   
    	Option[] options = new Option[5];
    	options[0] = new Option("练习武艺（初级）","practice","1");
    	options[1] = new Option("练习武艺（中级）","practice","2");
    	options[2] = new Option("练习武艺（高级）","practice","3");
    	options[3] = new Option("购买门派特产","buy");
    	options[4] = new Option("哪也不去","close");
    	
    	Option result = doTalk(evt.getPlayer(),chat,options);
		if(result!=null) {
			if("practice".equals(result.getAction())) {
				practice(evt);
			}else if("buy".equals(result.getAction())) {
				buy(evt);
			}
		}
    	
    	System.out.println("result: "+result);
    }
	
    /**
     * 练武
     * @param evt
     */
    public void practice(PlayerEvent evt) {
    	Task task = new Task();
    	patrol(task);
//    	while(!task.isFinished()) {
//    	}
    }
    
    /**
     * 购买门派特产
     * @param evt
     */
    public void buy(PlayerEvent evt) {
    	
    }
    
	private boolean patrol(final Task task) {
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
		int rounds = task.getInt("battle_rounds");
		helper.prompt("战斗准备，第"+(rounds==0?"一":"二")+"回合开始啦~~!",3000);
		window.addBattleListener(new BattleListener() {
			//战斗胜利处理
			public void battleWin(BattleEvent e) {
				System.out.println("战斗胜利");
				task.add("battle_rounds",1);
				task.add("battle_win",1);
				int exp = player.getData().level*(1000 + rand.nextInt(200))*elfCount/10;
				player.getData().exp += exp;
				helper.prompt("战斗胜利，获得"+exp+"点经验。",3000);
				battleCompelete();
			}
			//战斗失败处理
			public void battleDefeated(BattleEvent e) {
				System.out.println("战斗失败");
				task.add("battle_rounds",1);
				//气血为0的人物恢复一点气血
				if(player.getData().hp ==0) {
					player.getData().hp = 100;
				}
				helper.prompt("想不到我的武艺还差那么一点~~!",3000);
				battleCompelete();
			}
			public void battleTimeout(BattleEvent e) {
			}
			public void battleBreak(BattleEvent e) {
			}
			
			private void battleCompelete() {
				if(task.getInt("battle_rounds") >= 2) {
					task.setFinished(true);
					if(task.getInt("battle_rounds") == task.getInt("battle_win")) {
						helper.prompt("恭喜你大获全胜，实力还不错额。",3000);
					}else {
						helper.prompt("勤加练武，定能百战百胜。",3000);
					}
				}
			}
		});
		return true;
	}

}
