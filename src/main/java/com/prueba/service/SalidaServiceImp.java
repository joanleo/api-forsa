/**
 * 
 */
package com.prueba.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.prueba.entity.DetalleSalida;
import com.prueba.entity.DetalleTrasl;
import com.prueba.entity.Empresa;
import com.prueba.entity.Producto;
import com.prueba.entity.Salida;
import com.prueba.entity.TipoMov;
import com.prueba.exception.ResourceCannotBeAccessException;
import com.prueba.exception.ResourceCannotBeDeleted;
import com.prueba.exception.ResourceNotFoundException;
import com.prueba.repository.DetalleSalidaRepository;
import com.prueba.repository.ProductoRepository;
import com.prueba.repository.SalidaRepository;
import com.prueba.repository.TipoMovRepository;
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
	
	@Autowired
	private TipoMovRepository tipoMovRepo;
	
	@Autowired
	private DetalleSalidaRepository detalleSalidaRepo;
	
	@Override
	public Salida crearSalida(Salida salida){
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
		
		TipoMov tipomov = tipoMovRepo.findById(salida.getTipoMovimiento().getId())
				.orElseThrow(() -> new ResourceNotFoundException("Tipo de Movimiento", "id", salida.getTipoMovimiento().getId()));
		if(tipomov != null) {
			nuevaSalida.setTipoMovimiento(tipomov);
		}
		
		if(salida.getDetalles() != null) {
			System.out.println(salida.getDetalles());
		}
		List<DetalleSalida> detalles = salida.getDetalles();
		for(DetalleSalida detalle: detalles) {
			Producto activo = productoRepo.findByCodigoPieza(detalle.getProducto().getCodigoPieza());
			if(Objects.isNull(activo)) {
				throw new ResourceNotFoundException("activo", "codigo de pieza", detalle.getProducto().getCodigoPieza());
			}
			if(!activo.getEstaActivo()) {
				throw new ResourceCannotBeAccessException("Activo se encuentra inhabilitado");
			}
			activo.setEstadoSalida("P");
			activo = productoRepo.save(activo);
			nuevaSalida.addActivo(activo);
		}
		 
		nuevaSalida.setEstadoSalida("A");
		
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
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		Usuario usuario = usuarioRepo.findByNombreUsuarioOrEmail(authentication.getName(), authentication.getName()).get();
		
		Salida salida = salidaRepo.findByIdSalida(idsalida);
		if(Objects.isNull(salida)) {
			throw new ResourceNotFoundException("Salida", "id", String.valueOf(idsalida));
		}
		List<DetalleSalida> detalles = salida.getDetalles();
		for(DetalleSalida detalle:detalles) {
			if(detalle.getProducto().getCodigoPieza().equalsIgnoreCase(codigopieza)) {
				Producto activoEliminar = productoRepo.findByCodigoPieza(codigopieza);
				if(!activoEliminar.getEstaActivo()) {
					throw new ResourceCannotBeAccessException("El activo ya se encuentra inhabilitado");
				}
				if(activoEliminar.getEstadoSalida().equalsIgnoreCase("P")) {
					throw new ResourceCannotBeAccessException("El activo se encuentra en una salida");
				}
				activoEliminar.setEstaActivo(false);
				activoEliminar.setFechaAEliminacion(new Date());
				productoRepo.save(activoEliminar);
				salida.updateActivo(activoEliminar, usuario, new Date());
			}
		}
		
		salida = salidaRepo.save(salida);
		
		return salida;
	}

	@Override
	public Salida confirmarSalida(Integer idsalida) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		Usuario usuario = usuarioRepo.findByNombreUsuarioOrEmail(authentication.getName(), authentication.getName()).get();
		
		Salida salida = salidaRepo.findByIdSalida(idsalida);
		if(Objects.isNull(salida)) {
			throw new ResourceNotFoundException("Salida", "id", String.valueOf(idsalida));
		}
		List<DetalleSalida> detalles = salida.getDetalles();
		for(DetalleSalida detalle:detalles) {
				Producto activoEliminar = productoRepo.findByCodigoPieza(detalle.getProducto().getCodigoPieza());
				activoEliminar.setEstaActivo(false);
				System.out.println(activoEliminar.getCodigoPieza());
				activoEliminar = productoRepo.save(activoEliminar);
				salida.updateActivo(activoEliminar, usuario, new Date());
				salida.setEstadoSalida("F");
		}

		
		salida = salidaRepo.save(salida);
		
		return salida;
	}

	@Override
	public void eliminarSalida(Integer idsalida, Long nit) throws ResourceCannotBeDeleted{
		Salida salida = salidaRepo.findByIdSalida(idsalida);
		if(Objects.isNull(salida)) {
			throw new ResourceNotFoundException("Salida", "id", String.valueOf(idsalida));
		}
		List<DetalleSalida> detalles = salida.getDetalles();
		for(DetalleSalida detalle:detalles) {
				Producto activoEliminar = productoRepo.findByCodigoPieza(detalle.getProducto().getCodigoPieza());
				if(!activoEliminar.getEstaActivo()) {
					throw new ResourceCannotBeDeleted("Activo", "se encuentra inhabilitado");
				}
				if(activoEliminar.getEstadoTraslado().equalsIgnoreCase("F")) {
					List<DetalleTrasl> lista = activoEliminar.getDetalles();
					if(!lista.get(0).getTraslado().getEstadoTraslado().equalsIgnoreCase("F")) {
						throw new ResourceCannotBeDeleted("Salida");
					}
				}
		}
		for(DetalleSalida detalle:detalles) {
			Producto activoEliminar = productoRepo.findByCodigoPieza(detalle.getProducto().getCodigoPieza());
			salida.removeActivo(activoEliminar);
		}
		salida.setEmpresa(null);
		salida.setFechaCreacion(null);
		//salida.setIdSalida(null);
		salida.setNumDocumento(null);
		salida.setTipoMovimiento(null);
		salida.setUsuarioCrea(null);
		salidaRepo.delete(salida);
	}

	@Override
	public Salida obtieneSalida(Integer id) {
		
		Salida salida = salidaRepo.findByIdSalida(id);
		if(Objects.isNull(salida)) {
			throw new ResourceNotFoundException("Salida", "id", String.valueOf(id));
		}
		return salida;
	}

	@Override
	public Page<DetalleSalida> obtieneDetalleSalida(Integer idsalida, Integer pagina, Integer items) {
		
		Salida salida = salidaRepo.findByIdSalida(idsalida);
		if(Objects.isNull(salida)) {
			throw new ResourceNotFoundException("Salida", "id", String.valueOf(idsalida));
		}
		if(items == 0) {
			Page<DetalleSalida> detalles = detalleSalidaRepo.findBySalida(salida, PageRequest.of(0, 10));
			return detalles;
		}
		Page<DetalleSalida> detalles = detalleSalidaRepo.findBySalida(salida, PageRequest.of(pagina, items));
		return detalles;
	}

	@Override
	public void eliminarActivo(Integer idsalida, String codigopieza) {
		Salida salida = salidaRepo.findByIdSalida(idsalida);
		if(Objects.isNull(salida)) {
			throw new ResourceNotFoundException("Salida", "id", String.valueOf(idsalida));
		}
		Producto activo = productoRepo.findByCodigoPieza(codigopieza);
		if(Objects.isNull(activo)) {
			throw new ResourceNotFoundException("activo", "codigo de pieza", codigopieza);
		}
		if(!activo.getEstaActivo()) {
			throw new ResourceCannotBeDeleted("Activo", "se encuentra inhabilitado");
		}
		salida.removeActivo(activo);
		
		salidaRepo.save(salida);
		
	}

	@Override
	public void eliminarTodosActivos(Integer idsalida) {

		Salida salida = salidaRepo.findByIdSalida(idsalida);
		if(Objects.isNull(salida)) {
			throw new ResourceNotFoundException("Salida", "id", String.valueOf(idsalida));
		}
		List<DetalleSalida> detalles = salida.getDetalles();
		for(DetalleSalida detalle:detalles) {
				Producto activoEliminar = productoRepo.findByCodigoPieza(detalle.getProducto().getCodigoPieza());
				if(activoEliminar == null) {
					throw new ResourceNotFoundException("Activo", "codigo de pieza", detalle.getProducto().getCodigoPieza());
				}
				if(!activoEliminar.getEstaActivo()) {
					throw new ResourceCannotBeDeleted("Activo", "se encuentra inhabilitado");
				}
				if(activoEliminar.getEstadoTraslado() != null && activoEliminar.getEstadoTraslado().equalsIgnoreCase("F")) {
					List<DetalleTrasl> lista = activoEliminar.getDetalles();
					if(!lista.get(0).getTraslado().getEstadoTraslado().equalsIgnoreCase("F")) {
						throw new ResourceCannotBeDeleted("Activo", "se encuentra en un traslado sin finalizar");
					}
				}
		}
		salida.getDetalles().removeAll(detalles);
		/*for(DetalleSalida detalle:detalles) {
			Producto activoEliminar = productoRepo.findByCodigoPieza(detalle.getProducto().getCodigoPieza());
			salida.removeActivo(activoEliminar);
		}*/
		//salida.setEmpresa(null);
		//salida.setFechaCreacion(null);
		//salida.setIdSalida(null);
		//salida.setNumDocumento(null);
		//salida.setTipoMovimiento(null);
		//salida.setUsuarioCrea(null);
		
		salidaRepo.save(salida);
	}


}
