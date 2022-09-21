package com.prueba.util;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.Map.Entry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Service;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import com.prueba.dto.ComparativoInventarioDTO;
import com.prueba.dto.ComparativoUbicacionDTO;
import com.prueba.entity.DetalleInv;
import com.prueba.entity.Empresa;
import com.prueba.entity.MovInventario;
import com.prueba.entity.Permiso;
import com.prueba.entity.Producto;
import com.prueba.entity.Rutina;
import com.prueba.entity.Ubicacion;
import com.prueba.exception.ResourceNotFoundException;
import com.prueba.repository.EmpresaRepository;
import com.prueba.repository.MovInventarioRepository;
import com.prueba.repository.PermisoRepository;
import com.prueba.repository.ProductoRepository;
import com.prueba.repository.RutinaRepository;
import com.prueba.repository.UbicacionRepository;


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
	
	@Autowired
	private UbicacionRepository ubicacionRepo;
			
	@Autowired
	private ProductoRepository productoRepo;
	
	public List<ComparativoUbicacionDTO> analisisDiferencias(Long idUbicacion, Long idInven){
		Ubicacion ubicacion = ubicacionRepo.findById(idUbicacion)
				.orElseThrow(()-> new ResourceNotFoundException("ubicacion", "id", idUbicacion));
		List<Producto> activosUbicacion = productoRepo.findByUbicacionAndEstaActivoTrue(ubicacion);
		System.out.println("Total activos ubicacion: "+activosUbicacion.size());
		MovInventario inv = movInventarioRepo.findByidMov(idInven);
		if(Objects.isNull(inv)) {
			throw new ResourceNotFoundException("Inventario", "id", idInven);
		}
		//Obtengo la lista de activos del inventario
		List<DetalleInv> activosInv = inv.getDetalles();
		Iterator<DetalleInv> it = activosInv.iterator();
		List<Producto> activosInv1 = new ArrayList<>();
		Set<Producto> totalActivos = new HashSet<>();
	    while(it.hasNext()){
	        DetalleInv item=it.next();
	        activosInv1.add(item.getProducto());
	        totalActivos.add(item.getProducto());
	    }
	    
	    for(Producto producto: activosUbicacion) {
	    	totalActivos.add(producto);
	    }
	    System.out.println("Total activos en inventario: "+totalActivos.size());
	    
	    List<Producto> productosAmbosInv = totalActivos.stream().collect(Collectors.toList());
	    System.out.println("Activos en ambos "+productosAmbosInv.size());
	    Set<ComparativoUbicacionDTO> comparativo = new HashSet<ComparativoUbicacionDTO>();
	    for(Producto producto: productosAmbosInv) {
	    	ComparativoUbicacionDTO itemAddComparativo = new ComparativoUbicacionDTO(
	    			producto.getCodigoPieza(), 
	    			producto.getDescripcion(), 
	    			producto.getFamilia().getSigla(), 
	    			producto.getTipo().getNombre(),
	    			producto.getMedidas(), 
	    			producto.getArea(), 
	    			producto.getEstado() == null ? " ": producto.getEstado().getTipo(),
	    					ubicacion.getNombre(), 
	    					idInven);
	    	comparativo.add(itemAddComparativo);
	    	for(Producto item: activosUbicacion) {
	    		if(item.getCodigoPieza().equalsIgnoreCase(producto.getCodigoPieza())) {
	    			itemAddComparativo.setUbicacion(true);
	    			
	    		}
	    	}
	    	for(Producto item: activosInv1) {
	    		if(item.getCodigoPieza().equalsIgnoreCase(producto.getCodigoPieza())) {
	    			itemAddComparativo.setInv(true);
	    		}
	    	}
	    }
	    
	    List<ComparativoUbicacionDTO> comparativoLista = comparativo.stream().collect(Collectors.toList());
	    comparativoLista.sort(Comparator.comparing(ComparativoUbicacionDTO :: getCodigo));
		return comparativoLista;
	}
	
	public List<ComparativoInventarioDTO> compararInventarios(Long inventario1, Long inventario2) {
		
		
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
		Set<Producto> totalProductosAmbosInv = new HashSet<>();
	    while(it.hasNext()){
	        DetalleInv item=it.next();
	        activosInv1.add(item.getProducto());
	        totalProductosAmbosInv.add(item.getProducto());
	    }
	    //Obtengo la lista de activos del inventario2
		List<DetalleInv> detallesInv2 = inv2.getDetalles();
		it = detallesInv2.iterator();
		List<Producto> activosInv2 = new ArrayList<>();
	    while(it.hasNext()){
	        DetalleInv item=it.next();
	        activosInv2.add(item.getProducto());
	        totalProductosAmbosInv.add(item.getProducto());
	    }
	    List<Producto> productosAmbosInv = totalProductosAmbosInv.stream().collect(Collectors.toList());
	    Set<ComparativoInventarioDTO> comparativo = new HashSet<ComparativoInventarioDTO>();
	    for(Producto producto: productosAmbosInv) {
	    	ComparativoInventarioDTO itemAddComparativo = new ComparativoInventarioDTO(
	    			producto.getCodigoPieza(), 
	    			producto.getDescripcion(), 
	    			producto.getFamilia().getSigla(), 
	    			producto.getTipo().getNombre(),
	    			producto.getMedidas(), 
	    			producto.getArea(), 
	    			producto.getEstado() == null ? " ": producto.getEstado().getTipo(),
	    			detallesInv1.get(0).getMovimiento().getIdMov(), 
	    			detallesInv2.get(0).getMovimiento().getIdMov());
	    	comparativo.add(itemAddComparativo);
	    	for(Producto item: activosInv1) {
	    		if(item.getCodigoPieza().equalsIgnoreCase(producto.getCodigoPieza())) {
	    			itemAddComparativo.setInv1(true);
	    			
	    		}
	    	}
	    	for(Producto item: activosInv2) {
	    		if(item.getCodigoPieza().equalsIgnoreCase(producto.getCodigoPieza())) {
	    			itemAddComparativo.setInv2(true);
	    		}
	    	}
	    }
	    List<ComparativoInventarioDTO> comparativoLista = comparativo.stream().collect(Collectors.toList());
		comparativoLista.sort(Comparator.comparing(ComparativoInventarioDTO :: getCodigo));
		
		return comparativoLista;

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
