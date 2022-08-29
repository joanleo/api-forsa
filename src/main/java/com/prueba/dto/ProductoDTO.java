package com.prueba.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.prueba.entity.Empresa;
import com.prueba.entity.Fabricante;
import com.prueba.entity.Familia;
import com.prueba.entity.TipoActivo;
import com.prueba.entity.Ubicacion;
import com.prueba.security.entity.Usuario;

@JsonIdentityInfo(
		  generator = ObjectIdGenerators.PropertyGenerator.class, 
		  property = "codigoPieza")
public class ProductoDTO {
	 
	//@NotNull(message = "Este campo no puede ser nulo")
	private String codigoPieza;
	
	//@NotBlank(message = "Este campo no puede ser nulo")
	private String descripcion;
	
	//@NotNull(message = "Este campo no puede ser nulo")
	private Float area;
	
	//@NotBlank(message = "Este campo no puede ser nulo")
	private String orden;
	
	//@NotNull(message = "Este campo no puede ser nulo")
	private Fabricante fabricante;
	
	//@NotNull(message = "Este campo no puede ser nulo")
	private Familia familia;
	
	//@NotNull(message = "Este campo no puede ser nulo")
	private EstadoDTO estado;
	
	private Empresa empresa;
	
	private Boolean verificado = true;
	
	@NotNull(message = "Este campo no puede ser nulo")
	private Ubicacion ubicacion;
	
	private Boolean estaActivo = true;
	
	private String nconfirmacion;
	
	private String motivoIngreso ="Compra";
	
	private String medidas;
	
	private Usuario reviso;
	
	private TipoActivo tipo;

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

	public Fabricante getFabricante() {
		return fabricante;
	}

	public void setFabricante(Fabricante fabricante) {
		this.fabricante = fabricante;
	}

	public Familia getFamilia() {
		return familia;
	}

	public void setFamilia(Familia familia) {
		this.familia = familia;
	}

	public EstadoDTO getEstado() {
		return estado;
	}

	public void setEstado(EstadoDTO estado) {
		this.estado = estado;
	}

	public Empresa getEmpresa() {
		return empresa;
	}

	public void setEmpresa(Empresa empresa) {
		this.empresa = empresa;
	}

	public Boolean getVerificado() {
		return verificado;
	}

	public void setVerificado(Boolean verificado) {
		this.verificado = verificado;
	}

	public Ubicacion getUbicacion() {
		return ubicacion;
	}

	public void setUbicacion(Ubicacion ubicacion) {
		this.ubicacion = ubicacion;
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

	public String getMedidas() {
		return medidas;
	}

	public void setMedidas(String medidas) {
		this.medidas = medidas;
	}

	public Usuario getReviso() {
		return reviso;
	}

	public void setReviso(Usuario reviso) {
		this.reviso = reviso;
	}

	public TipoActivo getTipo() {
		return tipo;
	}

	public void setTipo(TipoActivo tipo) {
		this.tipo = tipo;
	}

	public ProductoDTO() {
	}

	
}
