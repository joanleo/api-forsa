package com.prueba.security.entity;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;



@Entity
@Table(name = "politica")
public class PoliRol {
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	@Column(name = "nidPolitica", length = 3)
	private Long id;
	
	@Column(name = "vcnombre", length = 80)
	private String nombre;
	
	@OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	private Ruta ruta;

	@Column(name = "bpermitido", columnDefinition="BOOLEAN NOT NULL DEFAULT 0")
    private Boolean permitido = false;

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

	public Boolean getPermitido() {
		return permitido;
	}

	public void setPermitido(Boolean permitido) {
		this.permitido = permitido;
	}

	public PoliRol() {
		super();
	}

	public PoliRol(Ruta ruta) {
		super();
		this.ruta = ruta;
	}

	public PoliRol(String nombre) {
		super();
		this.nombre = nombre;
	}

	public PoliRol(Long id, String nombre, Ruta ruta, Boolean permitido) {
		super();
		this.id = id;
		this.nombre = nombre;
		this.ruta = ruta;
		this.permitido = permitido;
	}

	

	
	

}
