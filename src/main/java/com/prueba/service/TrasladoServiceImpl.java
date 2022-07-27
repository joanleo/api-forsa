package com.prueba.service;


import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.prueba.dto.TrasladoDTO;
import com.prueba.entity.DetalleTrasl;
import com.prueba.entity.Producto;
import com.prueba.entity.Traslado;
import com.prueba.exception.ResourceNotFoundException;
import com.prueba.repository.ProductoRepository;
import com.prueba.repository.TrasladoRepository;

@Service
public class TrasladoServiceImpl implements TrasladoService {
	
	@Autowired
	private TrasladoRepository trasladoRepo;
	
	@Autowired
	private ProductoRepository productoRepo;
	
	@Autowired
	private ModelMapper modelMapper;

	@Override
	public TrasladoDTO create(TrasladoDTO trasladoDTO) {

		Traslado traslado = new Traslado();
		if(trasladoDTO.getCantProductos() == 0) {
			traslado.setCantActivos(trasladoDTO.getProductos().size());
		}
		traslado.setCantActivos(trasladoDTO.getCantProductos());
		traslado.setDestino(trasladoDTO.getDestino());
		traslado.setOrigen(trasladoDTO.getOrigen());
		traslado.setUsuarioEnvio(trasladoDTO.getUsuarioRecibe());
		traslado.setEstadoTraslado("A");
		
		List<Producto> productos = trasladoDTO.getProductos();		
		for(Producto producto: productos) {
			Producto nuevo = productoRepo.findByCodigoPieza(producto.getCodigoPieza());
			if(nuevo != null) {
				nuevo.setEstadoTraslado("A");
				nuevo = productoRepo.save(nuevo);
				traslado.addActivo(nuevo);				
			}else {
				throw new ResourceNotFoundException("Activo", "codigo de pieza", producto.getCodigoPieza());
			}
		}
		traslado = trasladoRepo.save(traslado);
		
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
	public Traslado confirmarPieza(Long idtraslado, String codigopieza) {
		Traslado traslado = trasladoRepo.findById(idtraslado)
				.orElseThrow(() -> new ResourceNotFoundException("Traslado", "id", idtraslado));
		Producto verificar = productoRepo.findByCodigoPieza(codigopieza);
		if(Objects.isNull(verificar)) {
			throw new ResourceNotFoundException("Activo", "codigo de pieza", codigopieza);
		}
		verificar.setEstadoTraslado("E");
		productoRepo.save(verificar);
		traslado.setEstadoTraslado("E");
		traslado = trasladoRepo.save(traslado);
		
		return traslado;
	}

	@Override
	public Traslado recibirPieza(Long idtraslado, String codigopieza) {
		Traslado traslado = trasladoRepo.findById(idtraslado)
				.orElseThrow(() -> new ResourceNotFoundException("Traslado", "id", idtraslado));
		Producto verificar = productoRepo.findByCodigoPieza(codigopieza);
		if(Objects.isNull(verificar)) {
			throw new ResourceNotFoundException("Activo", "codigo de pieza", codigopieza);
		}
		verificar.setEstadoTraslado("F");
		productoRepo.save(verificar);
		traslado.setEstadoTraslado("F");
		traslado = trasladoRepo.save(traslado);
		
		return traslado;
	}

	@Override
	public Traslado confirmarTodo(Long idtraslado) {
		Traslado traslado = trasladoRepo.findById(idtraslado)
				.orElseThrow(() -> new ResourceNotFoundException("Traslado", "id", idtraslado));
		
		List<DetalleTrasl> productos = traslado.getDetalles();
		for(DetalleTrasl producto: productos) {
			Producto nuevo = productoRepo.findByCodigoPieza(producto.getProducto().getCodigoPieza());
			if(nuevo != null) {
				nuevo.setEstadoTraslado("E");
				nuevo = productoRepo.save(nuevo);
				//traslado.addActivo(nuevo);				
			}else {
				throw new ResourceNotFoundException("Activo", "codigo de pieza", producto.getProducto().getCodigoPieza());
			}
		}
		traslado.setEstadoTraslado("E");
		traslado = trasladoRepo.save(traslado);
		return traslado;
	}

	@Override
	public Traslado recibirTodo(Long idtraslado) {
		Traslado traslado = trasladoRepo.findById(idtraslado)
				.orElseThrow(() -> new ResourceNotFoundException("Traslado", "id", idtraslado));
		
		List<DetalleTrasl> productos = traslado.getDetalles();
		for(DetalleTrasl producto: productos) {
			Producto nuevo = productoRepo.findByCodigoPieza(producto.getProducto().getCodigoPieza());
			if(nuevo != null) {
				nuevo.setEstadoTraslado("F");
				nuevo = productoRepo.save(nuevo);
				//traslado.addActivo(nuevo);				
			}else {
				throw new ResourceNotFoundException("Activo", "codigo de pieza", producto.getProducto().getCodigoPieza());
			}
		}
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

}
