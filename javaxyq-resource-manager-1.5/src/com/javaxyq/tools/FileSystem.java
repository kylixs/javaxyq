/*
 * JavaXYQ Engine 
 * 
 * javaxyq@2008 all rights. 
 * http://www.javaxyq.com
 */

package com.javaxyq.tools;

/**
 * 
 * @author 龚德伟
 * @history 2008-7-6 龚德伟 新建
 */
public interface FileSystem {

    FileObject getRoot();

    String getType();

    /**
     * save desc
     * @param filename
     */
    void save(String filename);

    /**
     * load desc
     * @param filename
     */
    void load(String filename);
}
