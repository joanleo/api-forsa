package com.prueba.dto;

import javax.validation.constraints.NotBlank;

public class FabricanteDTO {
	
	@NotBlank(message = "Este campo no puede ser nulo")
	private Long nit;
	
	@NotBlank(message = "Este campo no puede ser nulo")
    private String nombre;

	public Long getNit() {
		return nit;
	}

	public void setNit(Long nit) {
		this.nit = nit;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

}
