/*
 * JavaXYQ Source Code 
 * Cheat Cheat.groovy
 * by kylixs 2009-10
 * All Rights Reserved.
 * Please see also http://javaxyq.cn or http://javaxyq.googlecode.com.
 * Please email to  javaxyq@qq.com.
 */
package com.javaxyq.util;

import java.awt.Point;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;

import com.javaxyq.core.ApplicationHelper;
import com.javaxyq.core.GameCanvas;
import com.javaxyq.core.GameMain;
import com.javaxyq.core.SceneCanvas;
import com.javaxyq.model.PlayerVO;
import com.javaxyq.widget.Player;

/**
 * @author dewitt
 *
 */
public class Cheat {
	
	public static boolean process(String cmdLine) {
		if(!cmdLine.startsWith("$$ ")) {
			return false;
		}
		String[] args = cmdLine.substring(3).split(" ");
		if(args.length < 3) {
			return false;
		}
		System.out.println("cheat: "+cmdLine);
		try {
			String cmd = args[1];
			Player player = ApplicationHelper.getApplication().getContext().getPlayer();
			PlayerVO playerdata = player.getData();
			Map<String, Object> data = BeanUtils.describe(playerdata);
			Map<String, Object> newdata = new HashMap<String, Object>();
			if(cmd.equals("=")) {
				Object value = args[2];
				if(args[0].equals("xy")) {
					args[0]="sceneLocation";
				}
				if(args[0].equals("sceneLocation")) {
					value = parsePoint(args[2]);
				}
				newdata.put(args[0], value);
			}else if(cmd.equals("+")) {
				int value = (Integer) data.get(args[0]);
				value += Integer.parseInt(args[2]);
				newdata.put(args[0], value);
			}else if(cmd.equals("-")) {
				int value = (Integer) data.get(args[0]);
				value -= Integer.parseInt(args[2]);
				newdata.put(args[0], value);
			}
			BeanUtils.populate(playerdata, newdata);
			player.setData(playerdata);
			SceneCanvas canvas = (SceneCanvas) ApplicationHelper.getApplication().getCanvas();
			canvas.setPlayerSceneLocation(playerdata.getSceneLocation());
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	private static Point parsePoint(String str) {
		String[] xy = str.split(",");
		Point p = new Point(Integer.parseInt(xy[0]), Integer.parseInt(xy[1]));
		return p;
	}
}
