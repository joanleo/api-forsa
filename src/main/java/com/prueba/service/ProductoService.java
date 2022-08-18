package com.prueba.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.MultipartFile;

import com.prueba.dto.ProductoDTO;
import com.prueba.dto.ReconversionDTO;
import com.prueba.dto.SearchDTO;
import com.prueba.entity.Empresa;
import com.prueba.entity.Producto;


public interface ProductoService {

	public ProductoDTO create(ProductoDTO productoDTO);
	
	public Page<Producto> list(Empresa empresa, Integer offset, Integer pageSize);
	
	public Producto getProducto(String codigoPieza);
	
	public Producto update(String id, ProductoDTO productoDTO);
	
	public String delete(String codigoPieza);
	
	public Producto receive(String id, ProductoDTO productoDTO);
	
	public Page<Producto> searchProducts(Empresa empresa, SearchDTO searchDTO, int offset, int pageSize);
	
	public List<Producto> searchProducts(SearchDTO searchDTO, Empresa empresa);
	
	//public Page<Producto> searchProducts(Empresa empresa, String letra, int offset, int pageSize);
	
	public List<Producto> searchProducts(String letras, Empresa empresa);
	
	public void load(List<ProductoDTO> listProductoDTO);
	
	public String loadFile(MultipartFile file, WebRequest webRequest);

	public Page<Producto> getVerificacion(String orden, String filtro, Empresa empresa, int offset, int pageSize);

	public List<Producto> getVerificacion(String orden, String filtro, Empresa empresa);

	/**
	 * @param empresa
	 * @return
	 */
	public List<Producto> list(Empresa empresa);

	/**
	 * @param reconversion
	 * @return
	 */
	public List<ProductoDTO> reconversionPieza(ReconversionDTO reconversion);
}
