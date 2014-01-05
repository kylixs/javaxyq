/**
 * 
 */
package com.javaxyq.core;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import com.javaxyq.model.PlayerVO;

/**
 * @author dewitt
 *
 */
public class PlayerPropertyCalculator {
	
	private static String[] 人族门派 = { "大唐官府", "方寸山" ,"化生寺", "女儿村"};
	private static String[] 魔族门派 = {  "阴曹地府", "魔王寨", "狮驼岭", "盘丝洞 "};
	private static String[] 仙族门派 = {"天宫" ,"龙宫", "五庄观", "普陀山"};

	private static String[] 人族 = {"0001","0002","0003","0004"};
	private static String[] 魔族 = {"0005","0006","0007","0008"};
	private static String[] 仙族 = {"0000","0010","0011","0012"};
	/**
	 * 判断数组array是否包含值value
	 * @return
	 */
	private static boolean inArray(String[] array, String value) {
		for (int i = 0; i < array.length; i++) {
			if(array[i].equals(value))return true;
		}
		return false;
	}
	                            
	public static int calc_速度(PlayerVO attrs) {
		return (int) (attrs.physique*0.1 + attrs.durability*0.1 + attrs.strength*0.1 + attrs.agility*0.7 + attrs.magic*0);
	}

	public static int calc_灵力(PlayerVO attrs) {
		return (int) (attrs.physique*0.3 + attrs.magic*0.7 + attrs.durability*0.2 + attrs.strength*0.4 + attrs.agility*0);
	}

	public static int calc_躲避(PlayerVO attrs) {
		//FIXME 躲避计算公式？
		return attrs.agility*1 + 10;
	}
	public static int calc_stamina(PlayerVO attrs) {
		return attrs.level*10 + 50;
	}
	public static int calc_energy(PlayerVO attrs) {
		return attrs.level*10 + 50;
	}

	public static int calc_命中(PlayerVO attrs) {
		if(inArray(人族,attrs.character)) {			
			return attrs.strength*2+30;
		}else if(inArray(魔族,attrs.character)) {
			return (int) (attrs.strength*2.3+27);
		}else if(inArray(仙族,attrs.character)) {
			return (int) (attrs.strength*1.7+30);
		}
		return -1;
	}


	public static int calc_伤害(PlayerVO attrs) {
		if(inArray(人族,attrs.character)) {			
			return (int) (attrs.strength*0.7+34) ;
		}else if(inArray(魔族,attrs.character)) {
			return (int) (attrs.strength*0.8+34) ;
		}else if(inArray(仙族,attrs.character)) {
			return (int) (attrs.strength*0.6+40) ;
		}
		return -1;		
	}

	public static int calc_防御(PlayerVO attrs) {
		if(inArray(人族,attrs.character)) {			
			return (int) (attrs.durability*1.5) ;
		}else if(inArray(魔族,attrs.character)) {
			return (int) (attrs.durability*1.3) ;
		}else if(inArray(仙族,attrs.character)) {
			return (int) (attrs.durability*1.6) ;
		}
		return -1;
	}

	public static int calc_气血(PlayerVO attrs) {
		if(inArray(人族,attrs.character)) {			
			return attrs.physique*5 + 100 ;
		}else if(inArray(魔族,attrs.character)) {
			return attrs.physique*6 + 100 ;
		}else if(inArray(仙族,attrs.character)) {
			return (int) (attrs.physique*4.5 + 100) ;
		}
		return -1;
	}
	
	public static int calc_魔法(PlayerVO attrs) {
		if(inArray(人族,attrs.character)) {			
			return attrs.magic*3+80 ;
		}else if(inArray(魔族,attrs.character)) {
			return (int) (attrs.magic*2.5+80) ;
		}else if(inArray(仙族,attrs.character)) {
			return (int) (attrs.magic*3.5+80) ;
		}
		return -1;
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
	public static Object invokeMethod(String mName, Object arg) {
		try {
			Method m = PlayerPropertyCalculator.class.getMethod(mName, arg.getClass());
			return m.invoke(PlayerPropertyCalculator.class, arg);
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
		return null;
	}
		
}

/** 
	 1、体质：对于生命值的多少有很大影响，增加体质点数可以提高气血上限，并增加些许灵力。
	 2、魔力：法力高低的代表，增加魔力点数可以增加魔法上限，并提高灵力。
	 3、力量：增加力量点数可以提高命中和伤害，并增加些许灵力。
	 4、耐力：对物理攻击的抵抗能力，增加耐力点数可以提高物理防御，并略微增加灵力。
	 5、敏捷：增加敏捷点数可以提高躲避和速度，从而更容易掌握战斗的先机。
	 
	 (一)各种族初始资料
	 1)魔族
	 体12-魔力11-力量11-耐力8-敏8
	 血172-魔法值107-命中55-伤害43-防御11-速度8-躲闪18-灵力17
	 2)人族
	 体10-魔力10-力量10-耐力10-敏10
	 血150-魔法值110-命中50-伤害41-防御15-速度10-躲闪30-灵力16
	 3)仙族
	 体12-魔力5-力量11-耐力12-敏10
	 血154-魔法值97-命中48-伤害46-防御19-速度10-躲闪20-灵力13
	 
	 (二)人物属性关系比
	 速度=体质*0.1+耐力*0.1+力量*0.1+敏捷*0.7+魔力*0（魔力不加速度）
	 灵力=体质*0.3+魔力*0.7+耐力*0.2+力量*0.4+敏捷*0（敏捷不加灵力）
	 躲避=敏捷*1
	 命中：
	 人：力量*2+30
	 魔：力量*2.3+27
	 仙：力量*1.7+30
	 伤害：
	 人：力量*0.7+34
	 魔：力量*0.8+34
	 仙：力量*0.6+40
	 防御：
	 人：耐力*1.5
	 魔：耐力*1.3
	 仙：耐力*1.6
	 气血：
	 人：体质*5+100
	 魔：体质*6+100
	 仙：体质*4.5+100
	 魔法：
	 人：魔力*3+80
	 魔：魔力*2.5+80
	 仙：魔力*3.5+80 
 **/
