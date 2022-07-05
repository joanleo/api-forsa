package com.prueba.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.prueba.entity.Empresa;

public class EstadoDTO {

	private Long id;
	
	@NotBlank(message = "Este campo no puede ser nulo")
	private String tipo;
	
	@NotNull(message = "Este campo no puede ser nulo")
	private Empresa empresa;
	
	private Boolean estaActivo=true;

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
