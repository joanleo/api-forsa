package com.prueba.entity;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class Producto_id implements Serializable{

	private static final long serialVersionUID = 1L;
	
	@Column(name = "vcnitempresa")
	private Long nitEmpresa;
	
	@Column(name = "vccodigopieza")
	private String codigoPieza;

	public Producto_id() {
		super();
	}

	public Producto_id(Long nitEmpresa, String codigoPieza) {
		super();
		this.nitEmpresa = nitEmpresa;
		this.codigoPieza = codigoPieza;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public Long getNitEmpresa() {
		return nitEmpresa;
	}

	public void setNitEmpresa(Long nitEmpresa) {
		this.nitEmpresa = nitEmpresa;
	}

	public String getCodigoPieza() {
		return codigoPieza;
	}

	public void setCodigoPieza(String codigoPieza) {
		this.codigoPieza = codigoPieza;
	}

	@Override
	public int hashCode() {
		return Objects.hash(codigoPieza, nitEmpresa);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Producto_id other = (Producto_id) obj;
		return Objects.equals(codigoPieza, other.codigoPieza) && 
				Objects.equals(nitEmpresa, other.nitEmpresa);
	}
	
}
