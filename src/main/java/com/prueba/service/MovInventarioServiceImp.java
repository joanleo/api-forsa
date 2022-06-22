package com.prueba.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.prueba.dto.MovInventarioDTO;
import com.prueba.entity.Empresa;
import com.prueba.entity.MovInventario;
import com.prueba.entity.Producto;
import com.prueba.entity.Ubicacion;
import com.prueba.exception.ResourceNotFoundException;
import com.prueba.repository.MovInventarioRepository;
import com.prueba.repository.ProductoRepository;
import com.prueba.repository.UbicacionRepository;
import com.prueba.specifications.InventarioSpecifications;

@Service
public class MovInventarioServiceImp implements MovInventarioService {

	@Autowired
	private UbicacionRepository ubicacionRepo;
	
	@Autowired
	private ProductoRepository productoRepo;
	
	@Autowired
	private MovInventarioRepository movInvRepo;
	
	@Autowired
	private InventarioSpecifications inventarioSpec;
	
	@Override
	public MovInventarioDTO create(MovInventarioDTO movInventarioDto) {
		Optional<MovInventario> exist = movInvRepo.findById(movInventarioDto.getId());
		if(exist.isEmpty()) {
			MovInventario inventario = new MovInventario();
			Ubicacion ubicacion = ubicacionRepo.findById(movInventarioDto.getUbicacion().getId())
					.orElseThrow(() -> new ResourceNotFoundException("Ubicacion", "Id", movInventarioDto.getUbicacion().getId())); 
			inventario.setUbicacion(ubicacion);
			inventario.setId(movInventarioDto.getId());
			inventario.setRealizo(movInventarioDto.getRealizo());
			List<Producto> productos = movInventarioDto.getProductos();
			
			for(Producto producto: productos) {
				Producto actualizar = productoRepo.findByIdProducto(producto.getIdProducto());
				if(actualizar != null) {
					inventario.addActivo(actualizar);
				}else {
					throw new IllegalAccessError("El activo con codigo de pieza " + producto.getIdProducto().getCodigoPieza() + " no existe");
				}
				
			}			
			movInvRepo.save(inventario);
		}else {
			throw new IllegalAccessError("El inventario con id " + movInventarioDto.getId() + " ya existe");
		}
		

		return movInventarioDto;
	}

	@Override
	public Page<MovInventario> searchInv(String letras, Empresa empresa, Integer pagina, Integer items) {
		
		Page<MovInventario> inventarios = movInvRepo.findAll(inventarioSpec.getInvenId(letras, empresa), PageRequest.of(0, 10));
		return inventarios;
	}

	@Override
	public Page<MovInventario> list(Empresa empresa, Integer pagina, Integer items) {
		System.out.println("servicio");
		Page<MovInventario> inventarios = movInvRepo.findByEmpresa(empresa, PageRequest.of(pagina, items));
		return inventarios;
	}

	@Override
	public MovInventario getInventario(Long id) {

		MovInventario inventario = movInvRepo.findById(id)
									.orElseThrow(()-> new ResourceNotFoundException("inventario", "id", id));
		return inventario;
	}

}
