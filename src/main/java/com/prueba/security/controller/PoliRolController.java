package com.prueba.security.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;

@RestController
@RequestMapping("/polirol")
@Api(tags = "Politicas de Rol",description = "Operaciones referentes a la politicas de los roles")
public class PoliRolController {

}
