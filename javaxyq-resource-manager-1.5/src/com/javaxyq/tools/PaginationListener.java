/*
 * JavaXYQ Engine 
 * 
 * javaxyq@2008 all rights. 
 * http://www.javaxyq.com
 */

package com.javaxyq.tools;

import java.util.EventListener;

/**
 * @author 龚德伟
 * @history 2008-6-28 龚德伟 新建
 */
public interface PaginationListener extends EventListener {

    void loadPage(PaginationEvent e);
}
