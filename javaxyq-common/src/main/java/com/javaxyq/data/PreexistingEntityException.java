package com.javaxyq.data;

public class PreexistingEntityException extends Exception {
	private static final long serialVersionUID = 2829593176937895291L;
	public PreexistingEntityException(String message, Throwable cause) {
        super(message, cause);
    }
    public PreexistingEntityException(String message) {
        super(message);
    }
}
