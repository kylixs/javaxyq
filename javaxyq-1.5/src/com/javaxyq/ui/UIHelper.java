/**
 * 
 */
package com.javaxyq.ui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JComponent;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.BorderUIResource;
import javax.swing.plaf.ColorUIResource;
import javax.swing.plaf.FontUIResource;

import com.javaxyq.core.DefaultScript;
import com.javaxyq.core.DialogFactory;
import com.javaxyq.core.GameCanvas;
import com.javaxyq.core.GameWindow;
import com.javaxyq.core.ScriptEngine;
import com.javaxyq.event.Conditional;
import com.javaxyq.event.EventDispatcher;
import com.javaxyq.event.PanelEvent;
import com.javaxyq.event.PanelListener;
import com.javaxyq.util.UIUtils;
import com.javaxyq.widget.Animation;
import com.javaxyq.widget.Cursor;

/**
 * 游戏UI帮助类
 * @author dewitt
 */
public class UIHelper {
	/**
	 * 初始化UI参数
	 */
	static {
		LightweightToolTipManager.sharedInstance().setInitialDelay(100);
		UIManager.put("ToolTip.border", new BorderUIResource(new CompoundBorder(
				new RoundLineBorder(Color.WHITE,1, 8, 8),new EmptyBorder(3, 3, 3, 3))));
		UIManager.put("ToolTip.foreground", new ColorUIResource(Color.WHITE));
		//		UIManager.put("ToolTip.background", new ColorUIResource(new Color(255,
		//				255, 224)));
		UIManager.put("ToolTip.font", new FontUIResource(UIUtils.TEXT_FONT));
		//UIManager.put("ToolTipUI", "com.javaxyq.ui.TranslucentTooltipUI");
		
		UIManager.put("GameLabelUI", "com.javaxyq.ui.GameLabelUI");
		UIManager.put("GameButtonUI", "com.javaxyq.ui.GameButtonUI");
	}	
	
	public List<PromptLabel> prompts = new ArrayList<PromptLabel>();
	private static Map<String, Cursor>cursors = new HashMap<String, Cursor>();
	
	private boolean debug = false;
	private GameWindow window;
	private ScriptEngine scriptEngine ;
	public UIHelper(GameWindow window) {
		super();
		this.window = window;
		this.scriptEngine = DefaultScript.getInstance();
	}

	public boolean isDebug() {
		return debug;
	}

	public void setDebug(boolean debug) {
		this.debug = debug;
	}

	/**
	 * 弹出提示信息
	 * @param text 提示内容
	 * @param delay 延时关闭时间(ms)
	 */
	public void prompt(String text,long delay) {
		final PromptLabel label = new PromptLabel(text);
		int offset = prompts.size()*15;
		label.setLocation( (640-label.getWidth())/2+offset, 180+offset);
		final Container container = getCanvasComponent();
		container.add(label,0);
		prompts.add(label);
		
		TimerTask task = new TimerTask() {
			@Override
			public void run() {
				container.remove(label);
				prompts.set(prompts.indexOf(label),null);
				boolean empty = true;
				for (int i = 0; i < prompts.size(); i++) {
					if(prompts.get(i)!=null) {
						empty = false;
					}
				}
				if(empty) {
					prompts.clear();
				}
			}
		};
		new Timer().schedule(task, delay);
	}
	
	/**
	 * 显示tooltip
	 * @param c
	 * @param src
	 * @param e
	 */
	public void showToolTip(JComponent c,JComponent src,MouseEvent e) {
		final Container canvas = getCanvasComponent();
		Point p = SwingUtilities.convertPoint(src, src.getToolTipLocation(e), canvas);
		p.translate(20, 30);
		p.x = (p.x+c.getWidth() < canvas.getWidth()-2)?p.x:(canvas.getWidth()-c.getWidth()-2);
		p.y = (p.y+c.getHeight() < canvas.getHeight() - 2)?p.y:(canvas.getHeight()-c.getHeight()-2);
		c.setLocation(p);
		canvas.add(c,0);
	}
	
	/**
	 * 隐藏tooltip
	 * @param c
	 */
	public void hideToolTip(JComponent c) {
		final Container canvas = getCanvasComponent();
		canvas.remove(c);		
	}
	
	/**
	 * 设置跟随鼠标一起移动的对象
	 * @param c
	 * @param offset 与鼠标的相对位置
	 */
	public void setMovingObject(Animation anim,Point offset) {
		GameCanvas canvas = window.getCanvas();
		canvas.setMovingObject(anim, offset);
	}
	
	/**
	 * 删除鼠标随动对象
	 */
	public void removeMovingObject() {
		GameCanvas canvas = window.getCanvas();
		canvas.removeMovingObject();
	}
	
	/**
	 * 查找最近的面板
	 * @param c
	 * @return
	 */
	public Panel findPanel(Component c) {
		for(;c!=null;) {
			if (c instanceof Panel) {
				break;
			}
			c=c.getParent();
		} 
		return (Panel) c;
	}
	/**
	 * 显示或隐藏指定对话框
	 * 
	 * @param autoSwap
	 */
	public void showHideDialog(Panel dialog) {
		if (dialog != null) {
			Container canvas = getCanvasComponent();
			if (dialog.getParent() == canvas) {
				hideDialog(dialog);
			} else {
				showDialog(dialog);
			}
		}
	}
	
	/**
	 * 显示面板
	 * @param dialog
	 */
	public void showDialog(Panel dialog) {
		Container canvas = getCanvasComponent();
		if (dialog != null && dialog.getParent() != canvas) {
			//阻塞执行初始化事件
			dialog.handleEvent(new PanelEvent(dialog,"initial"));
			canvas.add(dialog,0);
		}
	}
	public void showModalDialog(final Panel dialog) {
		System.out.println("showModalDialog: "+Thread.currentThread().getName());
		Container canvas = getCanvasComponent();
		if (dialog != null && dialog.getParent() != canvas) {
			PanelListener listener = scriptEngine.loadUIScript(dialog.getName());
			if(listener!=null) {
				dialog.removeAllPanelListeners();
				dialog.addPanelListener(listener);
			}
			//阻塞执行初始化事件
			dialog.handleEvent(new PanelEvent(dialog,"initial"));
			canvas.add(dialog);
			canvas.setComponentZOrder(dialog, 0);
			EventDispatcher.pumpEvents(Thread.currentThread(), new Conditional() {
				@Override
				public boolean evaluate() {
					return (dialog.getParent()!=null && dialog.isVisible());
				}
			});
			//TODO need to interrupt pump after close the panel!
		}
		System.out.println("exit showModalDialog: "+Thread.currentThread().getName());
	}
	
	/**
	 * 隐藏面板
	 * @param dialog
	 */
	public void hideDialog(Panel dialog) {
		if (dialog != null) {
			Container canvas = getCanvasComponent();
			if (dialog.getParent() == canvas) {
				canvas.remove(dialog);
				dialog.fireEvent(new PanelEvent(dialog,"dispose"));
				DialogFactory.dispose(dialog.getName(),dialog);
			}
		}
	}
	
	/**
	 * 显示面板
	 * @param id
	 */
	public void showDialog(String id) {
		Panel dlg = null;
		dlg = getDialog(id);
		if(dlg!=null) {
			showDialog(dlg);
		}else {
			System.err.println("获取Dialog失败： "+id);
		}
	}

	/**
	 * 获取Dialog，如果此Dialog还没有加载则自动加载
	 * @param id
	 * @return
	 */
	public Panel getDialog(String id) {
		Panel dlg;
		if(isDebug()) {//如果是调试状态，每次动态加载
			dlg = DialogFactory.createDialog(id);
			PanelListener listener = scriptEngine.loadUIScript(id);
			if(listener!=null) {
				dlg.removeAllPanelListeners();
				dlg.addPanelListener(listener);
			}
		}else {
			dlg = DialogFactory.getDialog(id,true);
		}
		return dlg;
	}
	
	public void showHideDialog(String id) {
		Panel dlg = DialogFactory.getDialog(id,false);
		System.out.println("showHideDialog: "+id+", "+dlg);
		if(dlg!=null && dlg.isShowing()) {
			hideDialog(dlg);
		}else {
			showDialog(id);
		}
	}
	
	/**
	 * 隐藏面板
	 * @param id
	 */
	public void hideDialog(String id) {
		if(id!=null) {
			Panel dlg = DialogFactory.getDialog(id,false);
			if(dlg!=null)hideDialog(dlg);
		}
	}
	
	/**
	 * 获得当前的talkPanel
	 * @return
	 */
	public TalkPanel getTalkPanel() {
		Component comp = getCanvasComponent().getComponent(0);
		if (comp instanceof TalkPanel) {
			return (TalkPanel) comp;
		}
		return null;
	}

	private Container getCanvasComponent() {
		return window.getCanvas().getComponent();
	}
	
	public static Cursor getCursor(String cursorId) {
		Cursor cursor = cursors.get(cursorId);
		if(cursor==null) {
			boolean effect = Cursor.DEFAULT_CURSOR.equals(cursorId);
			cursor = new Cursor(cursorId, effect);
			cursors.put(cursorId, cursor);
		}
		return cursor;
	}

	/** global action map */
	//private static ActionMap actionMap = new ActionMap();
	//private static InputMap inputMap = new InputMap();
	//private static LoadingCanvas loadingCanvas;
	//private static SceneCanvas sceneCanvas;
	//private static BattleCanvas battleCanvas;
//	private static FontMetrics fontMetrics;
	//private static GameWindow gameWindow;
	//private static DisplayMode displayMode;
	//private static List<Listener> listeners = new ArrayList<Listener>();
	//private static List<Panel> uiComponents = new ArrayList<Panel>();
//	public static void addWindowListener(WindowListener handler) {
//		gameWindow.addWindowListener(handler);
//	}
//	public static void addWindowStateListener(WindowStateListener handler) {
//		gameWindow.addWindowStateListener(handler);
//	}
//	public static void fullScreen() {
//		if (gameWindow.isFullScreen()) {
//			gameWindow.restoreScreen();
//		} else {
//			gameWindow.setFullScreen();
//		}
//	}
//	public static ActionMap getActionMap() {
//		return actionMap;
//	}
//	public static void setInputMap(InputMap inputMap) {
//		UIHelper.inputMap = inputMap;
//	}
//	public static void installUI() {
//		String[] uiIds = new String[] {"mainwin"};
//		for(String id : uiIds) {
//			System.out.println("安装UI："+id);
//			Panel dlg = DialogFactory.getDialog(id, true);
//			addUIComponent(dlg);
//		}
//	}
//	public static InputMap getInputMap() {
//		return inputMap;
//	}
//	public static void addListener(String type, Class handler) {
//		listeners.add(new Listener(type, handler));
//	}
//	public static void installListener() {
//		//TODO Canvas切换时，避免重复添加监听器
//		for (Listener l : listeners) {
//			String strType = l.getType();
//			try {
//				Object instance = l.getInstance();
//				if ("MouseListener".equals(strType)) {
//					sceneCanvas.removeMouseListener((MouseListener) instance);
//					sceneCanvas.addMouseListener((MouseListener) instance);
//				} else if ("MouseMotionListener".equals(strType)) {
//					sceneCanvas.removeMouseMotionListener((MouseMotionListener) instance);
//					sceneCanvas.addMouseMotionListener((MouseMotionListener) instance);
//				} else if ("KeyListener".equals(strType)) {
//					sceneCanvas.removeKeyListener((KeyListener) instance);
//					sceneCanvas.addKeyListener((KeyListener) instance);
//				} else if ("MouseWheelListener".equals(strType)) {
//					sceneCanvas.removeMouseWheelListener((MouseWheelListener) instance);
//					sceneCanvas.addMouseWheelListener((MouseWheelListener) instance);
//				} else if ("WindowListener".equals(strType)) {
//					gameWindow.removeWindowListener((WindowListener) instance);
//					gameWindow.addWindowListener((WindowListener) instance);
//				} else if ("WindowStateListener".equals(strType)) {
//					gameWindow.removeWindowStateListener((WindowStateListener) instance);
//					gameWindow.addWindowStateListener((WindowStateListener) instance);
//				}
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//		}
//	}
//	public static void addUIComponent(Panel dlg) {
//		uiComponents.add(dlg);
//	}
//	public static FontMetrics getFontMetrics() {
//		return fontMetrics;
//	}
//	public static void updateUI() {
//	    ComponentInputMap canvasInputMap = new ComponentInputMap(getGameCanvas());
//	    for (KeyStroke k : inputMap.keys()) {
//	    	canvasInputMap.put(k, inputMap.get(k));
//	    }
//	    getGameCanvas().setInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW, canvasInputMap);
//	    getGameCanvas().setActionMap(getActionMap());
//	    Component[] precedingComps = getGameCanvas().getComponents();
//	    showUIComponents();
//	    //还原先前打开的面板等
//	    for (int i = 0; i < precedingComps.length; i++) {
//	    	getGameCanvas().add(precedingComps[i],0);
//		}
//	    installListener();
//	}

//	public static void showUIComponents() {
//		for (int i = 0; i < uiComponents.size(); i++) {
//	    	Panel c = uiComponents.get(i);
//	    	showDialog(c);
//	    }
//	}
//	public static Point getMousePosition() {
//			try {
//				Point p = gameWindow.getMousePosition();
//	//			if(p!=null && gameWindow instanceof JFrame) {
//	//				//SwingUtilities.convertPoint(gameWindow, p, canvas);
//	//				p.y -= 20;
//	//			}
//				return p;
//			} catch (Exception e) {
//				System.out.println("获取鼠标位置失败！"+e.getMessage());
//				//e.printStackTrace();
//			}
//			return null;
//		}
//	public static Canvas getGameCanvas() {
//		return gameWindow.getCanvas();
//	}


//	public static void initApplet(AppletWindow applet, String[] args) {
//			System.getProperties().list(System.out);
//			System.out.println();
//			System.out.println("-------------------------");
//			System.out.println("cache dir: "+CacheManager.getInstance().getCacheBase());
//			
//			gameWindow = applet;
//			initDisplay(args);
//			Dimension preferredSize = new Dimension(displayMode.getWidth(), displayMode.getHeight());
//			// loading canvas
//			loadingCanvas = new LoadingCanvas();
//			loadingCanvas.setPreferredSize(preferredSize);
//			applet.setCanvas(loadingCanvas);
//			applet.setSize(preferredSize);
//			applet.invalidate();
//			//CacheManager.getInstance().addDownloadListener(loadingCanvas);
//			
//			//scene canvas
//			sceneCanvas = new SceneCanvas();
//			sceneCanvas.setPreferredSize(preferredSize);
//			sceneCanvas.setSize(preferredSize);
//			
//			fontMetrics =applet.getFontMetrics(UIUtils.TEXT_NAME_FONT);
//			
//			URL documentBase = applet.getDocumentBase();
//			CacheManager.getInstance().setDocumentBase(documentBase);
//		}
//
//	public static boolean isHover(Player player) {
//		Point p = getGameCanvas().getMousePosition();
//		if (p == null) {
//			return false;
//		}
//		Point vp = localToView(player.getLocation());
//		boolean hover = player.contains(p.x - vp.x, p.y - vp.y);
//		return hover;
//	}


}
