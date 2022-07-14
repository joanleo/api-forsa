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
	
	@Column(name = "nidpermiso")
	private Long idPermiso;

	@Override
	public int hashCode() {
		return Objects.hash(idPermiso, idRutina);
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
		return Objects.equals(idPermiso, other.idPermiso) && Objects.equals(idRutina, other.idRutina);
	}

	public Long getIdRutina() {
		return idRutina;
	}

	public void setIdRutina(Long idRutina) {
		this.idRutina = idRutina;
	}

	public Long getIdPermiso() {
		return idPermiso;
	}

	public void setIdPermiso(Long idPermiso) {
		this.idPermiso = idPermiso;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public DetalleRutina_id(Long idRutina, Long idPermiso) {
		super();
		this.idRutina = idRutina;
		this.idPermiso = idPermiso;
	}

	public DetalleRutina_id() {
		super();
	}
	

}
