package com.javaxyq.event;


/**
 * @author 龚德伟
 * @history 2008-5-10 龚德伟 新建
 */
public class Listener {
    private String type;
    private Class handler; 
    private Object instance;
    
    public Listener(String type,Class handler) {
        this.type = type;
        this.handler = handler;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Class getHandler() {
        return handler;
    }

    public void setHandler(Class handler) {
        this.handler = handler;
    }
    
	/**
	 * 获取处理类实例
	 * @return
	 * @throws IllegalAccessException 
	 * @throws InstantiationException 
	 */
	public Object getInstance() throws InstantiationException, IllegalAccessException {
		if(instance==null) {
			instance = handler.newInstance();
		}
		return instance;
	}
    
}
