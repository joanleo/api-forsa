package com.prueba.entity;

import java.util.Collection;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

//Para realizar inventariio

@Entity
@Table(name = "mov_inventario")
public class MovInventario {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "nidmov")
	private Long id;
	
	@Column(name = "dfecha")
	private Date fecha;
	
	@OneToOne(targetEntity = Ubicacion.class, cascade = CascadeType.ALL)
	private Ubicacion ubicacion;
	
	@ManyToMany
	@JoinTable(name = "detalles", joinColumns = @JoinColumn(name = "id_mov", referencedColumnName = "nidmov" ),
	inverseJoinColumns = @JoinColumn(name = "id_producto", referencedColumnName = "vccodigopieza"))
	private Collection<Producto> productos;

	public Ubicacion getUbicacion() {
		return ubicacion;
	}

	public void setUbicacion(Ubicacion ubicacion) {
		this.ubicacion = ubicacion;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getFecha() {
		return fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	public Collection<Producto> getProductos() {
		return productos;
	}

	public void setProductos(Collection<Producto> productos) {
		this.productos = productos;
	}

}
