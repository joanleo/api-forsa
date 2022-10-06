package com.prueba.entity;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name = "m_tipo_mov")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class TipoMov {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE,
    		generator = "seq_tipo_mov")
	@SequenceGenerator(name = "seq_tipo_mov", allocationSize = 10)
	@Column(name = "nidtipo_mov", length = 2)
	private Long id;
	
	@Column(name = "vcnombre", length = 20)
	private String nombre;
	
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "vcnitempresa", referencedColumnName = "vcnitempresa")
    private Empresa empresa;

	@Column(name = "bestaActivo", columnDefinition="BOOLEAN NOT NULL DEFAULT 1")
	private Boolean estaActivo;
	
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

	public Boolean getEstaActivo() {
		return estaActivo;
	}

	public void setEstaActivo(Boolean estaActivo) {
		this.estaActivo = estaActivo;
	}

	public Empresa getEmpresa() {
		return empresa;
	}
	
	

	public TipoMov(Long id, String nombre, Empresa empresa, Boolean estaActibo) {
		super();
		this.id = id;
		this.nombre = nombre;
		this.empresa = empresa;
		this.estaActivo = estaActibo;
	}

	public TipoMov(Long id) {
		super();
		this.id = id;
	}

	public TipoMov() {
		super();
	}
	
	
}
