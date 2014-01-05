package com.javaxyq.core;

import java.awt.DisplayMode;
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
import java.util.ArrayList;
import java.util.List;

import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import com.javaxyq.battle.BattleCanvas;
import com.javaxyq.battle.BattleListener;
import com.javaxyq.event.Listener;
import com.javaxyq.ui.Panel;
import com.javaxyq.ui.UIHelper;
import com.javaxyq.util.UIUtils;
import com.javaxyq.widget.Cursor;
//TODO UI分层：UI元素、场景、提示、拖拽...
public class DesktopWindow extends JFrame implements GameWindow {

    private static final long serialVersionUID = -8317898227965628232L;

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
	private int contentWidth ;

	private int contentHeight;

    public DesktopWindow() {
        setResizable(false);
        setTitle(GameMain.getApplicationName());
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);

        helper = new UIHelper(this);
        actionMap = new ActionMap();
        inputMap = new InputMap();
    }
    
    @Override
    public void init(Context context) {
    	context.setWindow(this);
    	String[] args = null;
		initDisplay(args);
	    this.setFont(UIUtils.TEXT_NAME_FONT);
		this.setTitle(System.getProperty("javaxyq.title","JavaXYQ"));
		hideCursor();
		setGameCursor(Cursor.DEFAULT_CURSOR);
    }
    
    @Override
    public void show() {
    	this.setLocationRelativeTo(null);
    	super.show();
    }
    
    @Override
    public void prepareUI() {
		String[] uiIds = new String[] {"mainwin"};
		for(String id : uiIds) {
			DialogFactory.getDialog(id, true);
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

    public void hideCursor() {
    	if(!cursorHided) {
    		cursorHided = true;
	        Toolkit toolkit = Toolkit.getDefaultToolkit();
	        Image blankImage = toolkit.createImage(new byte[0]);
	        java.awt.Cursor blankCursor = toolkit.createCustomCursor(blankImage, new Point(0, 0),
	            "BLANK_CURSOR");
	        setCursor(blankCursor);
    	}
    }

    public synchronized void setFullScreen() {
        setVisible(false);
        setState(JFrame.ICONIFIED);
        fullScreenWindow = new JFrame(GameMain.getApplicationName());
        fullScreenWindow.setContentPane(canvas.getComponent());
        fullScreenWindow.setUndecorated(true);
        fullScreenWindow.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
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
        pack();
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
        pack();
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
		this.contentWidth = width;
		this.contentHeight = height;
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
		return contentHeight;
	}

	@Override
	public int getContentWidth() {
		return contentWidth;
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

}
