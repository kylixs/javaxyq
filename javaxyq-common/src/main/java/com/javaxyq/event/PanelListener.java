/*
 * JavaXYQ Source Code
 * by kylixs
 * at 2009-11-26
 * please visit http://javaxyq.googlecode.com
 * or mail to kylixs@qq.com
 */
package com.javaxyq.event;

import java.util.EventListener;

/**
 * 面板事件监听器
 * 
 * @author dewitt
 * @date 2009-11-26 create
 */
public interface PanelListener extends EventListener {

	/**
	 * 初始化（每次显示时调用）
	 */
	void initial(PanelEvent evt);

	/**
	 * 注销（关闭时调用）
	 */
	void dispose(PanelEvent evt);

	/**
	 * 更新面板的数据
	 */
	void update(PanelEvent evt);

	/**
	 * 关闭窗口
	 */
	void close(ActionEvent evt);

	/**
	 * 显示帮助信息
	 */
	void help(ActionEvent evt);

	/**
	 * 按钮按下、点击Label等事件
	 */
	void actionPerformed(ActionEvent evt);

}
