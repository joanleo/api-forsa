package com.prueba.dto;

import com.prueba.entity.TipoEmpresa;

public class EmpresaDTO {
	//@NotNull(message = "Este campo no puede ser nulo")
	private Long nit;
	
	//@NotBlank(message = "Este campo no puede ser nulo")
	private String nombre;
	
	private TipoEmpresa tipoEmpresa;
	
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

	public TipoEmpresa getTipoEmpresa() {
		return tipoEmpresa;
	}

	public void setTipoEmpresa(TipoEmpresa tipoEmpresa) {
		this.tipoEmpresa = tipoEmpresa;
	}

	public Boolean getEstaActivo() {
		return estaActivo;
	}

	public void setEstaActivo(Boolean estaActivo) {
		this.estaActivo = estaActivo;
	}

}
