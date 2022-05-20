package com.prueba.entity;


import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.prueba.security.entity.Usuario;


@Entity
@Table(name = "Empresas")
public class Empresa {
	
	@Id
	@Column(name = "vcnitempresa", nullable = false)
	private Long nit;
	
	@Column(name = "vcnombre", nullable = false)
	private String nombre;
	
	@Column(name = "nconfirmacion")
	private String nconfimacion;
	
	@ManyToOne()
	@JoinColumn(name = "nidtipo_empresa")
	private TipoEmpresa tipoEmpresa;
	
	@JsonIgnore
	@OneToMany(mappedBy = "empresa")
	private List<Producto> productos;
	
	@JsonIgnore
	@OneToMany(mappedBy = "empresa")
	private List<Usuario> usuarios;

	public List<Usuario> getUsuarios() {
		return usuarios;
	}

	public void setUsuarios(List<Usuario> usuarios) {
		this.usuarios = usuarios;
	}

	public List<Producto> getProductos() {
		return productos;
	}

	public void setProductos(List<Producto> productos) {
		this.productos = productos;
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

	public String getNconfimacion() {
		return nconfimacion;
	}

	public void setNconfimacion(String nconfimacion) {
		this.nconfimacion = nconfimacion;
	}

	public TipoEmpresa getTipoEmpresa() {
		return tipoEmpresa;
	}

	public void setTipoEmpresa(TipoEmpresa tipoEmpresa) {
		this.tipoEmpresa = tipoEmpresa;
	}

	public Empresa() {
		super();
	}
	
	
}
