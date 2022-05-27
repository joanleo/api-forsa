package com.prueba.dto;

import com.prueba.entity.Empresa;
import com.prueba.entity.Estado;
import com.prueba.entity.Fabricante;
import com.prueba.entity.Familia;
import com.prueba.entity.Ubicacion;

public class SearchDTO {
	
	private String codigoPieza;
	
	private String descripcion;
	
	private String area;
	
	private String orden;
	
	private Fabricante fabricante;
	
	private Familia familia;
	
	private Estado estado;
	
	private Empresa empresa;
	
	private Ubicacion ubicacion;
	
	private Boolean verificado;
	
	private Boolean estaActivo;
	
	private String motivoIngreso;
	

	public SearchDTO() {
		super();
	}

	public String getCodigoPieza() {
		return codigoPieza;
	}

	public void setCodigoPieza(String codigoPieza) {
		this.codigoPieza = codigoPieza;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
	}

	public String getOrden() {
		return orden;
	}

	public void setOrden(String orden) {
		this.orden = orden;
	}

	public Fabricante getFabricante() {
		return fabricante;
	}

	public void setFabricante(Fabricante fabricante) {
		this.fabricante = fabricante;
	}

	public Familia getFamilia() {
		return familia;
	}

	public void setFamilia(Familia familia) {
		this.familia = familia;
	}

	public Estado getEstado() {
		return estado;
	}

	public void setEstado(Estado estado) {
		this.estado = estado;
	}

	public Empresa getEmpresa() {
		return empresa;
	}

	public void setEmpresa(Empresa empresa) {
		this.empresa = empresa;
	}

	public Boolean getVerificado() {
		return verificado;
	}

	public void setVerificado(Boolean verificado) {
		this.verificado = verificado;
	}

	public Ubicacion getUbicacion() {
		return ubicacion;
	}

	public void setUbicacion(Ubicacion ubicacion) {
		this.ubicacion = ubicacion;
	}

	public Boolean getEstaActivo() {
		return estaActivo;
	}

	public void setEstaActivo(Boolean estaActivo) {
		this.estaActivo = estaActivo;
	}

	public String getMotivoIngreso() {
		return motivoIngreso;
	}

	public void setMotivoIngreso(String motivoIngreso) {
		this.motivoIngreso = motivoIngreso;
	}

	@Override
	public String toString() {
		return "SearchDTO [codigoPieza=" + codigoPieza + ", descripcion=" + descripcion + ", area=" + area + ", orden="
				+ orden + ", fabricante=" + fabricante + ", familia=" + familia + ", estado=" + estado + ", empresa="
				+ empresa + ", ubicacion=" + ubicacion + ", verificado=" + verificado + ", estaActivo=" + estaActivo
				+ "]";
	}

}
