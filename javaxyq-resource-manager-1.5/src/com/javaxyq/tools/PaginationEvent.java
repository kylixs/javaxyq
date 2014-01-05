/*
 * JavaXYQ Engine 
 * 
 * javaxyq@2008 all rights. 
 * http://www.javaxyq.com
 */

package com.javaxyq.tools;

import java.util.EventObject;

/**
 * @author 龚德伟
 * @history 2008-6-28 龚德伟 新建
 */
public class PaginationEvent extends EventObject {

    private int pageNo;

    private int pageSize;

    public PaginationEvent(Object source, int pageNo, int pageSize) {
        super(source);
        this.pageNo = pageNo;
        this.pageSize = pageSize;
    }

    public int getPageNo() {
        return pageNo;
    }

    public int getPageSize() {
        return pageSize;
    }
}
