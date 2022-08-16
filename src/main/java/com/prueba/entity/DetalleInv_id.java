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
	private Integer idMov;
	
	@Column(name = "nid_pieza")
	private Integer idPieza;

	public DetalleInv_id() {
		super();
	}

	public DetalleInv_id(Integer idMov, Integer idPieza) {
		super();
		this.idMov = idMov;
		this.idPieza = idPieza;
	}

	public Integer getIdMov() {
		return idMov;
	}

	public void setIdMov(Integer idMov) {
		this.idMov = idMov;
	}

	public Integer getIdPieza() {
		return idPieza;
	}

	public void setIdPieza(Integer idPieza) {
		this.idPieza = idPieza;
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
               Objects.equals(idPieza, that.idPieza);
    }
 
    @Override
    public int hashCode() {
        return Objects.hash(idMov, idPieza);
    }

}
