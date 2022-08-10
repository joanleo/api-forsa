package com.prueba.entity;

import java.util.List;
import java.util.Objects;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;



@Entity
@Table(name = "m_fabricantes")
public class Fabricante {
    @Id
    @Column(name = "vcnitfabricante", length = 12)
    private Long nit;
    
    @Column(name = "vcnombre",length = 48, nullable = false)
    private String nombre;
    
    @JsonIgnore
    @OneToMany(mappedBy = "fabricante", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Producto> productos;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vcnitempresa")
    private Empresa empresa;
    
    @Column(name = "bestaActivo", columnDefinition="BOOLEAN NOT NULL DEFAULT 1")
    private Boolean estaActivo;
    
	public Empresa getEmpresa() {
		return empresa;
	}

	public void setEmpresa(Empresa empresa) {
		this.empresa = empresa;
	}

	public Long getNit() {
		return nit;
	}

	public void setNit(Long nit) {
		this.nit = nit;
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
	
	public Boolean getEstaActivo() {
		return estaActivo;
	}

	public void setEstaActivo(Boolean estaActivo) {
		this.estaActivo = estaActivo;
	}

	
	
	
	public Fabricante(Long nit) {
		super();
		this.nit = nit;
	}

	public Fabricante() {
		super();
	}

	@Override
	public int hashCode() {
		return Objects.hash(empresa, estaActivo, nit, nombre, productos);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Fabricante other = (Fabricante) obj;
		return Objects.equals(empresa, other.empresa) && Objects.equals(estaActivo, other.estaActivo)
				&& Objects.equals(nit, other.nit) && Objects.equals(nombre, other.nombre)
				&& Objects.equals(productos, other.productos);
	}    
}
