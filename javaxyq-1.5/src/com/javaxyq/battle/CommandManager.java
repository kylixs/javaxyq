/*
 * JavaXYQ Source Code
 * by kylixs
 * at 2009-11-11
 * http://javaxyq.googlecode.com
 * kylixs@qq.com
 */
package com.javaxyq.battle;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import com.javaxyq.widget.Player;
/**
 * @author dewitt
 * @date 2009-11-11 create
 */
public class CommandManager  {
	private BattleCanvas canvas;
	private List cmdlist;
	private List playerlist;
	private CommandInterpreter interpretor;
	private BattleCalculator battleCalculator;
	
	public CommandManager(BattleCanvas canvas) {
		this.canvas = canvas;
		this.interpretor = new CommandInterpreter(canvas);
		this.cmdlist = new ArrayList();
		this.battleCalculator = new BattleCalculator();
	}
	

	/**
	 * 回合战斗
	 */
	synchronized public void turnBattle() {
		turnBegin();
		//TODO 计算战斗结果
		Map params = new HashMap();//影响因素
		List<Player> t1 = canvas.getAdversaryTeam();
		List<Player> t2 = canvas.getOwnsideTeam();
		List<Command> results = battleCalculator.calc(cmdlist,t2,t1,params);
		//依次执行指令
		for (int i = 0; i < results.size(); i++) {
			Command cmd = results.get(i);
			try {
				System.out.println("执行："+cmd);
				this.interpretor.exec(cmd);
			}catch(Exception e) {
				System.out.println("战斗指令执行失败！"+cmd);
				e.printStackTrace();
			}
		}
		turnEnd();
	}

	private Random random = new Random();
	/**
	 * 回合开始
	 */
	protected void turnBegin() {
		//生成npc的指令
		List<Player> t1 = canvas.getAdversaryTeam();
		List<Player> t2 = canvas.getOwnsideTeam();
		for (int i = 0; i < t1.size(); i++) {
			Player elf = t1.get(i);
			Player target = t2.get(random.nextInt(t2.size()));
			cmdlist.add( new Command("attack",elf,target));
		}

	}
		/**
	 * 回合结束
	 */
	protected void turnEnd() {
		cmdlist.clear();
		// TODO fireEvent
		List<Player> t1 = canvas.getAdversaryTeam();
		List<Player> t2 = canvas.getOwnsideTeam();
		//如果敌方单位都已死亡，则我方胜利
		boolean win = true;
		for(int i=0;i<t1.size();i++) {
			Player elf = t1.get(i);
			if(elf.getData().hp > 0) {
				win = false;
				break;
			}
		}
		if(win) {
			canvas.fireBattleEvent(new BattleEvent(canvas,BattleEvent.BATTLE_WIN));
			return;
		}
		
		//如果我方单位全部死亡，则战斗失败
		boolean failure = true;
		for(int i=0;i<t2.size();i++) {
			Player player = t2.get(i);
			if(player.getData().hp>0) {
				failure = false;
				break;
			}
		}
		if(failure) {
			//战斗失败
			canvas.fireBattleEvent(new BattleEvent(canvas,BattleEvent.BATTLE_DEFEATED));
			return;
		}
		
		canvas.turnBegin();
	}	
	synchronized public void addCmd(Command cmd) {
		cmdlist.add(cmd);
	}

}
