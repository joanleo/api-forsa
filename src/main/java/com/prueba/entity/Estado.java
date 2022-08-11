package com.prueba.entity;

import java.util.Objects;

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

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;


@Entity
@Table(name = "m_estados")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Estado {
    
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE,
    		generator = "seq_estado")
	@SequenceGenerator(name = "seq_estado", allocationSize = 10)
	@Column(name = "nidestado", length = 2)
    private Long id;
	
	@Column(name = "vcnombre", length = 20)
    private String tipo="";
	
	@JsonIgnore
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

	
	
	
	public Estado() {
		super();
	}

	public Estado(Long id) {
		super();
		this.id = id;
	}

	public Estado(String tipo, Empresa empresa) {
		super();
		this.tipo = tipo;
		this.empresa = empresa;
	}

	@Override
	public int hashCode() {
		return Objects.hash(empresa, estaActivo, id, tipo);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Estado other = (Estado) obj;
		return Objects.equals(empresa, other.empresa) && Objects.equals(estaActivo, other.estaActivo)
				&& Objects.equals(id, other.id) && Objects.equals(tipo, other.tipo);
	}

}
