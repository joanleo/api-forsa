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


@Entity
@Table(name = "Poli_rol")
public class PoliRol {
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	@Column(name = "nidPoli_Rol", length = 3)
	private Long id;
	
	@Column(name = "vcnombre", length = 20)
	private String nombre;
	
	@ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Rol role;
	
	@OneToOne
	private Ruta ruta;
	
	public Rol getRoles() {
		return role;
	}

	public void setRoles(Rol roles) {
		this.role = roles;
	}

	public Ruta getRuta() {
		return ruta;
	}

	public void setRuta(Ruta ruta) {
		this.ruta = ruta;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Rol getRole() {
		return role;
	}

	public void setRole(Rol role) {
		this.role = role;
	}
	
	public PoliRol() {
		super();
	}

	public PoliRol(String nombre) {
		super();
		this.nombre = nombre;
	}

	public PoliRol(String nombre, Rol role, Ruta ruta) {
		super();
		this.nombre = nombre;
		this.role = role;
		this.ruta = ruta;
	}
	

}
