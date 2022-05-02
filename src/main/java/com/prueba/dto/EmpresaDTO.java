package com.prueba.dto;


import javax.validation.constraints.NotBlank;

public class EmpresaDTO {
	@NotBlank(message = "Este campo no puede ser nulo")
	private Long nit;
	
	@NotBlank(message = "Este campo no puede ser nulo")
	private String nombre;
	
	private String descripcion;

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

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

}
