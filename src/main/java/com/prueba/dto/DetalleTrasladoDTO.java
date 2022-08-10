/**
 * 
 */
package com.prueba.dto;

import java.util.Date;

import com.prueba.entity.DetalleTrasl_id;
import com.prueba.entity.Producto;
import com.prueba.entity.Traslado;

/**
 * @author Joan Leon
 *
 */
public class DetalleTrasladoDTO {
	
	private DetalleTrasl_id id;
	
	private Traslado traslado;
	
	private Producto producto;
	
	private UsuarioDTO usuarioconfirma;
	
	private UsuarioDTO usuarioRecibe;
	
	public Date fechaConfirma;

	public Date fechaRecibe;

	public DetalleTrasladoDTO() {
		super();
	}

	public DetalleTrasl_id getId() {
		return id;
	}

	public void setId(DetalleTrasl_id id) {
		this.id = id;
	}

	public Traslado getTraslado() {
		return traslado;
	}

	public void setTraslado(Traslado traslado) {
		this.traslado = traslado;
	}

	public Producto getProducto() {
		return producto;
	}

	public void setProducto(Producto producto) {
		this.producto = producto;
	}

	public UsuarioDTO getUsuarioconfirma() {
		return usuarioconfirma;
	}

	public void setUsuarioconfirma(UsuarioDTO usuarioconfirma) {
		this.usuarioconfirma = usuarioconfirma;
	}

	public UsuarioDTO getUsuarioRecibe() {
		return usuarioRecibe;
	}

	public void setUsuarioRecibe(UsuarioDTO usuarioRecibe) {
		this.usuarioRecibe = usuarioRecibe;
	}

	public Date getFechaConfirma() {
		return fechaConfirma;
	}

	public void setFechaConfirma(Date fechaConfirma) {
		this.fechaConfirma = fechaConfirma;
	}

	public Date getFechaRecibe() {
		return fechaRecibe;
	}

	public void setFechaRecibe(Date fechaRecibe) {
		this.fechaRecibe = fechaRecibe;
	}
}
