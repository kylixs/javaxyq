package com.javaxyq.event;



import java.util.EventListener;

/**
 * @author dewitt
 * 
 */
public interface SceneListener extends EventListener {

	/**
	 * 初始化场景
	 * 
	 * @param e
	 */
	void onInit(SceneEvent e);

	/**
	 * 场景加载完毕
	 * 
	 * @param e
	 */
	void onLoad(SceneEvent e);

	/**
	 * 退出场景
	 * 
	 * @param e
	 */
	void onUnload(SceneEvent e);
}
