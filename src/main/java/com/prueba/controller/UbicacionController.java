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
import com.prueba.dto.UbicacionDTO;
import com.prueba.entity.Empresa;
import com.prueba.entity.Ubicacion;
import com.prueba.security.dto.ResDTO;
import com.prueba.security.entity.Usuario;
import com.prueba.security.repository.UsuarioRepository;
import com.prueba.service.UbicacionService;
import com.prueba.util.CsvExportService;
import com.prueba.util.UtilitiesApi;

//import io.swagger.annotations.Api;
//import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/ubicaciones")
//@Api(tags = "Ubicaciones", description = "Operaciones referentes a las ubicaciones")
public class UbicacionController {

	@Autowired
	private UbicacionService ubicacionService;
	
	@Autowired
	private CsvExportService csvService;
	
	@Autowired
	private UsuarioRepository usuarioRepo;
	
	@Autowired
	private UtilitiesApi util;
	
	@PostMapping
	//@ApiOperation(value = "Crea una ubicacion", notes = "Crea una nueva ubicacion")
	public ResponseEntity<UbicacionDTO> create(@Valid @RequestBody UbicacionDTO ubicacionDTO){
		return new ResponseEntity<UbicacionDTO>(ubicacionService.create(ubicacionDTO), HttpStatus.CREATED);
	}
	
	@GetMapping
	//@ApiOperation(value="Encuentra las ubicaciones")
	public List<UbicacionDTO> get(
			@RequestParam(required=false)String letras,
			@RequestParam(required=false) Long nit){
		System.out.println("controller");
		Empresa empresa;
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		Usuario usuario = usuarioRepo.findByNombreUsuarioOrEmail(authentication.getName(), authentication.getName()).get();
		
		if(nit != null) {
			empresa = util.obtenerEmpresa(nit);
		}else {
			empresa = usuario.getEmpresa();
		}
		if(letras == null ) {
			return ubicacionService.list(empresa);
		}
		return  ubicacionService.findByName(letras, empresa);
	}
	
	@PostMapping("/indexados")
	//@ApiOperation(value = "Encuentra las ubicaciones", notes = "Retorna las ubicaciones que en su nombre contenga las letras indicadas, retorna todas las ubicaciones si no se especifica ninguna letra")
	public ApiResponse<Page<Ubicacion>> paginationList(
			@RequestParam(required=false, defaultValue = "0") Integer pagina, 
			@RequestParam(required=false, defaultValue = "0") Integer items,
			@RequestBody(required=false) UbicacionDTO ubicacionDTO,
			@RequestParam(required=false) Long nit){
		
		Empresa empresa;
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		Usuario usuario = usuarioRepo.findByNombreUsuarioOrEmail(authentication.getName(), authentication.getName()).get();
		
		
		if(nit != null) {
		empresa = util.obtenerEmpresa(nit);
		}else {
		empresa = usuario.getEmpresa();			
		}
		
		if(Objects.isNull(ubicacionDTO)) {
			Page<Ubicacion> ubicaciones = ubicacionService.searchUbicaciones(empresa, pagina, items);
			return new ApiResponse<>(ubicaciones.getSize(), ubicaciones);
		}else {
			Page<Ubicacion> ubicaciones = ubicacionService.searchUbicaciones(ubicacionDTO, empresa, pagina, items);
			return new ApiResponse<>(ubicaciones.getSize(), ubicaciones);
		}
		
	}
	
	@GetMapping("/{id},{nitEmpresa}")
	//@ApiOperation(value = "Encuentra una ubicacion", notes = "Retorna una ubicacion por su id")
	public ResponseEntity<UbicacionDTO> getUbicacion(@PathVariable(name = "id") Long id,
					 								 @PathVariable(required=false) Long nitEmpresa){
		Empresa empresa;
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		Usuario usuario = usuarioRepo.findByNombreUsuarioOrEmail(authentication.getName(), authentication.getName()).get();
		
		
		if(nitEmpresa != null) {
		empresa = util.obtenerEmpresa(nitEmpresa);
		}else {
		empresa = usuario.getEmpresa();			
		}
		
		return ResponseEntity.ok(ubicacionService.getUbicacion(id, empresa));
	}
	
	@PutMapping("/{id}")
	//@ApiOperation(value = "Actualiza una ubicacion", notes = "Actualiza los datos de una ubicacion")
	public ResponseEntity<UbicacionDTO> update(@Valid @RequestBody UbicacionDTO ubicacionDTO,
											@PathVariable Long id){
		UbicacionDTO actualizado = ubicacionService.update(id, ubicacionDTO);
		
		return new ResponseEntity<>(actualizado, HttpStatus.OK);
	}
	
	@DeleteMapping("/{id},{nitEmpresa}")
	//@ApiOperation(value = "Elimina una ubicacion", notes = "Elimina una ubicacion por su id")
 	public ResponseEntity<ResDTO> delete(@PathVariable(name="id")Long id,
										 @PathVariable(required=false) Long nitEmpresa){
		Empresa empresa;
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		Usuario usuario = usuarioRepo.findByNombreUsuarioOrEmail(authentication.getName(), authentication.getName()).get();
		
		
		if(nitEmpresa != null) {
		empresa = util.obtenerEmpresa(nitEmpresa);
		}else {
		empresa = usuario.getEmpresa();			
		}
		ubicacionService.delete(id, empresa);
		return new ResponseEntity<ResDTO>(new ResDTO("Ubicacion eliminada con exito"), HttpStatus.OK);
	}
	
	@PatchMapping("/{id},{nitEmpresa}")
	//@ApiOperation(value = "Inhabilita una ubicacion", notes = "Inhabilita una ubicacion por su id")
	public ResponseEntity<ResDTO> unable(@PathVariable(name="id")Long id,
	 									@PathVariable(required=false) Long nitEmpresa){
		Empresa empresa;
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		Usuario usuario = usuarioRepo.findByNombreUsuarioOrEmail(authentication.getName(), authentication.getName()).get();
		
		
		if(nitEmpresa != null) {
		empresa = util.obtenerEmpresa(nitEmpresa);
		}else {
		empresa = usuario.getEmpresa();			
		}
		
		ubicacionService.unable(id, empresa);
		
		return new ResponseEntity<ResDTO>(new ResDTO("Ubicacion inhabilitada con exito"), HttpStatus.OK);
	}
	
	@PostMapping("/descarga")
	//@ApiOperation(value = "Descarga listado en formato csv", notes = "Descarga listado de activos de la busqueda realizada en formato csv")
	public void getCsvUbicaciones(HttpServletResponse servletResponse,
				@RequestParam(required=false, defaultValue = "0") Integer pagina, 
				@RequestParam(required=false, defaultValue = "0") Integer items,
				@RequestBody(required=false) UbicacionDTO ubicacionDTO,
				@RequestParam(required=false) Long nit) throws IOException {
		servletResponse.setContentType("application/x-download");
		DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
		String currentDateTime = dateFormatter.format(new Date());
        servletResponse.addHeader("Content-Disposition", "attachment;filename=\"" + "estados"+ "_" + currentDateTime + ".csv" + "\"");
        
        Empresa empresa;
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		Usuario usuario = usuarioRepo.findByNombreUsuarioOrEmail(authentication.getName(), authentication.getName()).get();
		
		if(nit != null) {
			empresa = util.obtenerEmpresa(nit);
		}else {
			empresa = usuario.getEmpresa();			
		}
      		
        if(Objects.isNull(ubicacionDTO)){
        	List<UbicacionDTO> ubicaciones = ubicacionService.list(empresa);
        	csvService.writeUbicacionesToCsv(servletResponse.getWriter(), ubicaciones);
		}else{
			List<UbicacionDTO> ubicaciones =  ubicacionService.listUbicaciones(ubicacionDTO, empresa); 
			csvService.writeUbicacionesToCsv(servletResponse.getWriter(), ubicaciones);
		}
		        
	}
	
}
