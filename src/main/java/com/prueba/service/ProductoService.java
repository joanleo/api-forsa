package com.prueba.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import com.prueba.dto.ProductoDTO;
import com.prueba.dto.SearchDTO;
import com.prueba.entity.Producto;


public interface ProductoService {

	public ProductoDTO create(ProductoDTO productoDTO);
	
	public Page<Producto> list(Integer offset, Integer pageSize);
	
	public ProductoDTO getProducto(Long id);
	
	public ProductoDTO update(Long id, ProductoDTO productoDTO);
	
	public void delete(Long id);
	
	public ProductoDTO receive(Long id);
	
	public Page<Producto> searchProducts(SearchDTO searchDTO, int offset, int pageSize);
	
	public Page<Producto> searchProducts(String letra);
	
	public void load(List<ProductoDTO> listProductoDTO);
	
	public String loadFile(MultipartFile file);
}
