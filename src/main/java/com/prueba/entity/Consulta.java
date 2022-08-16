package com.prueba.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "vw_consulta")
@org.hibernate.annotations.Immutable
public class Consulta implements Serializable{

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "vcnitempresa")
	private Long nitEmpresa;
	
	@Column(name = "vcnitfabricante")
	private Long nitfabricante;
	
	@Column(name = "vcsigla")
	private String siglaFamilia;
	
	@Column(name = "vcnombre")
	private String tipoActivo;


	public Long getNitEmpresa() {
		return nitEmpresa;
	}

	public void setNitEmpresa(Long nitEmpresa) {
		this.nitEmpresa = nitEmpresa;
	}

	public Long getNitfabricante() {
		return nitfabricante;
	}

	public void setNitfabricante(Long nitfabricante) {
		this.nitfabricante = nitfabricante;
	}

	public String getSiglaFamilia() {
		return siglaFamilia;
	}

	public void setSiglaFamilia(String siglaFamilia) {
		this.siglaFamilia = siglaFamilia;
	}

	public String getTipoActivo() {
		return tipoActivo;
	}

	public void setTipoActivo(String tipoActivo) {
		this.tipoActivo = tipoActivo;
	}

	@Override
	public String toString() {
		return "Consulta [nitEmpresa=" + nitEmpresa + ", nitfabricante=" + nitfabricante
				+ ", siglaFamilia=" + siglaFamilia + ", tipoActivo=" + tipoActivo + "]";
	}
}
