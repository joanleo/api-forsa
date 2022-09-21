/**
 * 
 */
package com.prueba.dto;

import java.util.Objects;

import com.prueba.security.entity.Usuario;

/**
 * @author Joan Leon
 *
 */
public class ComparativoUbicacionDTO {
	
	private Usuario usuarioRealizo;
	
	private String codigo;
	
	private String descripcion;
	
	private String familia;
	
	private String tipo;
	
	private String medidas;
	
	private Float area; 
	
	private String estado;
	
	private Boolean ubicacion=false;
	
	private Boolean inv=false;
	
	private String nomUbicacion;

	private Long numInv;

	public ComparativoUbicacionDTO() {
		super();
	}

	public ComparativoUbicacionDTO(String codigo, String descripcion, String familia,
			String tipo, String medidas, Float area, String estado, String nomUbicacion, Long numInv) {
		super();
		this.codigo = codigo;
		this.descripcion = descripcion;
		this.familia = familia;
		this.tipo = tipo;
		this.medidas = medidas;
		this.area = area;
		this.estado = estado;
		this.nomUbicacion = nomUbicacion;
		this.numInv = numInv;
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

	public void setMedidas(String medidas) {
		this.medidas = medidas;
	}

	public Float getArea() {
		return area;
	}

	public void setArea(Float area) {
		this.area = area;
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public Boolean getUbicacion() {
		return ubicacion;
	}

	public void setUbicacion(Boolean ubicacion) {
		this.ubicacion = ubicacion;
	}

	public Boolean getInv() {
		return inv;
	}

	public void setInv(Boolean inv) {
		this.inv = inv;
	}

	public String getNomUbicacion() {
		return nomUbicacion;
	}

	public void setNomUbicacion(String nomUbicacion) {
		this.nomUbicacion = nomUbicacion;
	}

	public Long getNumInv() {
		return numInv;
	}

	public void setNumInv(Long numInv) {
		this.numInv = numInv;
	}

	@Override
	public int hashCode() {
		return Objects.hash(area, codigo, descripcion, estado, familia, inv, medidas, nomUbicacion, numInv, tipo,
				ubicacion, usuarioRealizo);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ComparativoUbicacionDTO other = (ComparativoUbicacionDTO) obj;
		return Objects.equals(area, other.area) && Objects.equals(codigo, other.codigo)
				&& Objects.equals(descripcion, other.descripcion) && Objects.equals(estado, other.estado)
				&& Objects.equals(familia, other.familia) && Objects.equals(inv, other.inv)
				&& Objects.equals(medidas, other.medidas) && Objects.equals(nomUbicacion, other.nomUbicacion)
				&& Objects.equals(numInv, other.numInv) && Objects.equals(tipo, other.tipo)
				&& Objects.equals(ubicacion, other.ubicacion) && Objects.equals(usuarioRealizo, other.usuarioRealizo);
	}

	@Override
	public String toString() {
		return "ComparativoUbicacionDTO [usuarioRealizo=" + usuarioRealizo + ", codigo=" + codigo + ", descripcion="
				+ descripcion + ", familia=" + familia + ", tipo=" + tipo + ", medidas=" + medidas + ", area=" + area
				+ ", estado=" + estado + ", ubicacion=" + ubicacion + ", inv=" + inv + ", nomUbicacion=" + nomUbicacion
				+ ", numInv=" + numInv + "]";
	}
		

}
