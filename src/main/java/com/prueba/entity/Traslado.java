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
import javax.persistence.PrePersist;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.prueba.security.entity.Usuario;

@Entity
@Table(name = "mov_traslados")
@JsonAutoDetect(fieldVisibility = Visibility.ANY)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "idTraslado")
public class Traslado implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private static int countBase = 0;

	@Id
	@Column(name = "idtraslado")
	public Long idTraslado;
	
	@Column(name = "vcnumdocumento")
	public String numDocumento;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "origen", referencedColumnName = "nidubicacion")
	private Ubicacion origen;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "destino", referencedColumnName = "nidubicacion")
	private Ubicacion destino;

	@Column(name = "dfecha_Salida")
	public Date fechaSalida;

	@PrePersist
	private void onCreate() {
		fechaSalida = new Date();
	}

	@Column(name = "dfecha_Ingreso")
	public Date fechaIngreso;

	@Column(name = "vcestadotraslado")
	public String estadoTraslado;

	@Column(name = "ncantActivos")
	public int cantActivos;

	@OneToMany(mappedBy = "traslado", cascade = CascadeType.ALL, orphanRemoval = true)
	public List<DetalleTrasl> detalles = new ArrayList<DetalleTrasl>();

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "vcnitempresa")
	private Empresa empresa;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "usuarioenvio", referencedColumnName = "nidusuario")
	private Usuario usuarioEnvio;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "usuariorecibe", referencedColumnName = "nidusuario")
	private Usuario usuarioRecibe;

	public Traslado() {
		super();
		Traslado.countBase += 1;
		this.idTraslado = Long.valueOf(countBase);
		this.numDocumento = "TR-" + String.valueOf(countBase);
	}

	public Long getIdTraslado() {
		return idTraslado;
	}

	public void setIdTraslado(Long idTraslado) {
		this.idTraslado = idTraslado;
	}

	public Ubicacion getOrigen() {
		return origen;
	}

	public void setOrigen(Ubicacion origen) {
		this.origen = origen;
	}

	public Ubicacion getDestino() {
		return destino;
	}

	public void setDestino(Ubicacion destino) {
		this.destino = destino;
	}

	public Date getFechaSalida() {
		return fechaSalida;
	}

	public void setFechaSalida(Date fechaSalida) {
		this.fechaSalida = fechaSalida;
	}

	public Date getFechaIngreso() {
		return fechaIngreso;
	}

	public void setFechaIngreso(Date fechaIngreso) {
		this.fechaIngreso = fechaIngreso;
	}

	public String getEstadoTraslado() {
		return estadoTraslado;
	}

	public void setEstadoTraslado(String estadoTraslado) {
		this.estadoTraslado = estadoTraslado;
	}

	public int getCantActivos() {
		return cantActivos;
	}

	public void setCantActivos(int cantActivos) {
		this.cantActivos = cantActivos;
	}

	public List<DetalleTrasl> getDetalles() {
		return detalles;
	}

	public void setDetalles(List<DetalleTrasl> detalles) {
		this.detalles = detalles;
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

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public void addActivo(Producto producto) {
		DetalleTrasl detalle = new DetalleTrasl(this, producto);
		System.out.println(this.getIdTraslado());
		detalles.add(detalle);
	}

	public void removeActivo(Producto producto) {
		for (Iterator<DetalleTrasl> iterator = detalles.iterator(); iterator.hasNext();) {
			DetalleTrasl detalle = iterator.next();

			if (detalle.getTraslado().equals(this) && detalle.getProducto().equals(producto)) {
				iterator.remove();
				detalle.setTraslado(null);
				detalle.setProducto(null);
			}
		}
	}

	/**
	 * @param producto 
	 * @param empresa 
	 * @param usuario
	 */
	public void addActivo(Producto producto, Empresa empresa, Usuario usuario) {
		DetalleTrasl detalle = new DetalleTrasl(this, producto, empresa, usuario);
		System.out.println(this.getIdTraslado());
		detalles.add(detalle);
		
	}

}
