/**
 * 
 */
package com.prueba.security.dto;

import java.util.List;
import java.util.Set;

/**
 * @author Joan Leon
 *
 */
public class PoliticaDTO {
	
	private Long idPolitica;

	private List<String> url;
	
	private String rol;
	
	private String nombre;
	
	private Boolean permiso;


	public Long getIdPolitica() {
		return idPolitica;
	}

	public void setIdPolitica(Long idPolitica) {
		this.idPolitica = idPolitica;
	}

	public List<String> getUrl() {
		return url;
	}

	public void setUrl(List<String> url) {
		this.url = url;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getRol() {
		return rol;
	}

	public void setRol(String rol) {
		this.rol = rol;
	}

	public Boolean getPermiso() {
		return permiso;
	}

	public void setPermiso(Boolean permiso) {
		this.permiso = permiso;
	}
}
