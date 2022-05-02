package com.prueba.dto;

import javax.validation.constraints.NotBlank;

public class EstadoDTO {

	private Long id;
	
	@NotBlank(message = "Este campo no puede ser nulo")
	private String tipo;

	public EstadoDTO() {
		super();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}
	
}
