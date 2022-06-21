package com.prueba.entity;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "m_familias")
public class Familia {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "nidfamilia", length = 3)
    private Long id;
    
    @Column(name = "vcnombre", length = 80)
    private String nombre;
    
    @Column(name = "vcsigla", length = 3)
    private String sigla;

    @JsonIgnore
    @OneToMany(mappedBy = "familia")
    private List<Producto> productos;
    
    @JsonIgnore
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "vcnitempresa")
    private Empresa empresa;
    
    @Column(name = "bestaActiva", columnDefinition="BOOLEAN NOT NULL DEFAULT 1")
    private Boolean estaActiva;
    

	public Empresa getEmpresa() {
		return empresa;
	}

	public void setEmpresa(Empresa empresa) {
		this.empresa = empresa;
	}

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

	public List<Producto> getProductos() {
		return productos;
	}

	public void setProductos(List<Producto> productos) {
		this.productos = productos;
	}

	public String getSigla() {
		return sigla;
	}

	public void setSigla(String sigla) {
		this.sigla = sigla;
	}

	public Boolean getEstaActiva() {
		return estaActiva;
	}

	public void setEstaActiva(Boolean estaActiva) {
		this.estaActiva = estaActiva;
	}

	
	
	
	public Familia(Long id) {
		super();
		this.id = id;
	}

	public Familia() {
		super();
	}

	public Familia(String nombre) {
		super();
		this.nombre = nombre;
	}    
}

