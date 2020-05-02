package com.javaxyq.event;

import java.util.EventObject;

public interface EventTarget {
	// public void addEventListener(String type, EventListener listener, boolean
	// useCapture);

	// public void removeEventListener(String type, EventListener listener,
	// boolean useCapture);

	public boolean handleEvent(EventObject evt) throws EventException;

}
