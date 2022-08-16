package com.prueba.entity;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class DetalleTrasl_id implements Serializable{


	private static final long serialVersionUID = 1L;

	@Column(name = "nidtraslado")
	private Long idTraslado;
	
	@Column(name = "nidpieza")
	private Integer idPieza;

	public DetalleTrasl_id() {
		super();
	}

	public DetalleTrasl_id(Long idTraslados, Integer idPieza) {
		super();
		this.idTraslado = idTraslados;
		this.idPieza = idPieza;
	}
	
	 public Long getIdTraslado() {
		return idTraslado;
	}

	public void setIdTraslado(Long idTraslado) {
		this.idTraslado = idTraslado;
	}

	public Integer getIdPieza() {
		return idPieza;
	}

	public void setIdPieza(Integer idPieza) {
		this.idPieza = idPieza;
	}

	@Override
    public boolean equals(Object o) {
        if (this == o) return true;
 
        if (o == null || getClass() != o.getClass())
            return false;
 
        DetalleTrasl_id that = (DetalleTrasl_id) o;
        return Objects.equals(idTraslado, that.idTraslado) &&
               Objects.equals(idPieza, that.idPieza);
    }
 
    @Override
    public int hashCode() {
        return Objects.hash(idTraslado, idPieza);
    }
	
}
