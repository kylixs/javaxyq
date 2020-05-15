/*
 * JavaXYQ Source Code
 * by kylixs
 * at 2010-5-9
 * please visit http://javaxyq.googlecode.com
 * or mail to kylixs@qq.com
 */
package com.javaxyq.tools.maker;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;

import javax.swing.AbstractListModel;
import javax.swing.BorderFactory;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JToolBar;
import javax.swing.ListModel;
import javax.swing.SwingConstants;
import javax.swing.ToolTipManager;
import javax.swing.UIDefaults;
import javax.swing.UIManager;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.plaf.BorderUIResource;
import javax.swing.plaf.ColorUIResource;
import javax.swing.plaf.FontUIResource;

import org.jdesktop.application.Action;
import org.jdesktop.application.FrameView;

import com.javaxyq.ui.RoundLineBorder;
import com.javaxyq.util.UIUtils;
import com.jidesoft.icons.JideIconsFactory;
import com.jidesoft.plaf.LookAndFeelFactory;
import com.jidesoft.plaf.UIDefaultsLookup;
import com.jidesoft.plaf.basic.ThemePainter;
import com.jidesoft.plaf.office2003.BasicOffice2003Theme;
import com.jidesoft.plaf.office2003.Office2003Painter;
import com.jidesoft.utils.PortingUtils;

/**
 * @author Administrator
 * @date 2010-5-9 create
 */
public class GameMakerView extends FrameView {

	/**
	 * 场景编辑事件监听器 
	 */
	private class SceneHandler extends MouseAdapter{
		
		public void mouseMoved(MouseEvent e) {
			setStatus(1,point2String(e.getPoint()));
			setStatus(2,point2String(sceneEditor.getSelectingCell()));
		}
	}

	private static class RepaintThread extends Thread{
		private FrameView view;
		public RepaintThread(FrameView view) {
			super("RepaintThread");
			setDaemon(true);
			this.view = view;
		}
		@Override
		public void run() {
			while(true) {
				view.getFrame().repaint();
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
	/** 选择地图文件 */
	private static final String ACTION_OPEN_MAP_FILE = "openMapFile";
	/** 显示、隐藏网格 */
	private static final String ACTION_TOGGLE_GRID= "toggleGrid";
	
	
	private JFrame mainFrame;
	private JFileChooser fileChooser;
	private int defaultWidth = 800;
	private int defaultHeight = 600;
	private SceneEditor sceneEditor;
	
	private SceneHandler sceneHandler= new SceneHandler();
	private JLabel[] statusLabels;
	/**
	 * @param application
	 */
	public GameMakerView(GameMaker application) {
		super(application);
		initGUI();
		
		sceneEditor.setScene("1146");
		new RepaintThread(this).start();
	}
	
	public GameMaker getMaker() {
		return (GameMaker) super.getApplication();
	}
	
	public javax.swing.Action getAction(String key) {
		return getContext().getActionMap(GameMakerView.class,this).get(key);
	}
	
	protected String getProperty(String key) {
		return getResourceMap().getString(key);//TODO
	}
	
	protected void setProperty(String key,String value) {
		//TODO  __@yoop____$$
	}
	/**
	 * 坐标转成字符串
	 * @param p
	 * @return
	 */
	private String point2String(Point p) {
		if(p!=null)
			return p.x+","+p.y;
		else return "--";
	}

	//---------------- GUI ----------------------------------//
	/**
	 * 
	 */
	private void initGUI() {
        PortingUtils.prerequisiteChecking();

        // add an example custom theme
        BasicOffice2003Theme theme = new BasicOffice2003Theme("Custom");
        theme.setBaseColor(new Color(50, 190, 150), true, "default");
        ((Office2003Painter) Office2003Painter.getInstance()).addTheme(theme);

        LookAndFeelFactory.installDefaultLookAndFeelAndExtension();
		LookAndFeelFactory.UIDefaultsCustomizer uiDefaultsCustomizer = new LookAndFeelFactory.UIDefaultsCustomizer() {
            public void customize(UIDefaults defaults) {
                ThemePainter painter = (ThemePainter) UIDefaultsLookup.get("Theme.painter");
                defaults.put("OptionPaneUI", "com.jidesoft.plaf.basic.BasicJideOptionPaneUI");

                defaults.put("OptionPane.showBanner", Boolean.TRUE); // show banner or not. default is true
                defaults.put("OptionPane.bannerIcon", JideIconsFactory.getImageIcon(JideIconsFactory.JIDE50));
                defaults.put("OptionPane.bannerFontSize", 13);
                defaults.put("OptionPane.bannerFontStyle", Font.BOLD);
                defaults.put("OptionPane.bannerMaxCharsPerLine", 60);
                defaults.put("OptionPane.bannerForeground", painter != null ? painter.getOptionPaneBannerForeground() : null);  // you should adjust this if banner background is not the default gradient paint
                defaults.put("OptionPane.bannerBorder", null); // use default border

                // set both bannerBackgroundDk and // set both bannerBackgroundLt to null if you don't want gradient
                defaults.put("OptionPane.bannerBackgroundDk", painter != null ? painter.getOptionPaneBannerDk() : null);
                defaults.put("OptionPane.bannerBackgroundLt", painter != null ? painter.getOptionPaneBannerLt() : null);
                defaults.put("OptionPane.bannerBackgroundDirection", Boolean.TRUE); // default is true

                // optionally, you can set a Paint object for BannerPanel. If so, the three UIDefaults related to banner background above will be ignored.
                defaults.put("OptionPane.bannerBackgroundPaint", null);

                defaults.put("OptionPane.buttonAreaBorder", BorderFactory.createEmptyBorder(6, 6, 6, 6));
                defaults.put("OptionPane.buttonOrientation", SwingConstants.RIGHT);
            }
        };
        uiDefaultsCustomizer.customize(UIManager.getDefaults());
		UIManager.put("ToolTip.border", new BorderUIResource(new CompoundBorder(
			new RoundLineBorder(Color.WHITE,1, 8, 8),new EmptyBorder(3, 3, 3, 3))));
		UIManager.put("ToolTip.foreground", new ColorUIResource(Color.WHITE));
		UIManager.put("ToolTip.font", new FontUIResource(UIUtils.TEXT_FONT));
        ToolTipManager.sharedInstance().setInitialDelay(100);
        ToolTipManager.sharedInstance().setReshowDelay(500);
		
		mainFrame = new JFrame(getResourceMap().getString("Application.title"));
		mainFrame.setJMenuBar(createMenubar());
		mainFrame.add(createToolBar(),BorderLayout.NORTH);
		mainFrame.add(createMainPanel(),BorderLayout.CENTER);
		mainFrame.add(createStatusBar(),BorderLayout.SOUTH);
		mainFrame.setSize(defaultWidth, defaultHeight);
		mainFrame.setLocationRelativeTo(null);
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setFrame(mainFrame);
		fileChooser = new JFileChooser();
		fileChooser.setCurrentDirectory(new File(getProperty("LastOpenDir")));
	}

	/**
	 * @return
	 */
	private JMenuBar createMenubar() {
		JMenuBar menuBar = new JMenuBar();
		JMenu fileMenu = new JMenu("File");
		menuBar.add(fileMenu );
		fileMenu.add(getAction(ACTION_OPEN_MAP_FILE));
		
		return menuBar;
	}
	
	/**
	 * @return
	 */
	private Component createToolBar() {
		JToolBar toolbar = new JToolBar();
		
		toolbar.add(getAction(ACTION_OPEN_MAP_FILE));
		toolbar.add(getAction(ACTION_TOGGLE_GRID));
		return toolbar;
	}

	/**
	 * @return
	 */
	private Component createMainPanel() {
		JTabbedPane main = new JTabbedPane();
		main.addTab("总览", createOverviewPanel());
		main.addTab("场景", createScenePanel());
		main.addTab("界面", createUIPanel());
		main.addTab("脚本", createScriptPanel());
		main.addTab("数据", createDataPanel());
		main.setSelectedIndex(1);
		return main;
	}

	/**
	 * @return
	 */
	private Component createDataPanel() {
		return null;
	}

	/**
	 * @return
	 */
	private Component createScriptPanel() {
		return null;
	}

	/**
	 * @return
	 */
	private Component createUIPanel() {
		return null;
	}

	/**
	 * @return
	 */
	private Component createScenePanel() {
		sceneEditor = new SceneEditor();
		sceneEditor.addMouseListener(sceneHandler);
		sceneEditor.addMouseMotionListener(sceneHandler);
		JScrollPane scrollpane = new JScrollPane(sceneEditor);
		scrollpane.getHorizontalScrollBar().setUnitIncrement(sceneEditor.getCellWidth());
		scrollpane.getVerticalScrollBar().setUnitIncrement(sceneEditor.getCellHeight());
		Component toolkitPanel = createScenePalette();
		
		
		//JideSplitPane scenePanel = new JideSplitPane();
		JSplitPane scenePanel = new JSplitPane();
		scenePanel.setDividerLocation(600);
		scenePanel.setLeftComponent(scrollpane);
		scenePanel.setRightComponent(toolkitPanel);
		return scenePanel;
	}

	/**
	 * @return
	 */
	private Component createScenePalette() {
		JPanel palette = new JPanel(new BorderLayout());
		final String[] characterIds = getMaker().getDataFacade().getAllCharacters();
		ListModel characterListModel = new AbstractListModel() {
			@Override
			public int getSize() {
				return characterIds.length;
			}
			
			@Override
			public Object getElementAt(int index) {
				return characterIds[index];
			}
		};
		final JList list = new JList(characterListModel);
		list.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if(e.getButton() == MouseEvent.BUTTON1 && e.getClickCount() == 2) {
					String charId = (String) list.getSelectedValue();
					addNpcToScene(charId);
				}
			}
		});
		list.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e) {
				String characterId = (String) list.getSelectedValue();
				sceneEditor.setCharacterId(characterId);
			}
		});
		list.setDragEnabled(true);
		palette.add(list,BorderLayout.CENTER);
		return new JScrollPane(palette);
	}

	/**
	 * @param charId
	 */
	protected void addNpcToScene(String characterId) {
		sceneEditor.setCharacterId(characterId);
		sceneEditor.createNewNpc();
	}

	/**
	 * @return
	 */
	private Component createOverviewPanel() {
		return null;
	}

	//--------------------- StatusBar -----------------------//
	/**
	 * @return
	 */
	private Component createStatusBar() {
		//StatusBar statusBar = new StatusBar(app, taskMonitor)
		JToolBar statusBar = new JToolBar("Status");
		//statusBar.setLayout(new FlowLayout());
		statusLabels = new  JLabel[4];
		int[] widths = new int[] {300,100,100,100};
		for (int i = 0; i < statusLabels.length; i++) {
			JLabel label = new JLabel();
			Dimension d = new Dimension(widths[i], 20);
			label.setPreferredSize(d);
			label.setSize(d);
			label.setMinimumSize(d);
			label.setMaximumSize(d);
			statusLabels[i] = label;
			statusBar.add(label);
			statusBar.addSeparator();
		}
		
		return statusBar;
	}
	/**
	 * 设置提示信息
	 * @param index
	 * @param msg
	 */
	private void setStatus(int index, Object msg) {
		statusLabels[index].setText(msg.toString());
	}
	
	//------------------------ Actions --------------------------//
	@Action
	public void openMapFile() {
//		int rtn = fileChooser.showOpenDialog(mainFrame);
//		if(rtn == JFileChooser.APPROVE_OPTION) {
//			File file = fileChooser.getSelectedFile();
//			setProperty("LastOpenDir",file.getParent());
//			this.sceneEditor.setMap(file);
//			this.sceneEditor.getParent().validate();
//		}
		
	}
	
	/**
	 * 显示、隐藏网格
	 */
	@Action
	public void toggleGrid() {
		this.sceneEditor.toggleGrid();
	}
}
