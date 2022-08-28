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

import com.prueba.dto.SalidaDTO;
import com.prueba.entity.DetalleSalida;
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
import com.prueba.util.UtilitiesApi;

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
	
	@Autowired
	private UtilitiesApi util;
	
	@Override
	public Salida crearSalida(SalidaDTO salidaDTO){
		System.out.println(salidaDTO);
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		Usuario usuario = usuarioRepo.findByNombreUsuarioOrEmail(authentication.getName(), authentication.getName()).get();
		System.out.println("Creo salida vacia");
		Salida nuevaSalida = new Salida();
		Empresa empresa;
		TipoMov tipomov = new TipoMov();
		
		System.out.println("Agregando empresa");
		if(salidaDTO.getEmpresa() != null) {
			empresa = util.obtenerEmpresa(salidaDTO.getEmpresa().getNit());
		}else {
			empresa = usuario.getEmpresa();;
		}
		System.out.println("Agregando usuario");
		if(salidaDTO.getUsuarioCrea() != null) {
			Long idUsuario = salidaDTO.getUsuarioCrea().getId();
			usuario = usuarioRepo.findById(idUsuario)
					.orElseThrow(() -> new ResourceNotFoundException("Usuario", "id", idUsuario));
		}else {
			nuevaSalida.setUsuarioCrea(usuario);
		}
		System.out.println("Agregando tipo");
		tipomov = tipoMovRepo.findById(salidaDTO.getTipoMovimiento().getId())
				.orElseThrow(() -> new ResourceNotFoundException("Tipo de Movimiento", "id", salidaDTO.getTipoMovimiento().getId()));
		if(salidaDTO.getTipoMovimiento() != null) {
			tipomov = tipoMovRepo.findById(salidaDTO.getTipoMovimiento().getId())
					.orElseThrow(() -> new ResourceNotFoundException("Tipo de Movimiento", "id", salidaDTO.getTipoMovimiento().getId()));
		}
		
		nuevaSalida.setEmpresa(empresa);
		nuevaSalida.setUsuarioCrea(usuario);
		nuevaSalida.setTipoMovimiento(tipomov);
		
		System.out.println("Obteniendo detalles(productos)");
		if(salidaDTO.getDetalles() == null) {
			throw new ResourceCannotBeAccessException("Debe incluir al menos un activo");
		}
		nuevaSalida = salidaRepo.saveAndFlush(nuevaSalida);
		List<Producto> productos = salidaDTO.getDetalles();
		Salida actualizar = salidaRepo.findByIdSalida(nuevaSalida.idSalida);
		if(Objects.isNull(actualizar)) {
			throw new ResourceNotFoundException("Salida", "id", nuevaSalida.toString());
		}
				
		for(Producto producto: productos) {
			System.out.println(producto.getCodigoPieza());
			Producto activo = productoRepo.findByCodigoPieza(producto.getCodigoPieza());
			if(Objects.isNull(activo)) {
				throw new ResourceNotFoundException("activo", "codigo de pieza", producto.getCodigoPieza());
			}
			if(!activo.getEstaActivo()) {
				throw new ResourceCannotBeAccessException("Activo se encuentra inhabilitado");
			}
			activo.setEstadoSalida("A");
			activo = productoRepo.save(activo);
			actualizar.addActivo(activo);
		}
		 
		actualizar.setEstadoSalida("A");
		
		actualizar = salidaRepo.save(actualizar);
		
		return actualizar;
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
	public Salida confirmarActivoSalida(Long idsalida, String codigopieza) {
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
	public Salida confirmarSalida(Long idsalida) {
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
	public void eliminarSalida(Long idsalida, Long nit) throws ResourceCannotBeDeleted{
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
				/*if(activoEliminar.getEstadoTraslado().equalsIgnoreCase("F")) {
					List<DetalleTrasl> lista = activoEliminar.getDetalles();
					if(!lista.get(0).getTraslado().getEstadoTraslado().equalsIgnoreCase("F")) {
						throw new ResourceCannotBeDeleted("Salida");
					}
				}*/
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
	public Salida obtieneSalida(Long id) {
		
		Salida salida = salidaRepo.findByIdSalida(id);
		if(Objects.isNull(salida)) {
			throw new ResourceNotFoundException("Salida", "id", String.valueOf(id));
		}
		return salida;
	}

	@Override
	public Page<DetalleSalida> obtieneDetalleSalida(Long idsalida, Integer pagina, Integer items) {
		
		Salida salida = salidaRepo.findByIdSalida(idsalida);
		if(Objects.isNull(salida)) {
			throw new ResourceNotFoundException("Salida", "id", String.valueOf(idsalida));
		}
		System.out.println("Id salida: "+idsalida);
		System.out.println("Tamaño detalle: "+salida.getDetalles().size());
		if(items == 0) {
			List<DetalleSalida> lista = detalleSalidaRepo.findBySalida(salida);
			for(DetalleSalida detalle: lista) {
				System.out.println(detalle.getProducto().getCodigoPieza());
			}
			Page<DetalleSalida> detalles = detalleSalidaRepo.findBySalida(salida, PageRequest.of(0, 10));
			return detalles;
		}
		Page<DetalleSalida> detalles = detalleSalidaRepo.findBySalida(salida, PageRequest.of(pagina, items));
		return detalles;
	}

	@Override
	public void eliminarActivo(Long idsalida, String codigopieza) {
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
	public void eliminarTodosActivos(Long idsalida) {

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
				/*if(activoEliminar.getEstadoTraslado() != null && activoEliminar.getEstadoTraslado().equalsIgnoreCase("F")) {
					List<DetalleTrasl> lista = activoEliminar.getDetalles();
					if(!lista.get(0).getTraslado().getEstadoTraslado().equalsIgnoreCase("F")) {
						throw new ResourceCannotBeDeleted("Activo", "se encuentra en un traslado sin finalizar");
					}
				}*/
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

	@Override
	public Salida crearSalida(Salida salida) {
		// TODO Auto-generated method stub
		return null;
	}


}
