package com.prueba.dto;

import java.util.List;

import com.prueba.entity.DetalleSalida;
import com.prueba.entity.Empresa;
import com.prueba.entity.Producto;
import com.prueba.entity.TipoMov;
import com.prueba.security.entity.Usuario;

public class SalidaDTO {
	
	private Empresa empresa;
	
	private List<DetalleSalida> detalles;
	
	private TipoMov tipoMovimiento;
	
	private String observacion;

	private Usuario usuarioCrea;

	public Empresa getEmpresa() {
		return empresa;
	}

	public void setEmpresa(Empresa empresa) {
		this.empresa = empresa;
	}

	public List<DetalleSalida> getDetalles() {
		return detalles;
	}

	public void setDetalles(List<DetalleSalida> detalles) {
		this.detalles = detalles;
	}

	public TipoMov getTipoMovimiento() {
		return tipoMovimiento;
	}

	public void setTipoMovimiento(TipoMov tipoMovimiento) {
		this.tipoMovimiento = tipoMovimiento;
	}

	public String getObservacion() {
		return observacion;
	}

	public void setObservacion(String observacion) {
		this.observacion = observacion;
	}

	public Usuario getUsuarioCrea() {
		return usuarioCrea;
	}

	public void setUsuarioCrea(Usuario usuarioCrea) {
		this.usuarioCrea = usuarioCrea;
	}
	
	
	
}
