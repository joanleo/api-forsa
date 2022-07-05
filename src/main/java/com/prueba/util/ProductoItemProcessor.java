package com.prueba.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;

import com.prueba.entity.Empresa;
import com.prueba.entity.Fabricante;
import com.prueba.entity.Familia;
import com.prueba.entity.Producto;

public class ProductoItemProcessor implements ItemProcessor<Producto, Producto> {

	private static final Logger LOGGER = LoggerFactory.getLogger(ProductoItemProcessor.class);
	
	@Override
	public Producto process(Producto item) throws Exception {
		String codigoPieza = item.getCodigoPieza();
        String descripcion = item.getDescripcion();
        Float area = item.getArea();
        String orden = item.getOrden();
        Familia familia = new Familia(item.getFamilia().getId());
        Fabricante fabricante = new Fabricante(item.getFabricante().getNit());
        Empresa empresa = new Empresa(item.getEmpresa().getNit());

        

        Producto transformedProduct = new Producto(codigoPieza,descripcion,area,orden,familia,fabricante,empresa);
        LOGGER.info("Converting ( {} ) into ( {} )", item, transformedProduct);

        return transformedProduct;
	}

}
