package com.prueba.security.entity;

import java.util.Collection;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.prueba.entity.Empresa;


@Entity
@Table(name = "m_usuario", uniqueConstraints = { @UniqueConstraint(columnNames = { "vcusername", "vcemail" }) })
public class Usuario {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "nidusuario", length = 3)
    private Long id;
    
    @Column(name = "vcnombre", length = 48)
    private String nombre;
    
    @JsonIgnore
    @Column(name = "vcusername", length = 20)
    private String username;
    
    @JsonIgnore
    @Column(name = "vcemail", length = 48)
    private String email;
    
    @JsonIgnore
    @Column(name = "vcpassword")
    private String password;
    
    @JsonIgnore
    @Column(name = "vctokenpassword")
    private String tokenPassword;
    
    @JsonIgnore
	@ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@JoinTable(name = "usuarios_roles", joinColumns = @JoinColumn(name = "usuario_id", referencedColumnName = "nidusuario"), 
	inverseJoinColumns = @JoinColumn(name = "rol_id", referencedColumnName = "nidrol"))
	private Collection<Rol> roles;
	
    @JsonIgnore
	@ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "vcnitempresa")
    private Empresa empresa;
	
	public Empresa getEmpresa() {
		return empresa;
	}

	public void setEmpresa(Empresa empresa) {
		this.empresa = empresa;
	}

	public Collection<Rol> getRoles() {
		return roles;
	}

	public void setRoles(Collection<Rol> roles) {
		this.roles = roles;
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

	public String getTokenPassword() {
		return tokenPassword;
	}

	public void setTokenPassword(String tokenPassword) {
		this.tokenPassword = tokenPassword;
	}
	

}
