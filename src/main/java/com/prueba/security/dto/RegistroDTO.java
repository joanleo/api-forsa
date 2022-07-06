package com.prueba.security.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class RegistroDTO {

	@NotBlank
	private String nombre;
	@NotBlank
	private String username;
	@NotEmpty
	private String email;
	@NotNull
	@Size(min = 4, max = 15)
	private String password;
	@NotEmpty
	private Long nitEmpresa;

	public RegistroDTO() {
		super();
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Long getNitEmpresa() {
		return nitEmpresa;
	}

	public void setNitEmpresa(Long nitEmpresa) {  
		this.nitEmpresa = nitEmpresa;
	}
}

