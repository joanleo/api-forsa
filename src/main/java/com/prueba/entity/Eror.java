package com.prueba.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.UpdateTimestamp;

@Entity
@Table(name = "errores")
public class Eror {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", length = 9)
	private Long id;
	
	@Column(name = "niderror", length = 4)
	private Integer idError;
	
	@Column(name = "vcproceso", length = 64)
	private String proceso;
	
	@Column(name = "vcdescripcion")
	private String descripcion;
	
	@Column(name = "vcusuario")
	private String usuario;
	
	@UpdateTimestamp
	@Column(name = "dfecha")
	private Date fecha;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getIdError() {
		return idError;
	}

	public void setIdError(Integer idError) {
		this.idError = idError;
	}

	public String getProceso() {
		return proceso;
	}

	public void setProceso(String proceso) {
		this.proceso = proceso;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public String getUsuario() {
		return usuario;
	}

	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}

	public Date getFecha() {
		return fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	public Eror() {
		super();
	}

	public Eror(Integer idError, String proceso, String descripcion, String usuario) {
		super();
		this.idError = idError;
		this.proceso = proceso;
		this.descripcion = descripcion;
		this.usuario = usuario;
	}

	@Override
	public String toString() {
		return id + "," + idError + "," + proceso + "," + descripcion + "," + usuario;
	}

}