package com.prueba.security.entity;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.prueba.entity.DetalleRutina;

@Entity
@Table(name = "m_politicas")
@JsonAutoDetect(fieldVisibility = Visibility.ANY)
@JsonIdentityInfo(
		  generator = ObjectIdGenerators.PropertyGenerator.class, 
		  property = "idPolitica")
public class Politica implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE,
    		generator = "seq_politicas")
	@SequenceGenerator(name = "seq_politica", allocationSize = 10)
	private Long idPolitica;
	
	@JsonIgnore
	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "nidrol")
	private Rol rol;
	
	@ManyToOne
	@JoinColumn(name = "nidrutina")
	@JoinColumn(name = "nidruta")
	private DetalleRutina detalle;
	
	@Column(name = "bpermiso")
	private Boolean permiso;

	public Politica() {
		super();
	}

	public Politica(Rol rol, DetalleRutina detalle, Boolean permiso) {
		super();
		this.rol = rol;
		this.detalle = detalle;
		this.permiso = permiso;
	}

	public Politica(Long idPolitica, Rol rol, DetalleRutina detalle, Boolean permiso) {
		super();
		this.idPolitica = idPolitica;
		this.rol = rol;
		this.detalle = detalle;
		this.permiso = permiso;
	}

	public Long getIdPolitica() {
		return idPolitica;
	}

	public void setIdPolitica(Long idPolitica) {
		this.idPolitica = idPolitica;
	}

	public Rol getRol() {
		return rol;
	}

	public void setRol(Rol rol) {
		this.rol = rol;
	}

	public DetalleRutina getDetalle() {
		return detalle;
	}

	public void setDetalle(DetalleRutina detalle) {
		this.detalle = detalle;
	}

	public Boolean getPermiso() {
		return permiso;
	}

	public void setPermiso(Boolean permiso) {
		this.permiso = permiso;
	}
}
