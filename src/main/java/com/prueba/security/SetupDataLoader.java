package com.prueba.security;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import javax.transaction.Transactional;

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
		
		if(isConfig) return;
		System.out.println("Application Event inicial");
		
		ApplicationContext applicationContext = event.getApplicationContext();
        RequestMappingHandlerMapping requestMappingHandlerMapping = applicationContext
          .getBean("requestMappingHandlerMapping", RequestMappingHandlerMapping.class);
        Map<RequestMappingInfo, HandlerMethod> map = requestMappingHandlerMapping.getHandlerMethods();

        Map<String, String> MetodoRuta = new HashMap<>();
        map.forEach((key, value) ->{ 

        MetodoRuta.put(key.getDirectPaths().toString(), key.getMethodsCondition().toString());
        Ruta nuevaruta = new Ruta(key.getActivePatternsCondition().toString().replace("[", "").replace("]", ""), key.getMethodsCondition().toString().replace("[", "").replace("]", ""));
        Ruta exist = rutaRepo.findByRutaAndMetodo(nuevaruta.getRuta(), nuevaruta.getMetodo());
        if(Objects.isNull(exist)) {
        		rutaRepo.save(nuevaruta);        		
        }
        });
        
        
		PoliRol readPrivilege = createPrivilegeIfNotFound("READ_PRIVILEGE");
		PoliRol writePrivilege = createPrivilegeIfNotFound("WRITE_PRIVILEGE");
		
		List<PoliRol> adminPrivileges = Arrays.asList(readPrivilege, writePrivilege);		
		createRoleIfNotFound("ROLE_ADMIN", adminPrivileges);
		
		List<PoliRol> rolePrivileges = Arrays.asList(readPrivilege);
		createRoleIfNotFound("ROLE_USER", rolePrivileges);
		
		String currentUserName = "";
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
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
			Usuario usuario = usuarioRepo.findByUsername(authentication.getName()).get();
			
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
