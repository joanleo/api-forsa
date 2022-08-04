/**
 * 
 */
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
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.prueba.security.entity.Usuario;

/**
 * @author Joan Leon
 *
 */
@Entity
@Table(name = "mov_det_salidas")
@JsonAutoDetect(fieldVisibility = Visibility.ANY)
public class DetalleSalida implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@EmbeddedId
	private DetalleSalida_id id;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@MapsId("idSalida")
	@JsonBackReference
	private Salida salida;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@MapsId("codigoPieza")
	private Producto producto;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "usuarioConfirma", referencedColumnName = "nidusuario")
	private Usuario usuarioConfirma;
	
	@Column(name = "dfecha_confirma")
	public Date fechaConfirma;

	public DetalleSalida(Salida salida, Producto producto) {
		super();
		this.salida = salida;
		this.producto = producto;
		this.id = new DetalleSalida_id(salida.getIdSalida(), producto.getCodigoPieza());
	}

	public DetalleSalida() {
		super();
	}

	public DetalleSalida_id getId() {
		return id;
	}

	public void setId(DetalleSalida_id id) {
		this.id = id;
	}
	
	public Salida getSalida() {
		return salida;
	}

	public void setSalida(Salida salida) {
		this.salida = salida;
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

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, producto, salida, usuarioConfirma);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DetalleSalida other = (DetalleSalida) obj;
		return Objects.equals(id, other.id) && Objects.equals(producto, other.producto)
				&& Objects.equals(salida, other.salida) && Objects.equals(usuarioConfirma, other.usuarioConfirma);
	}

}
