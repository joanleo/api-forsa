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
public class ResourceAlreadyExistsException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String recurso;
	private String campo;
	private Long valor;
	private String val;

	public ResourceAlreadyExistsException(String recurso, String campo, Long valor) {
		super(String.format("%s ya existe con %s: '%s'", recurso, campo, valor));
		this.recurso = recurso;
		this.campo = campo;
		this.valor = valor;
	}

	public ResourceAlreadyExistsException(String recurso, String campo, String val) {
		super(String.format("%s ya existe con %s: '%s'", recurso, campo, val));
		this.recurso = recurso;
		this.campo = campo;
		this.val = val;
	}

	public String getRecurso() {
		return recurso;
	}

	public void setRecurso(String recurso) {
		this.recurso = recurso;
	}

	public String getCampo() {
		return campo;
	}

	public void setCampo(String campo) {
		this.campo = campo;
	}

	public Long getValor() {
		return valor;
	}

	public void setValor(Long valor) {
		this.valor = valor;
	}

	public String getVal() {
		return val;
	}

	public void setVal(String val) {
		this.val = val;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
}
