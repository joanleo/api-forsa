package com.prueba.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.UpdateTimestamp;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

@JsonIdentityInfo(
		  generator = ObjectIdGenerators.PropertyGenerator.class, 
		  property = "codigoPieza")
@Entity
@Table(name = "mov_activos")
public class Producto {
	
	@Id
    @Column(name = "vccodigopieza", length = 20)
    private String codigoPieza;

	@Column(name = "vcnombre", length = 32)
    private String descripcion;
	
    @Column(name = "narea", nullable = false)
    private Float area;
    
    @Column(name = "vcorden", length = 7, nullable = false)
    private String orden;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "nidfamilia", nullable = false)
    private Familia familia;

    @Column(name = "vcnconfirmacion", length = 8)
    private String nconfirmacion;

    @Column(name = "bverificado", columnDefinition="BOOLEAN NOT NULL DEFAULT 0")
    private Boolean verificado = false;
    
    @Column(name = "bestaActivo", columnDefinition="BOOLEAN NOT NULL DEFAULT 0")
    private Boolean estaActivo = true;
    
    
    @Column(name = "vcmotivoIngreso")
    private String motivoIngreso = "Compra";
    
    @UpdateTimestamp
	@Column(name = "dupdatefecha")
	private Date fechaActualizacion;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "vcnitfabricante")
    private Fabricante fabricante;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "vcnitempresa")
    private Empresa empresa;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "nidestado")
    private Estado estado;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @Cascade(CascadeType.SAVE_UPDATE)
    @JoinColumn(name = "nidubicacion")
    private Ubicacion ubicacion;
    
    @Column(name = "bimportado", columnDefinition="BOOLEAN NOT NULL DEFAULT 0")
    private Boolean importado = true;
    
	public Boolean getImportado() {
		return importado;
	}

	public void setImportado(Boolean importado) {
		this.importado = importado;
	}

	public String getCodigoPieza() {
		return codigoPieza;
	}

	public void setCodigoPieza(String codigoPieza) {
		this.codigoPieza = codigoPieza;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public Float getArea() {
		return area;
	}

	public void setArea(Float area) {
		this.area = area;
	}

	public String getOrden() {
		return orden;
	}

	public void setOrden(String orden) {
		this.orden = orden;
	}

	public Familia getFamilia() {
		return familia;
	}

	public void setFamilia(Familia familia) {
		this.familia = familia;
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

	public Boolean getVerificado() {
		return verificado;
	}

	public void setVerificado(Boolean verificado) {
		this.verificado = verificado;
	}

	public Boolean getEstaActivo() {
		return estaActivo;
	}

	public void setEstaActivo(Boolean estaActivo) {
		this.estaActivo = estaActivo;
	}

	public String getNconfirmacion() {
		return nconfirmacion;
	}

	public void setNconfirmacion(String nconfirmacion) {
		this.nconfirmacion = nconfirmacion;
	}

	public String getMotivoIngreso() {
		return motivoIngreso;
	}

	public void setMotivoIngreso(String motivoIngreso) {
		this.motivoIngreso = motivoIngreso;
	}

	public Date getFechaActualizacion() {
		return fechaActualizacion;
	}

	public void setFechaActualizacion(Date fechaActualizacion) {
		this.fechaActualizacion = fechaActualizacion;
	}

	public Producto() {
		super();
	}


	public Producto(String codigoPieza, String descripcion, Float area, String orden, Familia familia,
			Fabricante fabricante, Empresa empresa, Estado estado, Ubicacion ubicacion, String nconfirmacion,
			String motivoIngreso) {
		super();
		this.codigoPieza = codigoPieza;
		this.descripcion = descripcion;
		this.area = area;
		this.orden = orden;
		this.familia = familia;
		this.fabricante = fabricante;
		this.empresa = empresa;
		this.estado = estado;
		this.ubicacion = ubicacion;
		this.nconfirmacion = nconfirmacion;
		this.motivoIngreso = motivoIngreso;
	}

	public Producto(String codigoPieza, String descripcion, Float area, String orden, Familia familia,
			Fabricante fabricante, Empresa empresa) {
		super();
		this.codigoPieza = codigoPieza;
		this.descripcion = descripcion;
		this.area = area;
		this.orden = orden;
		this.familia = familia;
		this.fabricante = fabricante;
		this.empresa = empresa;
	}

	public Producto(String codigoPieza) {
		super();
		this.codigoPieza = codigoPieza;
	}

	@Override
	public String toString() {
		return codigoPieza + "," + descripcion + "," + area + ","
				+ orden + "," + familia.getId() + "," + fabricante.getNit() + "," + empresa.getNit();
	}
    
    
}
