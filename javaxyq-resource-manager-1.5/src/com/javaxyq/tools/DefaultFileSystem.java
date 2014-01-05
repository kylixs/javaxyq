/*
 * JavaXYQ Engine 
 * 
 * javaxyq@2008 all rights. 
 * http://www.javaxyq.com
 */

package com.javaxyq.tools;

import java.io.File;
import java.net.URI;

/**
 * @author 龚德伟
 * @history 2008-7-14 龚德伟 新建
 */
public class DefaultFileSystem  implements FileSystem {
    
    private DefaultFileObject root;
    
    public DefaultFileSystem() {
        this("");
    }

    public DefaultFileSystem(String pathname) {
        root = new DefaultFileObject(this,pathname);
    }

    public DefaultFileSystem(URI uri) {
        root = new DefaultFileObject(this,uri);
    }

    public DefaultFileSystem(File dir) {
        root = new DefaultFileObject(this,dir);
    }

    public FileObject getRoot() {
        return root;
    }

    public String getType() {
        return "file";
    }

    public void load(String filename) {
        //TODO 
    }

    public void save(String filename) {
        // TODO DefaultFileSystem: save

    }
    
    public File getFile() {
        return root.getFile();
    }

}
