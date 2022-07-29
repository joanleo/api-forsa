/**
 * 
 */
package com.prueba.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.prueba.entity.DetalleSalida;
import com.prueba.entity.Empresa;
import com.prueba.entity.Producto;
import com.prueba.entity.Salida;
import com.prueba.exception.ResourceNotFoundException;
import com.prueba.repository.ProductoRepository;
import com.prueba.repository.SalidaRepository;
import com.prueba.security.entity.Usuario;
import com.prueba.security.repository.UsuarioRepository;
import com.prueba.specifications.SalidaSpecifications;

/**
 * @author Joan Leon
 *
 */
@Component
public class SalidaServiceImp implements SalidaService {
	
	@Autowired
	private SalidaRepository salidaRepo;

	@Autowired
	private UsuarioRepository usuarioRepo;
	
	@Autowired
	private ProductoRepository productoRepo;
	
	@Autowired
	private SalidaSpecifications salidaSpec;
	
	@Override
	public Salida crearSalida(Salida salida) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		Usuario usuario = usuarioRepo.findByNombreUsuarioOrEmail(authentication.getName(), authentication.getName()).get();
		
		Salida nuevaSalida = new Salida();
		
		if(salida.getEmpresa().getNit() != null) {
			nuevaSalida.setEmpresa(salida.getEmpresa());
		}else {
			nuevaSalida.setEmpresa(usuario.getEmpresa());
		}
		
		if(salida.getUsuarioCrea() != null) {
			nuevaSalida.setUsuarioCrea(salida.getUsuarioCrea());
		}else {
			nuevaSalida.setUsuarioCrea(usuario);
		}
		
		List<DetalleSalida> detalles = salida.getDetalles();
		for(DetalleSalida detalle: detalles) {
			Producto activo = productoRepo.findByCodigoPieza(detalle.getProducto().getCodigoPieza());
			if(Objects.isNull(activo)) {
				throw new ResourceNotFoundException("activo", "codigo de pieza", detalle.getProducto().getCodigoPieza());
			}
			nuevaSalida.addActivo(activo);
		}
		
		nuevaSalida = salidaRepo.save(nuevaSalida);
		
		return nuevaSalida;
	}

	@Override
	public Page<Salida> buscarSalidas(Empresa empresa, Integer pagina, Integer items) {
		if(items == 0) {
			Page<Salida> salidas = salidaRepo.findByEmpresa(empresa, PageRequest.of(0, 10));
			return salidas;
		}
		Page<Salida> salidas = salidaRepo.findByEmpresa(empresa, PageRequest.of(pagina, items));
		return salidas;
	}

	@Override
	public Page<Salida> buscarSalidas(Salida salida, Empresa empresa, Integer pagina, Integer items) {
		if(items == 0) {
			Page<Salida> salidas = salidaRepo.findAll(salidaSpec.obtenerFabricantes(salida, empresa), PageRequest.of(0, 10));
			return salidas;
		}
		Page<Salida> salidas = salidaRepo.findAll(salidaSpec.obtenerFabricantes(salida, empresa), PageRequest.of(pagina, items));
		return salidas;
	}

	@Override
	public List<Salida> buscarSalidas(String letras, Empresa empresa) {
		List<Salida> salidas = new ArrayList<>();
		if(letras == null) {
			salidas = salidaRepo.findByEmpresa(empresa);
		}
		salidas = salidaRepo.findByNumDocumentoContainsAndEmpresa(letras, empresa);
	
		return salidas;
	}

	@Override
	public Salida confirmarActivoSalida(Integer idsalida, String codigopieza) {
		Salida salida = salidaRepo.findByIdSalida(idsalida);
		if(Objects.isNull(salida)) {
			throw new ResourceNotFoundException("Salida", "id", String.valueOf(idsalida));
		}
		List<DetalleSalida> detalles = salida.getDetalles();
		for(DetalleSalida detalle:detalles) {
			if(detalle.getProducto().getCodigoPieza().equalsIgnoreCase(codigopieza)) {
				Producto activoEliminar = productoRepo.findByCodigoPieza(codigopieza);
				activoEliminar.setEstaActivo(false);
				productoRepo.save(activoEliminar);
			}
		}
		
		return salidaRepo.findByIdSalida(idsalida);
	}

	@Override
	public Salida confirmarSalida(Integer idsalida) {
		Salida salida = salidaRepo.findByIdSalida(idsalida);
		if(Objects.isNull(salida)) {
			throw new ResourceNotFoundException("Salida", "id", String.valueOf(idsalida));
		}
		List<DetalleSalida> detalles = salida.getDetalles();
		for(DetalleSalida detalle:detalles) {
				Producto activoEliminar = productoRepo.findByCodigoPieza(detalle.getProducto().getCodigoPieza());
				activoEliminar.setEstaActivo(false);
				productoRepo.save(activoEliminar);
		}
		
		return salidaRepo.findByIdSalida(idsalida);
	}


}
