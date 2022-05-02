package com.prueba.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	private String recurso;
	private String campo;
	private Long valor;

	public ResourceNotFoundException(String recurso, String campo, Long valor) {
		super(String.format("%s no encontrado con %s: '%s'", recurso, campo, valor));
		this.recurso = recurso;
		this.campo = campo;
		this.valor = valor;
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

}


