/*
 * JavaXYQ Engine 
 * 
 * javaxyq@2008 all rights. 
 * http://www.javaxyq.com
 */

package com.javaxyq.tools;

import javax.swing.tree.DefaultMutableTreeNode;

/**
 * @author 龚德伟
 * @history 2008-7-6 龚德伟 新建
 */
public class FileObjectTreeNode extends DefaultMutableTreeNode {

    private boolean loaded;

    /**
     * @param userObject
     */
    public FileObjectTreeNode(FileObject userObject) {
        super(userObject);
    }

    @Override
    public boolean isLeaf() {
        return ((FileObject) userObject).listFiles().length == 0;
    }

    public boolean isLoaded() {
        return loaded;
    }

    public void setLoaded(boolean loaded) {
        this.loaded = loaded;
    }
}
