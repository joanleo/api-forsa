package com.prueba.dto;

public class TipoEmpresaDTO {
	
	private Long id;
	
	//@NotBlank(message="este campo no puede ser nulo")
	private String tipo;
	
	private Boolean estaActivo;

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

	public Boolean getEstaActivo() {
		return estaActivo;
	}

	public void setEstaActivo(Boolean estaActivo) {
		this.estaActivo = estaActivo;
	}

}
