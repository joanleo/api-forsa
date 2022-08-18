package com.prueba.dto;

import javax.validation.constraints.NotNull;

import com.prueba.entity.Empresa;

public class FabricanteDTO {
	
	//@NotNull(message = "Este campo no puede ser nulo")
	private Long nit;
	
	//@NotBlank(message = "Este campo no puede ser nulo")
    private String nombre;
	
	//@NotBlank(message = "Este campo no puede ser nulo")
	private Empresa empresa;
	
	private Boolean estaActivo=true;

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

}
