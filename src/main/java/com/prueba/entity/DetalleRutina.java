package com.prueba.entity;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;

@Entity
@Table(name = "detalle_rutina")
@JsonAutoDetect(fieldVisibility = Visibility.ANY)
public class DetalleRutina implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private DetalleRutina_id idDetalle;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@MapsId("idRutina")
	@JsonBackReference
	private Rutina rutina;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@MapsId("idPermiso")
	private Permiso permiso;

	public DetalleRutina(Rutina rutina, Permiso permiso) {
		super();
		this.idDetalle = new DetalleRutina_id(rutina.getIdRutina(), permiso.getIdPermiso());
		this.rutina = rutina;
		this.permiso = permiso;
	}

	public DetalleRutina() {
		super();
	}

	public DetalleRutina_id getIdDetalle() {
		return idDetalle;
	}

	public void setIdDetalle(DetalleRutina_id idDetalle) {
		this.idDetalle = idDetalle;
	}

	public Rutina getRutina() {
		return rutina;
	}

	public void setRutina(Rutina rutina) {
		this.rutina = rutina;
	}

	public Permiso getPermiso() {
		return permiso;
	}

	public void setPermiso(Permiso permiso) {
		this.permiso = permiso;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	@Override
	public int hashCode() {
		return Objects.hash(idDetalle, permiso, rutina);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DetalleRutina other = (DetalleRutina) obj;
		return Objects.equals(idDetalle, other.idDetalle) && Objects.equals(permiso, other.permiso)
				&& Objects.equals(rutina, other.rutina);
	}
}
