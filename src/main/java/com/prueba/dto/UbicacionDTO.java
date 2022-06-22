package com.prueba.dto;

import com.prueba.entity.Empresa;
import com.prueba.entity.TipoUbicacion;

public class UbicacionDTO {
	
	private Long id;
	
	private String direccion;
	
	private String ciudad;
	
	private String nombre;
	
	private TipoUbicacion tipo;
	
	private Empresa empresa;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getDireccion() {
		return direccion;
	}

	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}

	public String getCiudad() {
		return ciudad;
	}

	public void setCiudad(String ciudad) {
		this.ciudad = ciudad;
	}

	public TipoUbicacion getTipo() {
		return tipo;
	}

	public void setTipo(TipoUbicacion tipo) {
		this.tipo = tipo;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public Empresa getEmpresa() {
		return empresa;
	}

	public void setEmpresa(Empresa empresa) {
		this.empresa = empresa;
	}

}
