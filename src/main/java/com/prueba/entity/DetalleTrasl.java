package com.prueba.entity;

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

import com.prueba.security.entity.Usuario;

@Entity
@Table(name = "detalle_traslado")
public class DetalleTrasl {

	@EmbeddedId
	private DetalleTrasl_id id;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@MapsId("idTraslados")
	private Traslado traslado;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@MapsId("codigoPieza")
	private Producto producto;
	
	
	@ManyToOne(targetEntity = Usuario.class, cascade = CascadeType.ALL)
	private Usuario usuarioEnvio;
	
	@ManyToOne(targetEntity = Usuario.class, cascade = CascadeType.ALL)
	private Usuario usuarioRecibe;
	
	@Column(name = "dfecha_envio")
	public Date fechaEnvio;
	
	@Column(name = "dfecha_recibe")
	public Date fechaRecibe;
	
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vcnitempresa")
    private Empresa empresa;
	
	public DetalleTrasl() {
		super();
	}

	public DetalleTrasl(Traslado traslado, Producto producto) {
		super();
		this.traslado = traslado;
		this.producto = producto;
		this.id = new DetalleTrasl_id(traslado.getIdTraslado(), producto.getCodigoPieza());

	}

	@Override
	public int hashCode() {
		return Objects.hash(empresa, fechaEnvio, fechaRecibe, id, producto, traslado, usuarioEnvio, usuarioRecibe);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DetalleTrasl other = (DetalleTrasl) obj;
		return Objects.equals(empresa, other.empresa) && Objects.equals(fechaEnvio, other.fechaEnvio)
				&& Objects.equals(fechaRecibe, other.fechaRecibe) && Objects.equals(id, other.id)
				&& Objects.equals(producto, other.producto) && Objects.equals(traslado, other.traslado)
				&& Objects.equals(usuarioEnvio, other.usuarioEnvio)
				&& Objects.equals(usuarioRecibe, other.usuarioRecibe);
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

	public Empresa getEmpresa() {
		return empresa;
	}

	public void setEmpresa(Empresa empresa) {
		this.empresa = empresa;
	}

	
	
}
