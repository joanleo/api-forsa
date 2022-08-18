/**
 * 
 */
package com.prueba.dto;

import java.util.List;
import java.util.Objects;

/**
 * @author Joan Leon
 *
 */
public class ReconversionDTO {
	
	String codigoPadre;
	
	List<ProductoDTO> piezasHijas;

	public String getCodigoPadre() {
		return codigoPadre;
	}

	public void setCodigoPadre(String codigoPadre) {
		this.codigoPadre = codigoPadre;
	}

	public List<ProductoDTO> getPiezasHijas() {
		return piezasHijas;
	}

	public void setPiezasHijas(List<ProductoDTO> piezasHijas) {
		this.piezasHijas = piezasHijas;
	}

	public ReconversionDTO(String codigoPadre, List<ProductoDTO> piezasHijas) {
		super();
		this.codigoPadre = codigoPadre;
		this.piezasHijas = piezasHijas;
	}

	@Override
	public int hashCode() {
		return Objects.hash(codigoPadre, piezasHijas);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ReconversionDTO other = (ReconversionDTO) obj;
		return Objects.equals(codigoPadre, other.codigoPadre) && Objects.equals(piezasHijas, other.piezasHijas);
	}
}
