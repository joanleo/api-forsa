package com.prueba.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.prueba.security.entity.Usuario;

@Entity
@Table(name = "mov_trazas")
public class Trazabilidad {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE,
    		generator = "seq_traza")
	@SequenceGenerator(name = "seq_traza", allocationSize = 10)
	@Column(name = "vcid_traza", length = 6)
	private Long id;
	
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "dfecha")
	private Date fecha;
	
	@PrePersist
	private void onCreate() {
		fecha = new Date();
	}
	
	@Column(name = "vcoperacion")
	private String operacion;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "nidusuario", referencedColumnName = "nidusuario")
	private Usuario usuario;

	@Column(name = "vcruta")
	private String ruta;
	
	/**
	 * 
	 */
	public Trazabilidad() {
		super();
	}

	/**
	 * @param operacion
	 * @param usuario
	 * @param ruta
	 */
	public Trazabilidad(String operacion, Usuario usuario, String ruta) {
		super();
		this.operacion = operacion;
		this.usuario = usuario;
		this.ruta = ruta;
	}

	/**
	 * @return
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @param id
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return
	 */
	public Date getFecha() {
		return fecha;
	}

	/**
	 * @param fecha
	 */
	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	/**
	 * @return
	 */
	public String getOperacion() {
		return operacion;
	}

	/**
	 * @param operacion
	 */
	public void setOperacion(String operacion) {
		this.operacion = operacion;
	}

	/**
	 * @return
	 */
	public Usuario getUsuario() {
		return usuario;
	}

	/**
	 * @param usuario
	 */
	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	/**
	 * @return
	 */
	public String getRuta() {
		return ruta;
	}

	/**
	 * @param ruta
	 */
	public void setRuta(String ruta) {
		this.ruta = ruta;
	}

	/**
	 *
	 */
	@Override
	public String toString() {
		return "Trazabilidad [id=" + id + ", fecha=" + fecha + ", operacion=" + operacion + ", usuario=" + usuario
				+ ", ruta=" + ruta + "]";
	}
}
