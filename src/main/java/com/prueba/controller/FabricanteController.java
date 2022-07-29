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
import com.prueba.dto.FabricanteDTO;
import com.prueba.entity.Empresa;
import com.prueba.entity.Fabricante;
import com.prueba.security.dto.ResDTO;
import com.prueba.security.entity.Usuario;
import com.prueba.security.repository.UsuarioRepository;
import com.prueba.service.FabricanteService;
import com.prueba.util.CsvExportService;
import com.prueba.util.UtilitiesApi;

import io.swagger.v3.oas.annotations.tags.Tag;




@RestController
@RequestMapping("/fabricantes")
//@Api(tags = "Fabricantes", description = "Operaciones referentes a los fabricantes")
@Tag(name = "Fabricantes", description = "Operaciones referentes a los fabricantes")
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
	//@ApiOperation(value = "Crea un fabricante", notes = "Crea un nuevo fabricante")
	public ResponseEntity<FabricanteDTO> create(@Valid @RequestBody FabricanteDTO fabricanteDTO){
		return new ResponseEntity<FabricanteDTO>(fabricanteService.create(fabricanteDTO), HttpStatus.CREATED);
	}
	
	@GetMapping
	//@ApiOperation(value="Encuentra los fabricantes")
	public List<FabricanteDTO> get(
			@RequestParam(required=false)String letras,
			@RequestParam(required=false) Long nit){
		
		Empresa empresa;
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		Usuario usuario = usuarioRepo.findByNombreUsuarioOrEmail(authentication.getName(), authentication.getName()).get();
		
		if(nit == null) {
			empresa = util.obtenerEmpresa(nit);
		}else {
			empresa = usuario.getEmpresa();
		}
		return  fabricanteService.findByNameAndEmpresaAndEstaActivo(letras, empresa);
	}
	
	@PostMapping("/indexados")
	//@ApiOperation(value = "Encuenta los fabricantes", notes = "Retorna los fabricantes que en su nombre contengan las letrtas indicadas, retorna todos los fabricantes si no se indica ninguna letra")
	public ApiResponse<Page<Fabricante>> paginationlist(
			@RequestParam(required=false, defaultValue = "0") Integer pagina, 
			@RequestParam(required=false, defaultValue = "0") Integer items,
			@RequestBody(required=false) FabricanteDTO fabricanteDTO,
			@RequestParam(required=false) Long nit){
		
		Empresa empresa;
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		Usuario usuario = usuarioRepo.findByNombreUsuarioOrEmail(authentication.getName(), authentication.getName()).get();
		
		if(nit != null) {
			empresa = util.obtenerEmpresa(nit);
		}else {
			empresa = usuario.getEmpresa();			
		}
		
		if(Objects.isNull(fabricanteDTO)) {
			Page<Fabricante> fabricantes = fabricanteService.searchFabricantes(empresa, pagina, items);
			return new ApiResponse<>(fabricantes.getSize(), fabricantes);
		}else {
			Page<Fabricante> fabricantes = fabricanteService.searchFabricantes(fabricanteDTO, empresa, pagina, items);
			return new ApiResponse<>(fabricantes.getSize(), fabricantes);
		}
	}
	
	@GetMapping("/{nitFabricante},{nitEmpresa}")
	//@ApiOperation(value = "Encuentra un fabricante", notes = "Retorna un fabricante por el id y la empresa a la que pertenece")
	public FabricanteDTO get(@PathVariable(name = "nitFabricante") Long nitFabricante,
			 				 @PathVariable(required=false) Long nitEmpresa){
		Empresa empresa;
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		Usuario usuario = usuarioRepo.findByNombreUsuarioOrEmail(authentication.getName(), authentication.getName()).get();
		
		
		if(nitEmpresa != null) {
		empresa = util.obtenerEmpresa(nitEmpresa);
		}else {
		empresa = usuario.getEmpresa();			
		}
		return fabricanteService.getFabricante(nitFabricante, empresa);
	}
	
	@PutMapping("/{nitFabricante}")
	//@ApiOperation(value = "Actualiza un fabricante", notes = "Actualiza los datos de un fabricante")
	public ResponseEntity<FabricanteDTO> update(@Valid @RequestBody FabricanteDTO fabricanteDTO,
												@PathVariable Long nitFabricante){
		FabricanteDTO actualizado = fabricanteService.update(nitFabricante, fabricanteDTO);
		
		return new ResponseEntity<FabricanteDTO>(actualizado, HttpStatus.OK);
	}
	
	
	@DeleteMapping("/{nitFabricante},{nitEmpresa}")
	//@ApiOperation(value = "Elimina un fabricante", notes = "Elimina un fabricante por su id")
	public ResponseEntity<ResDTO> delete(@PathVariable(name="nitFabricante")Long nitFabricante,
			 							 @PathVariable(required=false) Long nitEmpresa){
		Empresa empresa;
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		Usuario usuario = usuarioRepo.findByNombreUsuarioOrEmail(authentication.getName(), authentication.getName()).get();
		
		
		if(nitEmpresa != null) {
		empresa = util.obtenerEmpresa(nitEmpresa);
		}else {
		empresa = usuario.getEmpresa();			
}
		fabricanteService.delete(nitFabricante, empresa); 
		return new ResponseEntity<ResDTO>(new ResDTO("Fabricante eliminado con exito"), HttpStatus.OK);
	}
	
	@PatchMapping("/{nitFabricante},{nitEmpresa}")
	//@ApiOperation(value = "Inhabilita un fabricante", notes = "Inhabilita un fabricante por su id")
	public ResponseEntity<ResDTO> unable(@PathVariable(name="nitFabricante")Long nitFabricante,
				@PathVariable(required=false) Long nitEmpresa){
		Empresa empresa;
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		Usuario usuario = usuarioRepo.findByNombreUsuarioOrEmail(authentication.getName(), authentication.getName()).get();
		
		
		if(nitEmpresa != null) {
		empresa = util.obtenerEmpresa(nitEmpresa);
		}else {
		empresa = usuario.getEmpresa();			
		}
		fabricanteService.unable(nitFabricante, empresa);
		return new ResponseEntity<ResDTO>(new ResDTO("Fabricante inhabilitado con exito"), HttpStatus.OK);
	}
	
	@PostMapping("/descarga")
	//@ApiOperation(value = "Descarga listado en formato csv", notes = "Descarga listado de fabricantes de la busqueda realizada en formato csv")
	public void getCsvEmpresas(HttpServletResponse servletResponse,
								@RequestParam(required=false, defaultValue = "0") Integer pagina, 
								@RequestParam(required=false, defaultValue = "0") Integer items,
								@RequestBody(required=false) FabricanteDTO fabricanteDTO,
								@RequestParam(required=false) Long nit) throws IOException {
		servletResponse.setContentType("application/x-download");
		DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
		String currentDateTime = dateFormatter.format(new Date());
        servletResponse.addHeader("Content-Disposition", "attachment;filename=\"" + "fabricantes"+ "_" + currentDateTime + ".csv" + "\"");
        
        Empresa empresa;
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		Usuario usuario = usuarioRepo.findByNombreUsuarioOrEmail(authentication.getName(), authentication.getName()).get();
		
		if(nit != null) {
			empresa = util.obtenerEmpresa(nit);
		}else {
			empresa = usuario.getEmpresa();			
		}
        		
        if(Objects.isNull(fabricanteDTO)){
        	List<FabricanteDTO> fabricantes = fabricanteService.list(empresa);
        	csvService.writeFabricantesToCsv(servletResponse.getWriter(), fabricantes);
		}else{
			List<FabricanteDTO> fabricantes =  fabricanteService.listFabricantes(fabricanteDTO, empresa);
			csvService.writeFabricantesToCsv(servletResponse.getWriter(), fabricantes);
		}
		
		
        
	}
}
