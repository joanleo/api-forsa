package com.prueba.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.prueba.dto.ApiResponse;
import com.prueba.dto.ProductoDTO;
import com.prueba.dto.SearchDTO;
import com.prueba.entity.Producto;
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
		System.out.println("Controller busqueda por letras");
		if(letters != null) {
			System.out.println("letras "+letters);
			return productoService.searchProducts(letters);
		}else {
			return productoService.list();			
		}
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<ProductoDTO> get(@PathVariable(name = "id") Long id){
		return ResponseEntity.ok(productoService.getProducto(id));
	}
	
	@PatchMapping("/{id}")
	public ResponseEntity<ProductoDTO> verify(@PathVariable(name = "id") Long id){
		
		return new ResponseEntity<ProductoDTO>(productoService.receive(id), HttpStatus.ACCEPTED);
	}
	
	@GetMapping("{page}{items}")
	public ApiResponse<Page<Producto>> search(@RequestBody SearchDTO searchDTO, @PathVariable(name = "page") int page, @PathVariable(name = "items") int items){
		System.out.println(searchDTO.getClass().getName());
		Page<Producto> productos =  productoService.searchProducts(searchDTO, page, items);

		return new ApiResponse<>(productos.getSize(), productos);
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
