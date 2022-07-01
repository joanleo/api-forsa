package com.prueba.entity;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "m_tipos_empresa")
public class TipoEmpresa {
	
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "nidtipo_empresa", length = 2)
	private Long id;
	
	@Column(name = "vcnombre", length = 20)
	private String tipo;
	
	@Column(name = "bestaActivo", columnDefinition="BOOLEAN NOT NULL DEFAULT 1")
	private Boolean estaActivo=true;
	
	@JsonIgnore
	@OneToMany(mappedBy = "tipoEmpresa")
	private List<Empresa> empresas;
	
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public Boolean getEstaActivo() {
		return estaActivo;
	}

	public void setEstaActivo(Boolean estaActivo) {
		this.estaActivo = estaActivo;
	}

	public List<Empresa> getEmpresas() {
		return empresas;
	}

	public void setEmpresas(List<Empresa> empresas) {
		this.empresas = empresas;
	}	

	
	
	
	

	public TipoEmpresa() {
		super();
	}

	public TipoEmpresa(Long id, String tipo, String descripcion) {
		super();
		this.id = id;
		this.tipo = tipo;

	}
	
}
