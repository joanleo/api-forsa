package com.prueba.service;


import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.prueba.dto.TrasladoDTO;
import com.prueba.entity.DetalleTrasl;
import com.prueba.entity.Producto;
import com.prueba.entity.Traslado;
import com.prueba.entity.Ubicacion;
import com.prueba.exception.ResourceCannotBeDeleted;
import com.prueba.exception.ResourceNotFoundException;
import com.prueba.repository.ProductoRepository;
import com.prueba.repository.TrasladoRepository;
import com.prueba.repository.UbicacionRepository;
import com.prueba.security.entity.Usuario;
import com.prueba.security.repository.UsuarioRepository;

@Service
public class TrasladoServiceImpl implements TrasladoService {
	
	@Autowired
	private TrasladoRepository trasladoRepo;
	
	@Autowired
	private ProductoRepository productoRepo;
	
	@Autowired
	private UbicacionRepository ubicacionRepo;
	
	@Autowired
	private UsuarioRepository usuarioRepo;
	
	@Autowired
	private ModelMapper modelMapper;

	@Override
	public TrasladoDTO create(TrasladoDTO trasladoDTO) {

		Traslado traslado = new Traslado();
		Ubicacion destino = new Ubicacion();
		Ubicacion origen = new Ubicacion();
		Usuario usuario = new Usuario();
		if(trasladoDTO.getDestino() != null) {
			Long idDestino = trasladoDTO.getDestino().getId();
			destino = ubicacionRepo.findById(idDestino)
					.orElseThrow(() -> new ResourceNotFoundException("Ubicacion", "id", idDestino));			
		}
		if(trasladoDTO.getOrigen() != null) {
			Long idOrigen = trasladoDTO.getDestino().getId();
			origen = ubicacionRepo.findById(trasladoDTO.getOrigen().getId())
					.orElseThrow(() -> new ResourceNotFoundException("Ubicacion", "id", idOrigen));			
		}
		if(destino.equals(origen)) {
			throw new IllegalArgumentException("Las ubicaciones destino y origen deben ser distintos");
		}
		if(trasladoDTO.getCantActivos() == 0) {
			System.out.println("cantidad se toma del tamaño del la lista "+trasladoDTO.getDetalles().size());
			traslado.setCantActivos(trasladoDTO.getDetalles().size());
		}else {
			traslado.setCantActivos(trasladoDTO.getCantActivos());			
		}
		
		if(trasladoDTO.getUsuarioEnvio() != null) {
			Long idUsuario = trasladoDTO.getUsuarioEnvio().getId();
			usuario = usuarioRepo.findById(idUsuario)
					.orElseThrow(() -> new ResourceNotFoundException("Usuario", "id", idUsuario));
		}else {
			System.out.println("Usuario que envia es null");
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			usuario = usuarioRepo.findByNombreUsuarioOrEmail(authentication.getName(), authentication.getName()).get();
			traslado.setUsuarioEnvio(usuario);
		}
		

		traslado.setDestino(destino);
		traslado.setOrigen(origen);
		
		traslado.setUsuarioEnvio(usuario);
		traslado.setEstadoTraslado("A");
		traslado = trasladoRepo.saveAndFlush(traslado);
		Long idtraslado = traslado.getIdTraslado();
		System.out.println("Traslado creado con id" + idtraslado);
		List<Producto> productos = trasladoDTO.getDetalles();
		Traslado actualizar = trasladoRepo.findById(idtraslado)
				.orElseThrow(() -> new ResourceNotFoundException("Traslado", "id", idtraslado));
		for(Producto producto: productos) {
			Producto nuevo = productoRepo.findByCodigoPieza(producto.getCodigoPieza());
			if(nuevo != null) {
				nuevo.setEstadoTraslado("A");
				nuevo = productoRepo.save(nuevo);
				actualizar.addActivo(nuevo);
			}else {
				throw new ResourceNotFoundException("Activo", "codigo de pieza", producto.getCodigoPieza());
			}
		}
		traslado = trasladoRepo.save(actualizar);
		
		trasladoDTO = mapearEntidad(traslado);
		
		return trasladoDTO;
	}

	@Override
	public Traslado getTraslado(Long id) {
		
		Traslado traslado = trasladoRepo.findById(id)
							.orElseThrow(() -> new ResourceNotFoundException("Traslado", "id", id));
		
		return traslado;
	}

	@Override
	public Page<Traslado> findBetweenDates(LocalDate desde, LocalDate hasta, int offset, int pageSize) {
		if(pageSize == 0) {
			Page<Traslado> traslados = trasladoRepo.findByFechaSalidaBetween(desde, hasta, PageRequest.of(0, 10));
			return traslados;
		}
		
		Page<Traslado> traslados = trasladoRepo.findByFechaSalidaBetween(desde, hasta, PageRequest.of(offset, pageSize));
		
		return traslados;
	}
	
	public TrasladoDTO mapearEntidad(Traslado traslado) {
		return modelMapper.map(traslado, TrasladoDTO.class);
	}

	@Override
	public Traslado confirmarPieza(Long idtraslado, String codigopieza) throws Exception {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		Usuario usuario = usuarioRepo.findByNombreUsuarioOrEmail(authentication.getName(), authentication.getName()).get();
		Traslado traslado = trasladoRepo.findById(idtraslado)
				.orElseThrow(() -> new ResourceNotFoundException("Traslado", "id", idtraslado));
		Producto confirmar = productoRepo.findByCodigoPieza(codigopieza);
		if(Objects.isNull(confirmar)) {
			throw new ResourceNotFoundException("Activo", "codigo de pieza", codigopieza);
		}
		confirmar.setEstadoTraslado("E");
		productoRepo.save(confirmar);
		List<DetalleTrasl> detallesTras = traslado.getDetalles();
		for(DetalleTrasl detalle: detallesTras) {
			if(detalle.getProducto().getCodigoPieza() == codigopieza) {
				DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
				String currentDateTime = dateFormatter.format(new Date());
				Date fechaEnvio = dateFormatter.parse(currentDateTime);
				detalle.setUsuarioEnvio(usuario);
				detalle.setFechaEnvio(fechaEnvio);
			}
		}
		traslado.setDetalles(detallesTras);
		int cont = 0;
		for(DetalleTrasl detalle: detallesTras) {
			if(detalle.getProducto().getEstadoTraslado().equalsIgnoreCase("F")) {
				cont += 1;
			}
		}
		if(cont == traslado.getDetalles().size()) {
			traslado.setEstadoTraslado("E");			
		}
		traslado.setEstadoTraslado("E");
		traslado = trasladoRepo.save(traslado);
		
		return traslado;
	}

	@Override
	public Traslado recibirPieza(Long idtraslado, String codigopieza) throws Exception {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		Usuario usuario = usuarioRepo.findByNombreUsuarioOrEmail(authentication.getName(), authentication.getName()).get();
		Traslado traslado = trasladoRepo.findById(idtraslado)
				.orElseThrow(() -> new ResourceNotFoundException("Traslado", "id", idtraslado));
		Producto recibir = productoRepo.findByCodigoPieza(codigopieza);
		if(Objects.isNull(recibir)) {
			throw new ResourceNotFoundException("Activo", "codigo de pieza", codigopieza);
		}
		recibir.setEstadoTraslado("F");
		productoRepo.save(recibir);
		List<DetalleTrasl> detallesTras = traslado.getDetalles();
		for(DetalleTrasl detalle: detallesTras) {
			if(detalle.getProducto().getCodigoPieza() == codigopieza) {
				DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
				String currentDateTime = dateFormatter.format(new Date());
				Date fechaRecibo = dateFormatter.parse(currentDateTime);
				detalle.setUsuarioRecibe(usuario);
				detalle.setFechaRecibe(fechaRecibo);
			}
		}
		traslado.setDetalles(detallesTras);
		int cont = 0;
		for(DetalleTrasl detalle: detallesTras) {
			if(detalle.getProducto().getEstadoTraslado().equalsIgnoreCase("F")) {
				cont += 1;
			}
		}
		if(cont == traslado.getDetalles().size()) {
			traslado.setEstadoTraslado("F");			
		}
		traslado = trasladoRepo.save(traslado);
		
		return traslado;
	}

	@Override
	public Traslado confirmarTodo(Long idtraslado) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		Usuario usuario = usuarioRepo.findByNombreUsuarioOrEmail(authentication.getName(), authentication.getName()).get();
		Traslado traslado = trasladoRepo.findById(idtraslado)
				.orElseThrow(() -> new ResourceNotFoundException("Traslado", "id", idtraslado));
		
		List<DetalleTrasl> detalles = traslado.getDetalles();
		for(DetalleTrasl detalle: detalles) {
			Producto confirmar = productoRepo.findByCodigoPieza(detalle.getProducto().getCodigoPieza());
			if(confirmar != null) {
				confirmar.setEstadoTraslado("E");
				confirmar = productoRepo.save(confirmar);
			}else {
				throw new ResourceNotFoundException("Activo", "codigo de pieza", detalle.getProducto().getCodigoPieza());
			}
			DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
			String currentDateTime = dateFormatter.format(new Date());
			Date fechaEnvio;
			try {
				fechaEnvio = dateFormatter.parse(currentDateTime);
				detalle.setFechaEnvio(fechaEnvio);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			detalle.setUsuarioEnvio(usuario);
			
		}
		traslado.setDetalles(detalles);
		traslado.setEstadoTraslado("E");
		traslado = trasladoRepo.save(traslado);
		return traslado;
	}

	@Override
	public Traslado recibirTodo(Long idtraslado) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		Usuario usuario = usuarioRepo.findByNombreUsuarioOrEmail(authentication.getName(), authentication.getName()).get();
		Traslado traslado = trasladoRepo.findById(idtraslado)
				.orElseThrow(() -> new ResourceNotFoundException("Traslado", "id", idtraslado));
		
		List<DetalleTrasl> detalles = traslado.getDetalles();
		for(DetalleTrasl detalle: detalles) {
			Producto nuevo = productoRepo.findByCodigoPieza(detalle.getProducto().getCodigoPieza());
			if(nuevo != null) {
				nuevo.setEstadoTraslado("F");
				nuevo = productoRepo.save(nuevo);
			}else {
				throw new ResourceNotFoundException("Activo", "codigo de pieza", detalle.getProducto().getCodigoPieza());
			}
			DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
			String currentDateTime = dateFormatter.format(new Date());
			Date fechaRecibo;
			try {
				fechaRecibo = dateFormatter.parse(currentDateTime);
				detalle.setFechaRecibe(fechaRecibo);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			detalle.setUsuarioRecibe(usuario);
			
		}
		traslado.setDetalles(detalles);
		traslado.setEstadoTraslado("F");
		traslado = trasladoRepo.save(traslado);
		return traslado;
	}

	@Override
	public void eliminarPieza(Long idtraslado, String codigopieza) {
		Traslado traslado = trasladoRepo.findById(idtraslado)
				.orElseThrow(() -> new ResourceNotFoundException("Traslado", "id", idtraslado));
		Producto eliminar = productoRepo.findByCodigoPieza(codigopieza);
		eliminar.setEstadoTraslado("");
		eliminar = productoRepo.save(eliminar);
		traslado.removeActivo(eliminar);
		
		traslado = trasladoRepo.save(traslado);
		
	}

	@Override
	public void eliminarTodo(Long idtraslado) {
		Traslado traslado = trasladoRepo.findById(idtraslado)
				.orElseThrow(() -> new ResourceNotFoundException("Traslado", "id", idtraslado));
		
		List<DetalleTrasl> productos = traslado.getDetalles();
		for(DetalleTrasl producto: productos) {
			Producto eliminar = productoRepo.findByCodigoPieza(producto.getProducto().getCodigoPieza());
			if(eliminar != null) {
				eliminar.setEstadoTraslado("");
				eliminar = productoRepo.save(eliminar);
				traslado.removeActivo(eliminar);				
			}else {
				throw new ResourceNotFoundException("Activo", "codigo de pieza", producto.getProducto().getCodigoPieza());
			}
		}
		traslado = trasladoRepo.save(traslado);
		
		
	}

	@Override
	public Page<Traslado> buscarTraslados(Integer pagina, Integer items) {
		if(items == 0) {
			Page<Traslado> traslados = trasladoRepo.findAll(PageRequest.of(pagina, items));
			return traslados;
		}
		return null;
	}

	@Override
	public Page<Traslado> buscarTraslados(TrasladoDTO trasladoDTO, Integer pagina, Integer items) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void eliminarTraslado(Long idtraslado) {
		Traslado traslado = trasladoRepo.findById(idtraslado)
				.orElseThrow(() -> new ResourceNotFoundException("Traslado", "id", idtraslado));
		
		List<DetalleTrasl> detalles = traslado.getDetalles();
		for(DetalleTrasl detalle: detalles) {
			Producto activo = productoRepo.findByCodigoPieza(detalle.getProducto().getCodigoPieza());
			if(activo != null) {
				if(activo.getEstadoTraslado().equalsIgnoreCase("A")) {
					throw new ResourceCannotBeDeleted("Traslado");
				}				
			}else {
				throw new ResourceNotFoundException("Activo", "codigo de pieza", detalle.getProducto().getCodigoPieza());
			}
		}
		for(DetalleTrasl detalle: detalles) {
			Producto activo = productoRepo.findByCodigoPieza(detalle.getProducto().getCodigoPieza());
			if(activo != null) {
				activo.setEstadoTraslado("");
				activo = productoRepo.save(activo);
				traslado.removeActivo(activo);				
			}else {
				throw new ResourceNotFoundException("Activo", "codigo de pieza", detalle.getProducto().getCodigoPieza());
			}
		}
		
		trasladoRepo.delete(traslado);
		
	}

}
