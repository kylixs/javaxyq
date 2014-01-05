package com.javaxyq.ui;

import groovy.util.Node;
import groovy.util.XmlParser;

import java.awt.Component;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

import javax.swing.AbstractButton;
import javax.swing.Action;
import javax.swing.JLabel;
import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import com.javaxyq.config.ImageConfig;
import com.javaxyq.core.DefaultScript;
import com.javaxyq.core.DialogBuilder;
import com.javaxyq.core.ScriptEngine;
import com.javaxyq.core.SpriteFactory;
import com.javaxyq.event.PanelListener;
import com.javaxyq.io.CacheManager;
import com.javaxyq.util.StringUtils;
import com.javaxyq.util.UIUtils;


/**
 * 界面描述解析引擎
 * 
 * @author gongdewei
 * @date 2010-3-28 create
 */
public class XmlDialogBuilder implements DialogBuilder{
	
	/**  */
	private static final int WINDOW_HEIGHT = 480;
	/**  */
	private static final int WINDOW_WIDTH = 640;
	private ScriptEngine scriptEngine;
	private TooltipTemplate tooltipTemplate;
	
	public XmlDialogBuilder() {
		this.scriptEngine = DefaultScript.getInstance();
		tooltipTemplate = (TooltipTemplate) UIFactory.get(UIFactory.TOOLTIP_TEMPLATE);
	}
	
	/**
	 * 从界面描述文件创建对话框
	 * @param id 对话框id
	 * @param res 界面描述文件
	 */
	public Panel createDialog(String id, String res) {
		if(StringUtils.isBlank(id))throw new IllegalArgumentException("Dialog的id不能为空");
		if(StringUtils.isBlank(res))throw new IllegalArgumentException("Dialog["+id+"]的界面描述文件路径不能为空") ;
		System.out.println("createDialog "+id+" in "+res);
		File input = CacheManager.getInstance().getFile(res);
		try {
			Node xml = new XmlParser().parse(input);
			List<Node> nodes = xml.children();
			Node dlgNode = null;
			for (Node node : nodes) {
				if(StringUtils.equals((String) node.get("@id"),id)) {
					dlgNode = node;
					break;
				}
			}
			if(dlgNode!=null) {
				Panel dialog = processDialog(dlgNode);
				//components
				processComponents(dialog,dlgNode);
				return dialog;
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		}
		System.out.printf("创建Panel失败：%s in %s \n",id,res);
		return null;
	}

	/**
	 * 从界面描述xml字串创建对话框
	 * @param xml 界面描述的xml内容字串
	 * @return
	 */
	public Panel createDialog(String xml) {
		try {
			Node dlgEl = new XmlParser().parseText(xml);
			if(dlgEl!=null) {
				Panel dialog = processDialog(dlgEl);
				//components
				processComponents(dialog,dlgEl);
				return dialog;
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		}
		System.out.println("创建Panel失败！");
		return null;
	}

	public Panel processDialog(Node dlgEl) {
		Panel dialog = null;
		int width = Integer.valueOf((String)dlgEl.get("@width"));
		int height = Integer.valueOf((String)dlgEl.get("@height"));
		boolean isTalk =StringUtils.equals( (String) dlgEl.get("@talk"),"true");
		if(isTalk) {
			dialog = new TalkPanel(width,height);
		}else {
			dialog = new Panel(width,height);
		}
		dialog.setName((String) dlgEl.get("@id"));
		try {
			int x = Integer.valueOf((String)dlgEl.get("@x"));
			int y = Integer.valueOf((String)dlgEl.get("@y"));
			dialog.setLocation(x, y);
		}catch(Exception e) {
			try {
				int x = (WINDOW_WIDTH - width)/2;
				int y = (WINDOW_HEIGHT - height)/2;
				dialog.setLocation( x, y);
			}catch(Exception ex) {}
		}
		dialog.setLayout(null);
		String background = (String) dlgEl.get("@background");
		if(StringUtils.isNotBlank(background)) {
			dialog.setBgImage(new ImageConfig(background));
		}
		try {
			String strClosable = (String)dlgEl.get("@closable");
			if(StringUtils.isNotBlank(strClosable)) {
				boolean closable = Boolean.valueOf(strClosable);
				dialog.setClosable(closable);
			}
		}catch(Exception e) {}
		try {
			String strMovable = (String)dlgEl.get("@movable");
			if(StringUtils.isNotBlank(strMovable)) {
				boolean movable = Boolean.valueOf(strMovable);
				dialog.setMovable(movable);
			}
		}catch(Exception e) {}
		// 注册监听器
		//dialog.setActionMap(UIHelper.getActionMap());
		//dialog.setInitialAction(dlgEl.get("@initial"));
		//dialog.setDisposeAction(dlgEl.get("@dispose"));
		
		//加载面板的脚本
		PanelListener listener = scriptEngine.loadUIScript((String) dlgEl.get("@id"));
		if(listener!=null) {
			dialog.addPanelListener(listener);
		}

		return dialog;
	}
	
	public void processComponents(Panel dialog,Node dlgNode) {
		List<Node> nodes = dlgNode.children();
		for(Node node : nodes) {
			try {
				Component comp = (Component) this.invokeMethod ("process"+(String)node.name(),dialog,node);
				if(comp!=null) {
					//绑定事件
					String[] events = {"mousePressed","mouseReleased","mouseClicked","keyPressed","keyReleased","keyTyped"};
					for (String type : events) {
						String actionId = (String) node.get("@"+type);
						if(actionId!=null) {
							dialog.bindAction(comp,type,actionId);
						}
					}
				}
			}catch(Exception e) {
				System.err.println("处理控件失败：");
				node.print(new PrintWriter(System.err));
				e.printStackTrace();
			}
		}		
	}


	public AbstractButton processButton(Panel dialog,Node el) {
		boolean toggle = Boolean.valueOf((String) el.get("@toggle"));
		String actionId = (String) el.get("@actionId");
		AbstractButton btn = null;
		if(toggle) {
			btn = new ToggleButton();
		}else {
			btn = new Button();
		}
		if(actionId!=null ) {
//			Action action = dialog.actionMap.get(actionId);
//			if (!action) {
//				try {
//					String wildcard = actionId.substring(0, actionId.lastIndexOf(".")) + ".*";
//					action = dialog.actionMap.get(wildcard);
//				}catch(e) {}
//			}
//			if (!action) {
//				System.out.println("Warning: Action not found, actionId=$actionId");;
//			}else {
//				btn.setAction(action);
//			}
//			//set cmd after action
			btn.setActionCommand(actionId);
		}
		btn.setText( (String) el.get("@text"));
		try {
			String strEnable = (String) el.get("@enable");
			if(StringUtils.isNotBlank(strEnable)) {
				btn.setEnabled(Boolean.valueOf(strEnable));
			}
		}catch(Exception e) {}
		try {
			int x = Integer.valueOf((String)el.get("@x"));
			int y = Integer.valueOf((String)el.get("@y"));
			btn.setLocation(x, y);
		}catch(Exception e) {}
		btn.setName( (String) el.get("@name"));
		if (btn instanceof Button) {
			Button button = (Button) btn;
			button.init(SpriteFactory.loadSprite((String) el.get("@was")));
			//btn.setToolTipText(el.get("@tooltip"));
			String tooltip = (String) el.get("@tooltip");
			if(tooltip!=null) {
				button.setTooltipTpl(tooltip);
				button.setTemplate(tooltipTemplate);
			}
		}else if (btn instanceof ToggleButton) {
			ToggleButton tbtn = (ToggleButton) btn;			
			tbtn.init(SpriteFactory.loadSprite((String) el.get("@was")));
			//btn.setToolTipText(el.get("@tooltip"));
			String tooltip = (String) el.get("@tooltip");
			if(tooltip!=null) {
				tbtn.setToolTipText(tooltip);
			}
		}
		dialog.add(btn);
		return btn;
	}

	public Label processText(Panel dialog,Node el) {
		Label label = new Label((String) el.get("@text"));
		label.setName((String) el.get("@name"));
		try {
			int x = Integer.valueOf((String)el.get("@x"));
			int y = Integer.valueOf((String)el.get("@y"));
			label.setLocation(x, y);
		}catch(Exception e) {}
		try {
			int width = Integer.valueOf((String)el.get("@width"));
			int height = Integer.valueOf((String)el.get("@height"));
			label.setSize(width,height);
		}catch(Exception e) {
			label.setSize(100,20);
		}
		label.setPreferredSize(label.getSize());
		String color = (String) el.get("@color");
		String align = (String) el.get("@align");
		if (color!=null) {
			label.setForeground(UIUtils.getColor(color));
		}
		if (align != null) {
			if (align.equals("center")) {
				label.setHorizontalAlignment(JLabel.CENTER);
			} else if (align.equals("right")) {
				label.setHorizontalAlignment(JLabel.RIGHT);
			}
		}
		String tooltip = (String) el.get("@tooltip");
		if(tooltip != null) {
			label.setTooltipTpl(tooltip);
			label.setTemplate(tooltipTemplate);
		}
		dialog.add(label);
		return label;
	}
	
	public ItemLabel processItem(Panel dialog,Node el) {
		ItemLabel label = new ItemLabel();
		label.setName((String) el.get("@name"));
		try {
			int x = Integer.valueOf((String)el.get("@x"));
			int y = Integer.valueOf((String)el.get("@y"));
			label.setLocation(x, y);
		}catch(Exception e) {}
		try {
			int width = Integer.valueOf((String)el.get("@width"));
			int height = Integer.valueOf((String)el.get("@height"));
			label.setSize(width,height);
		}catch(Exception e) {
			label.setSize(100,20);
		}
		label.setPreferredSize(label.getSize());
		String tooltip = (String) el.get("@tooltip");
		if(tooltip != null) {
			label.setTooltipTpl(tooltip);
			label.setTemplate(tooltipTemplate);
		}
		dialog.add(label);
		return label;
	}

	public RichLabel processRichText(Panel dialog,Node el) {
		RichLabel label = new RichLabel((String) el.get("@text"));
		label.setName((String) el.get("@name"));
		try {
			int x = Integer.valueOf((String)el.get("@x"));
			int y = Integer.valueOf((String)el.get("@y"));
			label.setLocation(x, y);
		}catch(Exception e) {}
		try {
			int width = Integer.valueOf((String)el.get("@width"));
			int height = Integer.valueOf((String)el.get("@height"));
			label.setSize(width,height);
		}catch(Exception e) {
			label.setSize(100,20);
		}
		String tooltip = (String) el.get("@tooltip");
		if(tooltip != null)label.setToolTipText(tooltip);
		dialog.add(label);
		return label;
	}
	
	public Label processSprite(Panel dialog,Node el) {
		int index = 0;
		try {
			index = Integer.valueOf((String) el.get("@index"));
		} catch (Exception e) {
		}
		
		Label label = new Label(SpriteFactory.loadAnimation((String) el.get("@path"), index));
		try {
			int x = Integer.valueOf((String)el.get("@x"));
			int y = Integer.valueOf((String)el.get("@y"));
			label.setLocation(x, y);
		}catch(Exception e) {}
		try {
			int width = Integer.valueOf((String)el.get("@width"));
			int height = Integer.valueOf((String)el.get("@height"));
			label.setSize(width,height);
		}catch(Exception e) {
			//label.setSize(100,20); 
		}
		label.setName((String) el.get("@name"));		
		String tooltip = (String) el.get("@tooltip");
		if(tooltip != null) {
			label.setTooltipTpl(tooltip);
			label.setTemplate(tooltipTemplate);
		}
		dialog.add(label);
		return label;
	}
	
	public void processImage(Panel dialog,Node el) {
		ImageConfig cfg = new ImageConfig((String) el.get("@path"));
		cfg.setId((String) el.get("@id"));
		try {
			cfg.setX(Integer.valueOf((String)el.get("@x")));
			cfg.setY(Integer.valueOf((String)el.get("@y")));
		}catch(Exception e) {}
		try {
			cfg.setWidth(Integer.valueOf((String)el.get("@width")));
			cfg.setHeight(Integer.valueOf((String)el.get("@height")));
		}catch(Exception e) {}
		dialog.addImage(cfg);
	}
	
	public TextField processEditor(Panel dialog,Node el) {
		TextField editor = new TextField();
		editor.setName((String) el.get("@name"));
		String actionId = (String) el.get("@actionId");
		try {
			int x = Integer.valueOf((String)el.get("@x"));
			int y = Integer.valueOf((String)el.get("@y"));
			editor.setLocation(x, y);
		}catch(Exception e) {}
		try {
			int width = Integer.valueOf((String)el.get("@width"));
			int height = Integer.valueOf((String)el.get("@height"));
			editor.setSize(width,height);
		}catch(Exception e) {
			editor.setSize(100,20);
		}
		if (actionId != null) {
//			Action action = dialog.actionMap.get(actionId);
//			editor.addActionListener(action);
			editor.setActionCommand(actionId);
		}
		String foreground = (String) el.get("@foreground");
		if(foreground != null) {
			editor.setForeground(UIUtils.getColor(foreground));
		}
		editor.setToolTipText((String) el.get("@tooltip"));
		
		dialog.add(editor);
		return editor;
	}
	
	public void processAction(Panel dialog,Node el) {
		String actionId = (String) el.get("@id");
		String className = (String) el.get("@class");
		try {
			Action action = (Action) Class.forName(className).newInstance();
			action.putValue(Action.ACTION_COMMAND_KEY, actionId);
			dialog.getActionMap().put(actionId, action);
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
				
	}
	/**
	 * invoke a  method
	 * @param mName method name
	 * @param arg argument 
	 * @return
	 * @throws InvocationTargetException 
	 * @throws IllegalAccessException 
	 * @throws IllegalArgumentException 
	 * @throws NoSuchMethodException 
	 * @throws SecurityException 
	 */
	private Object invokeMethod(String mName, Object... arg) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException, SecurityException, NoSuchMethodException {
		Method m = this.getClass().getMethod(mName, arg[0].getClass(),arg[1].getClass());
		return m.invoke(this, arg);
	}	
}