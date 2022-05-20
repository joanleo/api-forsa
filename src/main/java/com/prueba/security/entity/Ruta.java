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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "nidRuta", length = 3)
    private Long id;

    @Column(name = "vcruta", length = 64, nullable = false)
    private String ruta;

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

	public Ruta() {
		super();
	}

	public Ruta(Long id, String ruta, String descripcion) {
		super();
		this.id = id;
		this.ruta = ruta;

	}
    
    
}
