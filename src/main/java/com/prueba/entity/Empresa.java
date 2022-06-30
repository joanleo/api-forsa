package com.prueba.entity;


import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
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
@Table(name = "m_empresas")
public class Empresa {
	@JsonIgnore
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
	@OneToMany(mappedBy = "empresa", cascade = CascadeType.ALL)
	private List<Producto> productos;
	
	@JsonIgnore
	@OneToMany(mappedBy = "empresa")
	private List<Usuario> usuarios;
	
	@JsonIgnore
	@OneToMany(mappedBy = "empresa")
	private List<Familia> familias;

	@Column(name = "bestaActivo", columnDefinition="BOOLEAN NOT NULL DEFAULT 1")
	private Boolean estaActivo=true;


	
	
	
	public List<Familia> getFamilias() {
		return familias;
	}

	public void setFamilias(List<Familia> familias) {
		this.familias = familias;
	}

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

	public Date getFecha() {
		return fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}	


	public Boolean getEstaActivo() {
		return estaActivo;
	}

	public void setEstaActivo(Boolean estaActivo) {
		this.estaActivo = estaActivo;
	}

	public Empresa() {
		super();
	}

	public Empresa(Long nit) {
		super();
		this.nit = nit;
	}

	@Override
	public String toString() {
		return "Empresa [fecha=" + fecha + ", nit=" + nit + ", nombre=" + nombre + ", nconfimacion=" + nconfimacion
				+ ", tipoEmpresa=" + tipoEmpresa + ", productos=" + productos + ", usuarios=" + usuarios + "]";
	}
	
	
}
