package com.prueba.entity;


import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.prueba.security.entity.Usuario;


@Entity
@Table(name = "empresas")
public class Empresa {
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "dfecha")
	private Date fecha;
	
	@PrePersist
	private void onCreate() {
		fecha = new Date();
	}
	
	@Id
	@Column(name = "vcnitempresa", length = 12, nullable = false)
	private Long nit;
	
	@Column(name = "vcnombre", length = 48, nullable = false)
	private String nombre;
	
	@Column(name = "nconfirmacion", length = 8)
	private String nconfimacion = "D-0";
	
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
