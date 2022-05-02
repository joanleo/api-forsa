package com.prueba.security.dto;

import javax.validation.constraints.NotBlank;

import com.prueba.dto.EmpresaDTO;

public class RolDTO {

	private Long id;
	
	@NotBlank(message = "Este campo no puede ser nulo")
	private String nombre;
	
	private String descripcion;
	
	private EmpresaDTO empresa;
	
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

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public EmpresaDTO getEmpresa() {
		return empresa;
	}

	public void setEmpresa(EmpresaDTO empresa) {
		this.empresa = empresa;
	}

	public RolDTO() {
		super();
	}
}

