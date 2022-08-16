/**
 * 
 */
package com.prueba.entity;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * @author Joan Leon
 *
 */
@Embeddable
public class DetalleSalida_id implements Serializable{

	private static final long serialVersionUID = 1L;
	
	@Column(name = "nidsalida")
	private Integer idSalida;
	
	@Column(name = "nid_pieza")
	private Integer idPieza;

	public DetalleSalida_id(Integer idSalida, Integer idPieza) {
		super();
		this.idSalida = idSalida;
		this.idPieza = idPieza;
	}

	public DetalleSalida_id() {
		super();
	}

	public Integer getIdSalida() {
		return idSalida;
	}

	public void setIdSalida(Integer idSalida) {
		this.idSalida = idSalida;
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
	public int hashCode() {
		return Objects.hash(idPieza, idSalida);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DetalleSalida_id other = (DetalleSalida_id) obj;
		return Objects.equals(idPieza, other.idPieza) && Objects.equals(idSalida, other.idSalida);
	}

}
