package com.prueba.dto;

import java.util.List;

import com.prueba.entity.Producto;

public class TrasladoDTO {
	
	private List<Producto> productos;
	
	private int cantProductos;
	
	private String origen;
	
	private String destino;

	public List<Producto> getProductos() {
		return productos;
	}

	public void setProductos(List<Producto> productos) {
		this.productos = productos;
	}

	public int getCantProductos() {
		return cantProductos;
	}

	public void setCantProductos(int cantProductos) {
		this.cantProductos = cantProductos;
	}

	public String getOrigen() {
		return origen;
	}

	public void setOrigen(String origen) {
		this.origen = origen;
	}

	public String getDestino() {
		return destino;
	}

	public void setDestino(String destino) {
		this.destino = destino;
	}

}
