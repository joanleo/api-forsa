package com.prueba.service;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.prueba.dto.ProductoDTO;
import com.prueba.dto.SearchDTO;
import com.prueba.entity.Producto;
import com.prueba.exception.ResourceNotFoundException;
import com.prueba.repository.ProductoRepository;
import com.prueba.specifications.ProductSpecifications;

@Service
public class ProductoServiceImpl implements ProductoService {
	
	@Autowired
	private ProductoRepository productoRepo;
	
	@Autowired
	private ModelMapper modelMapper;
	
	@Autowired
	private ProductSpecifications productSpec;

	@Override
	public ProductoDTO create(ProductoDTO productoDTO) {
		Producto producto = mapearDTO(productoDTO);
		Producto exist = productoRepo.findByCodigoPieza(producto.getCodigoPieza());
		
		if(exist == null) {
			productoRepo.save(producto);
		}else {
			throw new IllegalAccessError("El producto con id "+ producto.getCodigoPieza() + " que trata de crear ya existe");
		}
		
		return mapearEntidad(producto);
	}

	@Override
	public Page<Producto> list(Integer offset, Integer pageSize) {
		System.out.println("Sin parametros"); 
		Page<Producto> productos = productoRepo.findAll(PageRequest.of(0, 10));
		return productos;
	}

	@Override
	public Page<Producto> searchProducts(SearchDTO searchDTO, int offset, int pageSize) {
		Page<Producto> listProducts = productoRepo.findAll(productSpec.getProductos(searchDTO), PageRequest.of(offset, pageSize));		
		return listProducts;//listProducts.stream().map(producto -> mapearEntidad(producto)).collect(Collectors.toList());
	}

	@Override
	public ProductoDTO getProducto(Long id) {
		Producto producto = productoRepo.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Producto", "Codigo de pieza", id));
		
		return mapearEntidad(producto);
	}

	@Override
	public ProductoDTO update(Long id, ProductoDTO productoDTO) {
		Producto producto = productoRepo.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Producto", "Codigo de pieza", id));
		
		producto.setArea(productoDTO.getArea());
		producto.setOrden(productoDTO.getOrden());
		producto.setDescripcion(productoDTO.getDescripcion());
		
		return mapearEntidad(producto);
	}

	@Override
	public void delete(Long id) {
		Producto producto = productoRepo.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Producto", "Codigo de pieza", id));
		
		if(producto.getVerificado() == false) {
			producto.setEstaActivo(false);
		}else {
			throw new IllegalAccessError("No es posible realizar la accion solicitada");
		}
		productoRepo.save(producto);

	}

	public ProductoDTO mapearEntidad(Producto producto) {
		return modelMapper.map(producto, ProductoDTO.class);
	}

	public Producto mapearDTO(ProductoDTO productoDTO) {
		return modelMapper.map(productoDTO, Producto.class);
	}

	@Override
	public ProductoDTO receive(Long id) {
		Producto producto = productoRepo.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Producto", "Codigo de pieza", id));
		
		producto.setVerificado(true);	
		productoRepo.save(producto);
		
		return mapearEntidad(producto);
	}
	

	@Override
	public Page<Producto> searchProducts(String letra) {
		
		 Page<Producto> listProducts = productoRepo.findByDescripcionContains(letra, PageRequest.of(0, 10));				
		return listProducts;
	}
	

	@Override
	public void load(List<ProductoDTO> listProductoDTO) {
		List<Producto> listProducts = listProductoDTO.stream().map(productoDTO -> mapearDTO(productoDTO)).collect(Collectors.toList());
		productoRepo.saveAll(listProducts);
		
	}
	
}
