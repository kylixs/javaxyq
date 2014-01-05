/*
 * JavaXYQ Source Code
 * by kylixs
 * at 2010-3-21
 * please visit http://javaxyq.googlecode.com
 * or mail to kylixs@qq.com
 */
package com.javaxyq.data;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.Action;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.KeyStroke;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.javaxyq.core.DialogFactory;
import com.javaxyq.core.GameMain;
import com.javaxyq.core.GameWindow;
import com.javaxyq.event.Listener;
import com.javaxyq.io.CacheManager;
import com.javaxyq.util.StringUtils;

/**
 * @author Administrator
 * @date 2010-3-21 create
 */
public class XmlDataLoader {
	
	public class ActionHandler extends DefaultHandler {

		public ActionHandler() {
		}
		@Override
		public void startElement(String uri, String localName, String name,
				Attributes attributes) throws SAXException {
			if("Action".equalsIgnoreCase(name)) {
				procActionElement(attributes);
			}else if("Listener".equalsIgnoreCase(name)) {
				procListenerElement(attributes);
			}
		}
		private void procListenerElement(Attributes attributes) {
			String type = attributes.getValue("type");
			String className = attributes.getValue("class");
			System.out.println("listener: "+type+" => "+className);
			window.addListener(type, className);
		}
		private void procActionElement(Attributes attributes) {
			String actionId = attributes.getValue("id");
			String className = attributes.getValue("class");
			String shortcut = attributes.getValue("shortcut");
			System.out.println("Action: "+actionId+" => "+className);
			regAction(actionId,className);
			if(StringUtils.isNotBlank(shortcut)) {
				regShortcut(shortcut,actionId);
			}
		}
	}
	public class DialogHandler extends DefaultHandler {
		
		private String filename;
		public DialogHandler(String filename) {
			this.filename = filename;
		}
		@Override
		public void startElement(String uri, String localName, String name,
				Attributes attributes) throws SAXException {
			if("Dialog".equalsIgnoreCase(name)) {
				String id = attributes.getValue("id");
				if(StringUtils.isNotBlank(id)) {
					DialogFactory.addDialog(id, filename);
				}
				//System.out.println("Dialog: "+id+" => "+filename);
			}
		}
	}

	private SAXParser parser;
	private GameWindow window;
	private InputMap inputMap;
	private ActionMap actionMap;
	public XmlDataLoader(GameWindow window) {
		this.inputMap = window.getInputMap();
		this.actionMap = window.getActionMap();
		this.window = window;
		SAXParserFactory factory = SAXParserFactory.newInstance();
		// factory.setValidating(validating);
		try {
			parser = factory.newSAXParser();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		}
	}

	public void regShortcut(String key,String actionId){
		inputMap.put(KeyStroke.getKeyStroke(key), actionId);
	}
	
	public void regAction(String actionId, String className) {
		try {
			Action action = (Action) Class.forName(className).newInstance();
			action.putValue(Action.ACTION_COMMAND_KEY, actionId);
			actionMap.put(actionId, action);
		} catch (ClassNotFoundException e) {
			System.out.println("警告：找不到[ "+actionId+"] 的处理类 "+className);
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
	}	
	public void parseActions() {
		try {
			parser.parse(CacheManager.getInstance().getFile("xml/actions.xml"), new ActionHandler());
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void loadUI(String file) {
		try {
			parser.parse(CacheManager.getInstance().getFile(file),new DialogHandler(file));
			System.out.println("loadUI: "+file+", at "+new java.util.Date());
		} catch (Exception e) {
			System.err.println("load UI failure: "+file);
			e.printStackTrace();
		}
	}

}
