package com.prueba.security.entity;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;


@Entity
@Table(name = "politica")
public class PoliRol {
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	@Column(name = "nidPoli_Rol", length = 3)
	private Long id;
	
	@Column(name = "vcnombre", length = 20)
	private String nombre;
	
	@OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	private Ruta ruta;
	
	@Column(name = "vcmetodo", length = 20)
	private String metodo;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public Ruta getRuta() {
		return ruta;
	}

	public void setRuta(Ruta ruta) {
		this.ruta = ruta;
	}

	public String getMetodo() {
		return metodo;
	}

	public void setMetodo(String metodo) {
		this.metodo = metodo;
	}

	public PoliRol(Long id, String nombre, Rol role, Ruta ruta) {
		super();
		this.id = id;
		this.nombre = nombre;		this.ruta = ruta;
	}

	public PoliRol(String nombre) {
		super();
		this.nombre = nombre;
	}

	public PoliRol() {
		super();
	}
	
	

}
