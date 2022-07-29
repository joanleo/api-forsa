package com.prueba.dto;

import java.util.Date;
import java.util.List;

import com.prueba.entity.Empresa;
import com.prueba.entity.Producto;
import com.prueba.entity.Ubicacion;
import com.prueba.security.entity.Usuario;

public class TrasladoDTO {
	
	private List<Producto> detalles;
	
	private int cantActivos;
	
	private Ubicacion origen;
	
	private Ubicacion destino;
	
	private Usuario usuarioEnvio;
	
	private Usuario usuarioRecibe;
	
	public Date fechaSalida;
	
	private Date fechaIngreso;
	
	private String estadoTraslado;
	
	private Empresa empresa;

	public List<Producto> getDetalles() {
		return detalles;
	}

	public void setDetalles(List<Producto> detalles) {
		this.detalles = detalles;
	}

	public int getCantActivos() {
		return cantActivos;
	}

	public void setCantActivos(int cantActivos) {
		this.cantActivos = cantActivos;
	}

	public Ubicacion getOrigen() {
		return origen;
	}

	public void setOrigen(Ubicacion origen) {
		this.origen = origen;
	}

	public Ubicacion getDestino() {
		return destino;
	}

	public void setDestino(Ubicacion destino) {
		this.destino = destino;
	}

	public Usuario getUsuarioEnvio() {
		return usuarioEnvio;
	}

	public void setUsuarioEnvio(Usuario usuarioEnvio) {
		this.usuarioEnvio = usuarioEnvio;
	}

	public Usuario getUsuarioRecibe() {
		return usuarioRecibe;
	}

	public void setUsuarioRecibe(Usuario usuarioRecibe) {
		this.usuarioRecibe = usuarioRecibe;
	}

	public Date getFechaSalida() {
		return fechaSalida;
	}

	public void setFechaSalida(Date fechaSalida) {
		this.fechaSalida = fechaSalida;
	}

	public Date getFechaIngreso() {
		return fechaIngreso;
	}

	public void setFechaIngreso(Date fechaIngreso) {
		this.fechaIngreso = fechaIngreso;
	}

	public String getEstadoTraslado() {
		return estadoTraslado;
	}

	public void setEstadoTraslado(String estadoTraslado) {
		this.estadoTraslado = estadoTraslado;
	}

	public Empresa getEmpresa() {
		return empresa;
	}

	public void setEmpresa(Empresa empresa) {
		this.empresa = empresa;
	}

	

}
