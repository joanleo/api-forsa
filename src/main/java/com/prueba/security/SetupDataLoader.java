package com.prueba.security;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Set;

import javax.transaction.Transactional;

import org.apache.commons.lang3.StringUtils;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import com.prueba.security.entity.PoliRol;
import com.prueba.security.entity.Rol;
import com.prueba.security.entity.Ruta;
import com.prueba.security.entity.Usuario;
import com.prueba.security.repository.PoliRolRepository;
import com.prueba.security.repository.RolRepository;
import com.prueba.security.repository.RutaRepository;
import com.prueba.security.repository.UsuarioRepository;

@Component
public class SetupDataLoader implements ApplicationListener<ContextRefreshedEvent> {

	boolean isConfig = false;
	
	@Autowired
	private UsuarioRepository usuarioRepo;
	
	@Autowired
	private RolRepository rolRepo;
	
	@Autowired
	private PoliRolRepository poliRolRepo;
	
	@Autowired
	private RutaRepository rutaRepo;
	
	@Override
	@Transactional
	public void onApplicationEvent(ContextRefreshedEvent event) {
		
		String currentUserName = "";
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if ((authentication != null)) {
		    currentUserName = authentication.getName();
		    System.out.println(currentUserName);
		}
		
		if(isConfig) return;
		System.out.println("Application Event inicial");
		
		ApplicationContext applicationContext = event.getApplicationContext();
        RequestMappingHandlerMapping requestMappingHandlerMapping = applicationContext
          .getBean("requestMappingHandlerMapping", RequestMappingHandlerMapping.class);
        Map<RequestMappingInfo, HandlerMethod> map = requestMappingHandlerMapping.getHandlerMethods();

        String aux="";
        JSONArray jSonRutinas = new JSONArray();
        JSONObject jSonrutina = new JSONObject();
        JSONObject jSonmetodo = new JSONObject();
        JSONArray  metodos = new JSONArray();
        
        Set<String> arrRutinas = new HashSet<>();

        for(Entry<RequestMappingInfo, HandlerMethod> rutaMetodo: map.entrySet()) {
        	String rutina = rutaMetodo.getKey().getActivePatternsCondition().toString().replace("[", "").replace("]", "").split("/")[1];
        	String ruta = rutaMetodo.getKey().getActivePatternsCondition().toString().replace("[", "").replace("]", "");
        	String metodo = rutaMetodo.getKey().getMethodsCondition().toString().replace("[", "").replace("]", "");
        	if(rutina != aux) {
        		aux = rutina;
        		arrRutinas.add(aux);
        		try {
					jSonrutina.put("nombre", aux);
					//System.out.println(aux);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
        	}
        	try {
        		jSonmetodo.put("nombre", ruta);
        		jSonmetodo.put("metodo", metodo);
        		jSonmetodo.put("permitido", false);
        		metodos.put(jSonmetodo);
        		jSonrutina.put("metodos", metodos);
        		jSonRutinas.put(jSonrutina);
        		//jSonrutina.put("metodos",)
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        	
        }
        ArrayList<String> arr = new ArrayList<String>();
        for(String rut: arrRutinas) {
        	String stringRuina = "{\"rutina\":{\"nombre\":\""+rut+"\",\"opciones\":[";
        	String rutaMethod = "";
	        for(Entry<RequestMappingInfo, HandlerMethod> rutaMetodo: map.entrySet()) {
	        	String rutina = rutaMetodo.getKey().getActivePatternsCondition().toString().replace("[", "").replace("]", "").split("/")[1];
	        	String ruta = rutaMetodo.getKey().getActivePatternsCondition().toString().replace("[", "").replace("]", "");
	        	String metodo = rutaMetodo.getKey().getMethodsCondition().toString().replace("[", "").replace("]", "");
	        	
            	if(rut.equals(rutina)) {
            		//String ejemplo = "{\"id\":46,\"nombre\":\"Miguel\",\"empresa\":\"Autentia\"}";
            		if(metodo.equals("PUT")) {
            			rutaMethod += "{\"url\":\""+ruta+"\",\"actualizar\":\"false},";            			
            		}
            		if(metodo.equals("DELETE")) {
            			rutaMethod += "{\"url\":\""+ruta+"\",\"eliminar\":\"false},";            			
            		}
            		if(metodo.equals("PATCH")) {
            			rutaMethod += "{\"url\":\""+ruta+"\",\"inhabilitar\":\"false},";            			
            		}
            		if(metodo.equals("GET") && (!ruta.contains("id") || !ruta.contains("nit"))) {
            			rutaMethod += "{\"url\":\""+ruta+"\",\"leer\":\"false},";            			
            		}
            		if((ruta.contains("id") || ruta.contains("nit")) && metodo.equals("GET")){
            			rutaMethod += "{\"url\":\""+ruta+"\",\"autocompletar\":\"false},"; 
            		}
            		if(metodo.equals("POST") && !ruta.contains("indexados") && !ruta.contains("descarga") && !ruta.contains("cargar")) {
            			rutaMethod += "{\"url\":\""+ruta+"\",\"crear\":\"false},";
            		}
            		if(ruta.contains("indexados")) {
        				rutaMethod += "{\"url\":\""+ruta+"\",\"listar\":\"false},";            				
        			}
            		if(ruta.contains("descarga")) {
        				rutaMethod += "{\"url\":\""+ruta+"\",\"exportar\":\"false},";            				
        			}
            		if(ruta.contains("cargar")) {
        				rutaMethod += "{\"url\":\""+ruta+"\",\"importar\":\"false},";            				
        			}

            	}
	        }
	        if(rutaMethod.length() > 0) {
	        	stringRuina += rutaMethod.substring(0,rutaMethod.length()-1)+"]}";	        	
	        }
	        System.out.println(stringRuina);
	        arr.add(stringRuina);
	     }
        System.out.println(arr);
        
        /*Map<String, String> MetodoRuta = new HashMap<>();
        List<Ruta> rutas = new ArrayList<Ruta>();
        List<Ruta> nuevasRutas = new ArrayList<Ruta>();
        
        map.forEach((key, value) ->{    
        
        MetodoRuta.put(key.getDirectPaths().toString(), key.getMethodsCondition().toString());
        Ruta nuevaruta = new Ruta(key.getActivePatternsCondition().toString().replace("[", "").replace("]", ""), key.getMethodsCondition().toString().replace("[", "").replace("]", ""));
        Ruta exist = rutaRepo.findByRutaAndMetodo(nuevaruta.getRuta(), nuevaruta.getMetodo());
        if(Objects.isNull(exist)) {
        	System.out.println("ruta no existe "+ nuevaruta.getRuta()+" "+nuevaruta.getMetodo());
        		//rutaRepo.save(nuevaruta);
        		rutas.add(nuevaruta);
        		//System.out.println(rutaRepo.findByRutaAndMetodo(nuevaruta.getRuta(), nuevaruta.getMetodo()));
        }
        });
        if(rutas.size() > 0) {
        	System.out.println(" Guardando nuevas rutas");
        	rutaRepo.saveAll(rutas);        	
        }
        nuevasRutas = rutaRepo.findAll();

        List<PoliRol> politicas = new ArrayList<>();
        for(Ruta ruta: nuevasRutas) {
        	PoliRol politica = new PoliRol(ruta);
        	PoliRol exist = poliRolRepo.findByRuta(ruta);
        	if(Objects.isNull(exist)) {
        		//System.out.println("Ruta: "+politica.getRuta().getRuta());
        		String nombre = ruta.getRuta().toUpperCase().substring(1);
        		String toRemove = StringUtils.substringBetween(nombre, "{", "}");
        		nombre = StringUtils.remove(nombre, "{" + toRemove + "}");
        		String toRemove1 = StringUtils.substringBetween(nombre, ",", "}");
        		nombre = StringUtils.remove(nombre, "," + toRemove1 + "}");
        		if(nombre.contains("/")) {
        			nombre = nombre.replace("/", "_");
        			System.out.println("contenia / " + nombre);
        		}
        		if(nombre.endsWith("_")){
        			nombre = nombre + ruta.getMetodo() + "_" + "PRIVILEGE";
        			System.out.println("contenia / al final y se cambio por _" + nombre);
        		}else {
        			nombre = nombre + "_" + ruta.getMetodo() + "_" + "PRIVILEGE";        			
        			System.out.println("no contenia / al final " + nombre);
        		}
        		politica.setNombre(nombre);
        		politicas.add(politica);        		
        	}
        }
        if(politicas.size() > 0) {
        	System.out.println("guardando politicas");
        	poliRolRepo.saveAll(politicas);       	
        }
        */
		PoliRol readPrivilege = createPrivilegeIfNotFound("READ_PRIVILEGE");
		PoliRol writePrivilege = createPrivilegeIfNotFound("WRITE_PRIVILEGE");
		
		List<PoliRol> adminPrivileges = Arrays.asList(readPrivilege, writePrivilege);		
		createRoleIfNotFound("ROLE_ADMIN", adminPrivileges);
		
		List<PoliRol> rolePrivileges = Arrays.asList(readPrivilege);
		createRoleIfNotFound("ROLE_USER", rolePrivileges);
		
		
		if ((authentication != null)) {
		    currentUserName = authentication.getName();
		    System.out.println(currentUserName);
		}
		
		/*Rol adminRole = rolRepo.findByNombre("ROLE_ADMIN");
		Usuario usuarioAdmin = new Usuario();
		
		usuarioAdmin.setNombre("Admin");
		usuarioAdmin.setEmail("admin@test.com");
		usuarioAdmin.setPassword(passwordEncoder.encode("admin"));
		usuarioAdmin.setRoles(Arrays.asList(adminRole));
		usuarioRepo.save(usuarioAdmin);*/
		
		if(currentUserName != "") {
			Rol userRole = rolRepo.findByNombre("ROLE_USER");
			System.out.println(authentication.getName());
			Usuario usuario = usuarioRepo.findByNombreUsuario(authentication.getName()).get();
			
			usuario.setRoles(Arrays.asList(userRole));
			usuarioRepo.save(usuario);	        
					
			isConfig = true;
		}
		
	}
	
	@Transactional
	PoliRol createPrivilegeIfNotFound(String name) {
		
		PoliRol privilege = poliRolRepo.findByNombre(name);
		if(privilege == null) {
			privilege = new PoliRol(name);
			poliRolRepo.save(privilege);
		}
		return privilege;
	}
	
	@Transactional
	Rol createRoleIfNotFound(String name, Collection<PoliRol> privileges) {
		Rol role = rolRepo.findByNombre(name);
        if (role == null) {
            role = new Rol(name);
            role.setPoliRoles(privileges);
            rolRepo.save(role);
        }
        return role;
	}

}
