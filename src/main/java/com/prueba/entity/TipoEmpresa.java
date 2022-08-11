package com.prueba.entity;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * 
 * @author Joan Leon
 *
 */
@Entity
@Table(name = "m_tipos_empresa")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class TipoEmpresa {
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE,
    		generator = "seq_tipo_empresa")
	@SequenceGenerator(name = "seq_tipo_empresa", allocationSize = 10)
	@Column(name = "nidtipo_empresa", length = 2)
	private Long id;
	
	@Column(name = "vcnombre", length = 20)
	private String tipo;
	
	@Column(name = "bestaActivo", columnDefinition="BOOLEAN NOT NULL DEFAULT 1")
	private Boolean estaActivo=true;
	
	
	
	@JsonIgnore
	@OneToMany(mappedBy = "tipoEmpresa")
	private List<Empresa> empresas;
	
	/**
	 * 
	 * @return
	 */
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

	
	
	
	

	/**
	 * Constructor sin parametros
	 */
	public TipoEmpresa() {
		super();
	}

	/**
	 * Constructor
	 * 
	 * @param id Identificador unico de la empresa
	 */
	public TipoEmpresa(Long id) {
		super();
		this.id = id;
	}

	/**
	 * 
	 * @param id 
	 * @param tipo Nombre para el tipo de empresa
	 */
	public TipoEmpresa(Long id, String tipo) {
		super();
		this.id = id;
		this.tipo = tipo;

	}
	
}
