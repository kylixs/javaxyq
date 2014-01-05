/*
 * JavaXYQ Engine 
 * 
 * javaxyq@2008 all rights. 
 * http://www.javaxyq.com
 */

package com.javaxyq.tools;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.event.EventListenerList;

import org.jdesktop.application.Action;
import org.jdesktop.application.Application;
import org.jdesktop.application.ApplicationAction;
import org.jdesktop.application.ApplicationActionMap;
import org.jdesktop.application.Task;
import org.jdesktop.application.Task.BlockingScope;

import com.jidesoft.action.CommandBar;
import com.jidesoft.dialog.JideOptionPane;
import com.jidesoft.swing.JideButton;

/**
 * @author 龚德伟
 * @history 2008-6-28 龚德伟 新建
 */
public class PaginationBar {

    private CommandBar toolbar = new CommandBar();

    private JButton firstButton;

    private JButton prevButton;

    private JButton nextButton;

    private JButton lastButton;

    private JTextField pageField = new JTextField();

    private EventListenerList listenerList = new EventListenerList();

    private int pageNo = 1;

    private int pageSize = 20;

    private int pageCount = 0;

    private int totalCount;

    private ApplicationActionMap actionMap;

    public PaginationBar(int pageNo, int pageSize, int totalCount) {
    	actionMap = Application.getInstance().getContext().getActionMap(this);
    	
    	firstButton = createButton("first");
    	prevButton = createButton("prev");
    	nextButton = createButton("next");
    	lastButton = createButton("last");
    	
    	pageField.setAction(actionMap.get("reload"));
    	//pageField.setFocusable(false);
    	pageField.setPreferredSize(new Dimension(100, 20));
    	pageField.addFocusListener(new FocusListener() {
    		public void focusGained(FocusEvent e) {
    			pageField.setText("" + PaginationBar.this.pageNo);
    			pageField.selectAll();
    		}
    		
    		public void focusLost(FocusEvent e) {
    			updatePageField();
    		}
    	});
    	pageField.addKeyListener(new KeyAdapter() {
    		@Override
    		public void keyPressed(KeyEvent e) {
    			if (e.getKeyChar() == KeyEvent.VK_ENTER) {
    				int pageNo = Integer.parseInt(pageField.getText());
    				PaginationBar.this.setPageNo(pageNo);
    			}
    		}
    	});
    	
    	toolbar.add(firstButton);
    	toolbar.add(prevButton);
    	toolbar.add(pageField);
    	toolbar.add(nextButton);
    	toolbar.add(lastButton);
    	
    	toolbar.addSeparator();
    	toolbar.add(actionMap.get("setting"));
    	
        this.init(pageNo, pageSize, totalCount);
        //updatePageField();
    }

    public PaginationBar() {
        this(1, 30, 0);
    }

    public void init(int pageNo, int pageSize, int totalCount) {
        this.pageNo = pageNo;
        this.totalCount = totalCount;
        this.pageCount = (totalCount + pageSize - 1) / pageSize;
        this.pageSize = pageSize;
        updatePageField();
        updateActions();
    }

    private JButton createButton(String actionKey) {
        JButton button = new JideButton();
        button.setAction(actionMap.get(actionKey));
        button.setText("");
        button.setFocusable(false);
        return button;
    }

    public ApplicationActionMap getActionMap() {
        return actionMap;
    }

    public void registerKeyboardAction(JComponent c, int condition) {
        javax.swing.Action firstAction = actionMap.get("first");
        javax.swing.Action prevAction = actionMap.get("prev");
        javax.swing.Action nextAction = actionMap.get("next");
        javax.swing.Action lastAction = actionMap.get("last");
        //ctrl home
        c.registerKeyboardAction(firstAction, KeyStroke.getKeyStroke(KeyEvent.VK_HOME,
            KeyEvent.CTRL_DOWN_MASK), condition);
        //ctrl pageUp
        c.registerKeyboardAction(prevAction, KeyStroke.getKeyStroke(KeyEvent.VK_PAGE_UP,
            KeyEvent.CTRL_DOWN_MASK), condition);
        //ctrl left
        c.registerKeyboardAction(prevAction, KeyStroke.getKeyStroke(KeyEvent.VK_LEFT,
            KeyEvent.CTRL_DOWN_MASK), condition);
        //ctrl right
        c.registerKeyboardAction(nextAction, KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT,
            KeyEvent.CTRL_DOWN_MASK), condition);
        //ctrl pageDown
        c.registerKeyboardAction(nextAction, KeyStroke.getKeyStroke(KeyEvent.VK_PAGE_DOWN,
            KeyEvent.CTRL_DOWN_MASK), condition);
        //ctrl end
        c.registerKeyboardAction(lastAction, KeyStroke.getKeyStroke(KeyEvent.VK_END,
            KeyEvent.CTRL_DOWN_MASK), condition);
    }

    private void updatePageField() {
        pageField.setText(pageNo + "/" + pageCount);
    }

    public CommandBar getComponent() {
        return toolbar;
    }

    public void addPaginationListener(PaginationListener l) {
        listenerList.add(PaginationListener.class, l);
    }

    public void removePaginationListener(PaginationListener l) {
        listenerList.remove(PaginationListener.class, l);
    }

    @Action(block = BlockingScope.ACTION)
    public Task first() {
        this.pageNo = 1;
        PaginationEvent e = new PaginationEvent(this, pageNo, pageSize);
        return new LoadTask(Application.getInstance(), e);
    }

    @Action(block = BlockingScope.ACTION)
    public Task prev() {
        if (this.pageNo > 1) {
            this.pageNo--;
            PaginationEvent e = new PaginationEvent(this, pageNo, pageSize);
            return new LoadTask(Application.getInstance(), e);
        }
        return null;
    }

    @Action(block = BlockingScope.ACTION)
    public Task next() {
        if (this.pageNo < this.pageCount) {
            this.pageNo++;
            PaginationEvent e = new PaginationEvent(this, pageNo, pageSize);
            return new LoadTask(Application.getInstance(), e);
        }
        return null;
    }

    @Action(block = BlockingScope.ACTION)
    public Task last() {
        this.pageNo = this.pageCount;
        PaginationEvent e = new PaginationEvent(this, pageNo, pageSize);
        return new LoadTask(Application.getInstance(), e);
    }

    public int getPageNo() {
        return pageNo;
    }

    public void setPageNo(int pageNo) {
        if (pageNo > pageCount) {
            pageNo = pageCount;
        }
        if (pageNo < 1) {
            pageNo = 1;
        }
        this.pageNo = pageNo;
        updatePageField();
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getPageCount() {
        return pageCount;
    }

    public void setPageCount(int pageCount) {
        this.pageCount = pageCount;
    }

    public int getStartNo() {
        return (pageNo - 1) * pageSize;
    }

    public int getEndNo() {
        int end = pageNo * pageSize;
        return end > totalCount ? totalCount : end;
    }

    @Action(block = BlockingScope.ACTION)
    public Task reload() {
        PaginationEvent e = new PaginationEvent(this, pageNo, pageSize);
        return new LoadTask(Application.getInstance(), e);
        //Application.getInstance().getContext().
    }

    private class LoadTask extends Task<Void, Void> {

        private PaginationEvent event;

        public LoadTask(Application application, PaginationEvent e) {
            super(application);
            this.event = e;
        }

        @Override
        protected Void doInBackground() throws Exception {
            setMessage("loading...");
            updatePageField();
            PaginationListener[] listeners = listenerList.getListeners(PaginationListener.class);
            for (int i = 0; i < listeners.length; i++) {
                PaginationListener listener = listeners[i];
                try {
                    listener.loadPage(event);
                } catch (Exception e) {
                    System.out.println("execute pagination listener error!");
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void succeeded(Void result) {
            setMessage("loaded.");
        }

        @Override
        protected void failed(Throwable cause) {
            setMessage("failed! "+cause.getMessage());
            cause.printStackTrace();
        }

        @Override
        protected void cancelled() {
            setMessage("user cancelled.");
        }

        @Override
        protected void finished() {
            updateActions();
        }

    }

    public void doAction(String actionKey) {
        ApplicationAction action = (ApplicationAction) getActionMap().get(actionKey);
        action.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, null));
    }

    private void updateActions() {
        try {
            actionMap.get("first").setEnabled(pageNo > 1);
            actionMap.get("prev").setEnabled(pageNo > 1);
            actionMap.get("next").setEnabled(pageNo < pageCount);
            actionMap.get("last").setEnabled(pageNo < pageCount);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void doReload() {
        this.doAction("reload");
    }

    public void doFirst() {
        this.doAction("first");
    }

    @Action
    public void setting() {
        String strSize = JideOptionPane.showInputDialog("请输入每页显示的记录数:", this.pageSize);
        try {
            this.pageSize = Integer.parseInt(strSize);
            doReload();
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
    }
}
