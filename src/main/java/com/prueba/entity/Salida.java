/**
 * 
 */
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
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.prueba.security.entity.Usuario;

/**
 * @author Joan Leon
 *
 */
@Entity
@Table(name = "mov_salidas")
@JsonAutoDetect(fieldVisibility = Visibility.ANY)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "idSalida")
public class Salida implements Serializable {
	
	private static final long serialVersionUID = 1L;

	private static int countBase = 0;

	@Id
	@Column(name = "nidsalida")
	public Integer idSalida;
	
	@Column(name = "vcnumdocumento")
	public String numDocumento;
	
	@ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "nidtipo_mov", nullable = false)
	public TipoMov tipoMovimiento;
	
	@Column(name = "dfecha_Salida")
	public Date fechaCreacion;

	@PrePersist
	private void onCreate() {
		fechaCreacion = new Date();
	}
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "vcnitempresa")
	private Empresa empresa;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "usuarioCrea", referencedColumnName = "nidusuario")
	private Usuario usuarioCrea;
	
	@OneToMany(mappedBy = "salida", cascade = CascadeType.ALL, orphanRemoval = true)
	@JsonManagedReference
	public List<DetalleSalida> detalles = new ArrayList<DetalleSalida>();

	public Salida(Empresa empresa, Usuario usuarioCrea, List<DetalleSalida> detalles) {
		super();
		Salida.countBase +=1;
		this.idSalida = countBase;
		this.numDocumento = "SIN-" + String.valueOf(countBase);
		this.empresa = empresa;
		this.usuarioCrea = usuarioCrea;
		this.detalles = detalles;
	}

	public Salida() {
		super();
		Salida.countBase +=1;
		this.idSalida = countBase;
		this.numDocumento = "SIN-" + String.valueOf(countBase);
	}

	public Integer getIdSalida() {
		return idSalida;
	}

	public void setIdSalida(Integer idSalida) {
		this.idSalida = idSalida;
	}

	public String getNumDocumento() {
		return numDocumento;
	}

	public void setNumDocumento(String numDocumento) {
		this.numDocumento = numDocumento;
	}

	public Date getFechaCreacion() {
		return fechaCreacion;
	}

	public void setFechaCreacion(Date fechaCreacion) {
		this.fechaCreacion = fechaCreacion;
	}

	public Empresa getEmpresa() {
		return empresa;
	}

	public void setEmpresa(Empresa empresa) {
		this.empresa = empresa;
	}

	public Usuario getUsuarioCrea() {
		return usuarioCrea;
	}

	public void setUsuarioCrea(Usuario usuarioCrea) {
		this.usuarioCrea = usuarioCrea;
	}
	
	public List<DetalleSalida> getDetalles() {
		return detalles;
	}

	public void setDetalles(List<DetalleSalida> detalles) {
		this.detalles = detalles;
	}

	public TipoMov getTipoMovimiento() {
		return tipoMovimiento;
	}

	public void setTipoMovimiento(TipoMov tipoMovimiento) {
		this.tipoMovimiento = tipoMovimiento;
	}

	public void addActivo(Producto producto) {
		DetalleSalida detalle = new DetalleSalida(this, producto);
		System.out.println(this.getIdSalida());
		detalles.add(detalle);
	}

	public void removeActivo(Producto producto) {
		for (Iterator<DetalleSalida> iterator = detalles.iterator(); iterator.hasNext();) {
			DetalleSalida detalle = iterator.next();

			if (detalle.getSalida().equals(this) && detalle.getProducto().equals(producto)) {
				iterator.remove();
				detalle.setSalida(null);
				detalle.setProducto(null);
			}
		}
	}
	
	
}	