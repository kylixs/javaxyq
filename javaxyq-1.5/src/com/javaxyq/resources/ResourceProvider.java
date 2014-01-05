/*
 * JavaXYQ Engine 
 * 
 * javaxyq@2008 all rights. 
 * http://www.javaxyq.com
 */

package com.javaxyq.resources;

/**
 * @author 龚德伟
 * @history 2008-5-22 龚德伟 新建
 */
public interface ResourceProvider<E> {
    E getResource(String resId);
    void dispose();
}
