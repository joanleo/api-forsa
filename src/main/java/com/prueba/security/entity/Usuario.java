package com.prueba.security.entity;

import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.prueba.entity.Empresa;


@Entity
@Table(name = "m_usuarios", uniqueConstraints = { @UniqueConstraint(columnNames = { "vcusername", "vcemail" }) })
@JsonAutoDetect(fieldVisibility = Visibility.ANY)
@JsonIdentityInfo(
		  generator = ObjectIdGenerators.PropertyGenerator.class, 
		  property = "id")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Usuario {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "nidusuario", length = 3)
    private Long id;
    
    @Column(name = "vcnombre", length = 48)
    private String nombre;
    
    
    @Column(name = "vcusername", length = 20)
    private String nombreUsuario;
    
    
    @Column(name = "vcemail", length = 48)
    private String email;
    
    @JsonIgnore
    @Column(name = "vcpassword")
    private String contrasena;
    
    @JsonIgnore
    @Column(name = "vctokenpassword")
    private String tokenPassword;
    
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="nidrol", referencedColumnName = "nidrol")
    private Rol rol;
	
   	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vcnitempresa", referencedColumnName = "vcnitempresa")
	private Empresa empresa;
	
	@Column(name = "bestaActivo", columnDefinition="BOOLEAN NOT NULL DEFAULT 1")
    private Boolean estaActivo=true;
	
	public Empresa getEmpresa() {
		return empresa;
	}

	public void setEmpresa(Empresa empresa) {
		this.empresa = empresa;
	}

	public Rol getRol() {
		return rol;
	}

	public void setRol(Rol rol) {
		this.rol = rol;
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

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}


	public String getNombreUsuario() {
		return nombreUsuario;
	}

	public void setNombreUsuario(String nombreUsuario) {
		this.nombreUsuario = nombreUsuario;
	}

	public String getContrasena() {
		return contrasena;
	}

	public void setContrasena(String contrasena) {
		this.contrasena = contrasena;
	}

	public String getTokenPassword() {
		return tokenPassword;
	}

	public void setTokenPassword(String tokenPassword) {
		this.tokenPassword = tokenPassword;
	}

	public Boolean getEstaActivo() {
		return estaActivo;
	}

	public void setEstaActivo(Boolean estaActivo) {
		this.estaActivo = estaActivo;
	}

	public Usuario() {
		super();
	}

	public Usuario(Long id) {
		super();
		this.id = id;
	}

	@Override
	public int hashCode() {
		return Objects.hash(contrasena, email, empresa, estaActivo, id, nombre, nombreUsuario, rol, tokenPassword);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Usuario other = (Usuario) obj;
		return Objects.equals(contrasena, other.contrasena) && Objects.equals(email, other.email)
				&& Objects.equals(empresa, other.empresa) && Objects.equals(estaActivo, other.estaActivo)
				&& Objects.equals(id, other.id) && Objects.equals(nombre, other.nombre)
				&& Objects.equals(nombreUsuario, other.nombreUsuario) && Objects.equals(rol, other.rol)
				&& Objects.equals(tokenPassword, other.tokenPassword);
	}
	

}
