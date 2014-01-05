/*
 * JavaXYQ Source Code
 * by kylixs
 * at 2010-3-4
 * please visit http://javaxyq.googlecode.com
 * or mail to kylixs@qq.com
 */
package com.javaxyq.event;

import java.net.URL;
import java.util.EventObject;

/**
 * 下载事件对象
 * @author gongdewei
 * @date 2010-3-4 create
 */
public class DownloadEvent extends EventObject {

	private static final long serialVersionUID = -4581277706915926395L;
	
	public static final int DOWNLOAD_STARTED = 1;
	public static final int DOWNLOAD_COMPLETED = 2;
	public static final int DOWNLOAD_INTERRUPTED = 3;
	public static final int DOWNLOAD_UPDATE= 4;
	
	private String resource;
	private int size;
	private int received;

	private int id;
	
	public int getSize() {
		return size;
	}
	public int getReceived() {
		return received;
	}
	/**
	 * @return the resource
	 */
	public String getResource() {
		return resource;
	}
	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}
	/**
	 * @param source
	 */
	public DownloadEvent(Object source,int id,  String resource) {
		this(source,id,resource,-1,0);
	}
	
	public DownloadEvent(Object source,int id, String resource, int size, int received) {
		super(source);
		this.id = id;
		this.resource = resource;
		this.size = size;
		this.received = received;
	}


}
