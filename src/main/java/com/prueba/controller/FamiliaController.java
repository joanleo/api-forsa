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

import com.prueba.dto.FamiliaDTO;
import com.prueba.entity.Empresa;
import com.prueba.security.dto.ResDTO;
import com.prueba.security.entity.Usuario;
import com.prueba.security.repository.UsuarioRepository;
import com.prueba.service.FamiliaService;
import com.prueba.util.CsvExportService;
import com.prueba.util.UtilitiesApi;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/familias")
@Api(tags = "Familias", description = "Operaciones referentes a las familias")
public class FamiliaController {

	@Autowired
	private FamiliaService familiaService;
	
	@Autowired
	private CsvExportService csvService;
	
	@Autowired
	private UsuarioRepository usuarioRepo;
	
	@Autowired
	private UtilitiesApi util;
	
	@PostMapping
	@ApiOperation(value = "Crea una familia", notes = "Crea una nueva familia")
	public ResponseEntity<FamiliaDTO> create(@Valid @RequestBody FamiliaDTO familiaDTO,
			 								 @RequestParam(required=false) Long nit){
		Empresa empresa;
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		Usuario usuario = usuarioRepo.findByUsernameOrEmail(authentication.getName(), authentication.getName()).get();
		
		if(nit != null) {
			empresa = util.obtenerEmpresa(nit);
		}else {
			empresa = usuario.getEmpresa();			
		}
		return new ResponseEntity<FamiliaDTO>(familiaService.create(familiaDTO, empresa), HttpStatus.CREATED);
	}
	
	@PutMapping("/{id}")
	@ApiOperation(value = "Actualiza una familia", notes = "Actualiza los datos de una familia")
	public ResponseEntity<FamiliaDTO> update(@Valid @RequestBody FamiliaDTO familiaDTO,
											@PathVariable Long id){
		FamiliaDTO actualizada = familiaService.update(id, familiaDTO);
		
		return new ResponseEntity<>(actualizada, HttpStatus.OK);
	}
	
	
	@GetMapping
	@ApiOperation(value = "Encuentra las familias", notes = "Retorna las familias que en su nombre contengan las letrtas indicadas, retorna todas las familias si no se indica ninguna letra")
	public List<FamiliaDTO> list(@RequestParam(required=false) String letras,
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
			return familiaService.findByNameAndEmpreaAndEstaActiva(letras, empresa, true);
		}else {
			return familiaService.list(empresa);			
		}
	}
	
	@GetMapping("/{id},{nit}")
	@ApiOperation(value = "Encuentra una familia", notes = "Retorna una familia por el id")
	public ResponseEntity<FamiliaDTO> get(@PathVariable(name = "id") Long id,
			 							  @PathVariable(required=false) Long nit){
		Empresa empresa;
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		Usuario usuario = usuarioRepo.findByUsernameOrEmail(authentication.getName(), authentication.getName()).get();
		
		
		if(nit != null) {
		empresa = util.obtenerEmpresa(nit);
		}else {
		empresa = usuario.getEmpresa();			
		}
		return ResponseEntity.ok(familiaService.getFamilia(id, empresa));
	}
	
	@DeleteMapping("/{id}")
	@ApiOperation(value = "Elimina un estado", notes = "Elimina un estado por su id")
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
		
		familiaService.delete(id, empresa);
		return new ResponseEntity<ResDTO>(new ResDTO("Estado eliminada con exito"), HttpStatus.OK);
	}
	
	@PatchMapping("/{id}")
	@ApiOperation(value = "Inhabilita un estado", notes = "Inhabilita un estado por su id")
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
		
		familiaService.unable(id, empresa);
		return new ResponseEntity<ResDTO>(new ResDTO("Estado inhabilitada con exito"), HttpStatus.OK);
	}
	
	@PostMapping("/descarga")
	@ApiOperation(value = "Descarga listado en formato csv", notes = "Descarga listado de familias de la busqueda realizada en formato csv")
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
		Usuario usuario = usuarioRepo.findByUsernameOrEmail(authentication.getName(), authentication.getName()).get();
		
		if(nit != null) {
			empresa = util.obtenerEmpresa(nit);
		}else {
			empresa = usuario.getEmpresa();			
		}
      		
        if(letras != null){
			List<FamiliaDTO> familias =  familiaService.findByNameAndEmpreaAndEstaActiva(letras, empresa, true);
			csvService.writeFamiliasToCsv(servletResponse.getWriter(), familias);
		}else{
			List<FamiliaDTO> familias = familiaService.list(empresa);
			csvService.writeFamiliasToCsv(servletResponse.getWriter(), familias);
		}
		        
	}
}

