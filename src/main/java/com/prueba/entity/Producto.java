package com.prueba.entity;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.UpdateTimestamp;

import com.prueba.security.entity.Usuario;

/*@JsonIdentityInfo(
		  generator = ObjectIdGenerators.PropertyGenerator.class, 
		  property = "codigoPieza")*/
@Entity
@Table(name = "mov_activos")
public class Producto{
	
	/*@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;*/
	
	private static final long serialVersionUID = 1L;
	
	@EmbeddedId
	private Producto_id idProducto;
	
	/*
    @Column(name = "vccodigopieza", length = 20)
    private String codigoPieza;*/

	@Column(name = "vcnombre", length = 60)
    private String descripcion;
	
    @Column(name = "narea", precision = 10, scale = 2, nullable = false)
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
    @JoinColumn(name = "vcnitempresa", insertable=false, updatable=false)
    private Empresa empresa;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "nidestado")
    private Estado estado;
    
    @ManyToOne(fetch = FetchType.EAGER, cascade= CascadeType.ALL)
    @JoinColumn(name = "nidubicacion")
    private Ubicacion ubicacion;
    
    @Column(name = "bimportado", columnDefinition="BOOLEAN NOT NULL DEFAULT 0")
    private Boolean importado = true;
    
    @ManyToOne(fetch = FetchType.EAGER, cascade= CascadeType.ALL)
    @JoinColumn(name = "nidusuario")
    private Usuario reviso;
    
    @Column(name = "vcmedidas")
    private String medidas;
    
	/*public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	public String getCodigoPieza() {
		return codigoPieza;
	}

	public void setCodigoPieza(String codigoPieza) {
		this.codigoPieza = codigoPieza;
	}*/

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

	public Producto_id getIdProducto() {
		return idProducto;
	}

	public void setIdProducto(Producto_id idProducto) {
		this.idProducto = idProducto;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	
	

	public Producto(Long nitEmpresa, String codigoPieza, String descripcion, Float area, String orden, Familia familia,
			String nconfirmacion, Boolean verificado, Boolean estaActivo, String motivoIngreso, Date fechaActualizacion,
			Fabricante fabricante, Empresa empresa, Estado estado, Ubicacion ubicacion, Boolean importado,
			Usuario reviso, String medidas) {
		super();
		this.idProducto = new Producto_id(nitEmpresa, codigoPieza);
		this.descripcion = descripcion;
		this.area = area;
		this.orden = orden;
		this.familia = familia;
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
	}

	public Producto() {
		super();
	}

	public Producto(Producto_id producto_id, String descripcion, Float area, String orden, Familia familia,
			Fabricante fabricante, Empresa empresa) {
		super();
		this.idProducto = producto_id;
		this.descripcion = descripcion;
		this.area = area;
		this.orden = orden;
		this.familia = familia;
		this.fabricante = fabricante;
		this.empresa = empresa;
	}






    
    
}
