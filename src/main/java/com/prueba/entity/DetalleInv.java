package com.prueba.entity;

import java.util.Objects;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
@Table(name = "detalle_mov")
@JsonAutoDetect(fieldVisibility = Visibility.ANY)
public class DetalleInv {

	@EmbeddedId
	private DetalleInv_id id;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@MapsId("idMov")
	@JsonBackReference
	private MovInventario movimiento;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@MapsId("id")
	private Producto producto;

	public DetalleInv() {
		super();
	}

	public DetalleInv(MovInventario movimiento, Producto producto) {
		super();
		this.id = new DetalleInv_id(movimiento.getId(), producto.getCodigoPieza());
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
}
