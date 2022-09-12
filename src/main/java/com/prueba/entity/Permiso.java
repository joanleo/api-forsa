package com.prueba.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.prueba.util.ListToStringConverter;

@Entity
@Table(name = "m_permisos")
public class Permiso implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE,
    		generator = "seq_ruta")
	@SequenceGenerator(name = "seq_ruta", allocationSize = 10)
	@Column(name = "nidruta")
	private Long idRuta;
	

	@Column(name = "vcurl")
	@Convert(converter = ListToStringConverter.class)
	private List<String> url = new ArrayList<>();
	
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

	public List<String> getUrl() {
		return url;
	}

	public void setUrl(List<String> url) {
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

	public Permiso() {
		super();
	}

	public Permiso(Long idRuta, List<String> url, String nombre, String metodo) {
		super();
		this.idRuta = idRuta;
		this.url = url;
		this.nombre = nombre;
		this.metodo = metodo;
	}
	
	public void addUrl(String url) {
		this.url.add(url);
	}

	@Override
	public String toString() {
		return "Permiso [idRuta=" + idRuta + ", url=" + url + ", nombre=" + nombre + ", metodo=" + metodo + "]";
	}


		

}
