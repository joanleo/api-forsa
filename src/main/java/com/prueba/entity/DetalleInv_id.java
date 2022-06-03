package com.prueba.entity;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class DetalleInv_id implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Column(name = "nidmov")
	private Long idMov;
	
	@Column(name = "vccodigopieza")
	private String codigoPieza;

	public DetalleInv_id() {
		super();
	}

	public DetalleInv_id(Long idMov, String codigoPieza) {
		super();
		this.idMov = idMov;
		this.codigoPieza = codigoPieza;
	}

	public Long getIdMov() {
		return idMov;
	}

	public void setIdMov(Long idMov) {
		this.idMov = idMov;
	}

	public String getCodigoPieza() {
		return codigoPieza;
	}

	public void setCodigoPieza(String codigoPieza) {
		this.codigoPieza = codigoPieza;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	@Override
    public boolean equals(Object o) {
        if (this == o) return true;
 
        if (o == null || getClass() != o.getClass())
            return false;
 
        DetalleInv_id that = (DetalleInv_id) o;
        return Objects.equals(idMov, that.idMov) &&
               Objects.equals(codigoPieza, that.codigoPieza);
    }
 
    @Override
    public int hashCode() {
        return Objects.hash(idMov, codigoPieza);
    }

}
