package com.prueba.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

@JsonIdentityInfo(
		  generator = ObjectIdGenerators.PropertyGenerator.class, 
		  property = "codigoPieza")
@Entity
@Table(name = "Productos")
public class Producto {
	
	@Id
    @Column(name = "vccodigopieza")
    private Long codigoPieza;

	@Column(name = "vcdescripcion")
    private String descripcion;
	
    @Column(name = "vcarea", nullable = false)
    private String area;
    
    @Column(name = "vcorden", nullable = false)
    private String orden;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "nidfamilia", nullable = false)
    private Familia familia;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vcnitfabricante")
    private Fabricante fabricante;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "vcnitEmpresa")
    private Empresa empresa;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "nidestado")
    private Estado estado;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @Cascade(CascadeType.SAVE_UPDATE)
    @JoinColumn(name = "nidubicacion")
    private Ubicacion ubicacion;

    @Column(name = "bverificado", columnDefinition="BOOLEAN DEFAULT false", nullable = false)
    private Boolean verificado = false;
    
    @Column(name = "bisenable", columnDefinition="BOOLEAN DEFAULT false", nullable = false)
    private Boolean isEnable = false;
    
	public Boolean getIsEnable() {
		return isEnable;
	}

	public void setIsEnable(Boolean isEnable) {
		this.isEnable = isEnable;
	}

	public Familia getFamilia() {
		return familia;
	}

	public void setFamilia(Familia familia) {
		this.familia = familia;
	}

	public Boolean getVerificado() {
		return verificado;
	}

	public void setVerificado(Boolean verificado) {
		this.verificado = verificado;
	}

	public Producto() {
		this.verificado = false;
		
	}

	public Long getCodigoPieza() {
		return codigoPieza;
	}

	public void setCodigoPieza(Long codigoPieza) {
		this.codigoPieza = codigoPieza;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
	}

	public String getOrden() {
		return orden;
	}

	public void setOrden(String orden) {
		this.orden = orden;
	}

	public Fabricante getFabricante() {
		return fabricante;
	}

	public void setFabricante(Fabricante fabricante) {
		this.fabricante = fabricante;
	}

	public Empresa getEmpresa() {
		return empresa;
	}

	public void setEmpresa(Empresa empresa) {
		this.empresa = empresa;
	}

	public Estado getEstado() {
		return estado;
	}

	public void setEstado(Estado estado) {
		this.estado = estado;
	}

	public Ubicacion getUbicacion() {
		return ubicacion;
	}

	public void setUbicacion(Ubicacion ubicacion) {
		this.ubicacion = ubicacion;
	}	
}
