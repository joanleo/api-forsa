package com.prueba.dto;

import java.util.List;

import com.prueba.entity.Empresa;
import com.prueba.entity.Producto;
import com.prueba.entity.Ubicacion;

public class MovInventarioDTO {
	
	private Integer id;
	
	private Ubicacion ubicacion;
	
	private List<Producto> productos;
	
	private UsuarioDTO realizo;
	
	private Empresa empresa;

	public UsuarioDTO getRealizo() {
		return realizo;
	}

	public void setRealizo(UsuarioDTO realizo) {
		this.realizo = realizo;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
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

	public Empresa getEmpresa() {
		return empresa;
	}

	public void setEmpresa(Empresa empresa) {
		this.empresa = empresa;
	}

	public MovInventarioDTO() {
		super();
	}
	

}
