package com.prueba.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.MultipartFile;

import com.prueba.dto.ProductoDTO;
import com.prueba.dto.SearchDTO;
import com.prueba.entity.Empresa;
import com.prueba.entity.Producto;
import com.prueba.entity.Producto_id;


public interface ProductoService {

	public ProductoDTO create(ProductoDTO productoDTO);
	
	public Page<Producto> list(Empresa empresa, Integer offset, Integer pageSize);
	
	public Producto getProducto(Producto_id id);
	
	public Producto update(String id, ProductoDTO productoDTO);
	
	public void delete(Producto_id id);
	
	public Producto receive(String id, ProductoDTO productoDTO) throws IllegalAccessException;
	
	public Page<Producto> searchProducts(Empresa empresa, SearchDTO searchDTO, int offset, int pageSize);
	
	public List<Producto> searchProducts(SearchDTO searchDTO);
	
	public Page<Producto> searchProducts(Empresa empresa, String letra, int offset, int pageSize);
	
	public List<Producto> searchProducts(String letras, Empresa empresa);
	
	public void load(List<ProductoDTO> listProductoDTO);
	
	public String loadFile(MultipartFile file, WebRequest webRequest);

	public Page<Producto> getVerificacion(String orden, String filtro, int offset, int pageSize);

	public List<Producto> getVerificacion(String orden, String filtro);
}
