package com.prueba.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.prueba.security.entity.Usuario;

//Para realizar inventariio

@Entity
@Table(name = "mov_inventario", uniqueConstraints = { @UniqueConstraint(columnNames = { "nidmov" }) })
@JsonAutoDetect(fieldVisibility = Visibility.ANY)
public class MovInventario implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "nidmov", length = 6)
	private Long idMov;
	
	@Column(name = "dfecha")
	private Date fecha;
	
	@PrePersist
	private void onCreate() {
		fecha = new Date();
	}
	
	@OneToOne(targetEntity = Ubicacion.class, cascade = CascadeType.ALL)
	private Ubicacion ubicacion;
	
	
	@OneToMany(mappedBy = "movimiento", cascade = CascadeType.ALL, orphanRemoval = true)
	@JsonManagedReference
	private List<DetalleInv> detalles = new ArrayList<DetalleInv>();
	
	@OneToOne(targetEntity = Usuario.class, cascade = CascadeType.ALL)
	private Usuario realizo;
	
	@JsonIgnore
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "vcnitempresa")
    private Empresa empresa;

	
	
	public Usuario getRealizo() {
		return realizo;
	}

	public void setRealizo(Usuario realizo) {
		this.realizo = realizo;
	}

	public MovInventario() {
		super();
	}

	public Ubicacion getUbicacion() {
		return ubicacion;
	}

	public void setUbicacion(Ubicacion ubicacion) {
		this.ubicacion = ubicacion;
	}

	public Long getIdMov() {
		return idMov;
	}

	public void setIdMov(Long idMov) {
		this.idMov = idMov;
	}

	public Date getFecha() {
		return fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	public List<DetalleInv> getDetalles() {
		return detalles;
	}

	public void setDetalles(List<DetalleInv> detalles) {
		this.detalles = detalles;
	}

	public Empresa getEmpresa() {
		return empresa;
	}

	public void setEmpresa(Empresa empresa) {
		this.empresa = empresa;
	}

	public void addActivo(Producto producto) {
        DetalleInv detalle = new DetalleInv(this, producto);
        detalles.add(detalle);
    }
	
	public void removeActivo(Producto producto) {
        for (Iterator<DetalleInv> iterator = detalles.iterator();
             iterator.hasNext(); ) {
            DetalleInv detalle = iterator.next();
 
            if (detalle.getMovimiento().equals(this) &&
            		detalle.getProducto().equals(producto)) {
                iterator.remove();
                detalle.setMovimiento(null);
                detalle.setProducto(null);
            }
        }
    }

}
