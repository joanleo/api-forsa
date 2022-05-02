package com.prueba.service;

import java.util.List;

import com.prueba.dto.ProductoDTO;
import com.prueba.dto.SearchDTO;


public interface ProductoService {

	public ProductoDTO create(ProductoDTO productoDTO);
	
	public List<ProductoDTO> list();
	
	public ProductoDTO getProducto(Long id);
	
	public ProductoDTO update(Long id, ProductoDTO productoDTO);
	
	public void delete(Long id);
	
	public ProductoDTO receive(Long id);
	
	/*public List<ProductoDTO> search(String area);
	
	public List<ProductoDTO> search(String area, Long codigopieza);*/
	
	public List<ProductoDTO> searchProducts(SearchDTO searchDTO);
	
	List<ProductoDTO> searchProducts(String letra);
	
	void load(List<ProductoDTO> listProductoDTO);
}
