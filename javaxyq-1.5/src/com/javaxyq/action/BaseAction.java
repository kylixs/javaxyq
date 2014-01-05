/*
 * JavaXYQ Engine 
 * 
 * javaxyq@2008 all rights. 
 * http://www.javaxyq.com
 */

package com.javaxyq.action;

import com.javaxyq.event.ActionEvent;

/**
 * @author 龚德伟
 * @history 2008-6-9 龚德伟 新建
 */
public abstract class BaseAction extends javax.swing.AbstractAction {

    public abstract void doAction(ActionEvent e);

    public void actionPerformed(java.awt.event.ActionEvent e) {
        this.doAction(new ActionEvent(e.getSource(), e.getActionCommand()));
    }

}
