package com.prueba.dto;

import java.util.List;

import com.prueba.entity.Producto;
import com.prueba.security.entity.Usuario;

public class ReporteVerificacionDTO {
	
	Usuario realizo;
	
	String orden;
	
	String filtro;
	
	List<Producto> productos;

}
