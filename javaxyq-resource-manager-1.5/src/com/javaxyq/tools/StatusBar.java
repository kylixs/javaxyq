

/*
 * Copyright (C) 2006 Sun Microsystems, Inc. All rights reserved. Use is
 * subject to license terms.
 */ 

package com.javaxyq.tools;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.Timer;
import javax.swing.border.EmptyBorder;

import org.jdesktop.application.Application;
import org.jdesktop.application.ResourceMap;
import org.jdesktop.application.TaskMonitor;



/**
 * A StatusBar panel that tracks a TaskMonitor.  Although one could certainly
 * create a more elaborate StatusBar class, this one is sufficient for the 
 * examples that need one.
 * <p>
 * This class loads resources from the ResourceBundle called
 * {@code resources.StatusBar}.
 * 
 */
public class StatusBar extends JPanel implements PropertyChangeListener {
    private final Insets zeroInsets = new Insets(0,0,0,0);
    private final JLabel messageLabel;
    private final JProgressBar progressBar;
    private final JLabel statusAnimationLabel;
    private final int messageTimeout;
    private final Timer messageTimer;
    private final Timer busyIconTimer;
    private final Icon idleIcon;
    private final Icon[] busyIcons = new Icon[15];
    private final int busyAnimationRate;
    private int busyIconIndex = 0;

    /**
     * Constructs a panel that displays messages/progress/state 
     * properties of the {@code taskMonitor's} foreground task.
     * 
     * @param taskMonitor the {@code TaskMonitor} whose 
     *     {@code PropertyChangeEvents} {@code this StatusBar} will track.
     */
    public StatusBar(Application app, TaskMonitor taskMonitor) {
	super(new GridBagLayout());
	setBorder(new EmptyBorder(0, 0, 1, 0)); // top, left, bottom, right
	messageLabel = new JLabel();
	progressBar = new JProgressBar(0, 100);
	statusAnimationLabel = new JLabel();

	ResourceMap resourceMap = app.getContext().getResourceMap(StatusBar.class);
	messageTimeout = resourceMap.getInteger("messageTimeout");
	messageTimer = 	new Timer(messageTimeout, new ClearOldMessage()); 
	messageTimer.setRepeats(false);
	busyAnimationRate = resourceMap.getInteger("busyAnimationRate");
	idleIcon = resourceMap.getIcon("idleIcon");
	for (int i = 0; i < busyIcons.length; i++) {
	    busyIcons[i] = resourceMap.getIcon("busyIcons[" + i + "]");
	}
	busyIconTimer = new Timer(busyAnimationRate, new UpdateBusyIcon()); 
	progressBar.setEnabled(false);
	statusAnimationLabel.setIcon(idleIcon);

	GridBagConstraints c = new GridBagConstraints();
	initGridBagConstraints(c);
//	c.gridwidth = GridBagConstraints.REMAINDER;
//	c.fill = GridBagConstraints.HORIZONTAL;
//	c.weightx = 1.0;
//	add(new JSeparator(), c);

	initGridBagConstraints(c);
	c.insets = new Insets(6, 6, 0, 3); // top, left, bottom, right;
	c.weightx = 1.0;
	c.fill = GridBagConstraints.HORIZONTAL;
	add(messageLabel, c);

	initGridBagConstraints(c);
	c.insets = new Insets(6, 3, 0, 3); // top, left, bottom, right;
	add(progressBar, c);

	initGridBagConstraints(c);
	c.insets = new Insets(6, 3, 0, 6); // top, left, bottom, right;
	add(statusAnimationLabel, c);

	taskMonitor.addPropertyChangeListener(this);
    }

    public void setMessage(String s) {
	messageLabel.setText((s == null) ? "" : s);
	messageTimer.restart();
    }

    private void initGridBagConstraints(GridBagConstraints c) {
	c.anchor = GridBagConstraints.CENTER;
	c.fill = GridBagConstraints.NONE;
	c.gridwidth = 1;
	c.gridheight = 1;
	c.gridx = GridBagConstraints.RELATIVE;
	c.gridy = GridBagConstraints.RELATIVE;
	c.insets = zeroInsets;
	c.ipadx = 0;
	c.ipady = 0;
	c.weightx = 0.0;
	c.weighty = 0.0;
    }

    private class ClearOldMessage implements ActionListener {
	public void actionPerformed(ActionEvent e) {
	    messageLabel.setText("");
	}
    }

    private class UpdateBusyIcon implements ActionListener {
	public void actionPerformed(ActionEvent e) {
	    busyIconIndex = (busyIconIndex + 1) % busyIcons.length;
	    statusAnimationLabel.setIcon(busyIcons[busyIconIndex]);
	}
    }

    public void showBusyAnimation() {
	if (!busyIconTimer.isRunning()) {
	    statusAnimationLabel.setIcon(busyIcons[0]);
	    busyIconIndex = 0;
	    busyIconTimer.start();
	}
    }

    public void stopBusyAnimation() {
	busyIconTimer.stop();
	statusAnimationLabel.setIcon(idleIcon);
    }

    /** 
     * The TaskMonitor (constructor arg) tracks a "foreground" task;
     * this method is called each time a foreground task property
     * changes.
     */
    public void propertyChange(PropertyChangeEvent e) {
	String propertyName = e.getPropertyName();
	if ("started".equals(propertyName)) {
	    showBusyAnimation();
	    progressBar.setEnabled(true);
	    progressBar.setIndeterminate(true);
	}
	else if ("done".equals(propertyName)) {
	    stopBusyAnimation();
	    progressBar.setIndeterminate(false);
	    progressBar.setEnabled(false);
	    progressBar.setValue(0);
	}
	else if ("message".equals(propertyName)) {
	    String text = (String)(e.getNewValue());
	    setMessage(text);
	}
	else if ("progress".equals(propertyName)) {
	    int value = (Integer)(e.getNewValue());
	    progressBar.setEnabled(true);
	    progressBar.setIndeterminate(false);
	    progressBar.setValue(value);
	}
    }
}

