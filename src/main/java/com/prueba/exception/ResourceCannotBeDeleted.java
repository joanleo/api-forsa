/**
 * 
 */
package com.prueba.exception;

/**
 * @author Joan Leon
 *
 */
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
