package com.prueba.config;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.apache.commons.collections.map.HashedMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import com.prueba.security.entity.Ruta;
import com.prueba.security.repository.RutaRepository;

@Configuration
public class AnnotationDrivenEndpointsListener {
    private final Logger LOGGER = LoggerFactory.getLogger("AnnotationDrivenEndpointsListener.class");
    
    @Autowired
    private RutaRepository rutaRepo;

    @EventListener
    public void handleContextRefresh(ContextRefreshedEvent event) {
        ApplicationContext applicationContext = event.getApplicationContext();
        RequestMappingHandlerMapping requestMappingHandlerMapping = applicationContext
          .getBean("requestMappingHandlerMapping", RequestMappingHandlerMapping.class);
        Map<RequestMappingInfo, HandlerMethod> map = requestMappingHandlerMapping.getHandlerMethods();
        //List<String> metodos = new ArrayList<>();
        //List<String> rutas = new ArrayList<>();
        Map<String, String> MetodoRuta = new HashedMap();
        map.forEach((key, value) ->{ 
        //metodos.add(key.getMethodsCondition().toString());
        //rutas.add(key.getDirectPaths().toString());
        MetodoRuta.put(key.getDirectPaths().toString(), key.getMethodsCondition().toString());
        //System.out.println(key.getActivePatternsCondition().toString());
        Ruta nuevaruta = new Ruta(key.getActivePatternsCondition().toString(), key.getMethodsCondition().toString());
        Ruta exist = rutaRepo.findByRutaAndMetodo(nuevaruta.getRuta(), nuevaruta.getMetodo());
        if(Objects.isNull(exist)) {
        		rutaRepo.save(nuevaruta);        		
        }
        });
        
        /*for(String metodo: metodos) {        	
        	System.out.println(metodo);
        }
        for(String ruta: rutas) {        	
        	System.out.println(ruta);
        }*/
        
        //MetodoRuta.forEach((key, value) -> System.out.println("Ruta: " + key + " Metodo: " + value));
    }
}
