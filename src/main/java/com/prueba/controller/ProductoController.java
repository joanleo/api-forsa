package com.prueba.controller;


import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
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
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.MultipartFile;

import com.prueba.dto.ApiResponse;
import com.prueba.dto.ProductoDTO;
import com.prueba.dto.SearchDTO;
import com.prueba.entity.Producto;
import com.prueba.repository.ProductoRepository;
import com.prueba.security.dto.ResDTO;
import com.prueba.service.ProductoService;
import com.prueba.util.CsvExportService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/activos")
@Api(tags = "Activos", description = "Operaciones referentes a los activos")
public class ProductoController {

	@Autowired
	private ProductoService productoService;
	
	@Autowired
	private CsvExportService csvService;
	
	@Autowired
	private ProductoRepository productoRepo;
	
	@PostMapping
	@ApiOperation(value = "Crea un activo", notes = "Crea un nuevo activo")
	public ResponseEntity<ProductoDTO> create(@Valid @RequestBody ProductoDTO productoDTO){
		return new ResponseEntity<ProductoDTO>(productoService.create(productoDTO), HttpStatus.CREATED);
	}
	
	@PostMapping("/indexados")
	@ApiOperation(value = "Encuentra los activos", notes = "Encuentra los activos que concuerden con las especificaciones enviadas en el Json, se puede indicar o no los parametros de la paginacion")
	public ApiResponse<Page<Producto>> list(@RequestParam(required=false, defaultValue = "0") Integer pagina, 
											@RequestParam(required=false, defaultValue = "0") Integer items, 
											@RequestBody(required=false) SearchDTO searchDTO){
		System.out.println("Controller busqueda por letras 2 "+ items);
		System.out.println(searchDTO);
		Page<Producto> productos =  productoService.searchProducts(searchDTO, pagina, items);
		return new ApiResponse<>(productos.getSize(), productos);
	}
	
	@GetMapping
	@ApiOperation(value = "Encuentra los activos", notes = "Retorna los activos que contengan las letras indicadas, retorna todos los activos si no se indica ninguna letra, se puede indicar o no los parametros de la paginacion")
	public ApiResponse<Page<Producto>> list(@RequestParam(required=false, defaultValue = "0") Integer pagina, 
											@RequestParam(required=false, defaultValue = "0") Integer items,
											@RequestParam(required=false) String letras){
		
		System.out.println("Letras digitadas");
		if(letras != null) {
			System.out.println("Controller busqueda por letras 1");
			System.out.println("letras "+letras);
			Page<Producto> productos = productoService.searchProducts(letras, pagina, items);
			return new ApiResponse<>(productos.getSize(), productos);
		}else {
			System.out.println("Controller busqueda por letras 3");
			Page<Producto> productos = productoService.list(pagina, items);
			return new ApiResponse<>(productos.getSize(), productos);
		}
	}
	
	@GetMapping("/{id}")
	@ApiOperation(value = "Encuentra un activo", notes = "Retorna un activo por el id")
	public ResponseEntity<ProductoDTO> get(@PathVariable(name = "id") String id){
		return ResponseEntity.ok(productoService.getProducto(id));
	}
	
	@PatchMapping("/{id}")
	@ApiOperation(value = "Verifica un activo", notes = "Actualiza un activo por su id")
	public ResponseEntity<ProductoDTO> verify(@PathVariable(name = "id") String id,
												@RequestBody(required=false) ProductoDTO productoDTO){
		if(productoDTO == null) {
			throw new IllegalArgumentException("Falta informacion"); 
		}
		return new ResponseEntity<ProductoDTO>(productoService.receive(id, productoDTO), HttpStatus.ACCEPTED);
	}
	
	@PostMapping("/cargar")
	@ApiOperation(value = "Carga de activos", notes = "AUN PENDIENTE POR DEFINIR")
	public ResponseEntity<ResDTO> loadProducts(@RequestParam("archivo") MultipartFile file, WebRequest webRequest){ //@RequestBody List<ProductoDTO> list
		
		try {
			productoService.loadFile(file, webRequest);
		} catch (Exception e) {
			// TODO: handle exception
		}
		//System.out.println(list);
		//productoService.load(list);
		
		return new ResponseEntity<ResDTO>(new ResDTO("Se ha cargado la lista con exito"), HttpStatus.OK);
	}
	
	@DeleteMapping("/{id}")
	@ApiOperation(value = "Elimina un activo", notes = "Elimina un activo por su id")
	public ResponseEntity<ResDTO> delete(@PathVariable(name="id")String id){
		productoService.delete(id);
		return new ResponseEntity<ResDTO>(new ResDTO("Item eliminado con exito"), HttpStatus.OK);
	}
	
	@PostMapping("/descarga")
	@ApiOperation(value = "Descarga listado en formato csv", notes = "Descarga listado de activos de la busqueda realizada en formato csv")
	public void getCsvProducts(HttpServletResponse servletResponse,
								@RequestParam(required=false, defaultValue = "0") Integer pagina, 
								@RequestParam(required=false, defaultValue = "0") Integer items,
								@RequestParam(required=false) String letras,
								@RequestBody(required=false) SearchDTO searchDTO) throws IOException {
		servletResponse.setContentType("csv");
        servletResponse.addHeader("Content-Disposition","attachment; filename=productos.csv");
        
        
        //List<Producto> productos = productoRepo.findAllByEstaActivoTrue();
        System.out.println("Descarga de activos en csv");
		if (searchDTO != null) {
			System.out.println("Se envio SearchDTO");
			List<Producto> productos =  productoService.searchProducts(searchDTO);
			csvService.writeEmployeesToCsv(servletResponse.getWriter(), productos);
		}else if(letras != null){
			System.out.println("Controller busqueda por letras");
			List<Producto> productos = productoService.searchProducts(letras);
			csvService.writeEmployeesToCsv(servletResponse.getWriter(), productos);
		}else{
			System.out.println("Controller busqueda vacia");
			List<Producto> productos = productoRepo.findAllByEstaActivoTrue();
			csvService.writeEmployeesToCsv(servletResponse.getWriter(), productos);
		}
		
		
        
	}
}
