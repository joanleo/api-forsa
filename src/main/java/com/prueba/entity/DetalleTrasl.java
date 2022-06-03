package com.prueba.entity;

import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.Table;

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
	
	@Column(name = "bestaRecibido")
	private Boolean estaRecibido = false;

	public DetalleTrasl() {
		super();
	}

	public DetalleTrasl(Traslado traslado, Producto producto) {
		super();
		this.traslado = traslado;
		this.producto = producto;
		this.id = new DetalleTrasl_id(traslado.getId(), producto.getCodigoPieza());

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

	public Boolean getEstaRecibido() {
		return estaRecibido;
	}

	public void setEstaRecibido(Boolean estaRecibido) {
		this.estaRecibido = estaRecibido;
	}
	@Override
    public boolean equals(Object o) {
        if (this == o) return true;
 
        if (o == null || getClass() != o.getClass())
            return false;
 
        DetalleTrasl that = (DetalleTrasl) o;
        return Objects.equals(traslado, that.traslado) &&
               Objects.equals(producto, that.producto);
    }
 
    @Override
    public int hashCode() {
        return Objects.hash(traslado, producto);
    }
	
}
