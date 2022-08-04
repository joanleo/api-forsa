package com.prueba.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.prueba.security.entity.Usuario;
import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
@Table(name = "mov_det_inventarios")
@JsonAutoDetect(fieldVisibility = Visibility.ANY)
public class DetalleInv implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private DetalleInv_id id;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@MapsId("idMov")
	@JsonBackReference
	private MovInventario movimiento;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@MapsId("codigoPieza")
	private Producto producto;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "usuarioConfirma", referencedColumnName = "nidusuario")
	private Usuario usuarioConfirma;
	
	@Column(name = "dfecha_confirma")
	public Date fechaConfirma;	

	public DetalleInv() {
		super();
	}

	public DetalleInv(MovInventario movimiento, Producto producto) {
		super();
		this.id = new DetalleInv_id(movimiento.getIdMov(), producto.getCodigoPieza());
		this.movimiento = movimiento;
		this.producto = producto;
	}

	public DetalleInv_id getId() {
		return id;
	}

	public void setId(DetalleInv_id id) {
		this.id = id;
	}

	public MovInventario getMovimiento() {
		return movimiento;
	}

	public void setMovimiento(MovInventario movimiento) {
		this.movimiento = movimiento;
	}

	public Producto getProducto() {
		return producto;
	}

	public void setProducto(Producto producto) {
		this.producto = producto;
	}
	
	public Usuario getUsuarioConfirma() {
		return usuarioConfirma;
	}

	public void setUsuarioConfirma(Usuario usuarioConfirma) {
		this.usuarioConfirma = usuarioConfirma;
	}

	public Date getFechaConfirma() {
		return fechaConfirma;
	}

	public void setFechaConfirma(Date fechaConfirma) {
		this.fechaConfirma = fechaConfirma;
	}

	@Override
    public boolean equals(Object o) {
        if (this == o) return true;
 
        if (o == null || getClass() != o.getClass())
            return false;
 
        DetalleInv that = (DetalleInv) o;
        return Objects.equals(movimiento, that.movimiento) &&
               Objects.equals(producto, that.producto);
    }
 
    @Override
    public int hashCode() {
        return Objects.hash(movimiento, producto);
    }



	public static long getSerialversionuid() {
		return serialVersionUID;
	}
}
