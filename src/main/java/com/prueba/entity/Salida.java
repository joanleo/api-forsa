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
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.PostPersist;
import javax.persistence.PrePersist;
import javax.persistence.SequenceGenerator;
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

	//private static int countBase = 0;

	@Id
	@Column(name = "nidsalida")
	@GeneratedValue(strategy = GenerationType.SEQUENCE,	generator = "seq_salida")
	@SequenceGenerator(name = "seq_salida", allocationSize = 10)
	public Long idSalida;
	
	@Column(name = "vcnumdocumento")
	public String numDocumento;
	
	@PostPersist
	public void numDoc() {
		numDocumento = "SIN-" + String.valueOf(this.idSalida);
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "nidtipo_mov", nullable = false)
	public TipoMov tipoMovimiento;
	
	@Column(name = "dfecha_Salida")
	public Date fechaCreacion;

	@PrePersist
	private void onCreate() {
		fechaCreacion = new Date();
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "vcnitempresa", referencedColumnName = "vcnitempresa")
	private Empresa empresa;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "usuarioCrea", referencedColumnName = "nidusuario")
	private Usuario usuarioCrea;
	
	@OneToMany(mappedBy = "salida", cascade = CascadeType.ALL, orphanRemoval = true)
	@JsonManagedReference
	public List<DetalleSalida> detalles = new ArrayList<DetalleSalida>();
	
	@Column(name = "vcestadosalida")
	private String estadoSalida;

	public Salida(Empresa empresa, Usuario usuarioCrea, List<DetalleSalida> detalles) {
		super();
		/*Salida.countBase +=1;
		this.idSalida = countBase;*/
		//this.numDocumento = "SIN-" + String.valueOf(this.idSalida);
		this.empresa = empresa;
		this.usuarioCrea = usuarioCrea;
		this.detalles = detalles;
	}

	public Salida() {
		super();
		/*Salida.countBase +=1;
		this.idSalida = countBase;*/
		this.numDocumento = "SIN-" + String.valueOf(this.idSalida);
	}

	public Long getIdSalida() {
		return idSalida;
	}

	public void setIdSalida(Long idSalida) {
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

	public String getEstadoSalida() {
		return estadoSalida;
	}

	public void setEstadoSalida(String estadoSalida) {
		this.estadoSalida = estadoSalida;
	}

	public void addActivo(Producto producto) {
		DetalleSalida detalle = new DetalleSalida(this, producto);
		detalles.add(detalle);
	}
	
	public void updateActivo(Producto producto, Usuario usuario, Date date) {
		/*for (Iterator<DetalleSalida> iterator = detalles.iterator(); iterator.hasNext();) {
			DetalleSalida detalle = iterator.next();
			
			if(detalle.getProducto().equals(producto)) {
				iterator.remove();
				detalle.setUsuarioConfirma(usuario);
				detalle.setFechaConfirma(date);
			}
		}*/
		
		for( int i = 0; i < detalles.size(); i++ )
		{
		    if(detalles.get(i).getProducto().equals(producto))
		    {
		    	//detalles.remove(producto);
		    	detalles.get(i).setUsuarioConfirma(usuario);
				detalles.get(i).setFechaConfirma(date);
		    }  
		}
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
