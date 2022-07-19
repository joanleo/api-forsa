package com.prueba.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

import javax.persistence.CascadeType;
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
@Table(name = "detalle_mov")
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
    @JoinColumn(name = "vcnitempresa")
    private Empresa empresa;
	
	@ManyToOne(targetEntity = Usuario.class, cascade = CascadeType.ALL)
	private Usuario usuarioEnvio;
	
	@ManyToOne(targetEntity = Usuario.class, cascade = CascadeType.ALL)
	private Usuario usuarioRecibe;
	
	@Column(name = "dfecha_envio")
	public Date fechaEnvio;
	
	@Column(name = "dfecha_recibe")
	public Date fechaRecibe;

	public DetalleInv() {
		super();
	}

	public DetalleInv(MovInventario movimiento, Producto producto) {
		super();
		System.out.println(movimiento.getIdMov() +" "+ producto.getCodigoPieza());
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

	public Empresa getEmpresa() {
		return empresa;
	}

	public void setEmpresa(Empresa empresa) {
		this.empresa = empresa;
	}

	public Usuario getUsuarioEnvio() {
		return usuarioEnvio;
	}

	public void setUsuarioEnvio(Usuario usuarioEnvio) {
		this.usuarioEnvio = usuarioEnvio;
	}

	public Usuario getUsuarioRecibe() {
		return usuarioRecibe;
	}

	public void setUsuarioRecibe(Usuario usuarioRecibe) {
		this.usuarioRecibe = usuarioRecibe;
	}

	public Date getFechaEnvio() {
		return fechaEnvio;
	}

	public void setFechaEnvio(Date fechaEnvio) {
		this.fechaEnvio = fechaEnvio;
	}

	public Date getFechaRecibe() {
		return fechaRecibe;
	}

	public void setFechaRecibe(Date fechaRecibe) {
		this.fechaRecibe = fechaRecibe;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
}
