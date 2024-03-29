package com.prueba.controller;


import java.io.IOException;
import java.util.List;
import java.util.Objects;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.MultipartFile;

import com.prueba.dto.ApiResponse;
import com.prueba.dto.ProductoDTO;
import com.prueba.dto.ReconversionDTO;
import com.prueba.dto.SearchDTO;
import com.prueba.entity.Empresa;
import com.prueba.entity.Producto;
import com.prueba.exception.ResourceCannotBeAccessException;
import com.prueba.repository.ProductoRepository;
import com.prueba.security.dto.ResDTO;
import com.prueba.security.entity.Usuario;
import com.prueba.security.repository.UsuarioRepository;
import com.prueba.service.ProductoService;
import com.prueba.util.CsvExportService;
import com.prueba.util.UtilitiesApi;

import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;




@RestController
@RequestMapping("/activos")
@Tag(name = "Activos", description = "Operaciones referentes a los activos")
public class ProductoController {

	@Autowired
	private ProductoService productoService;
	
	@Autowired
	private CsvExportService csvService;
	
	@Autowired
	private ProductoRepository productoRepo;
	
	@Autowired
	private UsuarioRepository usuarioRepo;
	
	@Autowired
	private UtilitiesApi util;
	
	@PostMapping
	@Operation(summary = "Crea un activo", description = "Crea un nuevo activo")
	public ResponseEntity<ProductoDTO> create(@Valid @RequestBody ProductoDTO productoDTO){
		return new ResponseEntity<ProductoDTO>(productoService.create(productoDTO), HttpStatus.CREATED);
	}
	
	@PostMapping("/indexados")
	@Operation(summary = "Encuentra los activos", description = "Encuentra los activos que concuerden con las especificaciones enviadas searchDTO, se puede indicar o no los parametros de la paginacion")
	public ApiResponse<Page<Producto>> listSearchDTO(@RequestParam(required=false, defaultValue = "0") Integer pagina, 
											@RequestParam(required=false, defaultValue = "0") Integer items, 
											@RequestParam(required=false) Long nit,
											@Valid@RequestBody(required=false) SearchDTO searchDTO){
		Empresa empresa;
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		Usuario usuario = usuarioRepo.findByNombreUsuarioOrEmail(authentication.getName(), authentication.getName()).get();
		
		if(nit != null) {
			empresa = util.obtenerEmpresa(nit);
		}else {
			empresa = usuario.getEmpresa();			
		}
		
		if(Objects.isNull(searchDTO)) {
			Page<Producto> productos = productoService.list(empresa, pagina, items);
			return new ApiResponse<>(productos.getSize(), productos);			
		}else {
			searchDTO.setEmpresa(empresa);
			Page<Producto> productos =  productoService.searchProducts(empresa, searchDTO, pagina, items);
			return new ApiResponse<>(productos.getSize(), productos);
		}
		
		
	}
	
	@GetMapping
	@Operation(summary = "Encuentra los activos", description = "Retorna los activos que contengan las letras indicadas, retorna todos los activos si no se indica ninguna letra, se puede indicar o no los parametros de la paginacion")
	public List<Producto> list(@RequestParam(required=false, defaultValue = "0") Integer pagina, 
											@RequestParam(required=false, defaultValue = "0") Integer items,
											@RequestParam(required=false) String letras,
											@RequestParam(required=false) Long nit){
		Empresa empresa;
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		Usuario usuario = usuarioRepo.findByNombreUsuarioOrEmail(authentication.getName(), authentication.getName()).get();
		
		if(nit != null) {
			empresa = util.obtenerEmpresa(nit);
		}else {
			empresa = usuario.getEmpresa();			
		}
		
		if(letras != null) {
			List<Producto> productos = productoService.searchProducts(letras, empresa);
			return  productos;
		}else {
			List<Producto> productos = productoService.list(empresa);
			return productos;
		}
	}
	
	@GetMapping("/{id},{nit}")
	@Operation(summary = "Encuentra un activo", description = "Retorna un activo por el id")
	public ResponseEntity<Producto> get(@PathVariable(name = "id") String codigoPieza,
										@PathVariable(name = "nit") Long nit){
		
		return ResponseEntity.ok(productoService.getProducto(codigoPieza));
	}
	
	@PutMapping("/{id}")
	@Operation(summary = "Actualiza un activo", description = "Actualiza los datos de un activo")
	public ResponseEntity<Producto> update(@Valid @RequestBody ProductoDTO productoDTO,
										   @PathVariable String id){
		
		Empresa empresa;
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		Usuario usuario = usuarioRepo.findByNombreUsuarioOrEmail(authentication.getName(), authentication.getName()).get();
		
		if(productoDTO.getEmpresa() != null) {
			empresa = util.obtenerEmpresa(productoDTO.getEmpresa().getNit());
		}else {
			empresa = usuario.getEmpresa();			
		}
		
		productoDTO.setEmpresa(empresa);
		Producto actualizado = productoService.update(id, productoDTO);
		
		return new ResponseEntity<Producto>(actualizado, HttpStatus.OK);
	}
	
	@PatchMapping("/{id}")
	@Operation(summary = "Verifica un activo", description = "Actualiza un activo por su id")
	public ResponseEntity<Producto> verify(@PathVariable(name = "id") String id,
										   @RequestBody(required=false) ProductoDTO productoDTO) {
		if(productoDTO == null) {
			throw new ResourceCannotBeAccessException("Falta informacion"); 
		}
		
		Empresa empresa;
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		Usuario usuario = usuarioRepo.findByNombreUsuarioOrEmail(authentication.getName(), authentication.getName()).get();
		
		if(productoDTO.getEmpresa() != null) {
			empresa = util.obtenerEmpresa(productoDTO.getEmpresa().getNit());
		}else {
			empresa = usuario.getEmpresa();			
		}
		
		productoDTO.setEmpresa(empresa);
		
		return new ResponseEntity<Producto>(productoService.receive(id, productoDTO), HttpStatus.ACCEPTED);
	}
	
	@Hidden
	@PostMapping("/cargar")
	@Operation(summary = "Carga de activos", description = "AUN PENDIENTE POR DEFINIR")
	public ResponseEntity<ResDTO> loadProducts(@RequestParam("archivo") MultipartFile file, WebRequest webRequest){ //@RequestBody List<ProductoDTO> list
		
		try {
			productoService.loadFile(file, webRequest);
		} catch (Exception e) {
			return new ResponseEntity<ResDTO>(new ResDTO("Error en la carga del archivo "+ e), HttpStatus.OK);
		}
		
		return new ResponseEntity<ResDTO>(new ResDTO("Se ha cargado la lista con exito"), HttpStatus.OK);
	}
	
	@DeleteMapping("/{id}")
	@Operation(summary = "Elimina un activo", description = "Elimina un activo por su id")
	public ResponseEntity<ResDTO> delete(@PathVariable(name="id")String codigoPieza){
		
		
		return new ResponseEntity<ResDTO>(new ResDTO(productoService.delete(codigoPieza)), HttpStatus.OK);
	}
	
	@PostMapping("/descarga")
	@Operation(summary = "Descarga listado en formato csv", description = "Descarga listado de activos de la busqueda realizada en formato csv")
	public void getCsvProducts(HttpServletResponse servletResponse,
								@RequestParam(required=false, defaultValue = "0") Integer pagina, 
								@RequestParam(required=false, defaultValue = "0") Integer items,
								@RequestParam(required=false) String letras,
								@RequestBody(required=false) SearchDTO searchDTO,
								@RequestParam(required=false) Long nit) throws IOException{
		Empresa empresa;
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		Usuario usuario = usuarioRepo.findByNombreUsuarioOrEmail(authentication.getName(), authentication.getName()).get();
		
		if(nit != null) {
			empresa = util.obtenerEmpresa(nit);
		}else {
			empresa = usuario.getEmpresa();			
		}
		servletResponse.setContentType("application/x-download");
        servletResponse.addHeader("Content-Disposition", "attachment;filename=\"" + "productos.csv" + "\"");
        
        
		if (searchDTO != null) {
			searchDTO.setEmpresa(empresa);
			List<Producto> productos =  productoService.searchProducts(searchDTO, empresa);
			csvService.writeProductsToCsv(servletResponse.getWriter(), productos);
		}else if(letras != null){
			List<Producto> productos = productoService.searchProducts(letras, empresa);
			csvService.writeProductsToCsv(servletResponse.getWriter(), productos);
		}else{
			List<Producto> productos = productoRepo.findAllByEmpresaAndEstaActivoTrue(empresa);
			csvService.writeProductsToCsv(servletResponse.getWriter(), productos);
		}
		
			 
        
	}
	
	@PostMapping("/reconversion")
	@Operation(summary = "Realiza la conversion de una pieza en una o varias mas", description = "Recibe un json con el codigo de la pieza padre y "
			+ "una lista de objetos tipo ProductoDTO con la informacion de las piezas hijas. Retorna una lista con las piezas creadas")
	public ResponseEntity<List<ProductoDTO>> reconversion(@RequestBody ReconversionDTO reconversion,
														  @RequestParam(required=false) Long nit){
		
		return new ResponseEntity<List<ProductoDTO>>(productoService.reconversionPieza(reconversion), HttpStatus.CREATED);
	}
}
