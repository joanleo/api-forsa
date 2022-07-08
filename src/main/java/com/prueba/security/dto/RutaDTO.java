package com.prueba.security.dto;

import javax.validation.constraints.NotBlank;

public class RutaDTO {
	
	private Long id;
	
	@NotBlank(message = "Este campo no puede ser nulo")
	private String ruta;
	
	private String metodo;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getRuta() {
		return ruta;
	}

	public void setRuta(String ruta) {
		this.ruta = ruta;
	}


	public String getMetodo() {
		return metodo;
	}

	public void setMetodo(String metodo) {
		this.metodo = metodo;
	}

	public RutaDTO() {
		super();
	}
	
}
