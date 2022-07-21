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
	
	private Set<PoliticaDTO> politica;

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public Set<PoliticaDTO> getPolitica() {
		return politica;
	}

	public void setPolitica(Set<PoliticaDTO> politica) {
		this.politica = politica;
	}

}
