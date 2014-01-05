package com.javaxyq.core;

import groovy.lang.GroovyClassLoader;
import groovy.lang.GroovyCodeSource;

import java.io.File;
import java.io.IOException;
import java.util.EventListener;

import org.codehaus.groovy.control.CompilationFailedException;

import com.javaxyq.event.ActionEvent;
import com.javaxyq.event.PanelListener;
import com.javaxyq.event.SceneListener;
import com.javaxyq.io.CacheManager;

public class GroovyScript implements ScriptEngine {

	private static GroovyScript instance = new GroovyScript();
	private GroovyClassLoader groovyCl = new GroovyClassLoader(GroovyScript.class.getClassLoader());
	
	private boolean debug;
	public static GroovyScript getInstance() {
		return instance;
	}
	private GroovyScript() {
	}
	public boolean isDebug() {
		return debug;
	}

	public void setDebug(boolean debug) {
		this.debug = debug;
	}

	public void loadScripts() {

	}

	public <T> T loadClass(String filename,Class<T> clazz) {
		try {
			//不缓存动态加载的脚本类
			File file = CacheManager.getInstance().getFile(filename);
			if(file!=null && file.exists()) {
				Class<T> groovyClass = groovyCl.parseClass(new GroovyCodeSource(file),false);
				return groovyClass.newInstance();
			}
		} catch (CompilationFailedException e) {
			System.err.println("Error: 脚本编译失败！"+filename);
			e.printStackTrace();
		} catch (IOException e) {
			System.err.println("Warning: 加载脚本失败，找不到脚本文件!"+filename);
			//e.printStackTrace();
		} catch (Exception e) {
			System.err.println("Error: 加载脚本失败! "+filename);
			e.printStackTrace();
		}
		return null;
	}
	
	public Object loadClass(String filename) {
		try {
			//不缓存动态加载的脚本类
			File file = CacheManager.getInstance().getFile(filename);
			if(file!=null && file.exists()) {
				Class groovyClass = groovyCl.parseClass(new GroovyCodeSource(file),!isDebug());
				return groovyClass.newInstance();
			}
		} catch (CompilationFailedException e) {
			System.err.println("Error: 脚本编译失败！"+filename);
			e.printStackTrace();
		} catch (IOException e) {
			System.err.println("Warning: 加载脚本失败，找不到脚本文件!"+filename);
			//e.printStackTrace();
		} catch (Exception e) {
			System.err.println("Error: 加载脚本失败! "+filename);
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 加载UI脚本
	 * @param id
	 * @return
	 */
	public PanelListener loadUIScript(String id) {
		return (PanelListener) loadClass(String.format("ui/%s.groovy",id));
	}

	public static void main(String[] args) {
		PanelListener listener = GroovyScript.getInstance().loadClass("scripts/ui/system.mainwin.groovy",PanelListener.class);
		listener.initial(null);
		listener.actionPerformed(new ActionEvent(listener, "autoaddhp"));
		listener.dispose(null);
		
	}
	@Override
	public EventListener loadNPCScript(String npcId) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public SceneListener loadSceneScript(String npcId) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public void clearCache() {
		// TODO Auto-generated method stub
		
	}

}