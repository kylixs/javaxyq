package com.javaxyq.tools;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.beans.PropertyVetoException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.WeakHashMap;

import javax.swing.ActionMap;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDesktopPane;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JSplitPane;
import javax.swing.JTree;
import javax.swing.JViewport;
import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;
import javax.swing.event.MouseInputAdapter;
import javax.swing.event.TreeExpansionEvent;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.event.TreeWillExpandListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.ExpandVetoException;
import javax.swing.tree.TreePath;

import org.jdesktop.application.Action;
import org.jdesktop.application.Application;
import org.jdesktop.application.FrameView;
import org.jdesktop.application.ResourceMap;
import org.jdesktop.application.SingleFrameApplication;

import com.javaxyq.util.ExampleFileFilter;
import com.javaxyq.util.UIUtils;
import com.jidesoft.action.CommandBar;
import com.jidesoft.dialog.JideOptionPane;
import com.jidesoft.status.LabelStatusBarItem;
import com.jidesoft.status.MemoryStatusBarItem;
import com.jidesoft.status.StatusBar;
import com.jidesoft.swing.FolderChooser;
import com.jidesoft.swing.JideBoxLayout;
import com.jidesoft.swing.JideButton;
import com.jidesoft.swing.JidePopupMenu;
import com.jidesoft.swing.JideScrollPane;
import com.jidesoft.swing.JideSwingUtilities;

//TODO
// 1.窗口排列:平铺/层叠
// 2.更加后缀名识别文件,提供调用系统程序打开功能
// 3.WDF打包,解包
// 4.文件改名,移动,复制,删除等功能
// 5.资源描述信息加载与保存
// ?.激活系统资源管理器的右键菜单?

/**
 * 资源管理器
 * 
 * @author 龚德伟
 * @history 2008-6-27 龚德伟 新建
 */
public class ResourceManager extends SingleFrameApplication {

	private JMenuBar menuBar;

	private JPanel topPanel;

	private JMenu editMenu;

	private JMenu fileMenu;

	private JButton openButton;

	private JPanel toolBarPanel;

	private FolderChooser folderChooser;

	private File lastOpenDir = new File("E:/Games/梦幻西游");
	private File lastSaveDir = new File(".");

	private JFileChooser fileChooser = new JFileChooser();

	private StatusBar statusBar;

	private JDesktopPane desktop;

	private JTree structTree;

	private PreviewPanel defaultPreviewPanel;

	private SingleFrameApplication app;

	private List<String> recentList;

	private ExpandHandler expandHandler = new ExpandHandler();

	private Map<FileSystem, JTree> treeMap;

	private Cursor handCursor;

	private Cursor grabCursor;

	private JScrollPane treePanel;

	private JMenu helpMenu;

	private JFileChooser savefileChooser;

	private JideButton openFolderButton;
	
	private JMenu windowMenu;
	
	private SpriteExtractor spriteExtractor;

	public ResourceManager() {
		folderChooser = new FolderChooser();
		recentList = new ArrayList<String>();
		treeMap = new WeakHashMap<FileSystem, JTree>();
		spriteExtractor = new SpriteExtractor();
	}

	private void createCursor() {
		// create cursor
		Image handImage = app.getContext().getResourceMap().getImageIcon("handIcon").getImage();
		handCursor = Toolkit.getDefaultToolkit().createCustomCursor(handImage, new Point(), "scroll_hand");
		Image grabImage = app.getContext().getResourceMap().getImageIcon("grabIcon").getImage();
		grabCursor = Toolkit.getDefaultToolkit().createCustomCursor(grabImage, new Point(), "scroll_grab");
	}

	/**
	 * open a wdf file
	 */
	@Action
	public void open() {
		fileChooser.setCurrentDirectory(lastOpenDir);
		ExampleFileFilter wdfFilter = new ExampleFileFilter(new String[] { "wdf" });
		wdfFilter.setExtensionRegex("wd.*");
		fileChooser.setFileFilter(wdfFilter);
		int rtnVal = fileChooser.showOpenDialog(getMainFrame());
		if (rtnVal != JFileChooser.APPROVE_OPTION) {
			return;
		}
		File selectFile = fileChooser.getSelectedFile();
		if (selectFile != null && selectFile.exists()) {
			lastOpenDir = selectFile.getParentFile();
			openPreviewFrame(selectFile);
		}
	}

	/**
	 * 打开选中的item
	 * 
	 * @param e
	 */
	@Action
	public void openItem(ActionEvent e) {
		PreviewPanel panel = (PreviewPanel) desktop.getSelectedFrame().getContentPane();
		Object[] files = panel.getSelectedFiles();
		for (int i = 0; i < files.length; i++) {
			if (files[i] instanceof WdfFileNode) {
				WdfFileNode filenode = (WdfFileNode) files[i];
				openNode(filenode);
			}
		}
	}

	/**
	 * 调用系统软件打开文件
	 * 
	 * @param fileObject
	 */
	public void openNode(FileObject fileObject) {
		try {
			File tmpFile = new File(fileObject.getPath());
			if (fileObject instanceof WdfFileNode) {
				//WdfFileNode wdfNode = (WdfFileNode) fileObject;
				// 导出临时文件
				byte[] buf = fileObject.getData();
				File tmpDir = new File("temp");
				if(!tmpDir.exists()) {
					tmpDir.mkdirs();
				}
				tmpFile = new File(tmpDir,fileObject.getName());
				FileOutputStream fos = new FileOutputStream(tmpFile);
				fos.write(buf);
				fos.close();
				System.out.println("tmp: "+tmpFile.getAbsolutePath());
			}
			// 调用命令行
			Desktop.getDesktop().open(tmpFile);
		} catch (Exception e) {
			UIUtils.showError("打开文件失败！",e);
			//e.printStackTrace();
		}

	}

	/**
	 * 导出事件处理代码
	 * 
	 * @param e
	 */
	@Action
	public void exportItem(ActionEvent e) {
		PreviewPanel panel = (PreviewPanel) desktop.getSelectedFrame().getContentPane();
		Object[] files = panel.getSelectedFiles();
		for (int i = 0; i < files.length; i++) {
			if (files[i] instanceof WdfFileNode) {
				WdfFileNode filenode = (WdfFileNode) files[i];
				exportNode(filenode);
				System.out.println("export item: " + filenode.getPath());
			}
		}
	}
	
	/**
	 * 导出某个结点
	 * 
	 * @param fileObject
	 */
	public void exportNode(FileObject fileObject) {
		if (savefileChooser == null) {
			savefileChooser = new JFileChooser();
		}
		savefileChooser.setCurrentDirectory(lastSaveDir);
		savefileChooser.setSelectedFile(new File(fileObject.getName()));
		savefileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		int opt = savefileChooser.showSaveDialog(getMainFrame());
		if (opt == JFileChooser.APPROVE_OPTION) {
			File file = savefileChooser.getSelectedFile();
			lastSaveDir = file.getParentFile();
			try {
				byte[] buf = fileObject.getData();
				FileOutputStream fos = new FileOutputStream(file);
				fos.write(buf);
				fos.close();
			} catch (Exception e1) {
				UIUtils.showError("导出精灵失败！" + fileObject.getName(),e1);
				//e1.printStackTrace();
			}
		}
	}

	@Action
	public void exportAsSprite(ActionEvent e) {
		PreviewPanel panel = (PreviewPanel) desktop.getSelectedFrame().getContentPane();
		Object[] files = panel.getSelectedFiles();
		for (int i = 0; i < files.length; i++) {
			if (files[i] instanceof WdfFileNode) {
				WdfFileNode filenode = (WdfFileNode) files[i];
				exportSprite(filenode);
			}
		}
	}
	
	private void exportSprite(FileObject fileObject) {
		if (savefileChooser == null) {
			savefileChooser = new JFileChooser();
		}
		savefileChooser.setCurrentDirectory(lastSaveDir);
		savefileChooser.setSelectedFile(new File(fileObject.getName()));
		savefileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
		int opt = savefileChooser.showSaveDialog(getMainFrame());
		if (opt == JFileChooser.APPROVE_OPTION) {
			File file = savefileChooser.getSelectedFile();
			lastSaveDir = file.getParentFile();
			
			spriteExtractor.extract(fileObject, file, null);
		}
		savefileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
	}

	@Action
	public void openItemWith(ActionEvent e) {
		// TODO
		System.out.println("open item with:" + e.getActionCommand());
	}

	private void openPreviewFrame(File selectFile) {
		String key = selectFile.getAbsolutePath();
		try {
			JInternalFrame frame = findFrame(key);
			if (frame == null) {
				frame = createPreviewFrame(selectFile);
				desktop.add(frame);
				
				final JInternalFrame _frame = frame;
				final JMenuItem menuItem = new JMenuItem(key);
				menuItem.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						try {
							_frame.setSelected(true);
							if(!_frame.isMaximum()) {
								_frame.setMaximum(true);
							}
						} catch (PropertyVetoException e1) {
							e1.printStackTrace();
						}
					}
				});
				windowMenu.add(menuItem);
				_frame.addInternalFrameListener(new InternalFrameAdapter() {
					@Override
					public void internalFrameClosed(InternalFrameEvent e) {
						windowMenu.remove(menuItem);
					}
				});
			}
			frame.setSelected(true);
			frame.setIcon(false);
			frame.setMaximum(true);
			desktop.setSelectedFrame(frame);
		} catch (Exception e) {
			System.out.println("open new frame failed!");
			e.printStackTrace();
		}
	}

	private void showInternalFrame(JInternalFrame frame) {
		try {
			Dimension ds = desktop.getSize();
			Dimension fs = frame.getSize();
			fs.width = Math.min(fs.width, ds.width);
			fs.height = Math.min(fs.height, ds.height);
			frame.setSize(fs);
			desktop.add(frame);
			frame.setSelected(true);
			frame.setIcon(false);
			desktop.setSelectedFrame(frame);
		} catch (Exception e) {
			System.err.println("show internal frame failed!");
			e.printStackTrace();
		}
	}

	/**
	 * open a folder as the root node
	 */
	@Action
	public void openFolder() {
		// TODO
		folderChooser.setCurrentDirectory(lastOpenDir);
		folderChooser.setRecentList(recentList);
		folderChooser.setFileHidingEnabled(true);
		int result = folderChooser.showOpenDialog(app.getMainFrame());
		if (result == FolderChooser.APPROVE_OPTION) {
			lastOpenDir = folderChooser.getSelectedFile();
			if (recentList.contains(lastOpenDir.toString())) {
				recentList.remove(lastOpenDir.toString());
			}
			recentList.add(0, lastOpenDir.toString());

			// open folder
			openPreviewFrame(lastOpenDir);
		}
	}

	private JInternalFrame findFrame(String key) {
		if (key == null) {
			return null;
		}
		JInternalFrame[] frames = desktop.getAllFrames();
		for (JInternalFrame frame : frames) {
			String title = frame.getTitle();
			if (title != null && title.equals(key)) {
				return frame;
			}
		}
		return null;
	}

	@Action
	public void save() {
		// TODO
	}

	@Action
	public void newFile() {
		JInternalFrame jif = new JInternalFrame("test frame", true, true, true, true);
		jif.setSize(400, 300);
		// jif.setContentPane(content);
		jif.setVisible(true);
		desktop.add(jif);
	}

	@Action
	public void exitApp() {
		int rtn = JideOptionPane.showConfirmDialog(getMainFrame(), "Are you sure to exit?", "Confirm Exit",
				JideOptionPane.YES_NO_OPTION);
		if (rtn == JideOptionPane.OK_OPTION) {
			exit();
		}
	}

	@Action
	public void showAbout() {
		JideOptionPane.showMessageDialog(getMainFrame(),
				"Resource Manger for JavaXYQ\n" +
				"version：1.4.03 \n" +
				"last update at 2010.9.20\n" +
				"create by kylixs \n" +
				"http://javaxyq.googlecode.com", "Resource Manger",
				JideOptionPane.CLOSE_OPTION);
	}

	private ActionMap getAppActionMap() {
		return Application.getInstance().getContext().getActionMap(this);
	}

	private javax.swing.Action getAction(String key) {
		return getAppActionMap().get(key);
	}

	@Override
	protected void startup() {
		loadProperties();	
		app = (SingleFrameApplication) Application.getInstance();
		createCursor();
		initGUI();

		FrameView frameView = getMainView();
		JFrame mainFrame = frameView.getFrame();
		ResourceMap resourceMap = getContext().getResourceMap();
		Integer w = resourceMap.getInteger("Application.width");
		Integer h = resourceMap.getInteger("Application.height");
		mainFrame.setSize(w, h);
		show(frameView);
		mainFrame.setLocationRelativeTo(null);
	}

	private void loadProperties() {
		try {
			//load configs
			Properties props = new Properties();
			props.load(new FileInputStream("ResourceManager.properties"));
			lastOpenDir = new File(props.getProperty("LastOpenDir"));
			lastSaveDir = new File(props.getProperty("LastSaveDir"));
		} catch (FileNotFoundException e) {
			//e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	private void saveProperties() {
		try {
			//save configs
			Properties props = new Properties();
			if(lastOpenDir!=null) {
				props.put("LastOpenDir", lastOpenDir.getPath());
			}
			if(lastSaveDir!=null) {
				props.put("LastSaveDir", lastSaveDir.getPath());
			}
			props.store(new FileOutputStream("ResourceManager.properties"),"Create By ResourceManager at "+new java.util.Date());
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	protected void shutdown() {
		saveProperties();
		JFrame mainFrame = getMainFrame();
		ResourceMap resourceMap = getContext().getResourceMap();
		Integer w = resourceMap.getInteger("Application.width");
		Integer h = resourceMap.getInteger("Application.height");
		mainFrame.setSize(w, h);
		mainFrame.setLocationRelativeTo(null);
		
		super.shutdown();
	}

	private void initGUI() {
		topPanel = new JPanel();
		BorderLayout panelLayout = new BorderLayout();
		topPanel.setLayout(panelLayout);
		topPanel.setPreferredSize(new java.awt.Dimension(600, 400));
		{
			toolBarPanel = new JPanel();
			topPanel.add(toolBarPanel, BorderLayout.NORTH);
			BorderLayout jPanel1Layout = new BorderLayout();
			toolBarPanel.setLayout(jPanel1Layout);
			{
				CommandBar toolBar = new CommandBar();
				toolBar.setFloatable(true);
				toolBarPanel.add(toolBar, BorderLayout.CENTER);
//				{
//					newButton = new JideButton();
//					toolBar.add(newButton);
//					newButton.setAction(getAction("newFile"));
//					newButton.setName("newButton");
//					newButton.setFocusable(false);
//				}
				{
					openButton = new JideButton();
					toolBar.add(openButton);
					openButton.setAction(getAction("open"));
					openButton.setName("openButton");
					openButton.setFocusable(false);
				}
				{
					openFolderButton = new JideButton();
					toolBar.add(openFolderButton);
					openFolderButton.setAction(getAction("openFolder"));
					openFolderButton.setName("openFolderButton");
					openFolderButton.setFocusable(false);
				}
//				{
//					saveButton = new JideButton();
//					toolBar.add(saveButton);
//					saveButton.setAction(getAction("save"));
//					saveButton.setName("saveButton");
//					saveButton.setFocusable(false);
//				}
				{
					toolBar.addSeparator();
				}
				{
					JButton button = new JideButton();
					toolBar.add(button);
					button.setAction(getAction("exitApp"));
					button.setName("exitButton");
					button.setFocusable(false);
				}
			}
			{
				JSeparator jSeparator = new JSeparator();
				toolBarPanel.add(jSeparator, BorderLayout.SOUTH);
			}
		}
		{
			treePanel = new JScrollPane();
			desktop = new JDesktopPane();
			JSplitPane centerPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,treePanel,desktop); 
			centerPane.setDividerLocation(150);
			topPanel.add(centerPane, BorderLayout.CENTER);
		}
		{
			statusBar = new StatusBar();
			topPanel.add(statusBar, BorderLayout.SOUTH);

			final LabelStatusBarItem label = new LabelStatusBarItem("Line");
			label.setText("Resource Manager for JavaXYQ.");
			statusBar.add(label, JideBoxLayout.FLEXIBLE);

			final MemoryStatusBarItem gc = new MemoryStatusBarItem();
			statusBar.add(gc, JideBoxLayout.FIX);

		}
		menuBar = new JMenuBar();
		{
			fileMenu = new JMenu();
			menuBar.add(fileMenu);
			fileMenu.setName("fileMenu");
//			{
//				JMenuItem menuItem = new JMenuItem();
//				fileMenu.add(menuItem);
//				menuItem.setAction(getAction("newFile"));
//			}
			{
				JMenuItem menuItem = new JMenuItem();
				fileMenu.add(menuItem);
				menuItem.setAction(getAction("open"));
			}
			{
				JMenuItem menuItem = new JMenuItem();
				fileMenu.add(menuItem);
				menuItem.setAction(getAction("openFolder"));
			}
//			{
//				JMenuItem menuItem = new JMenuItem();
//				fileMenu.add(menuItem);
//				menuItem.setAction(getAction("save"));
//			}
			fileMenu.addSeparator();
			{
				JMenuItem menuItem = new JMenuItem();
				fileMenu.add(menuItem);
				menuItem.setAction(getAction("exitApp"));
			}
		}
		{
			editMenu = new JMenu();
			menuBar.add(editMenu);
			editMenu.setName("editMenu");
			{
				JMenuItem menuItem = new JMenuItem();
				editMenu.add(menuItem);
				menuItem.setAction(getAction("copy"));
			}
			{
				JMenuItem menuItem = new JMenuItem();
				editMenu.add(menuItem);
				menuItem.setAction(getAction("cut"));
			}
			{
				JMenuItem menuItem = new JMenuItem();
				editMenu.add(menuItem);
				menuItem.setAction(getAction("paste"));
			}
			{
				JMenuItem menuItem = new JMenuItem();
				editMenu.add(menuItem);
				menuItem.setAction(getAction("delete"));
			}
		}
		{
			windowMenu = new JMenu("Window");
			windowMenu.setName("windowMenu");
			menuBar.add(windowMenu);
			windowMenu.addSeparator();
		}
		{
			helpMenu = new JMenu("Help");
			helpMenu.setName("helpMenu");
			menuBar.add(helpMenu);
			{
				JMenuItem menuItem = new JMenuItem();
				helpMenu.add(menuItem);
				menuItem.setAction(getAction("visitHome"));
			}
			{
				JMenuItem menuItem = new JMenuItem();
				helpMenu.add(menuItem);
				menuItem.setAction(getAction("showHelp"));
			}
			{
				JMenuItem menuItem = new JMenuItem();
				helpMenu.add(menuItem);
				menuItem.setAction(getAction("suggestion"));
			}
//			{
//				JMenuItem menuItem = new JMenuItem();
//				helpMenu.add(menuItem);
//				menuItem.setAction(getAction("donate"));
//			}
			{
				JMenuItem menuItem = new JMenuItem();
				helpMenu.add(menuItem);
				menuItem.setAction(getAction("showAbout"));
			}
		}

		JFrame mainFrame = getMainFrame();
		mainFrame.setJMenuBar(menuBar);
		mainFrame.setContentPane(topPanel);
	}

	protected JInternalFrame createPreviewFrame(File selectFile) throws Exception {
		FileSystem filesystem = null;
		if (selectFile.isDirectory()) {
			filesystem = new DefaultFileSystem(selectFile);
		} else {
			filesystem = new WdfFile(selectFile.getAbsolutePath());
		}
		final PreviewPanel panel = new PreviewPanel(filesystem);

		JInternalFrame frame = createFrame(selectFile.getAbsolutePath(), panel);
		frame.addInternalFrameListener(new InternalFrameAdapter() {
			@Override
			public void internalFrameOpened(InternalFrameEvent e) {
				panel.init();
			}

			@Override
			public void internalFrameActivated(InternalFrameEvent e) {
				defaultPreviewPanel = panel;
				panel.init();
				setFileSystem(panel.getFileSystem());
			}

			@Override
			public void internalFrameDeactivated(InternalFrameEvent e) {
				setFileSystem(null);
			}

		});
		frame.setSize(600, 450);
		return frame;
	}

	private JInternalFrame createFrame(String title, JComponent content) {
		final JInternalFrame frame = new JInternalFrame(title, true, true, true, true);
		frame.setDefaultCloseOperation(JInternalFrame.DISPOSE_ON_CLOSE);
//		final JComponent titlebar = ((javax.swing.plaf.basic.BasicInternalFrameUI) frame.getUI()).getNorthPane();
//		frame.addPropertyChangeListener(new PropertyChangeListener() {
//			@Override
//			public void propertyChange(PropertyChangeEvent evt) {
//				String propName = evt.getPropertyName();
//				if(JInternalFrame.IS_MAXIMUM_PROPERTY.equals(propName)) {
//					Boolean isMax = (Boolean) evt.getNewValue();
//					if(isMax) {
//						((javax.swing.plaf.basic.BasicInternalFrameUI) frame.getUI()).setNorthPane(null);
//					}else {
//						((javax.swing.plaf.basic.BasicInternalFrameUI) frame.getUI()).setNorthPane(titlebar);
//					}
//					frame.revalidate();
//				}
//			}
//		});
		// frame.add(content, BorderLayout.CENTER);
		frame.setContentPane(content);
		frame.pack();
		frame.setVisible(true);
		return frame;
	}

	private static JScrollPane createScrollPane(Component component) {
		JScrollPane pane = new JideScrollPane(component);
		pane.setVerticalScrollBarPolicy(JideScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		return pane;
	}

	public static void main(String[] args) {
		//System.getProperties().list(System.out);
		XYQTools.verifyJideLicense();
		launch(ResourceManager.class, args);
	}

	// public DockingManager getDockingManager() {
	// return dockingManager;
	// }

	public void setFileSystem(FileSystem filesystem) {
		if (filesystem == null) {
			treePanel.setViewportView(new JPanel());
			treePanel.validate();
			return;
		}
		// 查找这个文件系统的树,如果找不到则创建
		JTree tree = treeMap.get(filesystem);
		if (tree == null) {
			FileObject rootObj = filesystem.getRoot();
			FileObjectTreeNode rootNode = new FileObjectTreeNode(rootObj);
			tree = new JTree(rootNode);
			tree.setShowsRootHandles(true);
			tree.setComponentPopupMenu(getTreeMenu());
			// tree.setBackground(Color.LIGHT_GRAY);
			tree.addTreeWillExpandListener(expandHandler);
			tree.addTreeSelectionListener(new TreeSelectionListener() {
				public void valueChanged(TreeSelectionEvent e) {
					TreePath path = e.getPath();
					if (path != null && defaultPreviewPanel != null) {
						FileObjectTreeNode node = (FileObjectTreeNode) path.getLastPathComponent();
						setCurrentPath((FileObject) node.getUserObject());
					}
				}
			});

			TreePath rootPath = tree.getPathForRow(0);
			try {
				tree.fireTreeWillExpand(rootPath);
			} catch (ExpandVetoException e1) {
				e1.printStackTrace();
			}
			tree.setSelectionPath(rootPath);
			treeMap.put(filesystem, tree);
		}
		treePanel.setViewportView(new JScrollPane(tree));
		treePanel.validate();
		this.structTree = tree;
	}

	private JidePopupMenu getTreeMenu() {
		JidePopupMenu popupMenu = new JidePopupMenu();
		popupMenu.add(getAction("previewAction"));
		return popupMenu;
	}

	@Action
	public void previewAction() {
		TreePath[] paths = this.structTree.getSelectionPaths();
		for (TreePath path : paths) {
			DefaultMutableTreeNode node = (DefaultMutableTreeNode) path.getLastPathComponent();
			WdfFileNode fileNode = (WdfFileNode) node.getUserObject();
			previewNode(fileNode);
		}
	}

	public void previewNode(FileObject fileObject) {
		String type = fileObject.getContentType();
		JDialog dialog = null;
		if (FileObject.TCP_FILE.equals(type)) {
			dialog = new SpriteDialog(getMainFrame(), fileObject);
			show(dialog);
//		} else if (isImageFile(type)) {
//			final JDialog imageDialog = new JDialog();
//			ButtonPanel buttonPanel = new ButtonPanel();
//			JideButton closeButton = new JideButton("close");
//			closeButton.addActionListener(new ActionListener() {
//				public void actionPerformed(ActionEvent e) {
//					imageDialog.dispose();
//				}
//			});
//			buttonPanel.add(closeButton);
//			imageDialog.add(new JLabel(new ImageIcon(XYQTools.createImage(fileObject))));
//			imageDialog.add(buttonPanel, BorderLayout.SOUTH);
//			dialog = imageDialog;
//			show(dialog);
		} else if (FileObject.WDF_FILE.equals(type)) {
			// open a wdf
			DefaultFileObject defaultFileObj = (DefaultFileObject) fileObject;
			openPreviewFrame(defaultFileObj.getFile());
		} else if (FileObject.MAP_FILE.equals(type)) {
			DefaultFileObject defaultFileObj = (DefaultFileObject) fileObject;
			JMap map = new JMap(defaultFileObj.getFile());
			map.setCursor(handCursor);
			map.addMouseListener(simpleEventHandler);
			map.addMouseListener(mapMouseHandler);
			map.addMouseMotionListener(mapMouseHandler);
			// JInternalFrame frame = createFrame(fileObject.getPath(), new
			// JScrollPane(map));
			// showInternalFrame(frame);
			dialog = new JDialog(app.getMainFrame(), fileObject.getName());
			dialog.add(new JScrollPane(map));
			show(dialog);
		} else if (fileObject.isDirectory()) {
			setCurrentPath(fileObject);
		}else {
			openNode(fileObject);
		}
	}

	private void setCurrentPath(FileObject fileObject) {
		defaultPreviewPanel.setPath(fileObject);
		// structTree.setSelectionPath(path);
	}

	public void show(JDialog dialog) {
		dialog.pack();
		dialog.setMaximumSize(new Dimension(800, 600));
		Dimension size = dialog.getSize();
		size.width = Math.min(size.width, 800);
		size.height = Math.min(size.height, 600);
		dialog.setSize(size);
		dialog.setLocationRelativeTo(app.getMainFrame());
		dialog.setVisible(true);
	}
	
	@Action
	public void visitHome() {
		try {
			URI url = new URI("http://javaxyq.googlecode.com");
			Desktop.getDesktop().browse(url);
		} catch (Exception e) {
			UIUtils.showError("访问项目主页失败！请键入网址访问(http://javaxyq.googlecode.com)。", e);
			e.printStackTrace();
		}
	}
	@Action
	public void donate() {
		
	}
	@Action
	public void suggestion() {
		
	}
	@Action
	public void showHelp() {
		try {
			URI url = new URI("http://code.google.com/p/javaxyq/wiki/ResourceManager");
			Desktop.getDesktop().browse(url);
		} catch (Exception e) {
			UIUtils.showError("访问在线帮助失败！", e);
			e.printStackTrace();
		}
	}

	private boolean isImageFile(String type) {
		return FileObject.BMP_FILE.equals(type) || FileObject.JPG_FILE.equals(type) || FileObject.GIF_FILE.equals(type)
				|| FileObject.PNG_FILE.equals(type);
	}

	private static class ExpandHandler implements TreeWillExpandListener {

		public void treeWillCollapse(TreeExpansionEvent event) throws ExpandVetoException {

		}

		public void treeWillExpand(TreeExpansionEvent event) throws ExpandVetoException {
			JTree tree = (JTree) event.getSource();
			TreePath path = event.getPath();
			FileObjectTreeNode node = (FileObjectTreeNode) path.getLastPathComponent();
			if (node == null) {
				return;
			}
			if (!node.isLoaded()) {
				FileObject nodeObj = (FileObject) node.getUserObject();
				FileObject[] subfileObjs = nodeObj.listFiles();
				for (FileObject fileObject : subfileObjs) {
					if (fileObject.isDirectory()) {
						node.add(new FileObjectTreeNode(fileObject));
					}
				}
				node.setLoaded(true);
				DefaultTreeModel treeModel = (DefaultTreeModel) tree.getModel();
				treeModel.nodeStructureChanged(node);
			}
		}

	}

	MouseInputAdapter mapMouseHandler = new MouseInputAdapter() {
		Point begPos;

		public void mousePressed(MouseEvent e) {
			Component comp = (Component) e.getSource();
			comp.setCursor(grabCursor);
			begPos = e.getPoint();
		}

		public void mouseReleased(MouseEvent e) {
			Component comp = (Component) e.getSource();
			comp.setCursor(handCursor);
			begPos = null;
		}

		public void mouseDragged(MouseEvent e) {
			// setCursor(MOVE_CURSOR);
			if (begPos == null) {
				return;
			}
			Component comp = (Component) e.getSource();
			JViewport viewport = (JViewport) comp.getParent();
			Point nowPos = e.getPoint();
			Point viewPos = viewport.getViewPosition();
			viewPos.translate(begPos.x - nowPos.x, begPos.y - nowPos.y);
			if (viewPos.x < 0)
				viewPos.x = 0;
			if (viewPos.y < 0)
				viewPos.y = 0;
			Dimension size = viewport.getViewSize();
			Rectangle viewRect = viewport.getViewRect();
			int maxX = size.width - viewRect.width;
			int maxY = size.height - viewRect.height;
			if (viewPos.x > maxX)
				viewPos.x = maxX;
			if (viewPos.y > maxY)
				viewPos.y = maxY;
			viewport.setViewPosition(viewPos);
		}
	};

	private SimpleEventHandler simpleEventHandler = new SimpleEventHandler();

	private static class SimpleEventHandler implements MouseListener {

		public void mouseClicked(MouseEvent e) {
			if (e.getButton() == MouseEvent.BUTTON3) {
				Component cmp = (Component) e.getSource();
				Window win = JideSwingUtilities.getWindowForComponent(cmp);
				win.dispose();
			}
		}

		public void mouseEntered(MouseEvent e) {
		}

		public void mouseExited(MouseEvent e) {
		}

		public void mousePressed(MouseEvent e) {
		}

		public void mouseReleased(MouseEvent e) {
		}

	}

}
