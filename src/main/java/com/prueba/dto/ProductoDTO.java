package com.prueba.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.prueba.entity.Empresa;

@JsonIdentityInfo(
		  generator = ObjectIdGenerators.PropertyGenerator.class, 
		  property = "codigoPieza")
public class ProductoDTO {
	 
	@NotNull(message = "Este campo no puede ser nulo")
	private Long codigoPieza;
	
	@NotBlank(message = "Este campo no puede ser nulo")
	private String descripcion;
	
	@NotBlank(message = "Este campo no puede ser nulo")
	private String area;
	
	@NotBlank(message = "Este campo no puede ser nulo")
	private String orden;
	
	@NotNull(message = "Este campo no puede ser nulo")
	private FabricanteDTO fabricante;
	
	@NotNull(message = "Este campo no puede ser nulo")
	private FamiliaDTO familia;
	
	@NotNull(message = "Este campo no puede ser nulo")
	private EstadoDTO estado;
	
	private Empresa empresa;
	
	private Boolean verificado = false;
	
	private UbicacionDTO ubicacion;

	public UbicacionDTO getUbicacion() {
		return ubicacion;
	}

	public void setUbicacion(UbicacionDTO ubicacion) {
		this.ubicacion = ubicacion;
	}

	public Boolean getVerificado() {
		return verificado;
	}

	public void setVerificado(Boolean verificado) {
		this.verificado = verificado;
	}

	public ProductoDTO() {
		super();
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
	
	

	public FamiliaDTO getFamilia() {
		return familia;
	}

	public void setFamilia(FamiliaDTO familia) {
		this.familia = familia;
	}

	public EstadoDTO getEstado() {
		return estado;
	}

	public void setEstado(EstadoDTO estado) {
		this.estado = estado;
	}

	public FabricanteDTO getFabricante() {
		return fabricante;
	}

	public void setFabricante(FabricanteDTO fabricante) {
		this.fabricante = fabricante;
	}

	public Empresa getEmpresa() {
		return empresa;
	}

	public void setEmpresa(Empresa empresa) {
		this.empresa = empresa;
	}

}
