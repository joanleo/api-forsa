package com.prueba.dto;

import javax.validation.constraints.NotBlank;

import com.prueba.entity.Empresa;

public class FamiliaDTO {

	private Long id;
	
	@NotBlank(message = "Este campo no puede ser nulo")
    private String nombre;
	
	private Empresa empresa;

	public FamiliaDTO() {
		super();
	}

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

}
