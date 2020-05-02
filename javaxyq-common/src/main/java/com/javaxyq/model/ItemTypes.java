/*
 * JavaXYQ Source Code
 * by kylixs
 * at 2010-4-14
 * please visit http://javaxyq.googlecode.com
 * or mail to kylixs@qq.com
 */
package com.javaxyq.model;

import java.util.Arrays;
import java.util.List;

/**
 * 游戏物品类型
 * @author gongdewei
 * @date 2010-4-14 create
 */
public class ItemTypes {
	public static final int TYPE_MEDICINE = 0x1000; //药品
	public static final int TYPE_MEDICINE_HP = 0x1001;
	public static final int TYPE_MEDICINE_MP = 0x1002;
	public static final int TYPE_MEDICINE_HPMP = 0x1003;//hp+mp
	public static final int TYPE_MEDICINE_INJURY = 0x1004;//疗伤
	public static final int TYPE_MEDICINE_RESURGENT = 0x1008;//复活
	public static final int TYPE_MEDICINE_SP = 0x1010;//愤怒
	public static final int TYPE_MEDICINE_SOBERUP  = 0x1020;//解酒类异常
	public static final int TYPE_MEDICINE_DETOXIFY  = 0x1040;//解毒
	public static final int TYPE_MEDICINE_BREAKSEAL  = 0x1080;//解除封印
	
	public static final int TYPE_WEAPON = 0x2000; //武器
	public static final int TYPE_WEAPON_SWORD = 0x2001;//剑
	public static final int TYPE_WEAPON_MACHETES = 0x2002;//刀
	public static final int TYPE_WEAPON_HAMMER = 0x2003;//锤
	public static final int TYPE_WEAPON_CLAWTHORN = 0x2014;//爪刺
	public static final int TYPE_WEAPON_LARGEAXE = 0x2005;//斧钺
	public static final int TYPE_WEAPON_FAN = 0x2006;//扇
	public static final int TYPE_WEAPON_SPEAR = 0x2007;//枪矛
	public static final int TYPE_WEAPON_WHIP = 0x2008;//鞭
	public static final int TYPE_WEAPON_WAND = 0x2009;//魔棒
	public static final int TYPE_WEAPON_RIBBON = 0x2010;//飘带
	public static final int TYPE_WEAPON_HOOP = 0x2011;//环圈
	public static final int TYPE_WEAPON_DOUBLEDAGGER = 0x2012;//双短剑
	
	public static final int TYPE_EQUIPMENT = 0x4000; //衣服装备
	public static final int TYPE_EQUIPMENT_HELMET = 0x4001;//头盔
	public static final int TYPE_EQUIPMENT_JEWELRY = 0x4002;//饰物
	public static final int TYPE_EQUIPMENT_ARMOR = 0x4003;//护甲
	public static final int TYPE_EQUIPMENT_BELT = 0x4004;//腰带
	public static final int TYPE_EQUIPMENT_BOOTS = 0x4005;//鞋
	
	private static List<String> weaponTypes = Arrays.asList(new String[] {"剑","刀","锤","爪刺","斧钺","扇","枪矛","鞭","魔棒","飘带","环圈","双剑"});
	
	/**
	 * 获取物品的类型
	 * @param item
	 * @return
	 */
	public static int getType(Item item) {
		return Integer.valueOf(item.getType(),16);
	}
	
	/**
	 * 判断物品的类型
	 * @param item
	 * @param type
	 * @return
	 */
	public static boolean isType(Item item,int type) {
		return (getType(item) & type) == type;
	}

	public static boolean isMedicine(Item item) {
		return (getType(item) & TYPE_MEDICINE)==TYPE_MEDICINE;
	}
	public static boolean isHpMedicine(Item item) {
		return (getType(item) & TYPE_MEDICINE_HP)==TYPE_MEDICINE_HP;
	}
	public static boolean isMpMedicine(Item item) {
		return (getType(item) & TYPE_MEDICINE_MP)==TYPE_MEDICINE_MP;
	}
	
	public static boolean isWeapon(Item item) {
//		return (getType(item) & TYPE_WEAPON)==TYPE_WEAPON;
		return weaponTypes.contains(item.getType());
	}
	public static boolean isEquipment(Item item) {
		return (getType(item) & TYPE_EQUIPMENT)==TYPE_EQUIPMENT;
	}
	
	
}
