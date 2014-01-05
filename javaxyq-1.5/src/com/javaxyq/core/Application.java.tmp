package com.javaxyq.core;

import java.io.InputStream;

import com.javaxyq.model.Option;
import com.javaxyq.profile.Profile;
import com.javaxyq.profile.ProfileException;
import com.javaxyq.profile.ProfileManager;
import com.javaxyq.task.TaskManager;
import com.javaxyq.ui.UIHelper;
import com.javaxyq.widget.Player;

public interface Application {

	public static final float NORMAL_SPEED = 0.12f; 
	public static final float BEVEL_SPEED = 0.071f;
//	public static final float NORMAL_SPEED = 0.15f;
//	public static final float BEVEL_SPEED = 0.105f;
	public static final int STEP_DISTANCE = 20;
	public static final int DOUBLE_STEP_DISTANCE = 2 * STEP_DISTANCE;
	/** 冒泡对话显示的时间 (ms) */
	public static final int TIME_CHAT = 15 * 1000;
	/**
	 * 游戏状态
	 */
	public static final int STATE_BATTLE = 0x1;
	public static final int STATE_NORMAL = 0x0;
	/** 冒泡对话保留时间(ms) */
	public static long CHAT_REMAIND_TIME = 15000;

	public abstract void startup();

	public abstract void shutdown();

	public abstract Context getContext();

	public abstract java.net.URL getResource(String name);

	public abstract InputStream getResourceAsStream(String name);

	public abstract DataManager getDataManager();

	public abstract ItemManager getItemManager();

	public TaskManager getTaskManager();
	
	public ProfileManager getProfileManager();
	
	public abstract ScriptEngine getScriptEngine();

	/**
	 * 执行指定ActionCommand的Action
	 * 
	 * @param source
	 *            触发Action的源对象
	 * @param cmd动作的actiomCommand
	 *            ,而非类名
	 */
	public abstract void doAction(Object source, String actionId, Object[] args);

	/**
	 * 触发与npc的对话
	 * 
	 * @param npc
	 */
	public abstract void doTalk(Player p, String chat);

	/**
	 * 触发与npc的对话
	 * @param options
	 * @param npc
	 */
	public abstract Option doTalk(Player talker, String chat, Option[] options);

	public abstract void doAction(Object source, String actionId);

	public abstract boolean isDebug();

	public abstract void setDebug(boolean debug);

	public abstract int getState();

	public abstract void setState(int state);

	void playMusic();
	void stopMusic();
	
	GameWindow getWindow();
	GameCanvas getCanvas();
	UIHelper getUIHelper();
	
	/**
	 * 进入游戏场景
	 */
	void enterScene();
	
	/**
	 * 获取当前游戏存档
	 * @return
	 */
	public Profile getProfile();
	
	/**
	 * 加载存档
	 * @param profileName
	 * @throws ProfileException 
	 */
	void loadProfile(String profileName) throws ProfileException;
	
	/**
	 * 保存存档
	 */
	void saveProfile() throws ProfileException;
	
	/**
	 * 当前游戏存档另存为..
	 * @param newname
	 * @throws ProfileException
	 */
	public void saveProfileAs(String newname) throws ProfileException;
	
	/**
	 * 获取当前游戏存档名称
	 * @return
	 */
	String getProfileName();
}