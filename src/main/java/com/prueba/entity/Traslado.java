package com.prueba.entity;

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
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.prueba.security.entity.Usuario;

@Entity
@Table(name = "mov_traslados")
public class Traslado {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "nidMov_traslado")
	public Long id;
	
 	@ManyToOne(targetEntity = Ubicacion.class, cascade = CascadeType.ALL)
	private Ubicacion origen;
	
	@ManyToOne(targetEntity = Ubicacion.class, cascade = CascadeType.ALL)
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
	
	@JsonIgnore
	@OneToMany(mappedBy = "traslado", cascade = CascadeType.ALL, orphanRemoval = true)
	public List<DetalleTrasl> detalles = new ArrayList<DetalleTrasl>();
	
	@ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "vcnitempresa")
    private Empresa empresa;
	
	@ManyToOne(targetEntity = Usuario.class, cascade = CascadeType.ALL)
	private Usuario usuarioEnvio;
	
	@ManyToOne(targetEntity = Usuario.class, cascade = CascadeType.ALL)
	private Usuario usuarioRecibe;

	public Traslado() {
		super();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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
	
	public void addActivo(Producto producto) {
        DetalleTrasl detalle = new DetalleTrasl(this, producto);
        detalles.add(detalle);
    }
	
	public void removeTag(Producto producto) {
        for (Iterator<DetalleTrasl> iterator = detalles.iterator();
             iterator.hasNext(); ) {
            DetalleTrasl detalle = iterator.next();
 
            if (detalle.getTraslado().equals(this) &&
            		detalle.getProducto().equals(producto)) {
                iterator.remove();
                detalle.setTraslado(null);
                detalle.setProducto(null);
            }
        }
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
	
}
