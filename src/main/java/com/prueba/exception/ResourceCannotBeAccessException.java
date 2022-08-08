/**
 * 
 */
package com.prueba.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @author Joan Leon
 *
 */
@ResponseStatus(value = HttpStatus.CONFLICT)
public class ResourceCannotBeAccessException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public ResourceCannotBeAccessException() {
		super();
		// TODO Auto-generated constructor stub
	}

	public ResourceCannotBeAccessException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
		// TODO Auto-generated constructor stub
	}

	public ResourceCannotBeAccessException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	public ResourceCannotBeAccessException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	public ResourceCannotBeAccessException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}

}
