package com.prueba.entity;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.UpdateTimestamp;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.prueba.security.entity.Usuario;

@JsonIdentityInfo(
		  generator = ObjectIdGenerators.PropertyGenerator.class, 
		  property = "codigoPieza")
@Entity
@Table(name = "mov_activos")
public class Producto{
	
	@Id
    @Column(name = "vccodigopieza", length = 20)
    private String codigoPieza;

	@Column(name = "vcnombre", length = 60)
    private String descripcion;
	
    @Column(name = "narea", precision = 10, scale = 2, nullable = false)
    private Float area;
    
    @Column(name = "vcorden", length = 7, nullable = false)
    private String orden;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "nidfamilia", nullable = false)
    private Familia familia;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "nidtipo")
    private TipoActivo tipo;

    @Column(name = "vcnconfirmacion", length = 8)
    private String nconfirmacion;

    @Column(name = "bverificado", columnDefinition="BOOLEAN NOT NULL DEFAULT 0")
    private Boolean verificado = false;
    
    @Column(name = "bestaActivo", columnDefinition="BOOLEAN NOT NULL DEFAULT 1")
    private Boolean estaActivo = true;
    
    
    @Column(name = "vcmotivoIngreso")
    private String motivoIngreso = "Compra";
    
    @UpdateTimestamp
	@Column(name = "dupdatefecha")
	private Date fechaActualizacion;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "vcnitfabricante")
    private Fabricante fabricante;
    
    @ManyToOne(fetch = FetchType.EAGER, cascade= CascadeType.ALL)
    @JoinColumn(name = "vcnitempresa")
    private Empresa empresa;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "nidestado")
    private Estado estado;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "nidubicacion")
    private Ubicacion ubicacion;
    
    @Column(name = "bimportado", columnDefinition="BOOLEAN NOT NULL DEFAULT 0")
    private Boolean importado = false;
    
    @ManyToOne(fetch = FetchType.EAGER, cascade= CascadeType.ALL)
    @JoinColumn(name = "nidusuario")
    private Usuario reviso;
    
    @Column(name = "vcmedidas")
    private String medidas;
    
    @Column(name = "benviado", columnDefinition="BOOLEAN NOT NULL DEFAULT 0")
    private Boolean enviado = false;
    
    
	
	public String getCodigoPieza() {
		return codigoPieza;
	}

	public void setCodigoPieza(String codigoPieza) {
		this.codigoPieza = codigoPieza;
	}

	public Usuario getReviso() {
		return reviso;
	}

	public void setReviso(Usuario reviso) {
		this.reviso = reviso;
	}

	public Boolean getImportado() {
		return importado;
	}

	public void setImportado(Boolean importado) {
		this.importado = importado;
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
	
	public String getMedidas() {
		return medidas;
	}

	public void setMedidas(String medidas) {
		this.medidas = medidas;
	}		

	public TipoActivo getTipo() {
		return tipo;
	}

	public void setTipo(TipoActivo tipo) {
		this.tipo = tipo;
	}

	public Boolean getEnviado() {
		return enviado;
	}

	public void setEnviado(Boolean enviado) {
		this.enviado = enviado;
	}


	
	

	public Producto() {
		super();
	}

	public Producto(String codigoPieza, String descripcion, Float area, String orden, Familia familia,
			Fabricante fabricante, Empresa empresa, TipoActivo tipo, String medidas) {
		super();
		this.codigoPieza = codigoPieza;
		this.descripcion = descripcion;
		this.area = area;
		this.orden = orden;
		this.familia = familia;
		this.fabricante = fabricante;
		this.empresa = empresa;
		this.tipo = tipo;
		this.medidas = medidas;
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

	public Producto(String codigoPieza, String descripcion, Float area, String orden, Familia familia,
			String nconfirmacion, Boolean verificado, Boolean estaActivo, String motivoIngreso, Fabricante fabricante,
			Empresa empresa, Estado estado, Ubicacion ubicacion) {
		super();
		this.codigoPieza = codigoPieza;
		this.descripcion = descripcion;
		this.area = area;
		this.orden = orden;
		this.familia = familia;
		this.nconfirmacion = nconfirmacion;
		this.verificado = verificado;
		this.estaActivo = estaActivo;
		this.motivoIngreso = motivoIngreso;
		this.fabricante = fabricante;
		this.empresa = empresa;
		this.estado = estado;
		this.ubicacion = ubicacion;
	}

	public Producto(String codigoPieza, String descripcion, Float area, String orden, Familia familia, TipoActivo tipo,
			String motivoIngreso, Fabricante fabricante, Empresa empresa, Estado estado, Ubicacion ubicacion,
			Usuario reviso, String medidas) {
		super();
		this.codigoPieza = codigoPieza;
		this.descripcion = descripcion;
		this.area = area;
		this.orden = orden;
		this.familia = familia;
		this.tipo = tipo;
		this.motivoIngreso = motivoIngreso;
		this.fabricante = fabricante;
		this.empresa = empresa;
		this.estado = estado;
		this.ubicacion = ubicacion;
		this.reviso = reviso;
		this.medidas = medidas;
	}

	public Producto(String codigoPieza, String descripcion, Float area, String orden, Familia familia, TipoActivo tipo,
			Boolean verificado, Boolean estaActivo, String motivoIngreso, Date fechaActualizacion,
			Fabricante fabricante, Empresa empresa, Estado estado, Ubicacion ubicacion, Boolean importado,
			Usuario reviso, String medidas, Boolean enviado) {
		super();
		this.codigoPieza = codigoPieza;
		this.descripcion = descripcion;
		this.area = area;
		this.orden = orden;
		this.familia = familia;
		this.tipo = tipo;
		this.verificado = verificado;
		this.estaActivo = estaActivo;
		this.motivoIngreso = motivoIngreso;
		this.fechaActualizacion = fechaActualizacion;
		this.fabricante = fabricante;
		this.empresa = empresa;
		this.estado = estado;
		this.ubicacion = ubicacion;
		this.importado = importado;
		this.reviso = reviso;
		this.medidas = medidas;
		this.enviado = enviado;
	}

	public Producto(String codigoPieza, String descripcion, Float area, String orden, Familia familia, TipoActivo tipo,
			String nconfirmacion, Boolean verificado, Boolean estaActivo, String motivoIngreso, Date fechaActualizacion,
			Fabricante fabricante, Empresa empresa, Estado estado, Ubicacion ubicacion, Boolean importado,
			Usuario reviso, String medidas, Boolean enviado) {
		super();
		this.codigoPieza = codigoPieza;
		this.descripcion = descripcion;
		this.area = area;
		this.orden = orden;
		this.familia = familia;
		this.tipo = tipo;
		this.nconfirmacion = nconfirmacion;
		this.verificado = verificado;
		this.estaActivo = estaActivo;
		this.motivoIngreso = motivoIngreso;
		this.fechaActualizacion = fechaActualizacion;
		this.fabricante = fabricante;
		this.empresa = empresa;
		this.estado = estado;
		this.ubicacion = ubicacion;
		this.importado = importado;
		this.reviso = reviso;
		this.medidas = medidas;
		this.enviado = enviado;
	}

	    
}
