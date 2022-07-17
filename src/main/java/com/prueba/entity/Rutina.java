package com.prueba.entity;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;

@Entity
@Table(name = "rutinas")
@JsonAutoDetect(fieldVisibility = Visibility.ANY)
@JsonIdentityInfo(
		  generator = ObjectIdGenerators.PropertyGenerator.class, 
		  property = "idRutina")
public class Rutina implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	@Column(name = "nidutina")
	private Long idRutina;
	
	@Column(name = "vcnombre")
	private String nombre;
	
	@OneToMany(mappedBy = "rutina", cascade = CascadeType.ALL, orphanRemoval = true)
	private Set<DetalleRutina> detalles = new HashSet<>();
	
	public Long getIdRutina() {
		return idRutina;
	}

	public void setIdRutina(Long idRutina) {
		this.idRutina = idRutina;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public Set<DetalleRutina> getDetalles() {
		return detalles;
	}

	public void setDetalles(Set<DetalleRutina> detalles) {
		this.detalles = detalles;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public void addRuta(Ruta permiso) {
        DetalleRutina detalle = new DetalleRutina(this, permiso);
        detalles.add(detalle);
    }
	
	public void removeRuta(Ruta permiso) {
        for (Iterator<DetalleRutina> iterator = detalles.iterator();
             iterator.hasNext(); ) {
        	DetalleRutina detalle = iterator.next();
 
            if (detalle.getRutina().equals(this) &&
            		detalle.getRuta().equals(permiso)) {
                iterator.remove();
                detalle.setRutina(null);
                detalle.setRuta(null);
            }
        }
    }

	public Rutina(Long idRutina, String nombre, Set<DetalleRutina> detalles, Empresa empresa) {
		super();
		this.idRutina = idRutina;
		this.nombre = nombre;
		this.detalles = detalles;
	}

	public Rutina() {
		super();
	}
	
	
	
	

}
