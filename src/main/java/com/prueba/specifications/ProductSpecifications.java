package com.prueba.specifications;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.Predicate;

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
				System.out.println(searchDTO.getArea().getClass()+" 1");
				predicates.add(criteryBuilder.like(root.get("area"), "%"+searchDTO.getArea()+"%"));
			}
			if(searchDTO.getCodigoPieza() != null) {
				predicates.add(criteryBuilder.equal(root.get("codigoPieza"), searchDTO.getCodigoPieza()));
			}
			if(searchDTO.getDescripcion() != null && searchDTO.getDescripcion().isEmpty()) {
				predicates.add(criteryBuilder.like(root.get("descripcion"), "%"+searchDTO.getDescripcion()+"%"));
			}
			if(searchDTO.getEmpresa() != null) {
				predicates.add(criteryBuilder.equal(root.get("empresa"), searchDTO.getEmpresa()));
			}
			if(searchDTO.getEstado() != null) {
				
				predicates.add(criteryBuilder.equal(root.get("estado"), searchDTO.getEstado()));
			}
			if(searchDTO.getFabricante() != null) {
				String aux = searchDTO.getFabricante().getNit().toString();
				System.out.println(root.<String>get("fabricante").as(String.class).toString()+" 2 "+searchDTO.getFabricante().getNit().toString());
				predicates.add(criteryBuilder.like(root.<String>get("fabricante"), "%"+ aux +"%"));
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
}
