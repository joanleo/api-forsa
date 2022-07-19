package com.prueba.service;


import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.prueba.dto.TrasladoDTO;
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

	@Override
	public TrasladoDTO create(TrasladoDTO trasladoDTO) {

		Traslado traslado = new Traslado();
		traslado.setCantActivos(trasladoDTO.getCantProductos());
		traslado.setDestino(trasladoDTO.getDestino());
		traslado.setOrigen(trasladoDTO.getOrigen());
		traslado.setUsuarioEnvio(trasladoDTO.getUsuarioRecibe());
		
		List<Producto> productos = trasladoDTO.getProductos();		
		for(Producto producto: productos) {
			Producto nuevo = productoRepo.findByCodigoPieza(producto.getCodigoPieza());
			if(nuevo != null) {
				traslado.addActivo(nuevo);				
			}else {
				throw new IllegalAccessError("El producto con codigo de pieza " + producto.getCodigoPieza() + " no existe");
			}
		}
		trasladoRepo.save(traslado);
		
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

}
