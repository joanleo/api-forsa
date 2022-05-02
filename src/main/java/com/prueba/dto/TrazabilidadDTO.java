package com.prueba.dto;

import com.prueba.entity.Producto;
import com.prueba.entity.TipoMov;

public class TrazabilidadDTO {

	private String descripcion;
	
	private TipoMov tipoMov;
	
	private Producto producto;

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public TipoMov getTipoMov() {
		return tipoMov;
	}

	public void setTipoMov(TipoMov tipoMov) {
		this.tipoMov = tipoMov;
	}

	public Producto getProducto() {
		return producto;
	}

	public void setProducto(Producto producto) {
		this.producto = producto;
	}
}
