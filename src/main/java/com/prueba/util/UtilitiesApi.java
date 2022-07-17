package com.prueba.util;

import java.util.ArrayList;
import java.util.HashSet;
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
import com.prueba.entity.Ruta;
import com.prueba.entity.Rutina;
import com.prueba.repository.EmpresaRepository;
import com.prueba.repository.RutaRepository;
import com.prueba.repository.RutinaRepository;


@Service
public class UtilitiesApi {
	
	@Autowired
	private EmpresaRepository empresaRepo;
	
	@Autowired
	private RutaRepository permisoRepo;
	
	@Autowired
	private RutinaRepository rutinaRepo;

	public Empresa obtenerEmpresa(Long nit) {
		Empresa exist = empresaRepo.findByNit(nit);
		if (exist != null) {
			return exist;
		} else {
			throw new IllegalAccessError("La empresa " + nit + " no existe en la base de datos");
		}
	}
	
	public void crearRutinasBD(ContextRefreshedEvent event) {
		ApplicationContext applicationContext = event.getApplicationContext();
        RequestMappingHandlerMapping requestMappingHandlerMapping = applicationContext
          .getBean("requestMappingHandlerMapping", RequestMappingHandlerMapping.class);
        Map<RequestMappingInfo, HandlerMethod> map = requestMappingHandlerMapping.getHandlerMethods();

        String aux="";
        
        Set<String> arrRutinas = new HashSet<>();

        for(Entry<RequestMappingInfo, HandlerMethod> rutaMetodo: map.entrySet()) {
        	String rutina = rutaMetodo.getKey().getActivePatternsCondition().toString().replace("[", "").replace("]", "").split("/")[1];
        	if(rutina != aux) {
        		aux = rutina;
        		arrRutinas.add(aux);
        	}
        	
        }
        ArrayList<String> arr = new ArrayList<String>();
        for(String rut: arrRutinas) {
        	String stringRuina = "{\"rutina\":{\"nombre\":\""+rut+"\",\"opciones\":[";
        	String rutaMethod = "";
        	Rutina nuevaRutina = new Rutina();
        	nuevaRutina.setNombre(rut);
	        for(Entry<RequestMappingInfo, HandlerMethod> rutaMetodo: map.entrySet()) {
	        	String rutina = rutaMetodo.getKey().getActivePatternsCondition().toString().replace("[", "").replace("]", "").split("/")[1];
	        	String ruta = rutaMetodo.getKey().getActivePatternsCondition().toString().replace("[", "").replace("]", "");
	        	String metodo = rutaMetodo.getKey().getMethodsCondition().toString().replace("[", "").replace("]", "");
	        	
            	if(rut.equals(rutina)) {
            		//System.out.println("Rutina: "+rutina);
            		//System.out.println("Ruta: "+ruta);
            		//System.out.println("Metodo: "+metodo);
            		
            		Ruta nuevoPermiso = new Ruta();
            		//String ejemplo = "{\"id\":46,\"nombre\":\"Miguel\",\"empresa\":\"Autentia\"}";
            		if(metodo.equals("PUT")) {
            			rutaMethod += "{\"url\":\""+ruta+"\",\"actualizar\":\"false},";
            			//System.out.println("buscando permiso "+ruta);
            			Ruta existPermiso = permisoRepo.findByUrlAndNombre(ruta,"actualizar");
            			if(Objects.isNull(existPermiso)) {
            				nuevoPermiso.setUrl(ruta);
            				nuevoPermiso.setNombre("actualizar");
            				nuevoPermiso.setMetodo(metodo);
            			}else {
            				nuevoPermiso = existPermiso;
            			}
            			nuevoPermiso = permisoRepo.save(nuevoPermiso);
            			//System.out.println("buscando rutina "+rut);
            			Rutina existRutina = rutinaRepo.findByNombre(rut);
            			if(Objects.isNull(existRutina)) {
            				nuevaRutina.addRuta(nuevoPermiso);
            			}else {
            				nuevaRutina = existRutina;
            				nuevaRutina.addRuta(nuevoPermiso);
            			}
            		}
            		if(metodo.equals("DELETE")) {
            			rutaMethod += "{\"url\":\""+ruta+"\",\"eliminar\":\"false},";
            			//System.out.println("buscando permiso "+ruta);
            			Ruta existPermiso = permisoRepo.findByUrlAndNombre(ruta,"eliminar");
            			if(Objects.isNull(existPermiso)) {
            				nuevoPermiso.setUrl(ruta);
            				nuevoPermiso.setNombre("eliminar");
            				nuevoPermiso.setMetodo(metodo);
            			}else {
            				nuevoPermiso = existPermiso;
            			}
            			nuevoPermiso = permisoRepo.save(nuevoPermiso);
            			//System.out.println("buscando rutina "+rut);
            			Rutina existRutina = rutinaRepo.findByNombre(rut);
            			if(Objects.isNull(existRutina)) {
            				nuevaRutina.addRuta(nuevoPermiso);
            			}else {
            				nuevaRutina = existRutina;
            				nuevaRutina.addRuta(nuevoPermiso);
            			}
            		}
            		if(metodo.equals("PATCH")) {
            			rutaMethod += "{\"url\":\""+ruta+"\",\"inhabilitar\":\"false},";
            			//System.out.println("buscando permiso "+ruta);
            			Ruta existPermiso = permisoRepo.findByUrlAndNombre(ruta,"inhabilitar");
            			if(Objects.isNull(existPermiso)) {
            				nuevoPermiso.setUrl(ruta);
            				nuevoPermiso.setNombre("inhabilitar");
            				nuevoPermiso.setMetodo(metodo);
            			}else {
            				nuevoPermiso = existPermiso;
            			}
            			nuevoPermiso = permisoRepo.save(nuevoPermiso);
            			//System.out.println("buscando rutina "+rut);
            			Rutina existRutina = rutinaRepo.findByNombre(rut);
            			if(Objects.isNull(existRutina)) {
            				nuevaRutina.addRuta(nuevoPermiso);
            			}else {
            				nuevaRutina = existRutina;
            				nuevaRutina.addRuta(nuevoPermiso);
            			}
            		}
            		if(metodo.equals("GET") && (!ruta.contains("id") || !ruta.contains("nit"))) {
            			rutaMethod += "{\"url\":\""+ruta+"\",\"leer\":\"false},";
            			//System.out.println("buscando permiso "+ruta);
            			Ruta existPermiso = permisoRepo.findByUrlAndNombre(ruta,"autocompletar");
            			if(Objects.isNull(existPermiso)) {
            				nuevoPermiso.setUrl(ruta);
            				nuevoPermiso.setNombre("autocompletar");
            				nuevoPermiso.setMetodo(metodo);
            			}else {
            				nuevoPermiso = existPermiso;
            			}
            			//System.out.println("Permiso: "+nuevoPermiso.getNombre());
            			//System.out.println("guardando permiso");
            			nuevoPermiso = permisoRepo.save(nuevoPermiso);
            			//System.out.println("buscando rutina "+rut);
            			Rutina existRutina = rutinaRepo.findByNombre(rut);
            			if(Objects.isNull(existRutina)) {
            				//System.out.println("existe rutina");
            				nuevaRutina.addRuta(nuevoPermiso);
            			}else {
            				//System.out.println("noexiste rutina");
            				nuevaRutina = existRutina;
            				nuevaRutina.addRuta(nuevoPermiso);
            			}
            		}
            		if((ruta.contains("id") || ruta.contains("nit")) && metodo.equals("GET")){
            			rutaMethod += "{\"url\":\""+ruta+"\",\"autocompletar\":\"false},";
            			//System.out.println("buscando permiso "+ruta);
            			Ruta existPermiso = permisoRepo.findByUrlAndNombre(ruta,"leer");
            			if(Objects.isNull(existPermiso)) {
            				nuevoPermiso.setUrl(ruta);
            				nuevoPermiso.setNombre("leer");
            				nuevoPermiso.setMetodo(metodo);
            			}else {
            				nuevoPermiso = existPermiso;
            			}
            			nuevoPermiso = permisoRepo.save(nuevoPermiso);
            			//System.out.println("buscando rutina "+rut);
            			Rutina existRutina = rutinaRepo.findByNombre(rut);
            			if(Objects.isNull(existRutina)) {
            				nuevaRutina.addRuta(nuevoPermiso);
            			}else {
            				nuevaRutina = existRutina;
            				nuevaRutina.addRuta(nuevoPermiso);
            			}
            		}
            		if(metodo.equals("POST") && !ruta.contains("indexados") && !ruta.contains("descarga") && !ruta.contains("cargar")) {
            			rutaMethod += "{\"url\":\""+ruta+"\",\"crear\":\"false},";
            			//System.out.println("buscando permiso "+ruta);
            			Ruta existPermiso = permisoRepo.findByUrlAndNombre(ruta,"crear");
            			if(Objects.isNull(existPermiso)) {
            				nuevoPermiso.setUrl(ruta);
            				nuevoPermiso.setNombre("crear");
            				nuevoPermiso.setMetodo(metodo);
            			}else {
            				nuevoPermiso = existPermiso;
            			}
            			nuevoPermiso = permisoRepo.save(nuevoPermiso);
            			//System.out.println("buscando rutina "+rut);
            			Rutina existRutina = rutinaRepo.findByNombre(rut);
            			if(Objects.isNull(existRutina)) {
            				nuevaRutina.addRuta(nuevoPermiso);
            			}else {
            				nuevaRutina = existRutina;
            				nuevaRutina.addRuta(nuevoPermiso);
            			}
            		}
            		if(ruta.contains("indexados")) {
        				rutaMethod += "{\"url\":\""+ruta+"\",\"listar\":\"false},";
        				//System.out.println("buscando permiso "+ruta);
        				Ruta existPermiso = permisoRepo.findByUrlAndNombre(ruta,"listar");
            			if(Objects.isNull(existPermiso)) {
            				nuevoPermiso.setUrl(ruta);
            				nuevoPermiso.setNombre("listar");
            				nuevoPermiso.setMetodo(metodo);
            			}else {
            				nuevoPermiso = existPermiso;
            			}
            			nuevoPermiso = permisoRepo.save(nuevoPermiso);
            			//System.out.println("buscando rutina "+rut);
            			Rutina existRutina = rutinaRepo.findByNombre(rut);
            			if(Objects.isNull(existRutina)) {
            				nuevaRutina.addRuta(nuevoPermiso);
            			}else {
            				nuevaRutina = existRutina;
            				nuevaRutina.addRuta(nuevoPermiso);
            			}
        			}
            		if(ruta.contains("descarga")) {
        				rutaMethod += "{\"url\":\""+ruta+"\",\"exportar\":\"false},";
        				//System.out.println("buscando permiso "+ruta);
        				Ruta existPermiso = permisoRepo.findByUrlAndNombre(ruta,"exportar");
            			if(Objects.isNull(existPermiso)) {
            				nuevoPermiso.setUrl(ruta);
            				nuevoPermiso.setNombre("exportar");
            				nuevoPermiso.setMetodo(metodo);
            			}else {
            				nuevoPermiso = existPermiso;
            			}
            			nuevoPermiso = permisoRepo.save(nuevoPermiso);
            			//System.out.println("buscando rutina "+rut);
            			Rutina existRutina = rutinaRepo.findByNombre(rut);
            			if(Objects.isNull(existRutina)) {
            				nuevaRutina.addRuta(nuevoPermiso);
            			}else {
            				nuevaRutina = existRutina;
            				nuevaRutina.addRuta(nuevoPermiso);
            			}
        			}
            		if(ruta.contains("cargar")) {
        				rutaMethod += "{\"url\":\""+ruta+"\",\"importar\":\"false},";
        				//System.out.println("buscando permiso "+ruta);
        				Ruta existPermiso = permisoRepo.findByUrlAndNombre(ruta,"importar");
            			if(Objects.isNull(existPermiso)) {
            				nuevoPermiso.setUrl(ruta);
            				nuevoPermiso.setNombre("importar");
            				nuevoPermiso.setMetodo(metodo);
            			}else {
            				nuevoPermiso = existPermiso;
            			}
            			nuevoPermiso = permisoRepo.save(nuevoPermiso);
            			//System.out.println("buscando rutina "+rut);
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
	        //System.out.println("gardando rutina");
        	rutinaRepo.save(nuevaRutina);
	        if(rutaMethod.length() > 0) {
	        	stringRuina += rutaMethod.substring(0,rutaMethod.length()-1)+"]}";	        	
	        }
	        //System.out.println(stringRuina);
	        arr.add(stringRuina);
	     }
        //System.out.println(arr);
	}
}
