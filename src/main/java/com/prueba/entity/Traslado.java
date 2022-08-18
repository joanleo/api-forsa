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
import javax.persistence.PrePersist;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.prueba.security.entity.Usuario;

@Entity
@Table(name = "mov_traslados")
@JsonAutoDetect(fieldVisibility = Visibility.ANY)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "idTraslado")
public class Traslado implements Serializable {

	private static final long serialVersionUID = 1L;
	
	//private static int countBase = 0;

	@Id
	@Column(name = "nidtraslado")
	@GeneratedValue(strategy = GenerationType.SEQUENCE,	generator = "seq_traslado")
	@SequenceGenerator(name = "seq_traslado", allocationSize = 10)
	public Long idTraslado;
	
	@Column(name = "vcnumdocumento")
	public String numDocumento;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "origen", referencedColumnName = "nidubicacion")
	private Ubicacion origen;

	@ManyToOne(fetch = FetchType.LAZY)
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
	@JsonManagedReference
	public List<DetalleTrasl> detalles = new ArrayList<DetalleTrasl>();

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "vcnitempresa", referencedColumnName = "vcnitempresa")
	private Empresa empresa;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "usuarioenvio", referencedColumnName = "nidusuario")
	private Usuario usuarioEnvio;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "usuariorecibe", referencedColumnName = "nidusuario")
	private Usuario usuarioRecibe;

	public Traslado() {
		super();
		/*Traslado.countBase += 1;
		this.idTraslado = Long.valueOf(countBase);*/
		//this.numDocumento = "TR-" + String.valueOf(this.idTraslado);
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

	public String getNumDocumento() {
		return numDocumento;
	}

	public void setNumDocumento(String numDocumento) {
		this.numDocumento = numDocumento;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public void addActivo(Producto producto) {
		DetalleTrasl detalle = new DetalleTrasl(this, producto);
		detalles.add(detalle);
	}

	public void removeActivo(Producto producto) {
		

		/*DetalleTrasl toRemove = null;

		for (int i = 0; i < detalles.size(); i++ ) {
			System.out.println(detalles.get(i).getProducto().getCodigoPieza());
		    if(detalles.get(i).getProducto().equals(producto)) {
		    	System.out.println("item a remover "+producto.getCodigoPieza());
		        toRemove = detalles.get(i);
		    }
		}
		System.out.println("añadisos "+toRemove.getProducto().getCodigoPieza());
		System.out.println(detalles.size());
		detalles.remove(toRemove);
		System.out.println(detalles.size());*/
		Iterator<DetalleTrasl> iterator = detalles.iterator();
		while (iterator.hasNext()) {
			DetalleTrasl detalle = iterator.next();
			if (detalle.getTraslado().equals(this) && detalle.getProducto().equals(producto)) {
				iterator.remove();
				detalle.setTraslado(null);
				detalle.setProducto(null);
				detalle.setEmpresa(null);
			}
		}
	
		/*for( int i = 0; i < detalles.size(); i++ )
		{
		    if(detalles.get(i).getProducto().equals(producto))
		    {
		    	System.out.println("Removiendo en la clase "+producto.getCodigoPieza());
		    	//detalles.remove(producto);
		    	detalles.get(i).setTraslado(null);
		    	detalles.get(i).setProducto(null);
		    }  
		}*/
	}

	/**
	 * @param producto 
	 * @param empresa 
	 * @param usuario
	 */
	public void addActivo(Producto producto, Empresa empresa, Usuario usuario) {
		DetalleTrasl detalle = new DetalleTrasl(this, producto, empresa, usuario);
		detalles.add(detalle);
		
	}
	public void addActivo(Producto producto, Empresa empresa) {
		System.out.println("Funcion de traslado añadiendo producto "+producto.getCodigoPieza()+" al traslado "+this.getIdTraslado());
		DetalleTrasl detalle = new DetalleTrasl(this, producto, empresa);
		//System.out.println(detalle.getTraslado().getIdTraslado());
		detalles.add(detalle);
		
	}

}
