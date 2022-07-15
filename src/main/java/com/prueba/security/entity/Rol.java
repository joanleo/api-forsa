package com.prueba.security.entity;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

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
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.prueba.entity.Empresa;
import com.prueba.entity.Rutina;

@Entity
@Table(name = "Roles")
public class Rol implements Serializable{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "nidrol", length = 2)
    private Long id;
    
    @Column(name = "vcnombre", length = 20, nullable = false)
    private String nombre;
    
    @ManyToMany
    @JoinTable(
      name = "politicas", 
      joinColumns = @JoinColumn(name = "nidrol"), 
      inverseJoinColumns = @JoinColumn(name = "nidutina"))
    private Set<Rutina> politicas;
        
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vcnitEmpresa")
    private Empresa empresa;
    
    @OneToMany(targetEntity=Usuario.class, mappedBy="rol",cascade=CascadeType.ALL, fetch = FetchType.LAZY)  
    private Set<Usuario> usuario = new HashSet<>();
    
    @Column(name = "bestaActivo", columnDefinition="BOOLEAN NOT NULL DEFAULT 1")
    private Boolean estaActivo=true;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Set<Rutina> getPoliticas() {
		return politicas;
	}
	
	public void setPoliticas(Set<Rutina> politicas) {
		this.politicas = politicas;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	


	public Empresa getEmpresa() {
		return empresa;
	}

	public void setEmpresa(Empresa empresa) {
		this.empresa = empresa;
	}

	public Set<Usuario> getUsuario() {
		return usuario;
	}

	public void setUsuario(Set<Usuario> usuario) {
		this.usuario = usuario;
	}

	public Boolean getEstaActivo() {
		return estaActivo;
	}

	public void setEstaActivo(Boolean estaActivo) {
		this.estaActivo = estaActivo;
	}

	public Rol() {
		super();
	}

	public Rol(String nombre) {
		super();
		this.nombre = nombre;
	}

}