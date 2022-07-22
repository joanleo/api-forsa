package com.prueba.security.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import com.prueba.entity.Empresa;
import com.prueba.security.entity.Rol;

public class RegistroDTO {
	
	private Long id; 
	
	@NotBlank
	private String nombre;
	
	@NotBlank
	@Size(min = 6, max = 10)
	private String nombreUsuario;
	
	@NotBlank
	@Email
	private String email;
	
	@NotBlank
	@Size(min = 6, max = 10)
	private String contrasena;
	
	private Empresa empresa;
	
	private Boolean estaActivo=true;
	
	private Rol rol;

	public RegistroDTO() {
		super();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getNombreUsuario() {
		return nombreUsuario;
	}

	public void setNombreUsuario(String nombreUsuario) {
		this.nombreUsuario = nombreUsuario;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getContrasena() {
		return contrasena;
	}

	public void setContrasena(String contrasena) {
		this.contrasena = contrasena;
	}

	public Empresa getEmpresa() {
		return empresa;
	}

	public void setEmpresa(Empresa nitEmpresa) {
		this.empresa = nitEmpresa;
	}

	public Boolean getEstaActivo() {
		return estaActivo;
	}

	public void setEstaActivo(Boolean estaActivo) {
		this.estaActivo = estaActivo;
	}

	public Rol getRol() {
		return rol;
	}

	public void setRol(Rol rol) {
		this.rol = rol;
	}

}

