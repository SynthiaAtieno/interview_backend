package com.example.test.exception;

public class GenericException extends Exception {
	private int code;

	public GenericException(int code, String message) {
		super(message);
		this.code = code;
	}

}
