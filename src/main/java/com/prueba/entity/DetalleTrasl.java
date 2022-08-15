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
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.prueba.security.entity.Usuario;

/**
 * @author Joan Leon
 *
 */
@Entity
@Table(name = "mov_det_traslados")
@JsonAutoDetect(fieldVisibility = Visibility.ANY)
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class DetalleTrasl implements Serializable{

	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private DetalleTrasl_id id;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@MapsId("idTraslado")
	@JsonBackReference
	private Traslado traslado;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@MapsId("codigoPieza")
	private Producto producto;
	
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "usuarioconfirma", referencedColumnName = "nidusuario")
	private Usuario usuarioconfirma;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "usuariorecibe", referencedColumnName = "nidusuario")
	private Usuario usuarioRecibe;
	
	@Column(name = "dfecha_envio")
	public Date fechaConfirma;
	
	@Column(name = "dfecha_recibe")
	public Date fechaRecibe;
	
	@JsonIgnore
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

	public DetalleTrasl(Traslado traslado, Producto producto, Empresa empresa) {
		super();
		this.traslado = traslado;
		this.producto = producto;
		this.empresa = empresa;
		this.id = new DetalleTrasl_id(traslado.getIdTraslado(), producto.getCodigoPieza());
	}

	/**
	 * @param traslado
	 * @param producto
	 * @param empresa
	 * @param usuarioEnvia
	 */
	public DetalleTrasl(Traslado traslado, Producto producto, Empresa empresa, Usuario usuarioEnvia) {
		this.traslado = traslado;
		this.producto = producto;
		this.empresa = empresa;
		this.usuarioconfirma = usuarioEnvia;
		this.id = new DetalleTrasl_id(traslado.getIdTraslado(), producto.getCodigoPieza());
		
	}

	@Override
	public int hashCode() {
		return Objects.hash(empresa, fechaConfirma, fechaRecibe, id, producto, traslado, usuarioRecibe, usuarioconfirma);
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
		return Objects.equals(empresa, other.empresa) && Objects.equals(fechaConfirma, other.fechaConfirma)
				&& Objects.equals(fechaRecibe, other.fechaRecibe) && Objects.equals(id, other.id)
				&& Objects.equals(producto, other.producto) && Objects.equals(traslado, other.traslado)
				&& Objects.equals(usuarioRecibe, other.usuarioRecibe)
				&& Objects.equals(usuarioconfirma, other.usuarioconfirma);
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

	public Usuario getUsuarioconfirma() {
		return usuarioconfirma;
	}

	public void setUsuarioconfirma(Usuario usuarioconfirma) {
		this.usuarioconfirma = usuarioconfirma;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public Usuario getUsuarioRecibe() {
		return usuarioRecibe;
	}

	public void setUsuarioRecibe(Usuario usuarioRecibe) {
		this.usuarioRecibe = usuarioRecibe;
	}

	public Date getFechaEnvio() {
		return fechaConfirma;
	}

	public void setFechaEnvio(Date fechaEnvio) {
		this.fechaConfirma = fechaEnvio;
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

	@Override
	public String toString() {
		return "DetalleTrasl [id=" + id + ", traslado=" + traslado + ", producto=" + producto + ", usuarioconfirma="
				+ usuarioconfirma + ", usuarioRecibe=" + usuarioRecibe + ", fechaConfirma=" + fechaConfirma
				+ ", fechaRecibe=" + fechaRecibe + ", empresa=" + empresa + "]";
	}

	
	
}
