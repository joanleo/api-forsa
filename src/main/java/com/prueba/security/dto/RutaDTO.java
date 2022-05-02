package com.prueba.security.dto;

import javax.validation.constraints.NotBlank;

public class RutaDTO {
	
	private Long id;
	
	@NotBlank(message = "Este campo no puede ser nulo")
	private String ruta;
	
	private String descripcion;
	

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

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}


	public RutaDTO() {
		super();
	}
	
}
