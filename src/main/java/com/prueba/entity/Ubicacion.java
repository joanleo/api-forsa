package com.prueba.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "mov_ubicaciones")
public class Ubicacion {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "nidubicacion", length = 4)
	private Long id;
	
	@Column(name = "vcdireccion", length = 64)
	private String direccion;
	
	@Column(name = "vcciudad", length = 20)
	private String ciudad;
	
	@Column(name = "vcnombre", length = 32)
	private String nombre;
	
	@ManyToOne
	@JoinColumn(name = "nidtipo_ubicacion")
	private TipoUbicacion tipo;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getDireccion() {
		return direccion;
	}

	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}

	public String getCiudad() {
		return ciudad;
	}

	public void setCiudad(String ciudad) {
		this.ciudad = ciudad;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public TipoUbicacion getTipo() {
		return tipo;
	}

	public void setTipo(TipoUbicacion tipo) {
		this.tipo = tipo;
	}

	public Ubicacion() {
		super();
	}

	public Ubicacion(Long id) {
		super();
		this.id = id;
	}

}
