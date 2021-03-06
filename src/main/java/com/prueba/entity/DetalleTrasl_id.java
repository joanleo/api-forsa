package com.prueba.entity;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class DetalleTrasl_id implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Column(name = "nidMov_traslado")
	private Long idTraslados;
	
	@Column(name = "vccodigopieza")
	private String codigoPieza;

	public DetalleTrasl_id() {
		super();
	}

	public DetalleTrasl_id(Long idTraslados, String codigoPieza) {
		super();
		this.idTraslados = idTraslados;
		this.codigoPieza = codigoPieza;
	}
	
	 public Long getIdTraslados() {
		return idTraslados;
	}

	public void setIdTraslados(Long idTraslados) {
		this.idTraslados = idTraslados;
	}

	public String getCodigoPieza() {
		return codigoPieza;
	}

	public void setCodigoPieza(String codigoPieza) {
		this.codigoPieza = codigoPieza;
	}

	@Override
    public boolean equals(Object o) {
        if (this == o) return true;
 
        if (o == null || getClass() != o.getClass())
            return false;
 
        DetalleTrasl_id that = (DetalleTrasl_id) o;
        return Objects.equals(idTraslados, that.idTraslados) &&
               Objects.equals(codigoPieza, that.codigoPieza);
    }
 
    @Override
    public int hashCode() {
        return Objects.hash(idTraslados, codigoPieza);
    }
	
}
