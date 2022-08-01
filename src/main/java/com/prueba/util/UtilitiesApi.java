package com.prueba.util;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.Map.Entry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Service;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import com.prueba.entity.Empresa;
import com.prueba.entity.Permiso;
import com.prueba.entity.Rutina;
import com.prueba.exception.ResourceNotFoundException;
import com.prueba.repository.EmpresaRepository;
import com.prueba.repository.PermisoRepository;
import com.prueba.repository.RutinaRepository;


@Service
public class UtilitiesApi {
	
	@Autowired
	private EmpresaRepository empresaRepo;
	
	@Autowired
	private PermisoRepository permisoRepo;
	
	@Autowired
	private RutinaRepository rutinaRepo;

	public Empresa obtenerEmpresa(Long nit) {
		Empresa exist = empresaRepo.findByNit(nit);
		if (exist != null) {
			return exist;
		} else {
			throw new ResourceNotFoundException("Empresa", "nit", nit);
		}
	}
	
	public void crearRutinasBD(ContextRefreshedEvent event) {
		ApplicationContext applicationContext = event.getApplicationContext();
        RequestMappingHandlerMapping requestMappingHandlerMapping = applicationContext
          .getBean("requestMappingHandlerMapping", RequestMappingHandlerMapping.class);
        Map<RequestMappingInfo, HandlerMethod> map = requestMappingHandlerMapping.getHandlerMethods();

        String aux="";
        
        Set<String> arrRutinas = new HashSet<String>();

        for(Entry<RequestMappingInfo, HandlerMethod> rutaMetodo: map.entrySet()) {
        	String rutina = rutaMetodo.getKey().getActivePatternsCondition().toString().replace("[", "").replace("]", "").split("/")[1];
        	if(rutina.equalsIgnoreCase("error") || rutina.equalsIgnoreCase("v3") || rutina.equalsIgnoreCase("swagger-ui.html") || 
        			rutina.equalsIgnoreCase("auth") || rutina.equalsIgnoreCase("email")) {
        		continue;
        	}
        	if(rutina != aux) {
        		aux = rutina;
        		arrRutinas.add(aux);
        	}
        }
        
        for(String rut: arrRutinas) {
        	Rutina nuevaRutina = new Rutina();
        	nuevaRutina.setNombre(rut);
	        for(Entry<RequestMappingInfo, HandlerMethod> rutaMetodo: map.entrySet()) {
	        	String rutina = rutaMetodo.getKey().getActivePatternsCondition().toString().replace("[", "").replace("]", "").split("/")[1];
	        	String ruta = rutaMetodo.getKey().getActivePatternsCondition().toString().replace("[", "").replace("]", "");
	        	String metodo = rutaMetodo.getKey().getMethodsCondition().toString().replace("[", "").replace("]", "");
	        	
            	if(rut.equals(rutina)) {
            		
            		Permiso nuevoPermiso = new Permiso();
            		if(metodo.equals("PUT")) {
            			Permiso existPermiso = permisoRepo.findByNombre("editar_"+rut);
            			if(Objects.isNull(existPermiso)) {
            				nuevoPermiso.addUrl(ruta);
            				nuevoPermiso.setNombre("editar"+rut);
            				nuevoPermiso.setMetodo(metodo);
            			}else {
            				nuevoPermiso = existPermiso;
            			}
            			nuevoPermiso = permisoRepo.save(nuevoPermiso);
            			Rutina existRutina = rutinaRepo.findByNombre(rut);
            			if(Objects.isNull(existRutina)) {
            				nuevaRutina.addRuta(nuevoPermiso);
            			}else {
            				nuevaRutina = existRutina;
            				nuevaRutina.addRuta(nuevoPermiso);
            			}
            		}
            		if(ruta.contains("cargar")) {
            			Permiso existPermiso = permisoRepo.findByNombre("importar"+rut);
            			if(Objects.isNull(existPermiso)) {
            				nuevoPermiso.addUrl(ruta);
            				nuevoPermiso.setNombre("importar"+rut);
            				nuevoPermiso.setMetodo(metodo);
            			}else {
            				nuevoPermiso = existPermiso;
            			}
            			nuevoPermiso = permisoRepo.save(nuevoPermiso);
            			Rutina existRutina = rutinaRepo.findByNombre(rut);
            			if(Objects.isNull(existRutina)) {
            				nuevaRutina.addRuta(nuevoPermiso);
            			}else {
            				nuevaRutina = existRutina;
            				nuevaRutina.addRuta(nuevoPermiso);
            			}
            			
            		}
            		if(ruta.contains("descarga")) {
            			Permiso existPermiso = permisoRepo.findByNombre("exportar"+rut);
            			if(Objects.isNull(existPermiso)) {
            				nuevoPermiso.addUrl(ruta);
            				nuevoPermiso.setNombre("exportar"+rut);
            				nuevoPermiso.setMetodo(metodo);
            			}else {
            				nuevoPermiso = existPermiso;
            			}
            			nuevoPermiso = permisoRepo.save(nuevoPermiso);
            			Rutina existRutina = rutinaRepo.findByNombre(rut);
            			if(Objects.isNull(existRutina)) {
            				nuevaRutina.addRuta(nuevoPermiso);
            			}else {
            				nuevaRutina = existRutina;
            				nuevaRutina.addRuta(nuevoPermiso);
            			}
            		}
            		if(metodo.equals("DELETE")) {
            			Permiso existPermiso = permisoRepo.findByNombre("eliminar"+rut);
            			if(Objects.isNull(existPermiso)) {
            				nuevoPermiso.addUrl(ruta);
            				nuevoPermiso.setNombre("eliminar"+rut);
            				nuevoPermiso.setMetodo(metodo);
            			}else {
            				nuevoPermiso = existPermiso;
            			}
            			nuevoPermiso = permisoRepo.save(nuevoPermiso);
            			Rutina existRutina = rutinaRepo.findByNombre(rut);
            			if(Objects.isNull(existRutina)) {
            				nuevaRutina.addRuta(nuevoPermiso);
            			}else {
            				nuevaRutina = existRutina;
            				nuevaRutina.addRuta(nuevoPermiso);
            			}
            		}
            		if(metodo.equals("PATCH")) {
            			Permiso existPermiso = permisoRepo.findByNombre("inhabilitar"+rut);
            			if(Objects.isNull(existPermiso)) {
            				nuevoPermiso.addUrl(ruta);
            				nuevoPermiso.setNombre("inhabilitar"+rut);
            				nuevoPermiso.setMetodo(metodo);
            			}else {
            				nuevoPermiso = existPermiso;
            			}
            			nuevoPermiso = permisoRepo.save(nuevoPermiso);
            			Rutina existRutina = rutinaRepo.findByNombre(rut);
            			if(Objects.isNull(existRutina)) {
            				nuevaRutina.addRuta(nuevoPermiso);
            			}else {
            				nuevaRutina = existRutina;
            				nuevaRutina.addRuta(nuevoPermiso);
            			}
            		}
            		if(metodo.equals("GET") && (!ruta.contains("id") || !ruta.contains("nit"))) {
            			Permiso existPermiso = permisoRepo.findByNombre("consultar"+rut);
            			if(Objects.isNull(existPermiso)) {
            				nuevoPermiso.addUrl(ruta);
            				nuevoPermiso.setNombre("consultar"+rut);
            				nuevoPermiso.setMetodo(metodo);
            			}else {
            				nuevoPermiso = existPermiso;
            				nuevoPermiso.addUrl(ruta);
            			}
            			nuevoPermiso = permisoRepo.save(nuevoPermiso);
            			Rutina existRutina = rutinaRepo.findByNombre(rut);
            			if(Objects.isNull(existRutina)) {
            				nuevaRutina.addRuta(nuevoPermiso);
            			}else {
            				nuevaRutina = existRutina;
            				nuevaRutina.addRuta(nuevoPermiso);
            			}
            		}
            		if((ruta.contains("id") || ruta.contains("nit")) && metodo.equals("GET"+rut)){
            			Permiso existPermiso = permisoRepo.findByNombre("consultar");
            			if(Objects.isNull(existPermiso)) {
            				nuevoPermiso.addUrl(ruta);
            				nuevoPermiso.setNombre("consultar"+rut);
            				nuevoPermiso.setMetodo(metodo);
            			}else {
            				nuevoPermiso = existPermiso;
            				nuevoPermiso.addUrl(ruta);
            			}
            			nuevoPermiso = permisoRepo.save(nuevoPermiso);
            			Rutina existRutina = rutinaRepo.findByNombre(rut);
            			if(Objects.isNull(existRutina)) {
            				nuevaRutina.addRuta(nuevoPermiso);
            			}else {
            				nuevaRutina = existRutina;
            				nuevaRutina.addRuta(nuevoPermiso);
            			}
            		}
            		if(ruta.contains("indexados")) {
            			Permiso existPermiso = permisoRepo.findByNombre("consultar"+rut);
            			if(Objects.isNull(existPermiso)) {
            				nuevoPermiso.addUrl(ruta);
            				nuevoPermiso.setNombre("consultar"+rut);
            				nuevoPermiso.setMetodo(metodo);
            			}else {
            				nuevoPermiso = existPermiso;
            				nuevoPermiso.addUrl(ruta);
            			}
            			nuevoPermiso = permisoRepo.save(nuevoPermiso);
            			Rutina existRutina = rutinaRepo.findByNombre(rut);
            			if(Objects.isNull(existRutina)) {
            				nuevaRutina.addRuta(nuevoPermiso);
            			}else {
            				nuevaRutina = existRutina;
            				nuevaRutina.addRuta(nuevoPermiso);
            			}
            		}
            		if(metodo.equals("POST") && !ruta.contains("indexados") && !ruta.contains("descarga") && !ruta.contains("cargar")) {
            			Permiso existPermiso = permisoRepo.findByNombre("crear"+rut);
            			if(Objects.isNull(existPermiso)) {
            				nuevoPermiso.addUrl(ruta);
            				nuevoPermiso.setNombre("crear"+rut);
            				nuevoPermiso.setMetodo(metodo);
            			}else {
            				nuevoPermiso = existPermiso;
            			}
            			nuevoPermiso = permisoRepo.save(nuevoPermiso);
            			Rutina existRutina = rutinaRepo.findByNombre(rut);
            			if(Objects.isNull(existRutina)) {
            				nuevaRutina.addRuta(nuevoPermiso);
            			}else {
            				nuevaRutina = existRutina;
            				nuevaRutina.addRuta(nuevoPermiso);
            			}
            		}
            		
            	}
            	
	        }
        	rutinaRepo.save(nuevaRutina);
	        }
	     }
	
}
