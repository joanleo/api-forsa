package com.prueba.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.prueba.entity.Empresa;

/**
 * 
 * @author Joan Leon
 *
 */
public class EstadoDTO {

	private Long id;
	
	//@NotBlank(message = "Este campo no puede ser nulo")
	private String tipo;
	
	//@NotNull(message = "Este campo no puede ser nulo")
	private Empresa empresa;
	
	private Boolean estaActivo=true;

	/**
	 * 
	 */
	public EstadoDTO() {
		super();
	}

	/**
	 * @return
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @param id
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return
	 */
	public String getTipo() {
		return tipo;
	}

	/**
	 * @param tipo
	 */
	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	/**
	 * @return
	 */
	public Empresa getEmpresa() {
		return empresa;
	}

	/**
	 * @param empresa
	 */
	public void setEmpresa(Empresa empresa) {
		this.empresa = empresa;
	}

	/**
	 * @return
	 */
	public Boolean getEstaActivo() {
		return estaActivo;
	}

	/**
	 * @param estaActivo
	 */
	public void setEstaActivo(Boolean estaActivo) {
		this.estaActivo = estaActivo;
	}
	
}
