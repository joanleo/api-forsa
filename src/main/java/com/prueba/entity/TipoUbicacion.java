package com.prueba.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "tipos_ubicacion")
public class TipoUbicacion {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "nidtipo_ubicacion")
	private Long id;
	
	@Column(name = "vcnombre")
	private String nombre;


	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getnombre() {
		return nombre;
	}

	public void setnombre(String tipo) {
		this.nombre = tipo;
	}	

}
