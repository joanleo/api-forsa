package com.prueba.specifications;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.criteria.Predicate;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import com.prueba.entity.Empresa;
import com.prueba.entity.MovInventario;

@Component
public class InventarioSpecifications {

	public Specification<MovInventario> getInvenId(String id, Empresa empresa){
		return (root, query, criteryBuilder) ->{
			List<Predicate> predicates = new ArrayList<>();
			
			predicates.add(criteryBuilder.like(root.get("idMov").as(String.class), "%"+id+ "%"));
			
			predicates.add(criteryBuilder.equal(root.get("empresa"), empresa));
			
			return criteryBuilder.and(predicates.toArray(new Predicate[0]));
		};
	}
	
	public Specification<MovInventario> getInvBetweenDate(Empresa empresa, Date desde, Date hasta) {
		return (root, query, criteryBuilder) ->{
			
			List<Predicate> predicates = new ArrayList<>();
			
			predicates.add(criteryBuilder.equal(root.get("empresa"), empresa));
			System.out.println(desde);
			
			System.out.println(root.<Date>get("fecha").toString());
			predicates.add(criteryBuilder.lessThanOrEqualTo(root.<Date>get("fecha"), hasta));
			
			return criteryBuilder.and(predicates .toArray(new Predicate[0]));
		};
	}
}
