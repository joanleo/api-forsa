package com.prueba.util;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
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

import com.prueba.entity.DetalleInv;
import com.prueba.entity.Empresa;
import com.prueba.entity.MovInventario;
import com.prueba.entity.Permiso;
import com.prueba.entity.Producto;
import com.prueba.entity.Rutina;
import com.prueba.exception.ResourceNotFoundException;
import com.prueba.repository.EmpresaRepository;
import com.prueba.repository.MovInventarioRepository;
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
	
	@Autowired
	private MovInventarioRepository movInventarioRepo;
	
	public void compararInventarios(Integer inventario1, Integer inventario2) {
		MovInventario inv1 = movInventarioRepo.findByidMov(inventario1);
		if(Objects.isNull(inv1)) {
			throw new ResourceNotFoundException("Inventario", "id", inventario1.toString());
		}
		MovInventario inv2 = movInventarioRepo.findByidMov(inventario2);
		if(Objects.isNull(inv2)) {
			throw new ResourceNotFoundException("Inventario", "id", inventario2.toString());
		}
		
		//Obtengo la lista de activos del inventario1
		List<DetalleInv> detallesInv1 = inv1.getDetalles();
		Iterator<DetalleInv> it = detallesInv1.iterator();
		List<Producto> activosInv1 = new ArrayList<>();
		System.out.println("Creando lista de activos de inv1...");
	    while(it.hasNext()){
	        DetalleInv item=it.next();
	        System.out.println("Añadiendo "+item.getProducto().getCodigoPieza()+" a la lista de activos de inv1");
	        activosInv1.add(item.getProducto());
	    }
	    //Obtengo la lista de activos del inventario2
		List<DetalleInv> detallesInv2 = inv2.getDetalles();
		it = detallesInv2.iterator();
		List<Producto> activosInv2 = new ArrayList<>();
		System.out.println("Creando lista de activos de inv2...");
	    while(it.hasNext()){
	        DetalleInv item=it.next();
	        System.out.println("Añadiendo "+item.getProducto().getCodigoPieza()+" a la lista de activos de inv2");
	        activosInv2.add(item.getProducto());
	    }
	    
	    //Comparo ambas listas y obtengo item de inventario1 que no estan en inventario2
	    List<Producto> diferentes = new ArrayList<>();
		int sizeListActivosInv1 = activosInv1.size();
		int sizeListActivosInv2 = activosInv2.size();
		
		
		if(sizeListActivosInv1 > sizeListActivosInv2) {
			System.out.println("Inventario 1 mayor que inventario2");
			for(int count=0;count<sizeListActivosInv1;count++) {
				boolean add = true;
				for(Producto activo: activosInv2) {
					System.out.println("comparando "+activosInv1.get(count).getCodigoPieza()+" con "+activo.getCodigoPieza());
					if(activo.getCodigoPieza().equalsIgnoreCase((activosInv1.get(count).getCodigoPieza()))) {
						System.out.println("No son iguales se añade "+activosInv1.get(count).getCodigoPieza());
						add = false;
						break;
						
					}
				}
				if(add) {
					diferentes.add(activosInv1.get(count));
				}
				
				
			}
		}
		
		System.out.println("Codigos de activos diferentes");
		for(Producto producto:diferentes) {
			System.out.println(producto.getDescripcion());
		}	
		
	}

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
