//TODO 预览：读取系统文件图标

/*
 * JavaXYQ Engine 
 * 
 * javaxyq@2008 all rights. 
 * http://www.javaxyq.com
 */

package com.javaxyq.tools;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.ByteArrayInputStream;

import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.ListModel;

import org.jdesktop.application.Action;
import org.jdesktop.application.Application;
import org.jdesktop.application.ApplicationActionMap;
import org.jdesktop.application.ApplicationContext;
import org.jdesktop.application.ResourceMap;

import com.javaxyq.util.BMPLoader;
import com.jidesoft.action.CommandBar;
import com.jidesoft.dialog.JideOptionPane;
import com.jidesoft.list.DefaultPreviewImageIcon;
import com.jidesoft.list.ImagePreviewList;
import com.jidesoft.list.QuickListFilterField;
import com.jidesoft.swing.JideMenu;
import com.jidesoft.swing.JidePopupMenu;
import com.jidesoft.swing.SearchableUtils;

/**
 * WDF 预览面板
 * 
 * @author 龚德伟
 * @history 2008-6-25 龚德伟 新建
 */
public class PreviewPanel extends JPanel {
	/**  */
	private static final long serialVersionUID = -360585259392696696L;

	private ApplicationContext context = Application.getInstance().getContext();

	private ApplicationActionMap actionMap = context.getActionMap(this);

	private DefaultListModel listModel;

	private PaginationBar paginationBar;

	private FileObject[] fileNodes;

	private FileSystem fileSystem;

	private EventHandler eventHandler = new EventHandler();

	private ImagePreviewList imagePreviewList;

	private QuickListFilterField filterField;

	private String filterKeyword;

	private boolean init;

	private FileObject path;

	private int pageSize = 30;

	private int totalCount;

	private StatusBar statusBar;

	private JLabel statusLabel;

	private class EventHandler implements MouseListener, KeyListener {

		public void mouseClicked(MouseEvent e) {
			if (e.getButton() == MouseEvent.BUTTON1 && e.getClickCount() == 2) {
				int index = imagePreviewList.locationToIndex(e.getPoint());
				preview(index);
			}
		}

		public void mouseEntered(MouseEvent e) {
		}

		public void mouseExited(MouseEvent e) {
		}

		public void mousePressed(MouseEvent e) {
			if (e.getButton() == MouseEvent.BUTTON3) {
				JList list = (JList) e.getSource();
				int index = list.locationToIndex(e.getPoint());
				// 如果未选中鼠标点击的item,则取消当前选择的items
				// 重新设置只选中此item
				if (!list.isSelectedIndex(index)) {
					list.removeSelectionInterval(0, list.getModel().getSize());
					list.setSelectedIndex(index);
				}
			}
		}

		public void mouseReleased(MouseEvent e) {
		}

		public void keyPressed(KeyEvent e) {
			if (e.getKeyCode() == KeyEvent.VK_ENTER) {
				int index = imagePreviewList.getSelectedIndex();
				preview(index);
			}
		}

		public void keyReleased(KeyEvent e) {

		}

		public void keyTyped(KeyEvent e) {

		}

	}

	/**
	 * init
	 */
	public PreviewPanel(FileSystem filesystem) {
		this.fileSystem = filesystem;

		paginationBar = new PaginationBar();
		paginationBar.addPaginationListener(new PaginationListener() {
			public void loadPage(PaginationEvent e) {
				loadItems();
			}
		});
		this.setLayout(new BorderLayout());
		// pagination
		CommandBar toolbar = paginationBar.getComponent();
		// search
		//toolbar.add(actionMap.get("search"));
		// go up
		toolbar.add(actionMap.get("upwards"));
		// filter
		listModel = new DefaultListModel();
		filterField = new QuickListFilterField();
//		filterField = new QuickListFilterField(listModel) {
//			@Override
//			protected Filter createFilter() {
//				AbstractListFilter filter = new AbstractListFilter() {
//					public boolean isValueFiltered(Object value) {
//						String keyword = getSearchingText();
//						DefaultPreviewImageIcon icon = (DefaultPreviewImageIcon) value;
//						String nodeId = icon.getTitle();
//						if (fileSystem instanceof WdfFile) {
//							WdfFile wdffs = (WdfFile) fileSystem;
//							WdfFileNode node = wdffs.findNode(nodeId);
//							return nodeId.indexOf(keyword) == -1
//							&& node.getName().indexOf(keyword) == -1;
//						}
//						return nodeId!=null && !nodeId.contains(keyword);
//					}
//				};
//				return filter;
//			}
//		};
		filterField.setSearchingDelay(1000);
		filterField.setHintText("请键入关键字");
		filterField.addPropertyChangeListener("searchText",new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				String filter = (String) evt.getNewValue();
				refresh(filter);
			}
		});
		toolbar.add(filterField);

		imagePreviewList = new ImagePreviewList(listModel);
		imagePreviewList.setShowDetails(ImagePreviewList.SHOW_TITLE);
		imagePreviewList.setCellRenderer(new FileObjectPreviewPanel());
		imagePreviewList.setCellDimension(new Dimension(180, 135));
		imagePreviewList.addMouseListener(eventHandler);
		imagePreviewList.addKeyListener(eventHandler);
		imagePreviewList.setComponentPopupMenu(createPopupMenu());
		SearchableUtils.installSearchable(imagePreviewList);

		Application app = Application.getInstance();
		statusBar = new StatusBar(app, context.getTaskMonitor());
		// toolbar.add(statusBar);
		statusLabel = new JLabel();
		JPanel statusPanel = new JPanel(new BorderLayout());
		statusPanel.add(statusLabel, BorderLayout.CENTER);
		statusPanel.add(statusBar, BorderLayout.EAST);

		this.add(new JScrollPane(imagePreviewList));
		this.add(toolbar, BorderLayout.NORTH);
		this.add(statusPanel, BorderLayout.SOUTH);
		paginationBar.registerKeyboardAction(this, JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
		paginationBar.registerKeyboardAction(imagePreviewList, JComponent.WHEN_FOCUSED);
	}

	private JPopupMenu createPopupMenu() {
		JidePopupMenu popupMenu = new JidePopupMenu();
		popupMenu.add(actionMap.get("openItem"));

		JideMenu openWithMenu = new JideMenu("Open With ..");
		openWithMenu.add(createMenuItem("openItemWith", "UltraEdit", "ultra_edit"));
		openWithMenu.add(createMenuItem("openItemWith", "Was Tools", "wastools"));
		openWithMenu.add(createMenuItem("openItemWith", "Windows Media Player", "media_player"));
		popupMenu.add(openWithMenu);

		popupMenu.addSeparator();
		popupMenu.add(actionMap.get("exportItem"));
		popupMenu.add(actionMap.get("exportAsSprite"));

		return popupMenu;
	}

	private JMenuItem createMenuItem(String actionName, String text, String cmd) {
		javax.swing.Action action = actionMap.get(actionName);
		JMenuItem menuItem = new JMenuItem(action);
		menuItem.setText(text);
		menuItem.setActionCommand(cmd);
		return menuItem;
	}

	/**
	 * 加载当前页的items
	 */
	public void loadItems() {
		this.listModel.removeAllElements();
		int start = paginationBar.getStartNo();
		int end = paginationBar.getEndNo();
		int iCount = 0;
		long t1 = System.currentTimeMillis();
		for (int i = start; i < end; i++) {
			FileObject node = fileNodes[i];
			iCount++;
			if (!isAcceptNode(node)) {
				System.out.printf("filter node:%s\n", node);
				continue;
			}
			this.addIcon(node);
		}
		System.out.printf("loadItems:%s, cost: %sms.\n ", iCount, (System.currentTimeMillis() - t1));
	}

	public FileObject getPath() {
		return path;
	}

	public void setPath(FileObject fileObject) {
		if (fileObject!=null &&(this.path == null || !this.path.equals(fileObject))) {
			this.path = fileObject;
			refresh(null);
			// this.statusBar.setMessage(fileObject.getPath());
			this.statusLabel.setText(fileObject.getPath());
		}
	}

	private void refresh(String filter) {
		if (this.path != null) {
			this.fileNodes = this.path.listFiles(filter);
			this.totalCount = fileNodes.length;
			this.paginationBar.init(1, pageSize, totalCount);
			this.paginationBar.doReload();
		}
	}

	private boolean isAcceptNode(FileObject node) {
		if (filterKeyword == null || filterKeyword.trim().length() == 0) {
			return true;
		}
		return node.getName().indexOf(filterKeyword) != -1;
	}

	/**
	 * add a image icon to this panel
	 * 
	 * @param icon
	 * @param title
	 * @param description
	 */
	private void addIcon(FileObject node) {
		ResourceMap resourceMap = context.getResourceMap(PreviewPanel.class);
		ImageIcon icon = null;
		try {
			String type = node.getContentType();
			// System.out.printf("prepare node:[id=%s,name=%s,type=%s]\n",
			// nodeIdHex, node.getName(), type);
			if (FileObject.TCP_FILE.equals(type)) {
				icon = XYQTools.createSpriteIcon(node, 0);
				// ImageIcon tcpIcon =
				// resourceMap.getImageIcon("tcp_file_icon");
				// icon.setImage(
				// XYQTools.markImage(icon.getImage(),tcpIcon.getImage()));
			} else if (FileObject.JPG_FILE.equals(type)) {
				Image image = XYQTools.createImage(node);
				// ImageIcon jpgIcon =
				// resourceMap.getImageIcon("jpg_file_icon");
				// icon = new ImageIcon(XYQTools.markImage(image,
				// jpgIcon.getImage()));
				icon = new ImageIcon(image);
			} else if (FileObject.BMP_FILE.equals(type)) {
				if (node instanceof WdfFileNode) {
					byte[] buf = node.getData();
					Image image = BMPLoader.read(new ByteArrayInputStream(buf));
					icon = new ImageIcon(image);
				}else if (node instanceof DefaultFileObject) {
					Image image = BMPLoader.load(node.getPath());
					icon = new ImageIcon(image);
				}
			} else if (FileObject.PNG_FILE.equals(type)) {
				Image image = XYQTools.createImage(node);
				icon = new ImageIcon(image);
			} else if (FileObject.GIF_FILE.equals(type)) {
				Image image = XYQTools.createImage(node);
				icon = new ImageIcon(image);
			} else if (FileObject.MIDI_FILE.equals(type)) {
				icon = resourceMap.getImageIcon("midi_file_icon");
			} else if (FileObject.MP3_FILE.equals(type)) {
				icon = resourceMap.getImageIcon("mp3_file_icon");
			} else if (FileObject.WAV_FILE.equals(type)) {
				icon = resourceMap.getImageIcon("wav_file_icon");
				// } else if (FileObject.DIRECTORY.equals(type)) {
			} else if (FileObject.WDF_FILE.equals(type)) {
				icon = resourceMap.getImageIcon("wdf_file_icon");
			} else if (FileObject.MAP_FILE.equals(type)) {
				icon = resourceMap.getImageIcon("map_file_icon");
			} else if (FileObject.UNKNOWN_FILE.equals(type)) {
				icon = resourceMap.getImageIcon("unknown_file_icon");
			}

			if (node.isDirectory()) {
				icon = resourceMap.getImageIcon("directory_icon");
			}

		} catch (Exception e) {
			System.err.println("unknown node : " + node);
			icon = resourceMap.getImageIcon("unknown_file_icon");
			e.printStackTrace();
		}

		listModel.addElement(new PreviewImageIcon(icon, node));
	}

	public void init() {
		// Point top =getLocationOnScreen();
		// top.translate(20, 20);
		// BasicToolBarUI toolbarUI = (BasicToolBarUI) toolbar.getUI();
		// toolbarUI.setFloatingLocation(top.x, top.y);
		// toolbarUI.setFloating(true, top);
		// System.out.println("floating");
		imagePreviewList.requestFocus(false);
		if (!init) {
			paginationBar.doFirst();
			init = true;
		}
	}

	private void preview(int index) {
		if (index == -1) {
			return;
		}
		ListModel listModel = imagePreviewList.getModel();
		PreviewImageIcon item = (PreviewImageIcon) listModel.getElementAt(index);
		ResourceManager rm = (ResourceManager) Application.getInstance();
		rm.previewNode(item.getUserObject());
	}

	@Action
	public void search() {
		filterKeyword = JideOptionPane.showInputDialog("请输入要查找的关键字:", filterKeyword);
		if (filterKeyword != null) {
			System.out.println("search: " + filterKeyword);
			paginationBar.doReload();
		}
	}

	@Action
	public void upwards() {
		if (path == null) {
			return;
		}
		setPath(this.path.getParent());
	}

	public FileSystem getFileSystem() {
		return fileSystem;
	}

	public Object[] getSelectedFiles() {
		Object[] items = imagePreviewList.getSelectedValues();
		if (items != null) {
			Object[] fileObjs = new Object[items.length];
			for (int i = 0; i < fileObjs.length; i++) {
				fileObjs[i] = ((PreviewImageIcon) items[i]).getUserObject();
			}
			return fileObjs;
		}
		return null;
	}

	private static class PreviewImageIcon extends DefaultPreviewImageIcon {
		//private static Dimension defaultSize = new Dimension(100, 50);
		private FileObject userObject;

		public PreviewImageIcon(ImageIcon icon, FileObject userObject) {
			super(icon, userObject.getName(), userObject.getPath());
			setUserObject(userObject);
		}

		public FileObject getUserObject() {
			return userObject;
		}

		public void setUserObject(FileObject userObject) {
			this.userObject = userObject;
			String title = userObject.getName();
			String description = userObject.getPath();
			if(userObject instanceof WdfFileNode) {
				WdfFileNode wdfNode = (WdfFileNode) userObject;
				description = wdfNode.getDescription();
			}
			this.setTitle(title);
			this.setDescription(description);
		}

	}
}
