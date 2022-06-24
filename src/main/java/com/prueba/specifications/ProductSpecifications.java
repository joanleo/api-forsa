package com.prueba.specifications;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.Predicate;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import com.prueba.dto.SearchDTO;
import com.prueba.entity.Empresa;
import com.prueba.entity.Producto;
import com.prueba.entity.Producto_id;

@Component
public class ProductSpecifications {


	public Specification<Producto> getProductos(SearchDTO searchDTO, Empresa empresa){
		return (root, query, criteryBuilder) ->{
			
			if(searchDTO.getEmpresa() == null) {
				searchDTO.setEmpresa(empresa);
			}
			
			List<Predicate> predicates = new ArrayList<>();
			
			if(searchDTO.getArea() != null && !searchDTO.getArea().isEmpty()) {
				predicates.add(criteryBuilder.like(root.get("area").as(String.class), "%"+searchDTO.getArea().toString()+"%"));
			}
			if(searchDTO.getCodigoPieza() != null) {
				Producto_id idProducto= new Producto_id(searchDTO.getEmpresa().getNit(),searchDTO.getCodigoPieza());
				predicates.add(criteryBuilder.equal(root.get("idProducto"), idProducto));
			}
			if(searchDTO.getDescripcion() != null && !searchDTO.getDescripcion().isEmpty()) {
				predicates.add(criteryBuilder.like(root.get("descripcion"), "%"+searchDTO.getDescripcion()+"%"));
			}
			if(searchDTO.getEmpresa() != null) {
				predicates.add(criteryBuilder.equal(root.get("empresa"), searchDTO.getEmpresa()));
			}
			if(searchDTO.getEstado() != null) {				
				predicates.add(criteryBuilder.equal(root.get("estado"), searchDTO.getEstado()));
			}
			if(searchDTO.getFabricante() != null) {
				predicates.add(criteryBuilder.equal(root.get("fabricante"), searchDTO.getFabricante()));
			}
			if(searchDTO.getFamilia() != null) {
				predicates.add(criteryBuilder.equal(root.get("familia"), searchDTO.getFamilia()));
			}
			if(searchDTO.getOrden() != null) {
				predicates.add(criteryBuilder.like(root.get("orden"), "%"+searchDTO.getOrden()+"%"));
			}
			if(searchDTO.getUbicacion() != null) {
				predicates.add(criteryBuilder.equal(root.get("ubicacion"), searchDTO.getUbicacion()));
			}
			if(searchDTO.getVerificado() != null && searchDTO.getVerificado() ) {
				
				predicates.add(criteryBuilder.isTrue(root.get("verificado").as(Boolean.class)));
			}
			if(searchDTO.getVerificado() != null && !searchDTO.getVerificado() ) {
							
				predicates.add(criteryBuilder.isFalse(root.get("verificado").as(Boolean.class)));
			}
			if(searchDTO.getEstaActivo() != null && searchDTO.getEstaActivo()) {
				predicates.add(criteryBuilder.isTrue(root.get("estaActivo").as(Boolean.class)));				
			}
			if(searchDTO.getEstaActivo() != null && !searchDTO.getEstaActivo()) {
				predicates.add(criteryBuilder.isFalse(root.get("estaActivo").as(Boolean.class)));				
			}
			if(searchDTO.getMotivoIngreso() != null) {
				predicates.add(criteryBuilder.like(root.get("motivoIngreso"), "%"+searchDTO.getMotivoIngreso()+"%"));
			}
			
			
			
			query.orderBy(criteryBuilder.desc(root.get("descripcion")));
			
			return criteryBuilder.and(predicates.toArray(new Predicate[0]));
		};
	}
		
	public Specification<Producto> getProductosActivos(String letras, Empresa empresa){
		return (root, query, criteryBuilder) ->{
			List<Predicate> predicates = new ArrayList<>();
			System.out.println("Specification "+letras);
			predicates.add(criteryBuilder.like(root.get("descripcion"), "%"+letras+ "%"));
			
			predicates.add(criteryBuilder.equal(root.get("empresa"), empresa));

			predicates.add(criteryBuilder.isTrue(root.get("estaActivo").as(Boolean.class)));
			
			
			return criteryBuilder.and(predicates.toArray(new Predicate[0]));
		};
	}

	public Specification<Producto> getVerificacion(String orden, String filtro, Empresa empresa) {
		return (root, query, criteryBuilder) ->{
			
			List<Predicate> predicates = new ArrayList<>();
			System.out.println("Specification orden " + orden);
			System.out.println("Specification filtro " + filtro);
			if(orden != null) {
				predicates.add(criteryBuilder.equal(root.get("orden"), orden));				
			}
			if(filtro != null && filtro.equalsIgnoreCase("faltantes")) {
				predicates.add(criteryBuilder.equal(root.get("empresa"), empresa));
				predicates.add(criteryBuilder.isTrue(root.get("importado").as(Boolean.class)));
				predicates.add(criteryBuilder.isFalse(root.get("verificado").as(Boolean.class)));
			}
			if(filtro != null && filtro.equalsIgnoreCase("sobrantes" )) {
				predicates.add(criteryBuilder.equal(root.get("empresa"), empresa));
				predicates.add(criteryBuilder.isFalse(root.get("importado").as(Boolean.class)));
				predicates.add(criteryBuilder.isTrue(root.get("verificado").as(Boolean.class)));
			}
			if(filtro != null && filtro.equalsIgnoreCase("ok" )) {
				predicates.add(criteryBuilder.equal(root.get("empresa"), empresa));
				predicates.add(criteryBuilder.isTrue(root.get("importado").as(Boolean.class)));
				predicates.add(criteryBuilder.isTrue(root.get("verificado").as(Boolean.class)));
			}
						
			return criteryBuilder.and(predicates.toArray(new Predicate[0]));
		};
	}
}
