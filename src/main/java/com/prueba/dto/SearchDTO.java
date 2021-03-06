package com.prueba.dto;

import com.prueba.entity.Empresa;
import com.prueba.entity.Estado;
import com.prueba.entity.Fabricante;
import com.prueba.entity.Familia;
import com.prueba.entity.TipoActivo;
import com.prueba.entity.Ubicacion;
import com.prueba.security.entity.Usuario;

public class SearchDTO {
	
	private String codigoPieza;
	
	private String descripcion;
	
	private String area;
	
	private String orden;
	
	private Fabricante fabricante;
	
	private Familia familia;
	
	private TipoActivo tipo;
	
	private Estado estado;
	
	private Empresa empresa;
	
	private Ubicacion ubicacion;
	
	private Boolean verificado;
	
	private Boolean estaActivo;
	
	private String motivoIngreso;
	
	private String medidas;
	
	private Usuario reviso;
	
	//private Producto_id idProducto = new Producto_id(empresa.getNit(), codigoPieza);
	

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

	public TipoActivo getTipo() {
		return tipo;
	}

	public void setTipo(TipoActivo tipo) {
		this.tipo = tipo;
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
/*
	public Producto_id getIdProducto() {
		return idProducto;
	}

	public void setIdProducto(Producto_id idProducto) {
		this.idProducto = idProducto;
	}
*/

	public String getMedidas() {
		return medidas;
	}

	public void setMedidas(String medidas) {
		this.medidas = medidas;
	}

	public Usuario getReviso() {
		return reviso;
	}

	public void setReviso(Usuario reviso) {
		this.reviso = reviso;
	}


}
