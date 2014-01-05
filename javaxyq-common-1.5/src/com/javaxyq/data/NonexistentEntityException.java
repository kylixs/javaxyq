package com.javaxyq.data;

public class NonexistentEntityException extends Exception {
	private static final long serialVersionUID = 7613306744789423824L;
	public NonexistentEntityException(String message, Throwable cause) {
        super(message, cause);
    }
    public NonexistentEntityException(String message) {
        super(message);
    }
}
