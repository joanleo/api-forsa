package com.prueba.security.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity
@Table(name = "m_rutas")
public class Ruta {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "nidRuta", length = 3)
    private Long id;

    @Column(name = "vcruta", length = 64, nullable = false)
    private String ruta;
    
    @Column(name = "vcmetodo")
    private String metodo;

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

	public String getMetodo() {
		return metodo;
	}

	public void setMetodo(String metodo) {
		this.metodo = metodo;
	}

	public Ruta() {
		super();
	}

	public Ruta(String ruta) {
		super();
		this.ruta = ruta;
	}

	public Ruta(Long id, String ruta, String descripcion) {
		super();
		this.id = id;
		this.ruta = ruta;

	}

	public Ruta(String ruta, String metodo) {
		super();
		this.ruta = ruta;
		this.metodo = metodo;
	}
    
    
}
