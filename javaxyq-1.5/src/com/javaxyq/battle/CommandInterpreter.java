/**
 * 
 */
package com.javaxyq.battle;

import java.awt.Point;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import com.javaxyq.core.ApplicationHelper;
import com.javaxyq.core.ItemManager;
import com.javaxyq.core.SpriteFactory;
import com.javaxyq.data.ItemInstance;
import com.javaxyq.widget.Player;
import com.javaxyq.widget.Sprite;


/**
 *
 * 战斗指令解释器
 * @author dewitt
 */
public class CommandInterpreter {
	private BattleCanvas canvas;
	private ItemManager itemManager; 
	
	public CommandInterpreter(BattleCanvas canvas) {
		this.canvas = canvas;
		itemManager = ApplicationHelper.getApplication().getItemManager();
	}
	
	public boolean exec(Command cmd) {
		try {
			this.invokeMethod (cmd.getCmd(), cmd);
			return true;
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
		return false;
	}
	
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
			System.out.printf("%s 击中 %s，伤害%s点\n",source.getName(),target.getName(),hitpoints);
			showPoints(target, -hitpoints);
			target.getData().hp -= hitpoints;
			if(cmd.get("defend")!=null) {
				target.playOnce("defend");
				target.playEffect("defend", true);
			}else {
				target.playOnce("hit");
				target.playEffect("hit", true);
			}
			source.waitFor();
		}else {
			backward(target);
			System.out.printf("%s 躲开了%s的攻击\n",target.getName(),source.getName());
		}
		rushBack(source, p0.x, p0.y);
		source.setState("stand");
		//target.waitFor();
		if(target.getData().hp <=0) {
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
	
	public void magic(Command cmd) {
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

	public void defend(Command cmd) {
		setMsg(String.format("%s摆起防御招式",cmd.getSource().getName()));
		//cmd.source.playOnce("defend");
		delay(300);
	}
	
	public void runaway(Command cmd) {
		boolean success = cmd.getBool("runaway");
		canvas.runaway(cmd.getSource(),success);
	}
	
	public void item(Command cmd) {
		Player source = cmd.getSource();
		Player target = cmd.getTarget();
		String oldState = target.getState();
		
		//effect
		source.playOnce("magic");
		delay(100);
		ItemInstance item = (ItemInstance) cmd.get("item");
		setMsg(String.format("%s 使用了一个%s",source.getName(),item.getName()));
		itemManager.useItem(target, item);
//		ItemListener handler = itemManager.findItemAction(item.getItem());
//		if(handler!=null) {
//			handler.itemUsed(new ItemEvent(target,item,""));
//		}
//		if(item.getAmount() <= 0) {//如果消耗完，则销毁物品
//			dataManager.removePlayerItem(source,item);
//		}
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
	
	private void delay(long t) {
		try {
			Thread.sleep(t);
		}catch(Exception e) {}
	}

	public void hidePoints(Player player) {
		canvas.hidePoints(player);
	}

	public void rushBack(Player player, int x, int y) {
		canvas.rushBack(player, x, y);
	}

	public void rushForward(Player player, int x, int y) {
		canvas.rushForward(player, x, y);
	}

	public void setMsg(String text) {
		canvas.setMsg(text);
	}

	public void showPoints(Player player, int value) {
		canvas.showPoints(player, value);
	}
	
	public void backward(Player player) {
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
