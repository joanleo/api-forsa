package com.prueba.entity;

import java.io.Serializable;
import java.util.Objects;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.prueba.security.entity.Politica;

@Entity
@Table(name = "detalle_rutina")
@JsonAutoDetect(fieldVisibility = Visibility.ANY)
public class DetalleRutina implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private static int countBase = 0;

	@Column(name = "npkDetalle")
	private int pkDetalle;
	
	//@JsonIgnore
	@EmbeddedId
	private DetalleRutina_id idDetalle;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@MapsId("idRutina")
	//@JsonBackReference
	private Rutina rutina;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@MapsId("idRuta")
	private Ruta ruta;
	
	@JsonIgnore
	@OneToMany(mappedBy = "detalle")
	private Set<Politica> politica;

	public Set<Politica> getPolitica() {
		return politica;
	}

	public void setPolitica(Set<Politica> politica) {
		this.politica = politica;
	}

	public DetalleRutina(Rutina rutina, Ruta ruta) {
		super();
		DetalleRutina.countBase += 1;
		this.pkDetalle = countBase;
		this.idDetalle = new DetalleRutina_id(rutina.getIdRutina(), ruta.getIdRuta());
		this.rutina = rutina;
		this.ruta = ruta;
	}

	public DetalleRutina() {
		super();
		DetalleRutina.countBase += 1;
		this.pkDetalle = countBase;
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
	
	public Ruta getRuta() {
		return ruta;
	}

	public void setRuta(Ruta ruta) {
		this.ruta = ruta;
	}

	public int getPkDetalle() {
		return pkDetalle;
	}

	public void setPkDetalle(int pkDetalle) {
		this.pkDetalle = pkDetalle;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	@Override
	public int hashCode() {
		return Objects.hash(idDetalle, ruta, rutina);
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
		return Objects.equals(idDetalle, other.idDetalle) && Objects.equals(ruta, other.ruta)
				&& Objects.equals(rutina, other.rutina);
	}
}
