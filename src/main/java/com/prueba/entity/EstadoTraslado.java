/**
 * 
 */
package com.prueba.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * @author Joan Leon
 *
 */
@Entity
@Table(name = "m_estado_traslado")
public class EstadoTraslado {
	
	@Id
	@Column(name = "vcid")
	public String id;
	
	@Column(name = "vcdescripcion")
	public String descripcion;
	
	@ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "vcnitempresa")
    private Empresa empresa;
	

}
