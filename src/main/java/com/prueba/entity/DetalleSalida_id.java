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
	
	@Column(name = "nidtraslado")
	private Integer idSalida;
	
	@Column(name = "vccodigopieza")
	private String codigoPieza;

	public DetalleSalida_id(Integer idSalida, String codigoPieza) {
		super();
		this.idSalida = idSalida;
		this.codigoPieza = codigoPieza;
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
	public int hashCode() {
		return Objects.hash(codigoPieza, idSalida);
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
		return Objects.equals(codigoPieza, other.codigoPieza) && Objects.equals(idSalida, other.idSalida);
	}

}
