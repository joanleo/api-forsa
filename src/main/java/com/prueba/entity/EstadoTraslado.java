/**
 * 
 */
package com.prueba.entity;

import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * @author Joan Leon
 *
 */
@Entity
@Table(name = "m_estado_traslado")
public class EstadoTraslado {
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE,
    		generator = "seq_estado_tras")
	@SequenceGenerator(name = "seq_estado_tras", allocationSize = 10)
	@Column(name = "vcid")
	public String id;
	
	@Column(name = "vcdescripcion")
	public String descripcion;
	
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vcnitempresa")
    private Empresa empresa;

	public EstadoTraslado() {
		super();
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public Empresa getEmpresa() {
		return empresa;
	}

	@Override
	public int hashCode() {
		return Objects.hash(descripcion, empresa, id);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		EstadoTraslado other = (EstadoTraslado) obj;
		return Objects.equals(descripcion, other.descripcion) && Objects.equals(empresa, other.empresa)
				&& Objects.equals(id, other.id);
	}
	

}
