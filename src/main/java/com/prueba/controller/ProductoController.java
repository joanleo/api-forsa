package com.prueba.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.prueba.dto.ProductoDTO;
import com.prueba.dto.SearchDTO;
import com.prueba.service.ProductoService;

import io.swagger.annotations.Api;

@RestController
@RequestMapping("/productos")
@Api(tags = "Productos", description = "Operaciones referentes a los productos")
public class ProductoController {

	@Autowired
	private ProductoService productoService;
	
	@PostMapping
	public ResponseEntity<ProductoDTO> create(@Valid @RequestBody ProductoDTO productoDTO){
		return new ResponseEntity<ProductoDTO>(productoService.create(productoDTO), HttpStatus.CREATED);
	}
	
	@GetMapping
	public List<ProductoDTO> list(@RequestParam(required=false) String letters){
		if(letters != null) {
			return productoService.searchProducts(letters);
		}else {
			return productoService.list();			
		}
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<ProductoDTO> get(@PathVariable(name = "id") Long id){
		return ResponseEntity.ok(productoService.getProducto(id));
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<ProductoDTO> verify(@PathVariable(name = "id") Long id){
		
		return new ResponseEntity<ProductoDTO>(productoService.receive(id), HttpStatus.ACCEPTED);
	}
	
	@GetMapping("/search")
	public List<ProductoDTO> search(@RequestBody SearchDTO searchDTO){
		System.out.println(searchDTO.getClass().getName());
		/*String query = "SELECT p FROM productos as p WHERE ";
		if(searchDTO.getCodigoPieza() != null && searchDTO.getArea() != null) {
			System.out.println("Dos parametros");
			return productoService.search(searchDTO.getArea(), searchDTO.getCodigoPieza());
		}
		if(searchDTO.getArea() != null) return productoService.search(searchDTO.getArea());
		
		if(searchDTO.getDescripcion() != null) query += " and p.vcdescripcion=:" + searchDTO.getDescripcion();
		if(searchDTO.getEmpresa() != null) query += " and p.vcnitempresa=:" + searchDTO.getEmpresa();
		if(searchDTO.getEstado() != null) query += " and p.nidestado=:" + searchDTO.getEstado();
		if(searchDTO.getFabricante() != null) query += " and p.vcnitfabricante=:" + searchDTO.getFabricante();
		if(searchDTO.getFamilia() != null) query += " and p.nidfamilia=:" + searchDTO.getFamilia();
		if(searchDTO.getOrden() != null) query += " and p.vcorden=:" + searchDTO.getOrden();
		if(searchDTO.getVerificado() != null) query += " and p.bverificado=:" + searchDTO.getVerificado();
		
		System.out.println(query);
		
		
		return productoService.search(searchDTO.getArea());*/
		
		return productoService.searchProducts(searchDTO);
		
	}
	
	@PostMapping("/load")
	public ResponseEntity<String> loadProducts(@RequestBody List<ProductoDTO> list){
		System.out.println(list);
		productoService.load(list);
		
		return new ResponseEntity<>("Se ha cargado la lista con exito", HttpStatus.OK);
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<String> delete(@PathVariable(name="id")Long id){
		productoService.delete(id);
		
		return new ResponseEntity<>("Item eliminado con exito", HttpStatus.OK);
	}
}
