package com.prueba.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "permisos")
public class Permiso implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	@Column(name = "nidpermiso")
	private Long idPermiso;
	
	@Column(name = "vcurl")
	private String url;
	
	@Column(name = "vcnombre")
	private String nombre;
	
	@Column(name = "vcmetodo")
	private String metodo;
	
	@Column(name = "bpermitido", columnDefinition="BOOLEAN NOT NULL DEFAULT 0")
	private Boolean permitido=false;

	public Long getIdPermiso() {
		return idPermiso;
	}

	public void setIdPermiso(Long idPermiso) {
		this.idPermiso = idPermiso;
	}

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

	public String getMetodo() {
		return metodo;
	}

	public void setMetodo(String metodo) {
		this.metodo = metodo;
	}

	public Boolean getPermitido() {
		return permitido;
	}

	public void setPermitido(Boolean permitido) {
		this.permitido = permitido;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public Permiso() {
		super();
	}

	public Permiso(Long idPermiso, String url, String nombre, String metodo, Boolean permitido) {
		super();
		this.idPermiso = idPermiso;
		this.url = url;
		this.nombre = nombre;
		this.metodo = metodo;
		this.permitido = permitido;
	}

		

}
