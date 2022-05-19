package com.prueba.specifications;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import com.prueba.dto.SearchDTO;
import com.prueba.entity.Producto;

@Component
public class ProductSpecifications {


	public Specification<Producto> getProductos(SearchDTO searchDTO){
		return (root, query, criteryBuilder) ->{
			
			List<Predicate> predicates = new ArrayList<>();
			
			if(searchDTO.getArea() != null && !searchDTO.getArea().isEmpty()) {
				System.out.println(searchDTO.getArea().toString());
				predicates.add(criteryBuilder.like(root.get("area").as(String.class), "%"+searchDTO.getArea().toString()+"%"));
			}
			if(searchDTO.getCodigoPieza() != null) {
				predicates.add(criteryBuilder.like(root.get("codigoPieza"), "%"+searchDTO.getCodigoPieza()+"%"));
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
			
			query.orderBy(criteryBuilder.desc(root.get("codigoPieza")));
			
			return criteryBuilder.and(predicates.toArray(new Predicate[0]));
		};
	}
		
		public Specification<Producto> getProductosActivos(String letras){
			return (root, query, criteryBuilder) ->{
				List<Predicate> predicates = new ArrayList<>();
				
				predicates.add(criteryBuilder.like(root.get("descripcion"), "%"+letras+ "%"));
				//Root<Producto> activo = query.from(Producto.class);
				predicates.add(criteryBuilder.isTrue(root.get("estaActivo").as(Boolean.class)));
				
				
				return criteryBuilder.and(predicates.toArray(new Predicate[0]));
		};
	}
}
