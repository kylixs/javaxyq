/*
 * JavaXYQ Source Code
 * by kylixs
 * at 2010-2-25
 * please visit http://javaxyq.googlecode.com
 * or mail to kylixs@qq.com
 */
package com.javaxyq.core;

import java.awt.Dimension;
import java.awt.DisplayMode;
import java.awt.FontMetrics;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.HeadlessException;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.KeyListener;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelListener;
import java.awt.event.WindowListener;
import java.awt.event.WindowStateListener;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.swing.Action;
import javax.swing.ActionMap;
import javax.swing.ImageIcon;
import javax.swing.InputMap;
import javax.swing.JApplet;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import com.javaxyq.action.Actions;
import com.javaxyq.action.BaseAction;
import com.javaxyq.action.MedicineItemHandler;
import com.javaxyq.battle.BattleCanvas;
import com.javaxyq.battle.BattleListener;
import com.javaxyq.data.XmlDataLoader;
import com.javaxyq.event.ActionEvent;
import com.javaxyq.event.Listener;
import com.javaxyq.io.CacheManager;
import com.javaxyq.model.ItemTypes;
import com.javaxyq.model.Option;
import com.javaxyq.profile.Profile;
import com.javaxyq.profile.ProfileException;
import com.javaxyq.profile.ProfileManager;
import com.javaxyq.task.TaskManager;
import com.javaxyq.ui.Panel;
import com.javaxyq.ui.TalkPanel;
import com.javaxyq.ui.UIHelper;
import com.javaxyq.util.UIUtils;
import com.javaxyq.widget.Cursor;
import com.javaxyq.widget.Player;
import com.javaxyq.widget.TileMap;

/**
 * @author dewitt
 * @date 2010-2-25 create
 */
public class AppletApplication extends JApplet implements Application,GameWindow {

	private static final long serialVersionUID = -3873519830891129285L;

	private DisplayMode displayMode;

    private GameCanvas canvas;

    private GraphicsDevice device;

    private JFrame fullScreenWindow;

	private boolean cursorHided;

	private Cursor gameCursor;

	private UIHelper helper;
	private ActionMap actionMap = null;
	private InputMap inputMap = null;
	private List<Listener> listeners = new ArrayList<Listener>();

	public AppletApplication() throws HeadlessException {
		super();
        helper = new UIHelper(this);
        actionMap = new ActionMap();
        inputMap = new InputMap();
    }
	
	@Override
	public void init() {
		String[][] params = getParameterInfo();
        String[] args = null;
		//getParameter("width");
        
        ApplicationHelper.setApplication(this);
        createContext();
        //initCursor();
        init(context);
        window = this;
        
		dataManager = new DataStore(context);
		itemManager = new ItemManagerImpl(dataManager);
		scriptEngine = DefaultScript.getInstance();
	}
	@Override
	public void destroy() {
		shutdown();
	}
	
	@Override
	public void start() {
		Thread loadingThread = new Thread() {
			public void run() {
				startup();
			}
		};
		loadingThread.setDaemon(true);
		loadingThread.start();
	}
	
	@Override
	public void stop() {
		//shutdown();
	}
	
//======================== GameWindow Implements =============================//
	
	public void initApplet(String[] args) {
		System.out.println();
		System.out.println("-------------------------");
		System.out.println("cache dir: "+CacheManager.getInstance().getCacheBase());
		
		initDisplay(args);
		Dimension preferredSize = new Dimension(displayMode.getWidth(), displayMode.getHeight());
		// loading canvas
		Image img = SpriteFactory.loadImage("/resources/loading/cover.jpg");
		loadingCanvas = new LoadingCanvas(img, displayMode.getWidth(), displayMode.getHeight());
		this.setCanvas(loadingCanvas);
		this.setSize(preferredSize);
		this.setPreferredSize(preferredSize);
		this.invalidate();
		//CacheManager.getInstance().addDownloadListener(loadingCanvas);
		
		//scene canvas
		sceneCanvas = new SceneCanvas(displayMode.getWidth(), displayMode.getHeight());
		
		fontMetrics =this.getFontMetrics(UIUtils.TEXT_NAME_FONT);
		
		URL documentBase = this.getDocumentBase();
		CacheManager.getInstance().setDocumentBase(documentBase);
	}
	
	@Override
	public void init(Context context) {
		context.setWindow(this);
	    this.setFont(UIUtils.TEXT_NAME_FONT);
		hideCursor();
		setGameCursor(Cursor.DEFAULT_CURSOR);
	}
	
    public void hideCursor() {
        // set invisible cursor
    	if(!cursorHided) {
    		cursorHided = true;
	        Image blankImage = new ImageIcon("").getImage();
	        Toolkit toolkit = Toolkit.getDefaultToolkit();
	        java.awt.Cursor blankCursor = toolkit.createCustomCursor(blankImage, new Point(0, 0),
	            "BLANK_CURSOR");
	        setCursor(blankCursor);
    	}
    }
    
    @Override
    public void prepareUI() {
		String[] uiIds = new String[] {"mainwin"};
		for(String id : uiIds) {
			Panel dlg = DialogFactory.getDialog(id, true);
		}
    }
	/**
	 * 
	 */
	public void installUI() {
		String[] uiIds = new String[] {"mainwin"};
		for(String id : uiIds) {
			System.out.println("安装UI："+id);
			Panel dlg = DialogFactory.getDialog(id, true);
			helper.showDialog(dlg);
		}
	}
	
	/**
	 * 
	 */
	public void installListeners() {
		//TODO Canvas切换时，避免重复添加监听器
		for (Listener l : listeners) {
			String strType = l.getType();
			try {
				Object instance = l.getInstance();
				if (canvas instanceof SceneCanvas) {
					SceneCanvas sceneCanvas = (SceneCanvas) canvas;					
					if ("MouseListener".equals(strType)) {
						sceneCanvas.removeMouseListener((MouseListener) instance);
						sceneCanvas.addMouseListener((MouseListener) instance);
					} else if ("MouseMotionListener".equals(strType)) {
						sceneCanvas.removeMouseMotionListener((MouseMotionListener) instance);
						sceneCanvas.addMouseMotionListener((MouseMotionListener) instance);
					} else if ("KeyListener".equals(strType)) {
						sceneCanvas.removeKeyListener((KeyListener) instance);
						sceneCanvas.addKeyListener((KeyListener) instance);
					} else if ("MouseWheelListener".equals(strType)) {
						sceneCanvas.removeMouseWheelListener((MouseWheelListener) instance);
						sceneCanvas.addMouseWheelListener((MouseWheelListener) instance);
					} 
				}
				if ("WindowListener".equals(strType)) {
					this.removeWindowListener((WindowListener) instance);
					this.addWindowListener((WindowListener) instance);
				} else if ("WindowStateListener".equals(strType)) {
					this.removeWindowStateListener((WindowStateListener) instance);
					this.addWindowStateListener((WindowStateListener) instance);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * @param string
	 */
	public void updateLoading(String msg) {
		System.out.println(msg);
		firePropertyChange("loadingText", null, msg);
	}

    public synchronized void setFullScreen() {
        setVisible(false);
        setState(JFrame.ICONIFIED);
        fullScreenWindow = new JFrame(GameMain.getApplicationName());
        fullScreenWindow.setContentPane(canvas.getComponent());
        fullScreenWindow.setUndecorated(true);
        fullScreenWindow.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        fullScreenWindow.setCursor(getCursor());
        device.setFullScreenWindow(fullScreenWindow);
        if (displayMode != null && device.isDisplayChangeSupported()) {
            try {
                device.setDisplayMode(displayMode);
            } catch (IllegalArgumentException ex) {
                ex.printStackTrace();
            }
        }
    }

    public synchronized void restoreScreen() {
        device.setFullScreenWindow(null);
        if (fullScreenWindow != null) {
            fullScreenWindow.dispose();
        }
        setState(JFrame.NORMAL);
        setContentPane(canvas.getComponent());
        setVisible(true);
    }

    public boolean isFullScreen() {
        return device.getFullScreenWindow() != null;
    }

    public GameCanvas getCanvas() {
        return canvas;
    }

	@Override
    public void setCanvas(GameCanvas gameCanvas) {
        this.canvas = gameCanvas;
        //canvas.setCursor(Cursor.DEFAULT_CURSOR);
        canvas.setWindow(this);
        canvas.setGameCursor(getGameCursor());
        JComponent canvasComponent = canvas.getComponent();
        //canvas.getComponent().setInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW, inputMap);
        canvasComponent.setInputMap(JComponent.WHEN_FOCUSED, inputMap);
        canvasComponent.setActionMap(actionMap);
        setContentPane(canvasComponent);
        canvasComponent.requestFocusInWindow();
    }

	@Override
	public Point getMousePosition() throws HeadlessException {
		Point p = super.getMousePosition();
		if(p!=null) {
			SwingUtilities.convertPointToScreen(p, this);
			SwingUtilities.convertPointFromScreen(p, canvas.getComponent());
		}
		return p;
	}
	
	private void initDisplay(String[] args) {
        GraphicsEnvironment environment = GraphicsEnvironment.getLocalGraphicsEnvironment();
        device = environment.getDefaultScreenDevice();
		int width = 640, height = 480;
		if (args!=null && args.length == 3) {
			width = Integer.valueOf(args[0]);
			height = Integer.valueOf(args[1]);
			displayMode = new DisplayMode(width, height, Integer.valueOf(args[2]),
					DisplayMode.REFRESH_RATE_UNKNOWN);
		} else {
			displayMode = new DisplayMode(width, height, 16, DisplayMode.REFRESH_RATE_UNKNOWN);
		}
	}

	@Override
	public UIHelper getHelper() {
		return helper;
	}

	@Override
	public void setGameCursor(String cursor) {
		this.gameCursor = UIHelper.getCursor(cursor);
	}

	@Override
	public Cursor getGameCursor() {
		return this.gameCursor;
	}

	public ActionMap getActionMap() {
		return actionMap;
	}

	public InputMap getInputMap() {
		return inputMap;
	}

	public void addBattleListener(BattleListener listener) {
		if(canvas instanceof BattleCanvas) { 
			((BattleCanvas)canvas).addBattleListener(listener);
		}
	}

	public void removeBattleListener(BattleListener listener) {
		if(canvas instanceof BattleCanvas) { 
			((BattleCanvas)canvas).removeBattleListener(listener);
		}
	}

	@Override
	public int getContentHeight() {
		return getHeight();
	}

	@Override
	public int getContentWidth() {
		return getWidth();
	}
	public void addListener(String type, String className) {
		try {
			Class handler = Class.forName(className);
			listeners.add(new Listener(type, handler));
		} catch (ClassNotFoundException e) {
			System.err.println(e.getMessage());
			//e.printStackTrace();
		}
	}

	@Override
	public void addWindowListener(WindowListener handler) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addWindowStateListener(WindowStateListener handler) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void removeWindowListener(WindowListener handler) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void removeWindowStateListener(WindowStateListener handler) {
		// TODO Auto-generated method stub
		
	}	
//==================== implement of Application ====================//
	private static LoadingCanvas loadingCanvas;

	private static SceneCanvas sceneCanvas;

	private static FontMetrics fontMetrics;
	protected Context context;
	private DataManager dataManager;
	private ItemManager itemManager;
	private int state = STATE_NORMAL;
	private ScriptEngine scriptEngine;
	private GameWindow window;
	private boolean debug;
	
	@Override
	public void startup() {
		try {
			System.getProperties().store(System.out, "======= System Properties =====");
			System.out.println();
		} catch (Exception e) {
			System.err.println("列出系统属性时发生异常："+e.getMessage());
			//e.printStackTrace();
		}
		initApplet(null);
		
		Image img = SpriteFactory.loadImage("/resources/loading/cover.jpg");
		LoadingCanvas loadingCanvas = new LoadingCanvas(img, window.getContentWidth(), window.getContentHeight());
		window.setCanvas(loadingCanvas);
		
		CacheManager.getInstance().addDownloadListener(loadingCanvas);
		long startTime = System.currentTimeMillis();
		loadingCanvas.setLoading("start loading ...");
		loadingCanvas.playMusic();
		loadingCanvas.fadeIn(200);
		
		
		loadResources();
		
		loadData();
		promptMsg("starting game ...");	
		
		long endTime = System.currentTimeMillis();
		System.out.printf("total cost: %ss\n", (endTime - startTime) / 1000.0);
		
		Player player = context.getPlayer();
		SceneCanvas sceneCanvas = new SceneCanvas(window.getContentWidth(), window.getContentHeight());
		sceneCanvas.setPlayer(player);
		window.setCanvas(sceneCanvas);
		CacheManager.getInstance().removeDownloadListener(loadingCanvas);
		CacheManager.getInstance().addDownloadListener(sceneCanvas);
		loadingCanvas.stopMusic();
		loadingCanvas.dispose();
		window.installUI();
		window.installListeners();
		Point p = player.getSceneLocation();
		sceneCanvas.changeScene(context.getScene(), p.x, p.y);
	}

	protected void loadResources() {
		//setDebug(false);
		//setShowCopyright(false);
		//setApplicationName("JavaXYQ ");
		//setVersion("1.4 M2");
		//setHomeURL("http://javaxyq.googlecode.com/");
		promptMsg("loading resource ...");
		window.setGameCursor(Cursor.DEFAULT_CURSOR);
		
		//showCopyright();
		//promptMsg("loading groovy ...");
		
		promptMsg("loading actions ...");
		XmlDataLoader loader = new XmlDataLoader(window);
		loader.parseActions();
		//promptMsg("loading scenes ...");
		//XmlDataLoader.defScenes();
		//promptMsg("loading talks ...");
		//XmlDataLoader.defTalks();
		promptMsg("loading ui ...");
		loadUIs(loader);
		
		//task
		getTaskManager().register("school", "com.javaxyq.task.SchoolTaskCoolie");
		ApplicationHelper.getApplication().getItemManager().regItem(ItemTypes.TYPE_MEDICINE, new MedicineItemHandler());
	}
	
	private void loadUIs(XmlDataLoader loader) {
		File file = CacheManager.getInstance().getFile("ui/list.txt");
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
			String str = null;
			while((str=br.readLine())!=null) {
				String uifile = "ui/"+str;
				System.out.println("find ui: "+uifile);
				loader.loadUI(uifile);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

	public void shutdown() {
		saveData();
		System.exit(0);
	}
	
	public Context getContext() {
		return context;
	}
	
	public java.net.URL getResource(String name){
		;
		return null;
	}
	public InputStream getResourceAsStream(String name) {
		;
		return null;
	}
	
	protected void createContext() {
		if(context == null) {
			context = new Context();
		}
	}
	
	protected void promptMsg(String text) {
		//TODO
		System.out.println(text);
	}
	
	
	protected void loadData() {
		promptMsg("loading data ...");	
		dataManager.loadData();
	}
	protected void saveData() {
		dataManager.saveData();
	}

	public DataManager getDataManager() {
		return dataManager;
	}

	public ItemManager getItemManager() {
		return itemManager;
	}

	public ScriptEngine getScriptEngine() {
		return scriptEngine;
	}

	/**
	 * 执行指定ActionCommand的Action
	 * 
	 * @param source
	 *            触发Action的源对象
	 * @param cmd动作的actiomCommand
	 *            ,而非类名
	 */
	public void doAction(Object source, String actionId, Object[] args) {
		if(Actions.ENTER_BATTLE.equals(actionId)) {
			enterBattle((List)args[0], (List)args[1]);
			return ;
		}else if(Actions.QUIT_BATTLE.equals(actionId)) {
			quitBattle();
			return ;
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
	 * 
	 * @param npc
	 */
	public void doTalk(Player p,String chat) {
		doTalk(p, chat, null);
	}

	/**
	 * 触发与npc的对话
	 * @param options TODO
	 * @param npc
	 */
	public Option doTalk(Player talker,String chat, Option[] options) {
		context.setTalker(talker);
		TalkPanel dlg0 = (TalkPanel) DialogFactory.getDialog("npctalk", true);
		//make a copy
		TalkPanel dlg = new TalkPanel(dlg0.getWidth(),dlg0.getHeight());
		dlg.setLocation(dlg0.getX(), dlg0.getY());
		dlg.setBgImage(dlg0.getBgImage());
		dlg.setClosable(dlg0.isClosable());
		dlg.setMovable(dlg0.isMovable());
		dlg.setName(dlg0.getName());
		dlg.initTalk(chat,options);
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
	public void enterBattle(List team1,List team2) {
		state = STATE_BATTLE;
		GameWindow window = context.getWindow();
		SceneCanvas canvas = (SceneCanvas) window.getCanvas();
		sceneCanvas = canvas;
		Dimension size = window.getSize();
		//background
		BufferedImage bg = new BufferedImage(size.width, size.height, BufferedImage.TYPE_USHORT_565_RGB);
		TileMap map = canvas.getMap();
		Point viewPosition = canvas.getViewPosition();
		map.draw(bg.getGraphics(), viewPosition.x,viewPosition.y,size.width,size.height);
		
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

	public void playMusic() {
		canvas.playMusic();
	}

	public void stopMusic() {
		canvas.stopMusic();
	}

	@Override
	public GameWindow getWindow() {
		return window;
	}	

	@Override
	public UIHelper getUIHelper() {
		return window.getHelper();
	}

	@Override
	public void enterScene() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getProfileName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void loadProfile(String profileName) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void saveProfile() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public ProfileManager getProfileManager() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TaskManager getTaskManager() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Profile getProfile() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void saveProfileAs(String newname) throws ProfileException {
		// TODO Auto-generated method stub
		
	}
}
