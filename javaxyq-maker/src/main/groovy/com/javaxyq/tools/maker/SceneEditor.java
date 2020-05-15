/*
 * JavaXYQ Source Code
 * by kylixs
 * at 2009-12-5
 * please visit http://javaxyq.googlecode.com
 * or mail to kylixs@qq.com
 */
package com.javaxyq.tools.maker;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.SQLException;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.border.Border;
import javax.swing.text.BadLocationException;

import org.jdesktop.application.Action;
import org.jdesktop.application.Application;
import org.jdesktop.application.ApplicationContext;
import org.jdesktop.application.SingleFrameApplication;

import com.javaxyq.core.SpriteFactory;
import com.javaxyq.data.PreexistingEntityException;
import com.javaxyq.data.SceneNpc;
import com.javaxyq.tools.JMap;
import com.javaxyq.ui.Label;
import com.javaxyq.util.UIUtils;
import com.javaxyq.widget.Animation;
import com.jidesoft.dialog.ButtonPanel;
import com.jidesoft.dialog.StandardDialog;
import com.jidesoft.document.DocumentComponent;
import com.jidesoft.document.DocumentPane;
import com.jidesoft.editor.CodeEditor;
import com.jidesoft.editor.SyntaxDocument;
import com.jidesoft.editor.tokenmarker.JavaTokenMarker;
import com.jidesoft.swing.JideTabbedPane;

/**
 * 场景编辑器
 * 
 * @author dewitt
 * @date 2009-12-5 create
 */
public class SceneEditor extends JPanel implements MouseListener {

	private static final long serialVersionUID = 1L;
	private JMap map;
	private CellPanel cellPanel;
	private boolean gridShowing = true;
	private SceneLayer eventLayer;
	private SceneLayer topLayer;
	private CharacterChooser spriteChooser;
	private int cellWidth = 20;
	private int cellHeight = 20;
	
	private String characterId = "1001";
	private String sceneId;
	
	private String scriptTemplate;
	
	private Border spriteHoverBorder = BorderFactory.createLineBorder(Color.RED);
	private Border spriteBorder = BorderFactory.createEmptyBorder(1, 1, 1, 1);

	public SceneEditor() {
		initGUI();
	}

	/**
	 * 初始化GUI
	 * 
	 * @return
	 */
	private void initGUI() {
		setLayout(null);
		this.map = new JMap();
		cellPanel = new CellPanel();
		eventLayer = new SceneLayer();
		//eventLayer.setComponentPopupMenu(createEventLayerMenu());
		add(eventLayer);
		add(cellPanel);
		add(this.map);

		setTopLayer(eventLayer);
		
		addMouseListener(this);
		
		spriteChooser = new CharacterChooser(((SingleFrameApplication)Application.getInstance()).getMainFrame());
	}

	/**
	 * 获取Action实例
	 * 
	 * @param name
	 * @return
	 */
	private javax.swing.Action getAction(String name) {
		ApplicationContext context = Application.getInstance().getContext();
		return context.getActionMap(this).get(name);
	}

	/**
	 * @return
	 */
	private JPopupMenu createEventLayerMenu() {
		JPopupMenu popupMenu = new JPopupMenu();
		popupMenu.add(getAction("placeNpc"));
		return popupMenu;
	}

	public CellPanel getCellPanel() {
		return cellPanel;
	}

	public int getCellHeight() {
		return cellPanel.getCellHeight();
	}

	public int getCellWidth() {
		return cellPanel.getCellWidth();
	}

	public void setCellHeight(int cellHeight) {
		cellPanel.setCellHeight(cellHeight);
	}

	public void setCellWidth(int cellWidth) {
		cellPanel.setCellWidth(cellWidth);
	}

	/**
	 * 显示、隐藏网格
	 */
	public void toggleGrid() {
		gridShowing = !gridShowing;
		cellPanel.setVisible(gridShowing);
	}

	/**
	 * 当前是否显示网格
	 * 
	 * @return the gridShowing
	 */
	public boolean isGridShowing() {
		return gridShowing;
	}

	/**
	 * 设置最顶层的图层（置顶后可接收鼠标、键盘操作）
	 * 
	 * @param layer
	 */
	private void setTopLayer(SceneLayer layer) {
		if (this.topLayer != null) {
			removeListeners();
		}
		this.topLayer = layer;
		installListeners();
	}

	public String getCharacterId() {
		return characterId;
	}

	public void setCharacterId(String characterId) {
		this.characterId = characterId;
	}

	/**
	 * 删除topLayer的鼠标事件监听器
	 */
	private void removeListeners() {
		MouseListener[] listeners1 = getListeners(MouseListener.class);
		MouseMotionListener[] listeners2 = getListeners(MouseMotionListener.class);
		for (int i = 0; i < listeners1.length; i++) {
			topLayer.removeMouseListener(listeners1[i]);
		}
		for (int i = 0; i < listeners2.length; i++) {
			topLayer.removeMouseMotionListener(listeners2[i]);
		}
	}

	/**
	 * 给topLayer添加监听器
	 */
	private void installListeners() {
		MouseListener[] listeners1 = getListeners(MouseListener.class);
		MouseMotionListener[] listeners2 = getListeners(MouseMotionListener.class);
		for (int i = 0; i < listeners1.length; i++) {
			topLayer.addMouseListener(listeners1[i]);
		}
		for (int i = 0; i < listeners2.length; i++) {
			topLayer.addMouseMotionListener(listeners2[i]);
		}

	}

	public void addMouseListener(MouseListener l) {
		super.addMouseListener(l);
		topLayer.addMouseListener(l);
	}

	public void addMouseMotionListener(MouseMotionListener l) {
		super.addMouseMotionListener(l);
		topLayer.addMouseMotionListener(l);
	}

	public void removeMouseListener(MouseListener l) {
		super.removeMouseListener(l);
		topLayer.removeMouseListener(l);
	}

	public void removeMouseMotionListener(MouseMotionListener l) {
		super.removeMouseMotionListener(l);
		topLayer.removeMouseMotionListener(l);
	}

	public Point getSelectedCell() {
		return eventLayer.getSelectedCell();
	}
	
	public Point getSelectingCell() {
		return eventLayer.getSelectingCell();
	}


	// ------------------------- Actions --------------------------------//
	/**
	 * 放置npc、触发器等
	 */
	@Action
	public void createNewNpc() {
		try {
			Point cell = eventLayer.getSelectedCell();
			if(cell == null) {
				cell = new Point(10, 10);
			}
			SceneNpc npc = createNpc(characterId,sceneId, cell.x,cell.y, "NPC");
			replaceNpc(npc);
		} catch (Exception e) {
			e.printStackTrace();
			UIUtils.showError("创建NPC失败！",e);
		}
	}
	
	public void replaceNpc(SceneNpc npc) {
		try {
			int npcId = npc.getId();
			String characterId = npc.getCharacterId();
			int sceneX = npc.getSceneX();
			int sceneY = npc.getSceneY();
			System.out.println("replace "+characterId+" at (" + sceneX + "," + sceneY + ")");
			
			String filename = "shape/char/"+characterId+"/stand.tcp";
			Animation anim = SpriteFactory.loadAnimation(filename);
			Label label = new Label(anim);
			label.setHorizontalTextPosition(Label.CENTER);
			label.setVerticalTextPosition(Label.BOTTOM);
			label.setFont(UIUtils.TEXT_NAME_FONT);
			//label.setForeground(GameMain.COLOR_NAME);
			label.setForeground(UIUtils.COLOR_NAME);
			label.setBorder(spriteBorder);
			label.setName(String.valueOf(npcId));
			label.setText(npc.getName());
			Dimension prefSize = label.getPreferredSize();
			label.setSize(prefSize);
			Point pos = sceneToLocal(sceneX,sceneY);
			pos.translate(-anim.getRefPixelX(), -anim.getRefPixelY());
			label.setLocation(pos);
			draggable(label);
			eventLayer.add(label);
		} catch (Exception e) {
			e.printStackTrace();
			UIUtils.showError("还原NPC失败！"+npc,e);
		}
	}
	
	/**
	 * 设置当前场景
	 */
	public void setScene(String sceneId) {
		this.sceneId = sceneId;
		File file = new File("scene/"+sceneId+".map");
		this.map.loadMap(file);
		this.cellPanel.setSize(this.map.getWidth(), this.map.getHeight());
		this.eventLayer.setSize(this.map.getWidth(), this.map.getHeight());
		this.eventLayer.clearStatus();
		this.setPreferredSize(this.cellPanel.getSize());
		this.setSize(this.cellPanel.getSize());
		reloadSceneNpcs(sceneId);
	}


	private void reloadSceneNpcs(String sceneId) {
		List<SceneNpc> npcs;
		try {
			npcs = getDataFacade().findNpcsBySceneId(Integer.valueOf(sceneId));
			for (SceneNpc _npc : npcs) {
				replaceNpc(_npc);
			}	
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private SceneNpc createNpc(String characterId, String sceneId, int sceneX, int sceneY, String name) throws PreexistingEntityException, Exception {
		SceneNpc npcVO = new SceneNpc(0, Integer.parseInt(sceneId), characterId, name, sceneX, sceneY, "state=stand;");
		int npcId = getDataFacade().createSceneNpc(npcVO);
		return npcVO;
	}
	
	private void moveNpc(int npcId,int x,int y) {
		try {
			System.out.println("moveNpc: "+npcId+", "+x+", "+y);
			DataFacade dataFacade = getDataFacade();
			SceneNpc npcVO = dataFacade.findSceneNpc(npcId);
			npcVO.setSceneX(x);
			npcVO.setSceneY(y);
			dataFacade.updateSceneNpc(npcVO);
		} catch (Exception e) {
			e.printStackTrace();
			UIUtils.showError("移动npc失败！npcId="+npcId+", x="+x+", y="+y, e);
		}
	}

	private DataFacade getDataFacade() {
		return Application.getInstance(GameMaker.class).getDataFacade();
	}
	
	private JFrame getMainFrame() {
		return Application.getInstance(GameMaker.class).getMainFrame();
	}

	/**
	 * @param label
	 */
	protected void draggable(Label label) {
		MouseAdapter handler = new MouseAdapter() {
			private Point orginLocation;
			@Override
			public void mousePressed(MouseEvent e) {
				if(e.getButton()==MouseEvent.BUTTON1) {
					orginLocation = e.getPoint();
				}
			}
			@Override
			public void mouseReleased(MouseEvent e) {
				if(e.getButton()==MouseEvent.BUTTON1 && orginLocation!=null) {
					Label label = (Label) e.getSource();
					Point location = label.getLocation();
					location.translate(e.getX() - orginLocation.x, e.getY() - orginLocation.y);
					label.setLocation(location);
					Animation anim = label.getAnim();
					Point scenePos = localToScene(location.x+anim.getRefPixelX(),location.y+anim.getRefPixelY());
					moveNpc(Integer.valueOf(label.getName()),scenePos.x,scenePos.y);
				}
				orginLocation = null;
			}
			@Override
			public void mouseDragged(MouseEvent e) {
				if(orginLocation!=null) {
					Label label = (Label) e.getSource();
					Point location = label.getLocation();
					location.translate(e.getX() - orginLocation.x, e.getY() - orginLocation.y);
					label.setLocation(location);
				}
			}
			@Override
			public void mouseClicked(MouseEvent evt) {
				if(evt.getButton()==MouseEvent.BUTTON3) {
					Label label = (Label) evt.getSource();
					String npcId = label.getName();
					try {
						getDataFacade().deleteSceneNpc(Integer.valueOf(npcId));
						label.getParent().remove(label);
					} catch (Exception ex) {
						ex.printStackTrace();
						UIUtils.showError("移除NPC失败！npcId="+npcId, ex);
					}
					evt.consume();
					return;
				}
				if(evt.getButton()==MouseEvent.BUTTON1) {
					//单击npc
					if (evt.getSource() instanceof Label) {
						Label label = (Label) evt.getSource();
						String npcId = label.getName();
						if(evt.getClickCount()==2) {//双击npc
							showScriptDialog(npcId);
							return;
						}
						
					}
				}
			}
			@Override
			public void mouseEntered(MouseEvent e) {
				Label label = (Label) e.getComponent();
				label.setBorder(spriteHoverBorder);
				//tooltip
				Point location = label.getLocation();
				Animation anim = label.getAnim();
				Point scenePos = localToScene(location.x+anim.getRefPixelX(),location.y+anim.getRefPixelY());
				label.setToolTipText(label.getName()+" ("+scenePos.x+","+scenePos.y+")");
			}
			@Override
			public void mouseExited(MouseEvent e) {
				Label label = (Label) e.getComponent();
				label.setBorder(spriteBorder);
			}
		};
		label.addMouseListener(handler);
		label.addMouseMotionListener(handler);
	}

	/**
	 * 编辑npc脚本
	 * @param npcId
	 */
	protected void showScriptDialog(final String npcId) {
		JFrame frame = getMainFrame();
		final DocumentPane documentPane = createSourceCodePanel(npcId);
		StandardDialog dialog = new StandardDialog(frame, "编辑NPC脚本", false) {
			@Override
			public JComponent createBannerPanel() {
				return null;
			}

			@Override
			public JComponent createContentPanel() {
				JPanel panel = new JPanel(new BorderLayout());
				panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
				panel.add(documentPane);
				panel.setPreferredSize(new Dimension(600, 500));
				return panel;
			}

			@Override
			public ButtonPanel createButtonPanel() {
				JButton okButton = new JButton();
				okButton.setName(OK);
				okButton.setAction(new AbstractAction("OK") {
					@Override
					public void actionPerformed(ActionEvent e) {
						DocumentComponent comp = documentPane.getDocumentAt(0);
						CodeEditor editor = (CodeEditor) comp.getComponent();
						storeScript(npcId,editor.getText());
						setVisible(false);
						dispose();
					}

				});

				JButton closeButton = new JButton();
				closeButton.setName(CLOSE);
				closeButton.setAction(new AbstractAction("Cancel") {
					public void actionPerformed(ActionEvent e) {
						setDialogResult(RESULT_AFFIRMED);
						setVisible(false);
						dispose();
					}
				});

				setDefaultCancelAction(closeButton.getAction());
				ButtonPanel panel = new ButtonPanel();
				panel.addButton(okButton);
				panel.addButton(closeButton);
				return panel;
			}
		};
		dialog.pack();
		dialog.setLocationRelativeTo(frame);
		dialog.setVisible(true);
	}
    public DocumentPane createSourceCodePanel(String npcId) {
    	String filename = "scripts/npc/n"+npcId+".java";
        DocumentPane pane = new DocumentPane();
        pane.openDocument(new DocumentComponent(createTextComponent(npcId,filename), filename));
        pane.setTabbedPaneCustomizer(new DocumentPane.TabbedPaneCustomizer() {
            public void customize(JideTabbedPane tabbedPane) {
                tabbedPane.setTabPlacement(JideTabbedPane.BOTTOM);
            }
        });
        return pane;
    }
    public JComponent createTextComponent(String npcId,String filename) {
        CodeEditor area = new CodeEditor();
//        area.setFont(new Font("Monospaced", Font.PLAIN, 12));
        SyntaxDocument doc = new SyntaxDocument();
        try {
			// try to start reading
			InputStream in = new FileInputStream(filename);
			if (in != null) {
				byte[] buff = new byte[4096];
				int nch;
				while ((nch = in.read(buff, 0, buff.length)) != -1) {
					doc.insertString(doc.getLength(), new String(buff, 0, nch), null);
				}
				area.setDocument(doc);
			}
		} catch (FileNotFoundException ex) {
			//创建脚本
			area.setText(createScript(npcId));
		} catch (IOException ex) {
			ex.printStackTrace();
			UIUtils.showError("读取脚本失败！", ex);
		} catch (BadLocationException ex) {
			ex.printStackTrace();
			UIUtils.showError("渲染脚本失败！", ex);
		}
        area.setTokenMarker(new JavaTokenMarker());
        //area.setTokenMarker(new GroovyTokenMarker());
        return area;
    }    
	/**
	 * @param npcId
	 * @return
	 */
	private String createScript(String npcId) {
		try {
			if(scriptTemplate == null) {
				loadScriptTemplate(); 
			}
			return String.format(scriptTemplate,npcId,new java.util.Date());
		} catch (IOException e) {
			e.printStackTrace();
			UIUtils.showError("创建NPC脚本模板失败！", e);
		}
		return null;
	}

	/**
	 * 保存npc脚本
	 * @param npcId
	 * @param content
	 */
	private void storeScript(String npcId, String content) {
		try {
			if(content!=null) {
				String filename = "scripts/npc/n"+npcId+".java";
				FileOutputStream fos = new FileOutputStream(filename);
				fos.write(content.getBytes());
				fos.flush();
				System.out.println("保存NPC脚本："+filename);
			}
		} catch (Exception e) {
			e.printStackTrace();
			UIUtils.showError("保存npc脚本失败！"+e.getMessage(), e);
		}
	}
	/**
	 * @throws IOException 
	 * 
	 */
	private void loadScriptTemplate() throws IOException {
		StringBuilder sb =new StringBuilder(1024);
		BufferedReader br = new BufferedReader(new InputStreamReader(GameMaker.class.getResourceAsStream("resources/npcscript.txt")));
		String tmp = null;
		while((tmp=br.readLine())!=null) {
			sb.append(tmp);
			sb.append("\r\n");
		}
		scriptTemplate = sb.toString();
	}

	private Point sceneToLocal(int x,int y) {
		return new Point(x*cellWidth, getHeight()-y*cellHeight);
	}
	private Point localToScene(int x,int y) {
		return new Point(x/cellWidth, (getHeight()-y)/cellHeight);
	}
	
	// --------------------- Listeners -----------------------------//
	public void mouseClicked(MouseEvent e) {
		if(e.getButton()==MouseEvent.BUTTON1 && e.getClickCount()==2) {
			createNewNpc();
			
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
