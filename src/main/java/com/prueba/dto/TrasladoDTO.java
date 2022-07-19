package com.prueba.dto;

import java.util.List;

import com.prueba.entity.Producto;
import com.prueba.entity.Ubicacion;

public class TrasladoDTO {
	
	private List<Producto> productos;
	
	private int cantProductos;
	
	private Ubicacion origen;
	
	private Ubicacion destino;

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

	public Ubicacion getOrigen() {
		return origen;
	}

	public void setOrigen(Ubicacion origen) {
		this.origen = origen;
	}

	public Ubicacion getDestino() {
		return destino;
	}

	public void setDestino(Ubicacion destino) {
		this.destino = destino;
	}


}
