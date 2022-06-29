package com.prueba.controller;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
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

import com.prueba.dto.ApiResponse;
import com.prueba.dto.TipoUbicacionDTO;
import com.prueba.entity.Empresa;
import com.prueba.entity.TipoUbicacion;
import com.prueba.security.dto.ResDTO;
import com.prueba.security.entity.Usuario;
import com.prueba.security.repository.UsuarioRepository;
import com.prueba.service.TipoUbicacionService;
import com.prueba.util.CsvExportService;
import com.prueba.util.UtilitiesApi;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/tiposubicaciones")
@Api(tags = "Tipos ubicaciones", description = "Operaciones referentes a los tipos de ubicaciones")
public class TipoUbicacionController {
	
	@Autowired
	private TipoUbicacionService tipoUbicService;
	
	@Autowired
	private CsvExportService csvService;
	
	@Autowired
	private UsuarioRepository usuarioRepo;
	
	@Autowired
	private UtilitiesApi util;
	
	@PostMapping
	@ApiOperation(value = "Crea un tipo de ubicacion", notes = "Crea un nuevo tipo de ubicacion")
	public ResponseEntity<TipoUbicacionDTO> create(@Valid @RequestBody TipoUbicacionDTO tipoUbicacionDTO){
		return new ResponseEntity<TipoUbicacionDTO>(tipoUbicService.create(tipoUbicacionDTO), HttpStatus.CREATED); 
	}
	
	@PostMapping("/indexados")
	@ApiOperation(value = "Encuentra los tipos de ubicacion", notes = "Retorna todos los tipos de ubicacion")
	public ApiResponse<Page<TipoUbicacion>> paginationlist(
			@RequestParam(required=false, defaultValue = "0") Integer pagina, 
			@RequestParam(required=false, defaultValue = "0") Integer items,
			@RequestBody(required=false) TipoUbicacionDTO tipoUbicacionDTO,
			@RequestParam(required=false) Long nit){
		Empresa empresa;
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		Usuario usuario = usuarioRepo.findByUsernameOrEmail(authentication.getName(), authentication.getName()).get();
		
		
		if(nit != null) {
		empresa = util.obtenerEmpresa(nit);
		}else {
		empresa = usuario.getEmpresa();			
		}
		
		if(Objects.isNull(tipoUbicacionDTO)) {
			Page<TipoUbicacion> tipoUbicacion = tipoUbicService.searchTipoUbicacion(empresa, pagina, items);
			return new ApiResponse<>(tipoUbicacion.getSize(), tipoUbicacion);
		}else {
			Page<TipoUbicacion> tipoUbicacion = tipoUbicService.searchTipoUbicacion(tipoUbicacionDTO, empresa, pagina, items);
			return new ApiResponse<>(tipoUbicacion.getSize(), tipoUbicacion);
		}
	}

	@GetMapping("/{id},{nit}")
	@ApiOperation(value = "Encuentra un tipo de ubicacion", notes = "Retorna un tipo de ubicacion segun su id y empresa")
	public ResponseEntity<TipoUbicacionDTO> getTipoUbic(@PathVariable(name="id") Long id,
					 									@PathVariable(required=false) Long nit){
		Empresa empresa;
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		Usuario usuario = usuarioRepo.findByUsernameOrEmail(authentication.getName(), authentication.getName()).get();
		
		
		if(nit != null) {
		empresa = util.obtenerEmpresa(nit);
		}else {
		empresa = usuario.getEmpresa();			
		}
		return ResponseEntity.ok(tipoUbicService.getTipoMov(id, empresa));
	}
	
	@PutMapping("/{id}")
	@ApiOperation(value = "Actualiza un tipo de ubicacion", notes = "Actualiza los datos de un tipo de ubicacion")
	public ResponseEntity<TipoUbicacionDTO> update(@Valid @RequestBody TipoUbicacionDTO tipoUbicacionDTO,
											 @PathVariable Long id){
		TipoUbicacionDTO actualizado = tipoUbicService.update(id, tipoUbicacionDTO);
		
		return new ResponseEntity<>(actualizado, HttpStatus.OK);
	}
	
	@DeleteMapping("/{id},{nit}")
	@ApiOperation(value = "Elimina un tipo de ubicacion", notes = "Elimina un tipo de ubicacion por su id")
 	public ResponseEntity<ResDTO> delete(@PathVariable(name="id")Long id,
										 @PathVariable(required=false) Long nit){
		Empresa empresa;
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		Usuario usuario = usuarioRepo.findByUsernameOrEmail(authentication.getName(), authentication.getName()).get();
		
		
		if(nit != null) {
		empresa = util.obtenerEmpresa(nit);
		}else {
		empresa = usuario.getEmpresa();			
		}
		tipoUbicService.delete(id, empresa);
		return new ResponseEntity<ResDTO>(new ResDTO("Tipo de ubicacion eliminada con exito"), HttpStatus.OK);
	}
	
	@PatchMapping("/{id},{nit}")
	@ApiOperation(value = "Inhabilita un tipo de ubicacion", notes = "Inhabilita un tipo de ubicacion por su id")
	public ResponseEntity<ResDTO> unable(@PathVariable(name="id")Long id,
	 									@PathVariable(required=false) Long nit){
		Empresa empresa;
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		Usuario usuario = usuarioRepo.findByUsernameOrEmail(authentication.getName(), authentication.getName()).get();
		
		
		if(nit != null) {
		empresa = util.obtenerEmpresa(nit);
		}else {
		empresa = usuario.getEmpresa();			
		}
		tipoUbicService.unable(id, empresa);
		return new ResponseEntity<ResDTO>(new ResDTO("Tipo de movimiento ubicacion con exito"), HttpStatus.OK);
	}
	
	@PostMapping("/descarga")
	@ApiOperation(value = "Descarga listado en formato csv", notes = "Descarga listado de Tipo de movimiento de la busqueda realizada en formato csv")
	public void getCsvEmpresas(HttpServletResponse servletResponse,
								@RequestParam(required=false, defaultValue = "0") Integer pagina, 
								@RequestParam(required=false, defaultValue = "0") Integer items,
								@RequestBody(required=false) TipoUbicacionDTO tipoUbicacionDTO,
								@RequestParam(required=false) Long nit) throws IOException {
		servletResponse.setContentType("application/x-download");
		DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
		String currentDateTime = dateFormatter.format(new Date());
        servletResponse.addHeader("Content-Disposition", "attachment;filename=\"" + "estados"+ "_" + currentDateTime + ".csv" + "\"");
        
        Empresa empresa;
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		Usuario usuario = usuarioRepo.findByUsernameOrEmail(authentication.getName(), authentication.getName()).get();
		
		if(nit != null) {
			empresa = util.obtenerEmpresa(nit);
		}else {
			empresa = usuario.getEmpresa();			
		}
      		
        if(Objects.isNull(tipoUbicacionDTO)){
        	System.out.println("Controller busqueda vacia");
        	List<TipoUbicacionDTO> tiposUbic = tipoUbicService.list(empresa);
        	csvService.writeTiposUbiToCsv(servletResponse.getWriter(), tiposUbic);
		}else{
			List<TipoUbicacionDTO> tiposUbic =  tipoUbicService.listTipoUbicacion(tipoUbicacionDTO, empresa, true);
			csvService.writeTiposUbiToCsv(servletResponse.getWriter(), tiposUbic);
		}
		        
	}
}
