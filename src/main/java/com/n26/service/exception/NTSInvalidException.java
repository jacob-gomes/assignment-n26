package com.n26.service.exception;

/**
 * 
 * @author Jacob
 *
 */
public class NTSInvalidException extends Exception {
	/**
	 * Serial version ID
	 */
	private static final long serialVersionUID = 1L;
	
	private final String message;
	
	public NTSInvalidException(String message) {
		super(message);
		this.message = message;
	}

	/**
	 * @return the message
	 */
	@Override
	public String getMessage() {
		return message;
	}
	
	
}
