package com.javaxyq.core;

import groovy.lang.GroovyClassLoader;
import groovy.lang.GroovyCodeSource;

import java.io.File;
import java.io.IOException;
import java.util.EventListener;

import lombok.extern.slf4j.Slf4j;
import org.codehaus.groovy.control.CompilationFailedException;

import com.javaxyq.event.ActionEvent;
import com.javaxyq.event.PanelListener;
import com.javaxyq.event.SceneListener;
import com.javaxyq.io.CacheManager;

@Slf4j
public class GroovyScript implements ScriptEngine {

    private static final GroovyScript INSTANCE = new GroovyScript();

    private final GroovyClassLoader groovyCl = new GroovyClassLoader(GroovyScript.class.getClassLoader());

    private boolean debug;

    public static GroovyScript getINSTANCE() {
        return INSTANCE;
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

    public <T> T loadClass(String className, Class<T> clazz) {
        try {
            //不缓存动态加载的脚本类
            File file = CacheManager.getInstance().getFile(className);
            if (file != null && file.exists()) {
                Class<T> groovyClass = groovyCl.parseClass(new GroovyCodeSource(file), false);
                return groovyClass.newInstance();
            }
        } catch (CompilationFailedException e) {
            log.error("Error: 脚本编译失败！" + className, e);
        } catch (IOException e) {
            log.error("Warning: 加载脚本失败，找不到脚本文件!" + className, e);
        } catch (Exception e) {
            log.error("Error: 加载脚本失败! " + className,e );
        }
        return null;
    }

    public Object loadClass(String className) {
        try {
            //不缓存动态加载的脚本类
            File file = CacheManager.getInstance().getFile(className);
            if (file != null && file.exists()) {
                Class groovyClass = groovyCl.parseClass(new GroovyCodeSource(file), !isDebug());
                return groovyClass.newInstance();
            }
		} catch (CompilationFailedException e) {
			log.error("Error: 脚本编译失败！" + className, e);
		} catch (IOException e) {
			log.error("Warning: 加载脚本失败，找不到脚本文件!" + className, e);
		} catch (Exception e) {
			log.error("Error: 加载脚本失败! " + className,e );
		}
        return null;
    }

    /**
     * 加载UI脚本
     */
    public PanelListener loadUIScript(String id) {
        return (PanelListener) loadClass(String.format("ui/%s.groovy", id));
    }

    public static void main(String[] args) {
        PanelListener listener = GroovyScript.getINSTANCE().loadClass("scripts/ui/system.mainwin.groovy", PanelListener.class);
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