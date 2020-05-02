/**
 * 
 */
package com.javaxyq.core;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import com.javaxyq.widget.Player;

/**
 * @author dewitt
 * 
 */
public class MovementManager {

	private static Map<Player, String> movements = new HashMap<Player, String>();
	private static Map<String, ActionListener> actions = new HashMap<String, ActionListener>();
	private static Timer timer = new Timer("MovementTimer", true);

	private MovementManager() {
	}

	public static void put(Player player, String movement, long period) {
		movements.put(player, movement);
		ActionListener mAction = getMovementAction(movement);
		if (mAction != null) {
			timer.schedule(createTask(player, mAction), 100, period);
		}
	}

	private static ActionListener getMovementAction(String movement) {
		return actions.get(movement);
	}

	private static TimerTask createTask(final Player player, final ActionListener mAction) {
		return new TimerTask() {
			@Override
			public void run() {
				mAction.actionPerformed(new ActionEvent(player, ActionEvent.ACTION_PERFORMED,
						player.getSceneX() + " " + player.getSceneY()));
			}
		};
	}

	public static void remove(Player player) {
		movements.remove(player);
	}

	public static void addMovementAction(String movement, ActionListener action) {
		actions.put(movement, action);
	}
}
