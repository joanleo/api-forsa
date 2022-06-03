package com.prueba.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "mov_traslados")
public class Traslado {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "nidMov_traslado")
	public Long id;
	
	@Column(name = "vcubicacionOrigen")
	public String origen;
	
	@Column(name = "vcubicacionDestino")
	public String destino;
	
	@Column(name = "dfecha_Salida")
	public Date fechaSalida;
	
	@PrePersist
	private void onCreate() {
		fechaSalida = new Date();
	}
	
	@Column(name = "dfecha_Ingreso")
	public Date fechaIngreso;
	
	@Column(name = "bestaRecibido", columnDefinition="BOOLEAN NOT NULL DEFAULT 0")
	public Boolean estRecibido = false;

	@Column(name = "ncantActivos")
	public int cantActivos;
	
	@JsonIgnore
	@OneToMany(mappedBy = "traslado", cascade = CascadeType.ALL, orphanRemoval = true)
	public List<DetalleTrasl> detalles = new ArrayList<DetalleTrasl>();

	public Traslado() {
		super();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getOrigen() {
		return origen;
	}

	public void setOrigen(String origen) {
		this.origen = origen;
	}

	public String getDestino() {
		return destino;
	}

	public void setDestino(String destino) {
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

	public Boolean getEstRecibido() {
		return estRecibido;
	}

	public void setEstRecibido(Boolean estRecibido) {
		this.estRecibido = estRecibido;
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
}
