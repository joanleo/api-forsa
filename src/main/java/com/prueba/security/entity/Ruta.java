package com.prueba.security.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity
@Table(name = "Rutas")
public class Ruta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "nidRuta")
    private Long id;

    @Column(name = "vcruta", nullable = false)
    private String ruta;

    @Column(name = "vcdescripcion")
    private String descripcion;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getRuta() {
		return ruta;
	}

	public void setRuta(String ruta) {
		this.ruta = ruta;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}


	public Ruta() {
		super();
	}

	public Ruta(Long id, String ruta, String descripcion) {
		super();
		this.id = id;
		this.ruta = ruta;
		this.descripcion = descripcion;

	}
    
    
}
