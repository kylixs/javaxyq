package com.javaxyq;

import com.javaxyq.action.MedicineItemHandler;
import com.javaxyq.action.WeaponItemHandler;
import com.javaxyq.core.*;
import com.javaxyq.core.Canvas;
import com.javaxyq.data.XmlDataLoader;
import com.javaxyq.io.CacheManager;
import com.javaxyq.menu.MainMenuCanvas;
import com.javaxyq.model.ItemTypes;
import com.javaxyq.profile.ProfileException;
import com.javaxyq.ui.Panel;
import com.javaxyq.util.SuffixFilenameFilter;
import com.javaxyq.widget.Cursor;
import com.javaxyq.widget.Player;
import lombok.extern.slf4j.Slf4j;

import java.awt.*;
import java.io.File;

/**
 * 桌面游戏客户端程序
 *
 * @author gongdewei
 * @date 2010
 */
@Slf4j
public class DesktopApplication extends BaseApplication {

    public static void main(String[] args) {
        DesktopApplication app = new DesktopApplication();
        app.startup();
    }

    private GameWindow window;

    protected GameWindow createWindow() {
        window = new DesktopWindow();
        window.init(context);
        return window;
    }

    @Override
    protected void prepareShow() {
        super.prepareShow();
        Image img = SpriteFactory.loadImage("resources/loading/cover.jpg");
        LoadingCanvas loadingCanvas = new LoadingCanvas(img, window.getContentWidth(), window.getContentHeight());
        loadingCanvas.setLoading("loading ...");
        window.setCanvas(loadingCanvas);
        loadingCanvas.fadeIn(100);
        loadingCanvas.playMusic();
        CacheManager.getInstance().addDownloadListener(loadingCanvas);
    }

    @Override
    protected void loadData() {
        super.loadData();
    }

    protected void loadScene() {
        log.info("loading scene");
        Player player = context.getPlayer();
        Point p = player.getSceneLocation();
        if (sceneCanvas == null) {
            sceneCanvas = new SceneCanvas(window.getContentWidth(), window.getContentHeight());
        }
        sceneCanvas.setPlayer(player);
        sceneCanvas.changeScene(context.getScene(), p.x, p.y);
        log.info("loaded scene");
    }

    @Override
    protected void loadResources() {

        //setDebug(false);
        //setShowCopyright(false);
        //setApplicationName("JavaXYQ ");
        //setVersion("1.4 M2");
        //setHomeURL("http://javaxyq.googlecode.com/");
        log.info("loading resource ...");
        window.setGameCursor(Cursor.DEFAULT_CURSOR);

        //showCopyright();
        //promptMsg("loading groovy ...");

        log.info("loading actions ...");
        XmlDataLoader loader = new XmlDataLoader(window);
        loader.parseActions();
        //promptMsg("loading scenes ...");
        //XmlDataLoader.defScenes();
        //promptMsg("loading talks ...");
        //XmlDataLoader.defTalks();
        log.info("loading ui ...");
        loadUIs(loader);

        //task
        getTaskManager().register("school", "com.javaxyq.task.SchoolTaskCoolie");
        ApplicationHelper.getApplication().getItemManager().regItem(ItemTypes.TYPE_MEDICINE, new MedicineItemHandler());
        ApplicationHelper.getApplication().getItemManager().regItem(ItemTypes.TYPE_WEAPON, new WeaponItemHandler());

    }

    @Override
    protected void finish() {
        super.finish();
        showMainMenuCanvas();

        //后台预加载场景
        //preloadLastProfile();
    }

    public void showMainMenuCanvas() {
        Image img = SpriteFactory.loadImage("/wzife/login/background.jpg");
        MainMenuCanvas menuCanvas = new MainMenuCanvas(img, window.getContentWidth(), window.getContentHeight());
        Panel mainmenu = getUIHelper().getDialog("mainmenu");

        com.javaxyq.core.Canvas currentCanvas = (Canvas) window.getCanvas();
        //currentCanvas.stopMusic();
        currentCanvas.dispose();
        CacheManager.getInstance().removeDownloadListener(currentCanvas);

        //切换到主菜单
        window.setCanvas(menuCanvas);
        getUIHelper().showDialog(mainmenu);
        menuCanvas.playMusic();
        CacheManager.getInstance().addDownloadListener(menuCanvas);
    }

    private void preloadLastProfile() {
        new Thread(new Runnable() {
            public void run() {
                try {
                    //读取最近的存档
                    String profileName = getConfigManager().get("lastprofile");
                    if (profileName != null) {
                        loadProfile(profileName);
                        loadScene();
                    }
                } catch (ProfileException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    /**
     * 进入游戏
     */
    public void enterScene() {
        GameCanvas canvas = window.getCanvas();
        if (canvas != sceneCanvas) {
            //canvas.stopMusic();
            loadScene();
            window.setCanvas(sceneCanvas);
            window.installUI();
            window.installListeners();
            sceneCanvas.playMusic();
            CacheManager.getInstance().removeAllDownloadListeners();
            CacheManager.getInstance().addDownloadListener(sceneCanvas);
        }
    }

    private void loadUIs(XmlDataLoader loader) {
        File dir = CacheManager.getInstance().getFile("ui");
        String[] files = dir.list(new SuffixFilenameFilter(".xml"));
        for (String file : files) {
            loader.loadUI("ui/" + file);
        }
        window.prepareUI();

    }

    public void playMusic() {
        window.getCanvas().playMusic();
    }

    public void stopMusic() {
        window.getCanvas().stopMusic();
    }

    @Override
    public void endGame() {
        super.endGame();
        if (sceneCanvas != null) {
            sceneCanvas.dispose();
            sceneCanvas = null;
        }
        showMainMenuCanvas();
    }
}
