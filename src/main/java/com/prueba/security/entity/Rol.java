package com.prueba.security.entity;

import java.util.Collection;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.prueba.entity.Empresa;

@Entity
@Table(name = "Roles")
public class Rol {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "nidrol", length = 2)
    private Long id;
    
    @Column(name = "vcnombre", length = 20, nullable = false)
    private String nombre;
    
    @Column(name = "vcdescripcion", length = 100)
    private String descripcion;
    
    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(name = "roles_poli", joinColumns = @JoinColumn(name = "rol_id", referencedColumnName = "nidrol"),
    inverseJoinColumns = @JoinColumn(name = "poli"))
    private Collection<PoliRol> poliRoles;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vcnitEmpresa")
    private Empresa empresa;

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

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}


	public Collection<PoliRol> getPoliRoles() {
		return poliRoles;
	}

	public void setPoliRoles(Collection<PoliRol> poliRoles) {
		this.poliRoles = poliRoles;
	}

	public Rol() {
		super();
	}

	public Rol(String nombre) {
		super();
		this.nombre = nombre;
	}

}