package com.javaxyq.ui;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.EventObject;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.ActionMap;
import javax.swing.JPanel;
import javax.swing.event.EventListenerList;

import com.javaxyq.config.ImageConfig;
import com.javaxyq.core.DialogFactory;
import com.javaxyq.core.ResourceStore;
import com.javaxyq.event.ActionEvent;
import com.javaxyq.event.EventDelegator;
import com.javaxyq.event.EventException;
import com.javaxyq.event.EventTarget;
import com.javaxyq.event.PanelEvent;
import com.javaxyq.event.PanelListener;
import com.javaxyq.widget.SpriteImage;

//FIXME SpriteImage的处理有问题，Image后跟着Sprite，无法出现
/**
 * 面板组件
 * 
 * @author Langlauf
 * @date
 */
public class Panel extends JPanel implements EventTarget {
	private static final long serialVersionUID = 3207034027692111969L;

	private EventListenerList listenerList = new EventListenerList();

	private ArrayList<SpriteImage> sprites = new ArrayList<SpriteImage>();

	/** 是否可以右击关闭 */
	private boolean closable;

	/** 是否初始化 */
	private boolean initialized;

	/** 鼠标左键点击是否允许关闭 */
	private boolean clickClosabled;

	/** actionId绑定表 */
	private Map<String, String> actionIdBindings = new HashMap<String, String>();

	private MouseAdapter mouseHandler = new MouseAdapter() {
		public void mousePressed(MouseEvent e) {
			if(deliverMouseEvent(e)) {return;}

			Panel dlg = Panel.this;
			Container parent = dlg.getParent();
			Point p = e.getPoint();
			if (e.getButton() == MouseEvent.BUTTON1) {
				if (clickClosabled) {
					Panel.this.close();
				} else {
					lastPosition = p;
					parent.setComponentZOrder(dlg, 0);// 移到最上层
				}
				e.consume();
			} else if (e.getButton() == MouseEvent.BUTTON3) {// 右击关闭
				if (closable) {
					Panel.this.close();
				}
			}
			EventDelegator.getInstance().delegateEvent(e);
		}

		public void mouseReleased(MouseEvent e) {
			if(deliverMouseEvent(e)) {return;}
			lastPosition = null;
			EventDelegator.getInstance().delegateEvent(e);
		}

		public void mouseDragged(MouseEvent e) {// 移动面板
			if(deliverMouseEvent(e)) {return;}
			if (lastPosition != null && movable) {
				Point location = Panel.this.getLocation();
				location.translate(e.getX() - lastPosition.x, e.getY() - lastPosition.y);
				Panel.this.setLocation(location);
			}
		}

		public void mouseMoved(MouseEvent e) {
			Panel dlg = Panel.this;
			Container parent = dlg.getParent();
			Point p = e.getPoint();
			int x = dlg.getX();
			int y = dlg.getY();
			// 如果点击的是穿透的区域,把事件传递给父容器
			if (!isValid(p)) {
				MouseEvent event = new MouseEvent(parent, MouseEvent.MOUSE_MOVED, System.currentTimeMillis(), e
					.getModifiers(), x + p.x, y + p.y, e.getClickCount(), false);
				parent.dispatchEvent(event);
				return;
			} else {
				MouseEvent event = new MouseEvent(parent, MouseEvent.MOUSE_EXITED, System.currentTimeMillis(), e
						.getModifiers(), x + p.x, y + p.y, e.getClickCount(), false);
				parent.dispatchEvent(event);
			}
		}

		public void mouseEntered(MouseEvent e) {
			Point p = e.getPoint();
			Panel dlg = Panel.this;
			Container parent = dlg.getParent();
			int x = dlg.getX();
			int y = dlg.getY();
			// 如果点击的是穿透的区域,把事件传递给父容器
			if (!isValid(p)) {
				MouseEvent event = new MouseEvent(parent, MouseEvent.MOUSE_ENTERED, System.currentTimeMillis(), e
						.getModifiers(), x + p.x, y + p.y, 1, false);
				parent.dispatchEvent(event);
			} else {
				MouseEvent event = new MouseEvent(parent, MouseEvent.MOUSE_EXITED, System.currentTimeMillis(), e
						.getModifiers(), x + p.x, y + p.y, 1, false);
				parent.dispatchEvent(event);
			}
		}
		public void mouseClicked(MouseEvent e) {
			if(deliverMouseEvent(e)) {return;}
			EventDelegator.getInstance().delegateEvent(e);
		}
		
		public void mouseExited(MouseEvent e) {
			
		}

//		public void mouseWheelMoved(java.awt.event.MouseWheelEvent e) {
//			Panel dlg = Panel.this;
//			Container parent = dlg.getParent();
//			Point p = e.getPoint();
//			// 如果点击的是穿透的区域,把事件传递给父容器
//			if (!isValid(p)) {
//				int x = dlg.getX();
//				int y = dlg.getY();
//				
//				MouseWheelEvent event = new MouseWheelEvent(parent, e.getID(), System.currentTimeMillis(), e
//					.getModifiers(), x + p.x, y + p.y, e.getClickCount(), false,e.getScrollType(),e.getScrollAmount(),e.getWheelRotation());
//				parent.dispatchEvent(event);
//				return;
//			}
//		}
	};

	/** 是否可以拖动 */
	private boolean movable;

	private Point lastPosition;

	private List<ImageConfig> imageConfigs = new ArrayList<ImageConfig>();

	private String initialAction;

	private String disposeAction;

	private SpriteImage bgImage;

	public Panel(int width, int height) {
		this(width, height, true, true);
	}

	/**
	 * 关闭此面板/对话框
	 */
	public void close() {
		if(getParent()!=null) {
			getParent().remove(this);
			fireEvent(new PanelEvent(this, "dispose"));
			DialogFactory.dispose(this.getName(),this);
		}
	}

	/**
	 * 判断该点是否为有效区域内(非穿透区域)
	 * 
	 * @param p
	 * @return
	 */
	public boolean isValid(Point p) {
		if (!isVisible()) {
			return false;
		}
		if (bgImage != null && bgImage.contains(p.x - bgImage.getX(), p.y - bgImage.getY())) {
			return true;
		}
		for(int i=0;i<sprites.size();i++) {
			SpriteImage image = sprites.get(i);
			if (image.contains(p.x - image.getX(), p.y - image.getY())) {
				return true;
			}
		}
		return false;
	}

	/**
	 * @param bgImage
	 *            背景图片
	 * @param closable
	 *            是否可以关闭
	 * @param movable
	 *            是否可以移动
	 */
	public Panel(int width, int height, boolean closable, boolean movable) {
		this.closable = closable;
		this.movable = movable;
		// changed viewer properties
		setIgnoreRepaint(true);
		setBorder(null);
		setLayout(null);
		// setOpaque(false);
		setFocusable(false);
		setPreferredSize(new Dimension(width, height));
		setSize(width, height);
		addMouseListener(mouseHandler);
		addMouseMotionListener(mouseHandler);
	}

	public boolean handleEvent(EventObject evt) throws EventException {
		if (evt instanceof PanelEvent) {
			handlePanelEvent((PanelEvent) evt);
			return ((PanelEvent) evt).isConsumed();
		} else if (evt instanceof ActionEvent) {
			handleActionEvent((ActionEvent) evt);
			return true;
		} else if (evt instanceof java.awt.event.ActionEvent) {
			handleActionEvent(new ActionEvent((java.awt.event.ActionEvent) evt));
			return true;
		} else if (evt instanceof MouseEvent) {
			handleMouseEvent((MouseEvent) evt);
			return true;
		}
		return false;
	}

	public void fireEvent(PanelEvent e) {
		//EventDispatcher.getInstance(Panel.class, PanelEvent.class).dispatchEvent(e);
		EventDelegator.getInstance().delegateEvent(e);
	}

	/**
	 * 处理鼠标事件
	 * 
	 * @param event
	 */
	private void handleMouseEvent(MouseEvent evt) {
		String actionId = getBindingActionId(evt);
		if (actionId != null) {
			ActionEvent actionEvent = new ActionEvent(evt.getSource(), actionId, new Object[] { evt });
			handleActionEvent(actionEvent);
		}
	}

	/**
	 * @param evt
	 * @return
	 */
	private String getBindingActionId(ComponentEvent evt) {
		// actionKey -> name-eventType, like 'label气血-mousepressed'
		String paramString = evt.paramString();
		// 得到事件的类型 'MOUSE_PRESSED'
		String eventType = paramString.substring(0, paramString.indexOf(','));
		// 删除分割的下划线 'MOUSE_PRESSED' -> 'mousepressed'
		eventType = eventType.toLowerCase().replaceAll("_", "");
		String actionKey = evt.getComponent().getName() + "-" + eventType;
		// 获得绑定的actionId
		String actionId = actionIdBindings.get(actionKey);
		return actionId;
	}

	protected void handleActionEvent(ActionEvent evt) {
		PanelListener[] listeners = listenerList.getListeners(PanelListener.class);
		for (int i = 0; i < listeners.length; i++) {
			listeners[i].actionPerformed(evt);
		}
	}

	protected void handlePanelEvent(PanelEvent evt) {
		PanelListener[] listeners = listenerList.getListeners(PanelListener.class);
		for (int i = 0; i < listeners.length && !evt.isConsumed(); i++) {
			try {
				listeners[i].actionPerformed(evt);
			} catch (Exception e) {
				System.out.println("[panel]执行事件时发生异常："+evt);
				e.printStackTrace();
			}
		}
	}

	public void addPanelListener(PanelListener l) {
		listenerList.add(PanelListener.class, l);
	}

	public void removePanelListener(PanelListener l) {
		listenerList.remove(PanelListener.class, l);
	}

	/**
	 * 移除所有面板事件监听器
	 */
	public void removeAllPanelListeners() {
		PanelListener[] listeners = listenerList.getListeners(PanelListener.class);
		for (int i = 0; i < listeners.length; i++) {
			listenerList.remove(PanelListener.class, listeners[i]);
		}
	}

	/**
	 * 绑定控件的事件
	 * 
	 * @param comp
	 * @param eventType
	 * @param actionId
	 */
	public void bindAction(Component comp, String eventType, String actionId) {
		// like 'name-mouseclicked'
		String key = comp.getName() + "-" + eventType.toLowerCase();
		actionIdBindings.put(key, actionId);
	}

	/**
	 * 遍历查找指定名字的子控件
	 * 
	 * @param name
	 * @return
	 */
	public Component findCompByName(String name) {
		if (name != null) {
			Component[] comps = getComponents();
			for (Component comp : comps) {
				if (name.equals(comp.getName()))
					return comp;
			}
		}
		return null;
	}

	public void paint(Graphics g) {
		init();
		if (bgImage != null) {
			bgImage.draw(g, bgImage.getX(), bgImage.getY());
		}
		for(int i=0;i<sprites.size();i++) {
			SpriteImage image = sprites.get(i);
			image.draw(g, image.getX(), image.getY());
		}
		// paintComponent(g);
		paintBorder(g);
		paintChildren(g);
		// Component[] comps = getComponents();
		// for (int i = comps.length - 1; i >= 0; i--) {
		// Component c = comps[i];
		// Graphics g2 = g.create(c.getX(), c.getY(), c.getWidth(),
		// c.getHeight());
		// c.paint(g2);
		// g2.dispose();
		// }
		// System.out.println("paint panel ...");
	}

	private void init() {
		if (!this.initialized) {
			for (ImageConfig cfg : imageConfigs) {
				SpriteImage image = ResourceStore.getInstance().createImage(cfg);
				this.sprites.add(image);
			}
			this.initialized = true;
		}
	}

	public void setClosable(boolean b) {
		closable = b;
	}

	public void setMovable(boolean b) {
		movable = b;
	}

	public void addImage(ImageConfig imageConfig) {
		this.imageConfigs.add(imageConfig);
	}

	public boolean removeImage(String id) {
		for (int i = 0; i < this.imageConfigs.size(); i++) {
			ImageConfig cfg = this.imageConfigs.get(i);
			if (id.equals(cfg.getId())) {
				this.imageConfigs.remove(i);
				this.sprites.remove(i);
				return true;
			}
		}
		return false;
	}

	public boolean setImage(String id, ImageConfig newCfg) {
		for (int i = 0; i < this.imageConfigs.size(); i++) {
			ImageConfig cfg = this.imageConfigs.get(i);
			if (id.equals(cfg.getId())) {
				this.imageConfigs.set(i, newCfg);
				SpriteImage s = ResourceStore.getInstance().createImage(newCfg);
				this.sprites.set(i, s);
				return true;
			}
		}
		return false;
	}

	public void setInitialAction(String initialAction) {
		this.initialAction = initialAction;
	}

	public String getInitialAction() {
		return initialAction;
	}

	public void initActionMap(ActionMap map) {
		for (Object key : map.allKeys()) {
			getActionMap().put(key, map.get(key));
		}
	}

	public String getDisposeAction() {
		return this.disposeAction;
	}

	public void setDisposeAction(String disposeAction) {
		this.disposeAction = disposeAction;
	}

	@Override
	public void paintImmediately(int x, int y, int w, int h) {
		// super.paintImmediately(x, y, w, h);
	}

	public SpriteImage getBgImage() {
		return bgImage;
	}

	public void setBgImage(SpriteImage bgImage) {
		this.bgImage = bgImage;
	}

	public void setBgImage(ImageConfig cfg) {
		this.bgImage = ResourceStore.getInstance().createImage(cfg);
	}

	public boolean isClickClosabled() {
		return clickClosabled;
	}

	public void setClickClosabled(boolean clickClosabled) {
		this.clickClosabled = clickClosabled;
	}

	public boolean isClosable() {
		return closable;
	}

	public boolean isMovable() {
		return movable;
	}

	protected boolean deliverMouseEvent(MouseEvent e) {
		Panel dlg = Panel.this;
		Container parent = dlg.getParent();
		Point p = e.getPoint();
		// 如果点击的是穿透的区域,把事件传递给父容器
		if (!isValid(p)) {
			int x = dlg.getX();
			int y = dlg.getY();
			MouseEvent event = new MouseEvent(parent, e.getID(), System.currentTimeMillis(), e
				.getModifiers(), x + p.x, y + p.y, e.getClickCount(), false);
			parent.dispatchEvent(event);
			return true;
		}
		return false;
	}

}
