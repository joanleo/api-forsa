package com.prueba.dto;

import java.util.Objects;

import com.prueba.security.entity.Usuario;

/**
 * @author Joan Leon
 *
 */
public class ComparativoInventarioDTO{
	
	private Usuario usuarioRealizo;
	
	private String codigo;
	
	private String descripcion;
	
	private String familia;
	
	private String tipo;
	
	private String medidas;
	
	private Float area; 
	
	private String estado;
	
	private Boolean inv1=false;
	
	private Boolean inv2=false;
	
	private Long numInv1;
	
	private Long numInv2;
	
	private String Ubicacion;

	public ComparativoInventarioDTO() {
		super();
	}

	/**
	 * @param codigo
	 * @param descripcion
	 * @param familia
	 * @param tipo
	 * @param medidas
	 * @param area
	 * @param estado
	 * @param numInv1
	 * @param numInv2
	 */
	public ComparativoInventarioDTO(String codigo, String descripcion, String familia, String tipo,
			String medidas, float area, String estado, Long numInv1, Long numInv2) {
		super();
		this.codigo = codigo;
		this.descripcion = descripcion;
		this.familia = familia;
		this.tipo = tipo;
		this.medidas = medidas;
		this.area = area;
		this.estado = estado;
		this.numInv1 = numInv1;
		this.numInv2 = numInv2;
	}

	/**
	 * @param usuario 
	 * @param codigo 
	 * @param descripcion 
	 * @param familia 
	 * @param tipo 
	 * @param medidas 
	 * @param area 
	 * @param estado 
	 * @param numInv1 
	 * @param ubicacion 
	 
	 */
	public ComparativoInventarioDTO(String codigo, String descripcion, String familia, String tipo,
			String medidas, float area, String estado, Long numInv1, String ubicacion) {
		super();
		this.codigo = codigo;
		this.descripcion = descripcion;
		this.familia = familia;
		this.tipo = tipo;
		this.medidas = medidas;
		this.area = area;
		this.estado = estado;
		this.numInv1 = numInv1;
		this.Ubicacion = ubicacion;
	}



	public Usuario getUsuarioRealizo() {
		return usuarioRealizo;
	}

	public void setUsuarioRealizo(Usuario usuarioRealizo) {
		this.usuarioRealizo = usuarioRealizo;
	}

	public String getCodigo() {
		return codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public String getFamilia() {
		return familia;
	}

	public void setFamilia(String familia) {
		this.familia = familia;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public String getMedidas() {
		return medidas;
	}

	public Float getArea() {
		return area;
	}

	public void setArea(Float area) {
		this.area = area;
	}

	public void setMedidas(String medidas) {
		this.medidas = medidas;
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public Boolean getInv1() {
		return inv1;
	}

	public void setInv1(Boolean inv1) {
		this.inv1 = inv1;
	}

	public Boolean getInv2() {
		return inv2;
	}

	public void setInv2(Boolean inv2) {
		this.inv2 = inv2;
	}

	public Long getNumInv1() {
		return numInv1;
	}

	public void setNumInv1(Long numInv1) {
		this.numInv1 = numInv1;
	}

	public Long getNumInv2() {
		return numInv2;
	}

	public void setNumInv2(Long numInv2) {
		this.numInv2 = numInv2;
	}

	public String getUbicacion() {
		return Ubicacion;
	}

	public void setUbicacion(String ubicacion) {
		Ubicacion = ubicacion;
	}

	@Override
	public int hashCode() {
		return Objects.hash(Ubicacion, area, codigo, descripcion, estado, familia, inv1, inv2, medidas, numInv1,
				numInv2, tipo, usuarioRealizo);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ComparativoInventarioDTO other = (ComparativoInventarioDTO) obj;
		return Objects.equals(Ubicacion, other.Ubicacion) && Objects.equals(area, other.area)
				&& Objects.equals(codigo, other.codigo) && Objects.equals(descripcion, other.descripcion)
				&& Objects.equals(estado, other.estado) && Objects.equals(familia, other.familia)
				&& Objects.equals(inv1, other.inv1) && Objects.equals(inv2, other.inv2)
				&& Objects.equals(medidas, other.medidas) && Objects.equals(numInv1, other.numInv1)
				&& Objects.equals(numInv2, other.numInv2) && Objects.equals(tipo, other.tipo)
				;
	}

	@Override
	public String toString() {
		return "ComparativoInventarioDTO [usuarioRealizo=" + usuarioRealizo + ", codigo=" + codigo + ", descripcion="
				+ descripcion + ", familia=" + familia + ", tipo=" + tipo + ", medidas=" + medidas + ", area=" + area
				+ ", estado=" + estado + ", inv1=" + inv1 + ", inv2=" + inv2 + ", numInv1=" + numInv1 + ", numInv2="
				+ numInv2 + ", Ubicacion=" + Ubicacion + "]";
	}
	
}
