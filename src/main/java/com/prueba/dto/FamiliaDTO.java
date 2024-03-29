package com.prueba.dto;

import com.prueba.entity.Empresa;

public class FamiliaDTO {

	private Long id;
	
	//@NotBlank(message = "Este campo no puede ser nulo")
    private String nombre;
	
	//@NotNull(message = "Este campo no puede ser nulo")
	private Empresa empresa;
	
	private String sigla;
	
	private Boolean estaActivo;

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

	public String getSigla() {
		return sigla;
	}

	public void setSigla(String sigla) {
		this.sigla = sigla;
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
