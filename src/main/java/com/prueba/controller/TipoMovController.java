package com.prueba.controller;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

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

import com.prueba.dto.TipoMovDTO;
import com.prueba.entity.Empresa;
import com.prueba.security.dto.ResDTO;
import com.prueba.security.entity.Usuario;
import com.prueba.security.repository.UsuarioRepository;
import com.prueba.service.TipoMovService;
import com.prueba.util.CsvExportService;
import com.prueba.util.UtilitiesApi;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import springfox.documentation.annotations.ApiIgnore;

@RestController
@RequestMapping("/tiposmovimientos")
@Api(tags = "Tipos Movimiento", description = "operaciones referentes a los tipos de movimiento")
@ApiIgnore
public class TipoMovController {
	
	@Autowired
	private TipoMovService tipoMovService;
	
	@Autowired
	private CsvExportService csvService;
	
	@Autowired
	private UsuarioRepository usuarioRepo;
	
	@Autowired
	private UtilitiesApi util;
	
	@PostMapping
	@ApiOperation(value = "Crea un tipo de movimiento", notes = "Crea un nuevo tipo de movimiento")
	public ResponseEntity<TipoMovDTO> create(@Valid @RequestBody TipoMovDTO tipoMovDTO){
		return new ResponseEntity<TipoMovDTO>(tipoMovService.create(tipoMovDTO), HttpStatus.CREATED);
	}
	
	@GetMapping
	@ApiOperation(value = "Encuentra los tipos de movimientos", notes = "Retorna todos los tipos de movimiento")
	public List<TipoMovDTO> getTiposMov(@RequestParam(required=false) String letras,
										@RequestParam(required=false) Long nit){
		Empresa empresa;
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		Usuario usuario = usuarioRepo.findByUsernameOrEmail(authentication.getName(), authentication.getName()).get();
		
		
		if(nit != null) {
		empresa = util.obtenerEmpresa(nit);
		}else {
		empresa = usuario.getEmpresa();			
		}
		
		if(letras != null) {
			return tipoMovService.findByNombreAndEmpresaAndEstaActivo(letras, empresa, true);
		}else {
			return tipoMovService.list(empresa);			
		}
	}
	
	@GetMapping("/{id}")
	@ApiOperation(value = "Encuentra un tipo de movimiento", notes = "Retorna un tipo de movimiento segun su id")
	public ResponseEntity<TipoMovDTO> getTipoMov(@PathVariable(name="id") Long id,
					 							 @PathVariable(required=false) Long nit){
		Empresa empresa;
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		Usuario usuario = usuarioRepo.findByUsernameOrEmail(authentication.getName(), authentication.getName()).get();
		
		
		if(nit != null) {
		empresa = util.obtenerEmpresa(nit);
		}else {
		empresa = usuario.getEmpresa();			
		}
		return ResponseEntity.ok(tipoMovService.getTipoMov(id, empresa));
	}
	
	@PutMapping("/{id}")
	@ApiOperation(value = "Actualiza un tipo de movimiento", notes = "Actualiza los datos de un tipo de movimiento")
	public ResponseEntity<TipoMovDTO> update(@Valid @RequestBody TipoMovDTO tipoMovDTO,
											 @PathVariable Long id){
		TipoMovDTO actualizado = tipoMovService.update(id, tipoMovDTO);
		
		return new ResponseEntity<>(actualizado, HttpStatus.OK);
	}
	
	@DeleteMapping("/{id}")
	@ApiOperation(value = "Elimina un tipo de movimiento", notes = "Elimina un tipo de movimiento por su id")
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
		tipoMovService.delete(id, empresa);
		return new ResponseEntity<ResDTO>(new ResDTO("Tipo de movimiento eliminado con exito"), HttpStatus.OK);
	}
	
	@PatchMapping("/{id}")
	@ApiOperation(value = "Inhabilita un tipo de movimiento", notes = "Inhabilita un tipo de movimiento por su id")
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
		tipoMovService.unable(id, empresa);
		return new ResponseEntity<ResDTO>(new ResDTO("Tipo de movimiento inhabilitada con exito"), HttpStatus.OK);
	}
	
	@PostMapping("/descarga")
	@ApiOperation(value = "Descarga listado en formato csv", notes = "Descarga listado de activos de la busqueda realizada en formato csv")
	public void getCsvEmpresas(HttpServletResponse servletResponse,
								@RequestParam(required=false, defaultValue = "0") Integer pagina, 
								@RequestParam(required=false, defaultValue = "0") Integer items,
								@RequestParam(required=false) String letras) throws IOException {
		servletResponse.setContentType("application/x-download");
		DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
		String currentDateTime = dateFormatter.format(new Date());
        servletResponse.addHeader("Content-Disposition", "attachment;filename=\"" + "estados"+ "_" + currentDateTime + ".csv" + "\"");
        
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    	Usuario usuario = usuarioRepo.findByUsername(authentication.getName()).get();
    	Empresa empresa = usuario.getEmpresa();
      		
        if(letras != null){
			List<TipoMovDTO> estados =  tipoMovService.findByNombreAndEmpresaAndEstaActivo(letras, empresa, true);
			csvService.writeTiposMovToCsv(servletResponse.getWriter(), estados);
		}else{
			System.out.println("Controller busqueda vacia");
			List<TipoMovDTO> estados = tipoMovService.list(empresa);
			csvService.writeTiposMovToCsv(servletResponse.getWriter(), estados);
		}
		        
	}
	

}
