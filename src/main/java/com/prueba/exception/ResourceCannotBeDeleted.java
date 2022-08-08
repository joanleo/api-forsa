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
public class ResourceCannotBeDeleted extends RuntimeException  {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String recurso;
	
	public ResourceCannotBeDeleted(String recurso) {
		super(String.format("%s no puede ser eliminado tiene movimientos asociados", recurso));
		this.recurso = recurso;
	}
	public ResourceCannotBeDeleted(String recurso, String mensaje) {
		super(String.format("%s no puede ser eliminado, %s ", recurso, mensaje));
		this.recurso = recurso;
	}
	
	public String getRecurso() {
		return recurso;
	}
	public void setRecurso(String recurso) {
		this.recurso = recurso;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	

}
