package com.javaxyq.core;

import java.util.EventListener;

import com.javaxyq.event.PanelListener;
import com.javaxyq.event.SceneListener;

public interface ScriptEngine {

	boolean isDebug();

	void setDebug(boolean debug);

	void clearCache();

	<T> T loadClass(String className, Class<T> clazz);

	Object loadClass(String className);

	PanelListener loadUIScript(String id);

	EventListener loadNPCScript(String npcId);

	SceneListener loadSceneScript(String sceneId);
}