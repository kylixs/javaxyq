/**
 *
 */
package com.javaxyq.core;

import com.javaxyq.action.Actions;
import com.javaxyq.action.BaseAction;
import com.javaxyq.battle.BattleCanvas;
import com.javaxyq.config.ConfigManager;
import com.javaxyq.config.impl.ConfigManagerImpl;
import com.javaxyq.data.ItemInstance;
import com.javaxyq.data.WeaponItem;
import com.javaxyq.event.ActionEvent;
import com.javaxyq.model.ItemTypes;
import com.javaxyq.model.Option;
import com.javaxyq.model.PlayerVO;
import com.javaxyq.model.Task;
import com.javaxyq.profile.Profile;
import com.javaxyq.profile.ProfileException;
import com.javaxyq.profile.ProfileManager;
import com.javaxyq.profile.impl.ProfileManagerImpl;
import com.javaxyq.task.TaskManager;
import com.javaxyq.ui.*;
import com.javaxyq.util.DBToolkit;
import com.javaxyq.widget.Player;
import com.javaxyq.widget.TileMap;
import lombok.extern.slf4j.Slf4j;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Slf4j
public abstract class BaseApplication implements Application {
    private boolean debug = false;

    protected Context context;
    private DataManager dataManager;
    private ItemManager itemManager;
    private ProfileManager profileManager;
    private TaskManager taskManager;
    private ConfigManager configManager;

    private int state = STATE_NORMAL;
    private ScriptEngine scriptEngine;

    protected SceneCanvas sceneCanvas;
    private GameWindow window;
    private Thread dataLoadingThread;
    private Profile profile;

    public BaseApplication() {
        ApplicationHelper.setApplication(this);
    }

    //TODO 定义启动的几个阶段，基类维护之间的联系
    public final void startup() {
        //设置重绘管理器
        NullRepaintManager.install();
        long startTime = System.currentTimeMillis();
        log.info("starting");
        //初始化DB连接
        new Thread(() -> {
            //DEBUG
            if (debug) {
                DBToolkit.setForceInit(true);
            }
            DBToolkit.prepareDatabase();
        }).start();
        //创建上下文
        context = createContext();
        dataLoadingThread = new Thread(() -> loadData());
        dataLoadingThread.start();
        //创建窗口
        window = createWindow();
        window.installWindowListeners();
        prepareShow();
        log.info("prepareShow");
        show(window);

        prepareStartup();
        log.info("prepareStartup");


        loadResources();
        log.info("loadResources");

        try {
            dataLoadingThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        finish();
        log.info("finish");

        long endTime = System.currentTimeMillis();
        log.info("total cost: {}s", (endTime - startTime) / 1000.0);
    }

    protected void prepareShow() {
        // TODO Auto-generated method stub

    }

    /**
     * 加载完毕，进入游戏
     */
    protected void finish() {
        log.warn("starting game ...");

    }

    protected void prepareStartup() {
        scriptEngine = DefaultScript.getInstance();
        try {
            scriptEngine.clearCache();
        } catch (Exception e) {
            e.printStackTrace();
        }
        //注册Tooltip组件
        JEXLTooltipTemplate tooltipTemplate = new JEXLTooltipTemplate(context);
        UIFactory.put(UIFactory.TOOLTIP_TEMPLATE, tooltipTemplate);

        //NullRepaintManager.install();
    }

    public void shutdown() {
        try {
            saveProfile();
        } catch (ProfileException e) {
            e.printStackTrace();
        }
        System.exit(0);
    }

    protected void show(final GameWindow window) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                window.show();
            }
        });
    }

    public Context getContext() {
        return context;
    }

    protected abstract GameWindow createWindow();

    public java.net.URL getResource(String name) {
        ;
        return null;
    }

    public InputStream getResourceAsStream(String name) {
        ;
        return null;
    }

    protected Context createContext() {
        Context context = new Context();
        return context;
    }

    protected void loadResources() {
        log.warn("loading resources ...");

    }

    protected void loadData() {
        log.warn("loading data ...");
        configManager = new ConfigManagerImpl("javaxyq.properties");
        try {
            configManager.loadConfigs();
        } catch (Exception e) {
            log.error("load configs failure! ", e);
        }
        taskManager = new TaskManager();
        dataManager = new DataStore(context);
//		dataManager.loadData();
        itemManager = new ItemManagerImpl(dataManager);
        profileManager = new ProfileManagerImpl();
    }

    public DataManager getDataManager() {
        return dataManager;
    }

    public ItemManager getItemManager() {
        return itemManager;
    }

    public TaskManager getTaskManager() {
        return taskManager;
    }

    public ProfileManager getProfileManager() {
        return profileManager;
    }

    public ConfigManager getConfigManager() {
        return configManager;
    }

    public ScriptEngine getScriptEngine() {
        return scriptEngine;
    }

    /**
     * 执行指定ActionCommand的Action
     *
     * @param source
     *            触发Action的源对象
     * @param args 动作的actiomCommand
     *            ,而非类名
     */
    public void doAction(Object source, String actionId, Object[] args) {
        if (Actions.ENTER_BATTLE.equals(actionId)) {
            enterBattle((List) args[0], (List) args[1]);
            return;
        } else if (Actions.QUIT_BATTLE.equals(actionId)) {
            quitBattle();
            return;
        }

        ActionMap actionMap = context.getWindow().getActionMap();
        Action action = actionMap.get(actionId);
        if (action == null && actionId.startsWith("com.javaxyq.action.dialog")) {
            action = actionMap.get("com.javaxyq.action.dialog");
        }
        if (action == null) {
            String wildcard = actionId.substring(0, actionId.lastIndexOf('.')) + ".*";
            action = actionMap.get(wildcard);
        }
        if (action == null) {
            return;
        }
        ActionEvent e = new ActionEvent(source, actionId, args);
        if (action instanceof BaseAction) {
            BaseAction a = (BaseAction) action;
            a.doAction(e);
        } else {
            action.actionPerformed(e);
        }
    }

    /**
     * 触发与npc的对话
     */
    public void doTalk(Player p, String chat) {
        doTalk(p, chat, null);
    }

    /**
     * 触发与npc的对话
     */
    public Option doTalk(Player talker, String chat, Option[] options) {
        context.setTalker(talker);
        TalkPanel dlg0 = (TalkPanel) DialogFactory.getDialog("npctalk", true);
        //make a copy
        TalkPanel dlg = new TalkPanel(dlg0.getWidth(), dlg0.getHeight());
        dlg.setLocation(dlg0.getX(), dlg0.getY());
        dlg.setBgImage(dlg0.getBgImage());
        dlg.setClosable(dlg0.isClosable());
        dlg.setMovable(dlg0.isMovable());
        dlg.setName(dlg0.getName());
        dlg.initTalk(chat, options);
        dlg.setTalker(talker.getCharacter());
        context.getWindow().getHelper().showModalDialog(dlg);
        return dlg.getResult();
    }

    public void doAction(Object source, String actionId) {
        doAction(source, actionId, null);
    }

    /**
     * 进入战斗模式
     */
    public void enterBattle(List team1, List team2) {
        state = STATE_BATTLE;
        GameWindow window = context.getWindow();
        SceneCanvas canvas = (SceneCanvas) window.getCanvas();
        sceneCanvas = canvas;
        //background
        Dimension size = canvas.getSize();
        BufferedImage bg = new BufferedImage(size.width, size.height, BufferedImage.TYPE_USHORT_565_RGB);
        TileMap map = canvas.getMap();
        Point viewPosition = canvas.getViewPosition();
        map.draw(bg.getGraphics(), viewPosition.x, viewPosition.y, size.width, size.height);

        BattleCanvas battleCanvas = new BattleCanvas(size.width, size.height);
        battleCanvas.setBattleBackground(bg);
        battleCanvas.setOwnsideTeam(team1);
        battleCanvas.setAdversaryTeam(team2);
        battleCanvas.fadeIn(500);
        window.setCanvas(battleCanvas);
        //installUI();
        //UIHelper.showUIComponents();
        //installListener();

        battleCanvas.init();
        //battleCanvas.setLastMagic(lastMagic);
        battleCanvas.playMusic();
    }

    /**
     * 退出战斗模式
     */
    public void quitBattle() {
        SceneCanvas canvas = this.sceneCanvas;
        GameWindow window = context.getWindow();
        canvas.setPlayerSceneLocation(context.getPlayer().getSceneLocation());
        canvas.fadeIn(500);
        window.setCanvas(canvas);
        canvas.playMusic();
        //lastMagic = UIHelper.getBattleCanvas().getLastMagic();
        //UIHelper.getBattleCanvas().dispose();
        state = STATE_NORMAL;
        //window.updateUI();
        //TODO
    }

    public boolean isDebug() {
        return debug;
    }

    public void setDebug(boolean debug) {
        this.debug = debug;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public GameWindow getWindow() {
        return window;
    }

    @Override
    public GameCanvas getCanvas() {
        return window.getCanvas();
    }

    @Override
    public UIHelper getUIHelper() {
        return window.getHelper();
    }

    /**
     * 加载存档
     * @throws ProfileException
     */
    public void loadProfile(String profileName) throws ProfileException {
        if (profileName == null) {
            throw new IllegalArgumentException("profileName 不能为空");
        }
        Profile profile = profileManager.loadProfile(profileName);
        //初始化
        Player player = dataManager.createPlayer(profile.getPlayerData());
        context.setPlayer(player);
        context.setScene(profile.getSceneId());
        dataManager.setItems(player, profile.getItems());

        //装上武器
        ItemInstance[] items = dataManager.getItems(context.getPlayer());
        ItemInstance item = items[2];
        if (item != null) {
            if (ItemTypes.isWeapon(item.getItem())) {
                player.takeupWeapon((WeaponItem) item.getItem());
                log.info("takeup weapon: " + item.getItem());
            }
        }

        Task[] tasks = profile.getTasks();
        if (tasks != null) {
            for (int i = 0; i < tasks.length; i++) {
                getTaskManager().add(tasks[i]);
            }
        }
        this.profile = profile;
        try {
            configManager.put("lastprofile", profile.getName());
            configManager.saveConfigs();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 保存存档
     * @throws ProfileException
     */
    public void saveProfile() throws ProfileException {
        try {
            Player player = context.getPlayer();
            if (player == null) {
                return;
            }
            PlayerVO data = player.getData();

            profile.setPlayerData(data);
            profile.setCreateDate(new java.util.Date());
            profile.setItems(dataManager.getItems(player));
            profile.setSceneId(context.getScene());
            List<Task> tasklist = getTaskManager().getTaskList();
            Task[] tasks = new Task[tasklist.size()];
            tasklist.toArray(tasks);
            profile.setTasks(tasks);
            profileManager.saveProfile(profile);
        } catch (Exception e) {
            System.err.println("游戏存档失败！");
            e.printStackTrace();
        }
    }

    public void saveProfileAs(String newName) throws ProfileException {
        profile = new Profile();
        profile.setName(newName);
        saveProfile();
    }

    public Profile getProfile() {
        return profile;
    }

    /**
     * 获取当前游戏存档名称
     * @return
     */
    public String getProfileName() {
        if (profile != null) {
            return profile.getName();
        }
        return null;
    }

    @Override
    public void endGame() {
        try {
            saveProfile();
        } catch (Exception e) {
            e.printStackTrace();
        }

        //返回到欢迎界面
    }

    @Override
    public SceneCanvas getSceneCanvas() {
        return sceneCanvas;
    }
}
