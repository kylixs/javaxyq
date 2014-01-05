package com.javaxyq.event;
/**
 * Conditional is used by the EventDispatchThread's message pumps to
 * determine if a given pump should continue to run, or should instead exit
 * and yield control to the parent pump.
 *
 * @version 1.7 11/17/05
 * @author David Mendenhall
 */
public interface Conditional {
    boolean evaluate();
}
