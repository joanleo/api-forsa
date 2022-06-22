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

import com.prueba.dto.FabricanteDTO;
import com.prueba.entity.Empresa;
import com.prueba.security.dto.ResDTO;
import com.prueba.security.entity.Usuario;
import com.prueba.security.repository.UsuarioRepository;
import com.prueba.service.FabricanteService;
import com.prueba.util.CsvExportService;
import com.prueba.util.UtilitiesApi;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/fabricantes")
@Api(tags = "Fabricantes", description = "Operaciones referentes a los fabricantes")
public class FabricanteController {

	@Autowired
	private FabricanteService fabricanteService;
	
	@Autowired
	private CsvExportService csvService;
	
	@Autowired
	private UsuarioRepository usuarioRepo;
	
	@Autowired
	private UtilitiesApi util;
	
	@PostMapping
	@ApiOperation(value = "Crea un fabricante", notes = "Crea un nuevo fabricante")
	public ResponseEntity<FabricanteDTO> create(@Valid @RequestBody FabricanteDTO fabricanteDTO){
		return new ResponseEntity<FabricanteDTO>(fabricanteService.create(fabricanteDTO), HttpStatus.CREATED);
	}
	
	@GetMapping
	@ApiOperation(value = "Encuenta los fabricantes", notes = "Retorna los fabricantes que en su nombre contengan las letrtas indicadas, retorna todos los fabricantes si no se indica ninguna letra")
	public List<FabricanteDTO> list(@RequestParam(required = false) String letras,
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
			return fabricanteService.findByNameAndEmpresaAndEstaActivo(letras, empresa, true);			
		}else {
			return fabricanteService.list(empresa);
		}
	}
	
	@GetMapping("/{id},{nit}")
	@ApiOperation(value = "Encuentra un fabricante", notes = "Retorna un fabricante por el id")
	public FabricanteDTO get(@PathVariable(name = "id") Long id,
			 				 @PathVariable(required=false) Long nit){
		Empresa empresa;
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		Usuario usuario = usuarioRepo.findByUsernameOrEmail(authentication.getName(), authentication.getName()).get();
		
		
		if(nit != null) {
		empresa = util.obtenerEmpresa(nit);
		}else {
		empresa = usuario.getEmpresa();			
		}
		return fabricanteService.getFabricante(id, empresa);
	}
	
	@PutMapping("/{ni}")
	@ApiOperation(value = "Actualiza un fabricante", notes = "Actualiza los datos de un fabricante")
	public ResponseEntity<FabricanteDTO> update(@Valid @RequestBody FabricanteDTO fabricanteDTO,
												@PathVariable Long nit){
		FabricanteDTO actualizado = fabricanteService.update(nit, fabricanteDTO);
		
		return new ResponseEntity<FabricanteDTO>(actualizado, HttpStatus.OK);
	}
	
	
	@DeleteMapping("/{id}")
	@ApiOperation(value = "Elimina un fabricante", notes = "Elimina un fabricante por su id")
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
		fabricanteService.delete(id, empresa); 
		return new ResponseEntity<ResDTO>(new ResDTO("Fabricante eliminado con exito"), HttpStatus.OK);
	}
	
	@PatchMapping("/{id}")
	@ApiOperation(value = "Inhabilita un fabricante", notes = "Inhabilita un fabricante por su id")
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
		fabricanteService.unable(id, empresa);
		return new ResponseEntity<ResDTO>(new ResDTO("Fabricante eliminado con exito"), HttpStatus.OK);
	}
	
	@PostMapping("/descarga")
	@ApiOperation(value = "Descarga listado en formato csv", notes = "Descarga listado de fabricantes de la busqueda realizada en formato csv")
	public void getCsvEmpresas(HttpServletResponse servletResponse,
								@RequestParam(required=false, defaultValue = "0") Integer pagina, 
								@RequestParam(required=false, defaultValue = "0") Integer items,
								@RequestParam(required=false) String letras,
								@RequestParam(required=false) Long nit) throws IOException {
		servletResponse.setContentType("application/x-download");
		DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
		String currentDateTime = dateFormatter.format(new Date());
        servletResponse.addHeader("Content-Disposition", "attachment;filename=\"" + "fabricantes"+ "_" + currentDateTime + ".csv" + "\"");
        
        Empresa empresa;
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		Usuario usuario = usuarioRepo.findByUsernameOrEmail(authentication.getName(), authentication.getName()).get();
		
		if(nit != null) {
			empresa = util.obtenerEmpresa(nit);
		}else {
			empresa = usuario.getEmpresa();			
		}
        		
        if(letras != null){
			List<FabricanteDTO> fabricantes =  fabricanteService.findByNameAndEmpresaAndEstaActivo(letras, empresa, true);
			csvService.writeFabricantesToCsv(servletResponse.getWriter(), fabricantes);
		}else{
			List<FabricanteDTO> fabricantes = fabricanteService.list(empresa);
			csvService.writeFabricantesToCsv(servletResponse.getWriter(), fabricantes);
		}
		
		
        
	}
}
