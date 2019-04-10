package com.example.demo.exception;

/**
 * @author mc
 * Create date 2019/3/25 14:34
 * Version 1.0
 * Description
 */
public class StorageException extends RuntimeException {

	public StorageException(String message) {
		super(message);
	}

	public StorageException(String message, Throwable cause) {
		super(message, cause);
	}
}
