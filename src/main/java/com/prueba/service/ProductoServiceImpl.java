package com.prueba.service;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
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
	public List<ProductoDTO> list() {
		List<Producto> productos = productoRepo.findAll();
		
		return productos.stream().map(producto -> mapearEntidad(producto)).collect(Collectors.toList());
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
		producto.setCodigoPieza(productoDTO.getCodigoPieza());
		
		
		return mapearEntidad(producto);
	}

	@Override
	public void delete(Long id) {
		Producto producto = productoRepo.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Producto", "Codigo de pieza", id));
		
		productoRepo.delete(producto);

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
/*
	@Override
	public List<ProductoDTO> search(String area) {
		
		return productoRepo.search(area).stream().map(producto -> mapearEntidad(producto)).collect(Collectors.toList());
	}

	@Override
	public List<ProductoDTO> search(String area, Long codigopieza) {

		return productoRepo.search(area,codigopieza).stream().map(producto -> mapearEntidad(producto)).collect(Collectors.toList());
	}*/

	@Override
	public List<ProductoDTO> searchProducts(SearchDTO searchDTO) {
		
		List<Producto> listProducts = productoRepo.findAll(productSpec.getProductos(searchDTO));
		
		return listProducts.stream().map(producto -> mapearEntidad(producto)).collect(Collectors.toList());
	}

	@Override
	public List<ProductoDTO> searchProducts(String letra) {
		List<Producto> listProducts = productoRepo.findByDescripcionContains(letra);
				
		return listProducts.stream().map(producto -> mapearEntidad(producto)).collect(Collectors.toList());
	}

	@Override
	public void load(List<ProductoDTO> listProductoDTO) {
		List<Producto> listProducts = listProductoDTO.stream().map(productoDTO -> mapearDTO(productoDTO)).collect(Collectors.toList());
		
		productoRepo.saveAll(listProducts);
		
	}
}
