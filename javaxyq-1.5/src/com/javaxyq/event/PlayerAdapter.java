/*
 * JavaXYQ Engine 
 * 
 * javaxyq@2008 all rights. 
 * http://www.javaxyq.com
 */

package com.javaxyq.event;

import java.awt.Point;

import com.javaxyq.core.Application;
import com.javaxyq.core.ApplicationHelper;
import com.javaxyq.core.Context;
import com.javaxyq.core.DataManager;
import com.javaxyq.core.GameWindow;
import com.javaxyq.core.ScriptEngine;
import com.javaxyq.model.Option;
import com.javaxyq.ui.UIHelper;
import com.javaxyq.widget.Player;

/**
 * PlayerListener的适配器
 * 
 * @author 龚德伟
 * @history 2008-6-15 龚德伟 新建
 */
public class PlayerAdapter implements PlayerListener {

	
	protected Context context;
	protected DataManager dataManager;
	protected ScriptEngine scriptEngine;
	protected Application application;
	protected GameWindow window;
	protected UIHelper helper;

	public PlayerAdapter() {
		application = ApplicationHelper.getApplication();
		context = application.getContext();
		dataManager = application.getDataManager();
		scriptEngine = application.getScriptEngine();
		window = context.getWindow();
		helper = window.getHelper();
	}
	
    public void doAction(Object source, String actionId, Object[] args) {
		application.doAction(source, actionId, args);
	}

	public void doAction(Object source, String actionId) {
		application.doAction(source, actionId);
	}

	public Option doTalk(Player talker, String chat, Option[] options) {
		return application.doTalk(talker, chat, options);
	}

	public void doTalk(Player p, String chat) {
		application.doTalk(p, chat);
	}

	public void move(Player player, Point increment) {
    }

    public void stepOver(Player player) {
    }

    public void attack(PlayerEvent evt) {
    }

    public void click(PlayerEvent evt) {
    }

    public void detect(PlayerEvent evt) {
    }

    public void give(PlayerEvent evt) {
    }

    public void talk(PlayerEvent evt) {
    }

	public void walk(PlayerEvent evt) {
	}

}
