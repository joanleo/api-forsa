package com.prueba.controller;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
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

import com.prueba.entity.Empresa;
import com.prueba.entity.TipoActivo;
import com.prueba.security.dto.ResDTO;
import com.prueba.security.entity.Usuario;
import com.prueba.security.repository.UsuarioRepository;
import com.prueba.service.TipoActivoService;
import com.prueba.util.CsvExportService;
import com.prueba.util.UtilitiesApi;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;




@RestController
@RequestMapping("/tipos")
@Tag(name = "Tipos", description = "Operaciones referentes a los tipos de activos")
public class TipoController {
	
	@Autowired
	private TipoActivoService tipoActivoService;
	
	@Autowired
	private CsvExportService csvService;
	
	@Autowired
	private UsuarioRepository usuarioRepo;
	
	@Autowired
	private UtilitiesApi util;
	
	@PostMapping
	@Operation(summary = "Crea un tipo de activo", description = "Crea un nuevo tipo de activo")
	public ResponseEntity<TipoActivo> create(@RequestBody TipoActivo tipoActivo,
					 						 @RequestParam(required=false) Long nit){
		Empresa empresa;
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		Usuario usuario = usuarioRepo.findByNombreUsuarioOrEmail(authentication.getName(), authentication.getName()).get();
		
		if(nit != null) {
		empresa = util.obtenerEmpresa(nit);
		}else {
		empresa = usuario.getEmpresa();			
		}
		return new ResponseEntity<TipoActivo>(tipoActivoService.create(tipoActivo, empresa), HttpStatus.CREATED);
	}
	
	@GetMapping
	@Operation(summary = "Obtiene una lista de tipos de activos")
	public List<TipoActivo> list(@RequestParam(required=false) String letras,
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
			return tipoActivoService.findByNameAndEmpreaAndEstaActivo(letras, empresa);
		}else {
			return tipoActivoService.list(empresa);			
		}
	}
	
	@GetMapping("/{id},{nit}")
	@Operation(summary = "Encuentra una tipo de activo", description = "Retorna una tipo de activo por el id")
	public ResponseEntity<TipoActivo> get(@PathVariable(name = "id") Long id,
			 							  @PathVariable(required=false) Long nit){
		Empresa empresa;
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		Usuario usuario = usuarioRepo.findByNombreUsuarioOrEmail(authentication.getName(), authentication.getName()).get();
		
		
		if(nit != null) {
			empresa = util.obtenerEmpresa(nit);
		}else {
			empresa = usuario.getEmpresa();			
		}
		return ResponseEntity.ok(tipoActivoService.getFamilia(id, empresa));
	}
	
	@PutMapping("/{id}")
	@Operation(summary = "Actualiza una familia", description = "Actualiza los datos de una familia")
	public ResponseEntity<TipoActivo> update(@RequestBody TipoActivo tipoActivo,
											 @PathVariable Long id,
											 @RequestParam(required=false) Long nit){
		Empresa empresa;
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		Usuario usuario = usuarioRepo.findByNombreUsuarioOrEmail(authentication.getName(), authentication.getName()).get();
		
		if(nit != null) {
			empresa = util.obtenerEmpresa(nit);
		}else {
			empresa = usuario.getEmpresa();			
		}
		
		TipoActivo actualizada = tipoActivoService.update(id, tipoActivo, empresa);
		
		return new ResponseEntity<>(actualizada, HttpStatus.OK);
	}

	@DeleteMapping("/{id}")
	@Operation(summary = "Elimina un tipo de activo", description = "Elimina un tipo de activo por su id")
	public ResponseEntity<ResDTO> delete(@PathVariable(name="id")Long id,
					 					 @PathVariable(required=false) Long nit){
		Empresa empresa;
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		Usuario usuario = usuarioRepo.findByNombreUsuarioOrEmail(authentication.getName(), authentication.getName()).get();
		
		
		if(nit != null) {
			empresa = util.obtenerEmpresa(nit);
		}else {
			empresa = usuario.getEmpresa();			
		}
		
		tipoActivoService.delete(id, empresa);
		return new ResponseEntity<ResDTO>(new ResDTO("Tipo de activo eliminada con exito"), HttpStatus.OK);
	}
	
	@PatchMapping("/{id}")
	@Operation(summary = "Inhabilita un tipo de activo", description = "Inhabilita un tipo de activo por su id")
	public ResponseEntity<ResDTO> unable(@PathVariable(name="id")Long id,
					 					 @PathVariable(required=false) Long nit){
		Empresa empresa;
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		Usuario usuario = usuarioRepo.findByNombreUsuarioOrEmail(authentication.getName(), authentication.getName()).get();
		
		
		if(nit != null) {
			empresa = util.obtenerEmpresa(nit);
		}else {
			empresa = usuario.getEmpresa();			
		}
		
		tipoActivoService.unable(id, empresa);
		return new ResponseEntity<ResDTO>(new ResDTO("Tipo de activo inhabilitada con exito"), HttpStatus.OK);
	}
	
	@PostMapping("/descarga")
	@Operation(summary = "Descarga listado en formato csv", description = "Descarga listado de familias de la busqueda realizada en formato csv")
	public void getCsvEmpresas(HttpServletResponse servletResponse,
								@RequestParam(required=false, defaultValue = "0") Integer pagina, 
								@RequestParam(required=false, defaultValue = "0") Integer items,
								@RequestParam(required=false) String letras,
								@RequestParam(required=false) Long nit) throws IOException {
		servletResponse.setContentType("application/x-download");
		DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
		String currentDateTime = dateFormatter.format(new Date());
        servletResponse.addHeader("Content-Disposition", "attachment;filename=\"" + "familias"+ "_" + currentDateTime + ".csv" + "\"");
        
        Empresa empresa;
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		Usuario usuario = usuarioRepo.findByNombreUsuarioOrEmail(authentication.getName(), authentication.getName()).get();
		
		if(nit != null) {
			empresa = util.obtenerEmpresa(nit);
		}else {
			empresa = usuario.getEmpresa();			
		}
      		
        if(letras != null){
			List<TipoActivo> tiposActivos =  tipoActivoService.findByNameAndEmpreaAndEstaActivo(letras, empresa);
			csvService.writeTiposActivoToCsv(servletResponse.getWriter(), tiposActivos);
		}else{
			List<TipoActivo> tiposActivos = tipoActivoService.list(empresa);
			csvService.writeTiposActivoToCsv(servletResponse.getWriter(), tiposActivos);
		}
		        
	}
	

}
