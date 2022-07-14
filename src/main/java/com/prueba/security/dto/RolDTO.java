package com.prueba.security.dto;

import javax.validation.constraints.NotBlank;

import com.prueba.entity.Empresa;

public class RolDTO {

	private Long id;
	
	@NotBlank(message = "Este campo no puede ser nulo")
	private String nombre;
	
	@NotBlank(message = "Este campo no puede ser nulo")
	private Empresa empresa;
	
	private Boolean estaActivo=true;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public Empresa getEmpresa() {
		return empresa;
	}

	public void setEmpresa(Empresa empresa) {
		this.empresa = empresa;
	}

	public Boolean getEstaActivo() {
		return estaActivo;
	}

	public void setEstaActivo(Boolean estaActivo) {
		this.estaActivo = estaActivo;
	}

	public RolDTO() {
		super();
	}
}

