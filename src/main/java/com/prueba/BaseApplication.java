package com.prueba;


import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.prueba.security.entity.Politica;
import com.prueba.service.PoliticaService;

@SpringBootApplication
public class BaseApplication {
	
	@Autowired
	private PoliticaService politicaService;
	
	public static final List<Politica> politicas = new ArrayList<>();
	
	@Bean
	public ModelMapper modelMapper() {
		return new ModelMapper();
	}

	public static void main(String[] args) {
		SpringApplication.run(BaseApplication.class, args);
		
		
	}
	


}
