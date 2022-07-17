package com.prueba.security.entity;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.prueba.entity.DetalleRutina;
import com.prueba.entity.Empresa;
import com.prueba.entity.Politica;


@Entity
@Table(name = "Roles")
@JsonAutoDetect(fieldVisibility = Visibility.ANY)
@JsonIdentityInfo(
		  generator = ObjectIdGenerators.PropertyGenerator.class, 
		  property = "idRol")
public class Rol implements Serializable{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "nidrol", length = 2)
    private Long idRol;
    
    @Column(name = "vcnombre", length = 20, nullable = false)
    private String nombre;
    
    @OneToMany(mappedBy = "rol")
    private Set<Politica> politicas;
    
    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vcnitEmpresa")
    private Empresa empresa;
    
    @JsonIgnore
    @OneToMany(targetEntity=Usuario.class, mappedBy="rol",cascade=CascadeType.ALL, fetch = FetchType.LAZY)  
    private Set<Usuario> usuario = new HashSet<>();
    
    @Column(name = "bestaActivo", columnDefinition="BOOLEAN NOT NULL DEFAULT 1")
    private Boolean estaActivo=true;
    
    public void addPolitica(DetalleRutina detalle) {
    	System.out.println("añadiendo politica");
    	Politica nuevaPolitica = new Politica(this,detalle,false);
    	System.out.println(nuevaPolitica.getDetalle().getRuta().getNombre());
    	politicas.add(nuevaPolitica);
    	
    }

	public Long getIdRol() {
		return idRol;
	}

	public void setIdRol(Long idRol) {
		this.idRol = idRol;
	}


	public Set<Politica> getPoliticas() {
		return politicas;
	}

	public void setPoliticas(Set<Politica> politica) {
		this.politicas = politica;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
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

	public Set<Usuario> getUsuario() {
		return usuario;
	}

	public void setUsuario(Set<Usuario> usuario) {
		this.usuario = usuario;
	}

	public Boolean getEstaActivo() {
		return estaActivo;
	}

	public void setEstaActivo(Boolean estaActivo) {
		this.estaActivo = estaActivo;
	}

	public Rol() {
		super();
	}

	public Rol(String nombre) {
		super();
		this.nombre = nombre;
		this.politicas = new HashSet<>();
	}

}