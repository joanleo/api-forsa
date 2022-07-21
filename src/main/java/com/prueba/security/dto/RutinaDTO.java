/**
 * 
 */
package com.prueba.security.dto;

import java.util.Set;


/**
 * @author Joan Leon
 *
 */
public class RutinaDTO {
	
	private String nombre;
	
	private Set<PoliticaDTO> politicas;

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public Set<PoliticaDTO> getPoliticas() {
		return politicas;
	}

	public void setPoliticas(Set<PoliticaDTO> politicas) {
		this.politicas = politicas;
	}


}
