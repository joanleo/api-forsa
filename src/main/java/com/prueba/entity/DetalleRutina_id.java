package com.prueba.entity;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class DetalleRutina_id implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Column(name = "nidrutina")
	private Long idRutina;
	
	@Column(name = "nidruta")
	private Long idRuta;

	@Override
	public int hashCode() {
		return Objects.hash(idRuta, idRutina);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DetalleRutina_id other = (DetalleRutina_id) obj;
		return Objects.equals(idRuta, other.idRuta) && Objects.equals(idRutina, other.idRutina);
	}

	public Long getIdRutina() {
		return idRutina;
	}

	public void setIdRutina(Long idRutina) {
		this.idRutina = idRutina;
	}


	public Long getIdRuta() {
		return idRuta;
	}

	public void setIdRuta(Long idRuta) {
		this.idRuta = idRuta;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public DetalleRutina_id(Long idRutina, Long idRuta) {
		super();
		this.idRutina = idRutina;
		this.idRuta = idRuta;
	}

	public DetalleRutina_id() {
		super();
	}
	

}
