package com.javaxyq.profile;

public class ProfileException extends Exception {

	private static final long serialVersionUID = -8991799720487636650L;

	public ProfileException() {
	}

	public ProfileException(String message) {
		super(message);
	}

	public ProfileException(Throwable cause) {
		super(cause);
	}

	public ProfileException(String message, Throwable cause) {
		super(message, cause);
	}

}
