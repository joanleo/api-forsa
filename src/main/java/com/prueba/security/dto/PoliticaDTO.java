/**
 * 
 */
package com.prueba.security.dto;

/**
 * @author Joan Leon
 *
 */
public class PoliticaDTO {

	private String url;
	
	private String rol;
	
	private String nombre;
	
	private Boolean permiso;


	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
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
