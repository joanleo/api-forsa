package com.prueba.security.dto;

import org.springframework.http.HttpStatus;

public class ResDTO {

	String respuesta;
	HttpStatus status;
	
	public ResDTO(String respuesta) {
		super();
		this.respuesta = respuesta;

	}
	public String getRespuesta() {
		return respuesta;
	}
	public void setRespuesta(String respuesta) {
		this.respuesta = respuesta;
	}
	public HttpStatus getDescripcion() {
		return status;
	}
	public void setDescripcion(HttpStatus descripcion) {
		this.status = descripcion;
	}
}
