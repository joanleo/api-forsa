package com.prueba.security;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.prueba.entity.DetalleRutina;
import com.prueba.entity.Rutina;
import com.prueba.repository.RutinaRepository;
import com.prueba.security.entity.Politica;
import com.prueba.security.entity.Rol;
import com.prueba.security.entity.Usuario;
import com.prueba.security.repository.PoliticaRepository;
import com.prueba.security.repository.RolRepository;
import com.prueba.security.repository.UsuarioRepository;
import com.prueba.util.UtilitiesApi;

@Component
public class SetupDataLoader implements ApplicationListener<ContextRefreshedEvent> {

	boolean isConfig = false;
	
	@Autowired
	private UsuarioRepository usuarioRepo;
	
	@Autowired
	private RolRepository rolRepo;
		
	@Autowired
	private RutinaRepository rutinaRepo;
	
	@Autowired
	private UtilitiesApi util;;
	
	@Autowired
	private PoliticaRepository politicaRepo;
			
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
		System.out.println("Application initial event");
		System.out.println("buscando rutinas");
		List<Rutina> listRutinas = rutinaRepo.findAll();
		if(listRutinas.size() == 0) {
			System.out.println("No se encontraron rutinas");
			System.out.println("Creando rutinas");
			util.crearRutinasBD(event);
			listRutinas = rutinaRepo.findAll();
		}
        
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
        
		//Rutina readPrivilege = createPrivilegeIfNotFound("READ_PRIVILEGE");
		//Rutina writePrivilege = createPrivilegeIfNotFound("WRITE_PRIVILEGE");
		
		//List<Rutina> adminPrivileges = Arrays.asList(readPrivilege, writePrivilege);		
		//createRoleIfNotFound("ROLE_ADMIN", adminPrivileges);
		
		//List<Rutina> rolePrivileges = Arrays.asList(readPrivilege);
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
        
		
		
        Set<Rutina> targetSet = new HashSet<>(listRutinas);
        

        
        createRoleIfNotFound("ROLE_USER", targetSet);
        
		if(currentUserName != "") {
			Rol userRole = rolRepo.findByNombre("ROLE_USER");
			System.out.println(authentication.getName());
			Usuario usuario = usuarioRepo.findByNombreUsuario(authentication.getName()).get();
			
			usuario.setRol(userRole);
			usuarioRepo.save(usuario);	        
					
			isConfig = true;
		}
		
	}
	
	/*@Transactional
	Rutina createPrivilegeIfNotFound(String name) {
		
		Rutina privilege = poliRolRepo.findByNombre(name);
		if(privilege == null) {
			privilege = new PoliRol(name);
			poliRolRepo.save(privilege);
		}
		return privilege;
	}*/
	
	@Transactional
	Rol createRoleIfNotFound(String name, Set<Rutina> privileges) {
		List<Rol> listaRoles = rolRepo.findAll();
		if(listaRoles.size() == 0) {
			System.out.println("No se encontro ningun rol");
		}
		Rol role = rolRepo.findByNombre(name);
        if (role == null) {
        	System.out.println("Creando ROL_USER");
            role = new Rol(name);
            List<DetalleRutina> listaDetalle = new ArrayList<>();
            //List<Politica> listaPoliticas = new ArrayList<>();
            for(Rutina rutina: privileges) {
            	for(DetalleRutina detalle: rutina.getDetalles()) {
            		listaDetalle.add(detalle);
            	}
            }
            for(DetalleRutina detalle: listaDetalle) {
            	//System.out.println("Lista de detalle url: "+detalle.getPermiso().getUrl());
            	Politica politica = new Politica(role, detalle, false);
            	//politica.setDetalle(detalle);
            	//System.out.println("Poltica detalle url"+politica.getDetalle().getPermiso().getUrl());
            	//politica.setPermiso(false);
            	politica = politicaRepo.save(politica);
            	//role.addPolitica(detalle);
            	//listaPoliticas.add(politica);
            	
            }
            System.out.println(role.getPoliticas().size());
            //listaPoliticas = politicaRepo.saveAll(listaPoliticas);
            //listaPoliticas = politicaRepo.findAll();
            //Set<Politica> targetSet = new HashSet<>(listaPoliticas);
            //role.setPolitica(targetSet);
            rolRepo.save(role);
        }
        return role;
	}

}
