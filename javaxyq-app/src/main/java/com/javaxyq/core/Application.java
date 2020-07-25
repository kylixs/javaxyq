package com.javaxyq.core;

import com.javaxyq.model.Option;
import com.javaxyq.profile.Profile;
import com.javaxyq.profile.ProfileException;
import com.javaxyq.profile.ProfileManager;
import com.javaxyq.task.TaskManager;
import com.javaxyq.ui.UIHelper;
import com.javaxyq.widget.Player;

public interface Application {

    float NORMAL_SPEED = 0.12f;
    float BEVEL_SPEED = 0.071f;
    int STEP_DISTANCE = 20;
    int DOUBLE_STEP_DISTANCE = 2 * STEP_DISTANCE;
    /**
     * 冒泡对话显示的时间 (ms)
     */
    int CHAT_MS = 15 * 1000;
    /**
     * 游戏状态
     */
    int STATE_BATTLE = 0x1;
    int STATE_NORMAL = 0x0;
    /**
     * 冒泡对话保留时间(ms)
     */
    long CHAT_REMIND_MS = 15000;

    void startup();

    void shutdown();

    Context getContext();

    DataManager getDataManager();

    ItemManager getItemManager();

    TaskManager getTaskManager();

    ProfileManager getProfileManager();

    ScriptEngine getScriptEngine();

    /**
     * 执行指定ActionCommand的Action
     */
    void doAction(Object source, String actionId, Object[] args);

    /**
     * 触发与npc的对话
     */
    void doTalk(Player p, String chat);

    /**
     * 触发与npc的对话
     */
    Option doTalk(Player talker, String chat, Option[] options);

    void doAction(Object source, String actionId);

    boolean isDebug();

    void setDebug(boolean debug);

    int getState();

    void setState(int state);

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
     */
    Profile getProfile();

    /**
     * 加载存档
     */
    void loadProfile(String profileName) throws ProfileException;

    /**
     * 保存存档
     */
    void saveProfile() throws ProfileException;

    /**
     * 当前游戏存档另存为..
     */
    void saveProfileAs(String newName) throws ProfileException;

    /**
     * 获取当前游戏存档名称
     */
    String getProfileName();

    /**
     * 结束游戏
     */
    void endGame();

    SceneCanvas getSceneCanvas();
}