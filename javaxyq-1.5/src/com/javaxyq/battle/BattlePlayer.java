/**
 * 
 */
package com.javaxyq.battle;

import java.awt.Point;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import com.javaxyq.core.ApplicationHelper;
import com.javaxyq.core.BaseApplication;
import com.javaxyq.core.DataManager;
import com.javaxyq.core.ItemManager;
import com.javaxyq.core.SpriteFactory;
import com.javaxyq.data.ItemInstance;
import com.javaxyq.widget.Player;
import com.javaxyq.widget.Sprite;


/**
 *
 * 战斗播放器
 * @author gongdewei
 * @history 2010-5-27 create
 */
public class BattlePlayer {
	private BattleCanvas canvas;
	private DataManager dataManager;
	private ItemManager itemManager; 
	
	public BattlePlayer(BattleCanvas canvas) {
		this.canvas = canvas;
		dataManager = ApplicationHelper.getApplication().getDataManager();
		itemManager = ApplicationHelper.getApplication().getItemManager();
	}
	
	public boolean exec(Command cmd) {
		try {
			this.invokeMethod (cmd.getCmd(), cmd);
			return true;
		} catch (Exception e) {
			System.out.println("解析指令失败！");
			e.printStackTrace();
		}
		return false;
	}
	
	/**
	 * 物理攻击
	 * @param cmd
	 */
	public void attack(Command cmd) {
		Player source = cmd.getSource();
		Player target = cmd.getTarget();
		String oldState = target.getState();
		boolean hit = cmd.getBool("hit");
		int hitpoints = cmd.getInt("hitpoints");
		
		Point p0 = source.getLocation();
		setMsg(source.getName() + " 进行攻击 ");
		Sprite s = SpriteFactory.loadSprite("shape/char/"+source.getCharacter()+"/attack.tcp");
		int dx = s.getWidth()-s.getRefPixelX();
		int dy = s.getHeight()-s.getRefPixelY();
		if(target.getX() > source.getX()) {
			dx = -dx;
			dy = -dy;
		}
		rushForward(source, target.getX() + dx, target.getY() +dy);
		source.playOnce("attack");
		delay(300);
		if(hit) {
		}else {
		}
		rushBack(source, p0.x, p0.y);
		source.setState("stand");
		//target.waitFor();
		if(target.getData().hp <=0) {
		}else {
			target.setState(oldState);
		}
		setMsg("");
		hidePoints(target);
	}
	
	/**
	 * 攻击停留
	 * @param cmd
	 */
	public void attackstay(Command cmd) {
		
	}
	
	/**
	 * 施放法术
	 * @param cmd
	 */
	public void castspell(Command cmd) {
		Player source = cmd.getSource();
		Player target = cmd.getTarget();
		String oldState = target.getState();
		boolean hit = cmd.getBool("hit");
		int hitpoints = cmd.getInt("hitpoints");
		int usemp = cmd.getInt("mp");
		String magicId = (String) cmd.get("magic");
		setMsg(source.getName()+" 施法法术 — "+magicId);
		//消耗魔法
		source.getData().mp -= usemp;
		//effect
		source.playOnce("magic");
		delay(100);
		target.playEffect(magicId, true);
		delay(100);
		if(hit) {
			System.out.printf("%s击中%s，伤害%s点\n",source.getName(),target.getName(),hitpoints);
			target.playOnce("hit");
			//target.playEffect("hit");
			showPoints(target, -hitpoints);
			target.getData().hp -= hitpoints;
			source.waitFor();
			source.setState("stand");
		}else {
			System.out.printf("%s 抵御了%s的法术攻击\n",target.getName(),source.getName());
		}
		target.waitForEffect(null);//等待法术施法完毕
		if(target.getData().hp <=0) {
			//TODO 改善死亡的计算
			//target.playOnce("die");
			target.getData().hp = 0;
			System.out.printf("%s招架不住，倒在战场上。\n",target.getName());
			canvas.cleanPlayer(target);
		}else {
			target.setState(oldState);
		}
		setMsg("");
		hidePoints(target);
	}

	/**
	 * 受到伤害
	 * @param cmd
	 */
	public void damage(Command cmd) {
		setMsg(String.format("%s摆起防御招式",cmd.getSource().getName()));
		//cmd.source.playOnce("defend");
		delay(300);
	}
	
	/**
	 * 受伤并倒下
	 * @param cmd
	 */
	public void downdamage(Command cmd) {
		//target.playOnce("die");
		//target.getData().hp = 0;
		//System.out.printf("%s招架不住，倒在战场上。\n",target.getName());
		
		
	}
	/**
	 * 增益状态开始
	 * @param cmd
	 */
	public void buffbegin(Command cmd) {
		
	}
	/**
	 * 增益状态结束
	 * @param cmd
	 */
	public void buffend(Command cmd) {
		
	}
	/**
	 * 减益状态开始
	 * @param cmd
	 */
	public void debuffbegin(Command cmd) {
		
	}
	/**
	 * 减益状态结束
	 * @param cmd
	 */
	public void debuffend(Command cmd) {
		
	}
	/**
	 * 保护
	 * @param cmd
	 */
	public void protect(Command cmd) {
		
	}
	/**
	 * 伤害
	 * @param cmd
	 */
	public void hurt(Command cmd) {
		
	}
	
	/**
	 * 躲避
	 * @param cmd
	 */
	public void dodge(Command cmd) {
		backward(cmd.getSource());
		//System.out.printf("%s 躲开了%s的攻击\n",target.getName(),source.getName());
		
	}
	
	/**
	 * 使用药品
	 * @param cmd
	 */
	public void medicine(Command cmd) {
		Player source = cmd.getSource();
		Player target = cmd.getTarget();
		String oldState = target.getState();
		
		//effect
		source.playOnce("magic");
		delay(100);
		ItemInstance item = (ItemInstance) cmd.get("item");
		itemManager.useItem(source, item);
		setMsg(String.format("%s 使用了一个%s",source.getName(),item.getName()));
		int hpval = cmd.getInt("hp");
		if(hpval > 0) {
			showPoints(target, hpval);
			System.out.printf("%s 恢复了%s点气血\n",target.getName(),hpval);
		}
		source.waitFor();
		source.setState("stand");
		delay(300);
		setMsg("");
		hidePoints(target);		
	}
	/**
	 * 治愈
	 * @param cmd
	 */
	public void heal(Command cmd) {
		
	}
	/**
	 * 特技
	 * @param cmd
	 */
	public void teji(Command cmd) {
		
	}
	/**
	 * 法宝
	 * @param cmd
	 */
	public void fabao(Command cmd) {
		
	}
	
	/**
	 * 逃跑失败
	 * @param cmd
	 */
	public void failrun(Command cmd) {
		canvas.runaway(cmd.getSource(),false);
	}
	/**
	 * 逃跑成功
	 * @param cmd
	 */
	public void successrun(Command cmd) {
		canvas.runaway(cmd.getSource(),true);
	}
	/**
	 * 清场
	 * @param cmd
	 */
	public void outstage(Command cmd) {
		canvas.cleanPlayer(cmd.getSource());
	}	
	
	//-----------------------------------------------------------------//
	private void delay(long t) {
		try {
			Thread.sleep(t);
		}catch(Exception e) {}
	}

	private void hidePoints(Player player) {
		canvas.hidePoints(player);
	}

	private void rushBack(Player player, int x, int y) {
		canvas.rushBack(player, x, y);
	}

	private void rushForward(Player player, int x, int y) {
		canvas.rushForward(player, x, y);
	}

	private void setMsg(String text) {
		canvas.setMsg(text);
	}

	private void showPoints(Player player, int value) {
		canvas.showPoints(player, value);
	}
	
	private void backward(Player player) {
		canvas.backward(player);
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
	private Object invokeMethod(String mName, Object arg) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException, SecurityException, NoSuchMethodException {
		Method m = this.getClass().getMethod(mName, arg.getClass());
		return m.invoke(this, arg);
	}
	
}
