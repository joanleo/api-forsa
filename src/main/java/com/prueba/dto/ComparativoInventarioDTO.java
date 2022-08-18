package com.prueba.dto;

import java.util.Objects;

import com.prueba.security.entity.Usuario;

public class ComparativoInventarioDTO{
	
	private Usuario usuarioRealizo;
	
	private String codigo;
	
	private String familia;
	
	private String tipo;
	
	private String medidas;
	
	private String estado;
	
	private Boolean inv1=false;
	
	private Boolean inv2=false;
	
	private Integer numInv1;
	
	private Integer numInv2;

	public ComparativoInventarioDTO() {
		super();
	}

	public ComparativoInventarioDTO(Usuario usuario, String codigo, String familia, String tipo,
			String medidas, String estado, Integer numInv1, Integer numInv2) {
		super();
		this.usuarioRealizo = usuario;
		this.codigo = codigo;
		this.familia = familia;
		this.tipo = tipo;
		this.medidas = medidas;
		this.estado = estado;
		this.numInv1 = numInv1;
		this.numInv2 = numInv2;
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

	public Integer getNumInv1() {
		return numInv1;
	}

	public void setNumInv1(Integer numInv1) {
		this.numInv1 = numInv1;
	}

	public Integer getNumInv2() {
		return numInv2;
	}

	public void setNumInv2(Integer numInv2) {
		this.numInv2 = numInv2;
	}

	@Override
	public int hashCode() {
		return Objects.hash(codigo, estado, familia, inv1, inv2, medidas, numInv1, numInv2, tipo);
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
		return Objects.equals(codigo, other.codigo) && Objects.equals(estado, other.estado)
				&& Objects.equals(familia, other.familia) && Objects.equals(inv1, other.inv1)
				&& Objects.equals(inv2, other.inv2) && Objects.equals(medidas, other.medidas)
				&& Objects.equals(numInv1, other.numInv1) && Objects.equals(numInv2, other.numInv2)
				&& Objects.equals(tipo, other.tipo);
	}


	
}
