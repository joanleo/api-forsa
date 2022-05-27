package com.prueba.security.dto;

import org.springframework.http.HttpStatus;

public class ResDTO {

	String response;
	HttpStatus status;
	
	public ResDTO(String respuesta) {
		super();
		this.response = respuesta;

	}
	public String getRespuesta() {
		return response;
	}
	public void setRespuesta(String respuesta) {
		this.response = respuesta;
	}

}
