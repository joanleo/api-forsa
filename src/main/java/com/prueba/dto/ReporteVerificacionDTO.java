package com.prueba.dto;

import java.util.List;

import com.prueba.entity.Producto;

public class ReporteVerificacionDTO {
	
	//Usuario realizo;
	
	String orden;
	
	String filtro;
	
	List<Producto> activos;

	/*public Usuario getRealizo() {
		return realizo;
	}

	public void setRealizo(Usuario realizo) {
		this.realizo = realizo;
	}*/

	public String getOrden() {
		return orden;
	}

	public void setOrden(String orden) {
		this.orden = orden;
	}

	public String getFiltro() {
		return filtro;
	}

	public void setFiltro(String filtro) {
		this.filtro = filtro;
	}

	public List<Producto> getActivos() {
		return activos;
	}

	public void setActivos(List<Producto> activos) {
		this.activos = activos;
	}


}
