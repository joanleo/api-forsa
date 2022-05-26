package com.prueba.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.MultipartFile;

import com.prueba.dto.ProductoDTO;
import com.prueba.dto.SearchDTO;
import com.prueba.entity.Producto;


public interface ProductoService {

	public ProductoDTO create(ProductoDTO productoDTO);
	
	public Page<Producto> list(Integer offset, Integer pageSize);
	
	public ProductoDTO getProducto(String id);
	
	public ProductoDTO update(String id, ProductoDTO productoDTO);
	
	public void delete(String id);
	
	public ProductoDTO receive(String id);
	
	public Page<Producto> searchProducts(SearchDTO searchDTO, int offset, int pageSize);
	
	public List<Producto> searchProducts(SearchDTO searchDTO);
	
	public Page<Producto> searchProducts(String letra, int offset, int pageSize);
	
	public List<Producto> searchProducts(String letra);
	
	public void load(List<ProductoDTO> listProductoDTO);
	
	public String loadFile(MultipartFile file, WebRequest webRequest);
}
