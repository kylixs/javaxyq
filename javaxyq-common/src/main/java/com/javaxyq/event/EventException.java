package com.javaxyq.event;

/**
 * @author dewitt
 * @date 2009-11-26 create
 */
public class EventException extends RuntimeException {
	public EventException(short code, String message) {
		super(message);
		this.code = code;
	}

	public short code;
	// EventExceptionCode
	/**
	 * If the <code>Event</code>'s type was not specified by initializing the
	 * event before the method was called. Specification of the Event's type as
	 * <code>null</code> or an empty string will also trigger this exception.
	 */
	public static final short UNSPECIFIED_EVENT_TYPE_ERR = 0;

}
