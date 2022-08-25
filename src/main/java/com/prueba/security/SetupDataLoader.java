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
		   
		    
		}
		
		if(isConfig) return;
		List<Rutina> listRutinas = rutinaRepo.findAll();
		if(listRutinas.size() == 0) {
			util.crearRutinasBD(event);
			listRutinas = rutinaRepo.findAll();
		}
        
		Set<Rutina> targetSet = new HashSet<>(listRutinas);
		createRoleIfNotFound("ROLE_USER", targetSet);
		if(currentUserName != "") {
			Rol userRole = rolRepo.findByNombre("ROLE_USER");
			Usuario usuario = usuarioRepo.findByNombreUsuario(authentication.getName()).get();
			userRole.setEmpresa(usuario.getEmpresa());
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
            role = new Rol(name);
            List<DetalleRutina> listaDetalle = new ArrayList<>();
            //List<Politica> listaPoliticas = new ArrayList<>();
            for(Rutina rutina: privileges) {
            	for(DetalleRutina detalle: rutina.getDetalles()) {
            		listaDetalle.add(detalle);
            	}
            }
            for(DetalleRutina detalle: listaDetalle) {
            	Politica politica = new Politica(role, detalle, false);

            	politica = politicaRepo.save(politica);

            	
            }
            System.out.println(role.getPoliticas().size());

            rolRepo.save(role);
        }
        return role;
	}

}
