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

import com.prueba.dto.EstadoDTO;
import com.prueba.entity.Empresa;
import com.prueba.security.dto.ResDTO;
import com.prueba.security.entity.Usuario;
import com.prueba.security.repository.UsuarioRepository;
import com.prueba.service.EstadoService;
import com.prueba.util.CsvExportService;
import com.prueba.util.UtilitiesApi;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/estados")
@Api(tags = "Estado", description="Operaciones referentes al estado del activo")
public class EstadoController {

	@Autowired
	private EstadoService estadoService;
	
	@Autowired
	private CsvExportService csvService;
	
	@Autowired 
	private UsuarioRepository usuarioRepo;
	
	@Autowired
	private UtilitiesApi util;
	
	
	@PostMapping
	@ApiOperation(value = "Crea un estado para los activos de una empresa", notes = "Crea un nuevo estado para los activos")
	public ResponseEntity<EstadoDTO> create(@Valid @RequestBody EstadoDTO estadoDTO){
		return new ResponseEntity<EstadoDTO>(estadoService.create(estadoDTO), HttpStatus.CREATED);
	}
	
	@GetMapping
	@ApiOperation(value = "Encuentra los estados de los activos de una empresa", notes = "Retorna los estados que pueden tomar los activos que en su nombre contengan las letras indicadas, retorna todos los estados si no se especifica ninguna letra")
	public List<EstadoDTO> list(@RequestParam(required=false) String letras,
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
			return estadoService.findByTipoAndEmpresaAndEstaActivo(letras, empresa, true);
		}else {
			return estadoService.list(empresa);			
		}
	}
	
	@GetMapping("/{id},{nit}")
	@ApiOperation(value = "Encuentra un estado", notes = "Retorna un estado segun su id")
	public ResponseEntity<EstadoDTO> get(@PathVariable(name = "id") Long id,
										 @PathVariable(required=false) Long nit){
		Empresa empresa;
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		Usuario usuario = usuarioRepo.findByUsernameOrEmail(authentication.getName(), authentication.getName()).get();
		
		
		if(nit != null) {
			empresa = util.obtenerEmpresa(nit);
		}else {
			empresa = usuario.getEmpresa();			
		}
		return ResponseEntity.ok(estadoService.getEstado(id, empresa));
	}
	
	@PutMapping("/{id}")
	@ApiOperation(value = "Actualiza el estado de una empresa", notes = "Actualiza los datos de un estado")
	public ResponseEntity<EstadoDTO> update(@Valid @RequestBody EstadoDTO estadoDTO,
											@PathVariable Long id){
		EstadoDTO actualizado = estadoService.update(id, estadoDTO);
		
		return new ResponseEntity<>(actualizado, HttpStatus.OK);
	}
	
	@DeleteMapping("/{id}")
	@ApiOperation(value = "Elimina un estado de una empresa", notes = "Elimina un estado por su id")
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
		estadoService.delete(id, empresa);
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
		estadoService.unable(id, empresa);
		return new ResponseEntity<ResDTO>(new ResDTO("Estado inhabilitada con exito"), HttpStatus.OK);
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
			List<EstadoDTO> estados =  estadoService.findByTipoAndEmpresaAndEstaActivo(letras, empresa, true);
			csvService.writeEstadosToCsv(servletResponse.getWriter(), estados);
		}else{
			System.out.println("Controller busqueda vacia");
			List<EstadoDTO> estados = estadoService.list(empresa);
			csvService.writeEstadosToCsv(servletResponse.getWriter(), estados);
		}
		        
	}
}

