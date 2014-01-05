/*
 * JavaXYQ Source Code
 * by kylixs
 * at 2010-3-4
 * please visit http://javaxyq.googlecode.com
 * or mail to kylixs@qq.com
 */
package com.javaxyq.event;

import java.util.EventListener;

/**
 * 网络资源下载监听器
 * @author gongdewei
 * @date 2010-3-4 create
 */
public interface DownloadListener extends EventListener {

	/**
	 * 下载开始
	 * @param e
	 */
	void downloadStarted(DownloadEvent e);
	/**
	 * 下载完成
	 * @param e
	 */
	void downloadCompleted(DownloadEvent e);
	/**
	 * 下载中断
	 * @param e
	 */
	void downloadInterrupted(DownloadEvent e);
	/**
	 * 下载进度更新
	 * @param e
	 */
	void downloadUpdate(DownloadEvent e);
}
