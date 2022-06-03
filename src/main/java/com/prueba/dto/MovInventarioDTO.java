package com.prueba.dto;

import java.util.List;

import com.prueba.entity.Producto;
import com.prueba.entity.Ubicacion;

public class MovInventarioDTO {
	
	private Long id;
	
	private Ubicacion ubicacion;
	
	private List<Producto> productos;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Ubicacion getUbicacion() {
		return ubicacion;
	}

	public void setUbicacion(Ubicacion ubicacion) {
		this.ubicacion = ubicacion;
	}

	public List<Producto> getProductos() {
		return productos;
	}

	public void setProductos(List<Producto> productos) {
		this.productos = productos;
	}

	public MovInventarioDTO() {
		super();
	}
	

}
