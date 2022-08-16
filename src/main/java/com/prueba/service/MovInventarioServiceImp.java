 package com.prueba.service;

import java.util.Date;
import java.util.List;
import java.util.Objects;

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
import com.prueba.security.entity.Usuario;
import com.prueba.security.repository.UsuarioRepository;
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
	
	@Autowired
	private UsuarioRepository usuarioRepo;
	
	@Override
	public MovInventario create(MovInventarioDTO movInventarioDto) { 
		
		MovInventario inventario = new MovInventario();
		Ubicacion ubicacion = ubicacionRepo.findById(movInventarioDto.getUbicacion().getId())
				.orElseThrow(() -> new ResourceNotFoundException("Ubicacion", "Id", movInventarioDto.getUbicacion().getId())); 
		inventario.setUbicacion(ubicacion);
		Usuario realizo = usuarioRepo.findById(movInventarioDto.getRealizo().getId())
				.orElseThrow(()-> new ResourceNotFoundException("Usuario", "id", movInventarioDto.getRealizo().getId()));
		inventario.setRealizo(realizo);
		inventario.setEmpresa(movInventarioDto.getEmpresa());
		inventario = movInvRepo.save(inventario);
		List<Producto> productos = movInventarioDto.getProductos();
		Integer idInventario = inventario.getIdMov();
		MovInventario actualizar = movInvRepo.findByidMov(idInventario);
		for(Producto producto: productos) {
			Producto activo = productoRepo.findByCodigoPieza(producto.getCodigoPieza());
			if(activo != null) {
				actualizar.addActivo(activo, realizo, new Date());
			}else {
				throw new ResourceNotFoundException("Activo", "codigo de pieza", producto.getCodigoPieza());
			}
			
		}			
		actualizar = movInvRepo.save(actualizar);

		

		return actualizar;
	}

	@Override
	public Page<MovInventario> searchInv(String letras, Empresa empresa, Integer pagina, Integer items) {
		
		Page<MovInventario> inventarios = movInvRepo.findAll(inventarioSpec.getInvenId(letras, empresa), PageRequest.of(0, 10));
		return inventarios;
	}

	@Override
	public Page<MovInventario> list(Empresa empresa, Integer pagina, Integer items) {
		Page<MovInventario> inventarios = movInvRepo.findByEmpresa(empresa, PageRequest.of(pagina, items));
		return inventarios;
	}

	@Override
	public MovInventario getInventario(Integer id) {

		MovInventario inventario = movInvRepo.findByidMov(id);
		if(Objects.isNull(inventario)) {
			throw new ResourceNotFoundException("inventario", "id", id.toString());
			
		}
		return inventario;
	}



}
