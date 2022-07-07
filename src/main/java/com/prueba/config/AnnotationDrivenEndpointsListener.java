package com.prueba.config;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

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
    
    boolean isConfig = false;
    
    @Autowired
    private RutaRepository rutaRepo;

    @EventListener
    public void handleContextRefresh(ContextRefreshedEvent event) {
    	if(isConfig) return;
        ApplicationContext applicationContext = event.getApplicationContext();
        RequestMappingHandlerMapping requestMappingHandlerMapping = applicationContext
          .getBean("requestMappingHandlerMapping", RequestMappingHandlerMapping.class);
        Map<RequestMappingInfo, HandlerMethod> map = requestMappingHandlerMapping.getHandlerMethods();

        Map<String, String> MetodoRuta = new HashMap<>();
        map.forEach((key, value) ->{ 

        MetodoRuta.put(key.getDirectPaths().toString(), key.getMethodsCondition().toString());
        Ruta nuevaruta = new Ruta(key.getActivePatternsCondition().toString(), key.getMethodsCondition().toString());
        Ruta exist = rutaRepo.findByRutaAndMetodo(nuevaruta.getRuta(), nuevaruta.getMetodo());
        if(Objects.isNull(exist)) {
        		rutaRepo.save(nuevaruta);        		
        }
        });
        
        
        isConfig = true;
    }
}
