package com.prueba.dto;

import javax.validation.constraints.NotBlank;

public class TipoEmpresaDTO {
	
private Long id;
	
	@NotBlank(message="este campo no puede ser nulo")
	private String tipo;
	
	private String descripcion;

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

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

}
