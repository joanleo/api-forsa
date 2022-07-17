package com.prueba.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "rutas")
public class Ruta implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	@Column(name = "nidpermiso")
	private Long idRuta;
	
	@Column(name = "vcurl")
	private String url;
	
	@Column(name = "vcnombre")
	private String nombre;
	
	@Column(name = "vcmetodo")
	private String metodo;


	public Long getIdRuta() {
		return idRuta;
	}

	public void setIdRuta(Long idRuta) {
		this.idRuta = idRuta;
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

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public Ruta() {
		super();
	}

	public Ruta(Long idPermiso, String url, String nombre, String metodo, Boolean permitido) {
		super();
		this.idRuta = idPermiso;
		this.url = url;
		this.nombre = nombre;
		this.metodo = metodo;
	}

		

}
