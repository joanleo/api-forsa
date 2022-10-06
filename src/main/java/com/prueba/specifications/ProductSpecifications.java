package com.prueba.specifications;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.Predicate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import com.prueba.dto.SearchDTO;
import com.prueba.entity.Empresa;
import com.prueba.entity.Fabricante;
import com.prueba.entity.Producto;

import com.prueba.exception.ResourceNotFoundException;
import com.prueba.repository.FabricanteRepository;

@Component
public class ProductSpecifications {

	@Autowired
	private FabricanteRepository fabricanteRepo;
	

	public Specification<Producto> getProductos(SearchDTO searchDTO, Empresa empresa){
		return (root, query, criteryBuilder) ->{
			
			if(searchDTO.getEmpresa() == null) {
				searchDTO.setEmpresa(empresa);
			}
			
			List<Predicate> predicates = new ArrayList<>();
			
			if(searchDTO.getCodigoPieza() != null) {
				predicates.add(criteryBuilder.equal(root.get("codigoPieza"), searchDTO.getCodigoPieza()));
			}
			if(searchDTO.getDescripcion() != null && !searchDTO.getDescripcion().isEmpty()) {
				predicates.add(criteryBuilder.like(root.get("descripcion"), "%"+searchDTO.getDescripcion()+"%"));
			}
			if(searchDTO.getArea() != null && !searchDTO.getArea().isEmpty()) {
				predicates.add(criteryBuilder.equal(root.get("area"), Float.parseFloat(searchDTO.getArea())));
			}
			if(searchDTO.getOrden() != null) {
				predicates.add(criteryBuilder.like(root.get("orden"), "%"+searchDTO.getOrden()+"%"));
			}
			if(searchDTO.getFabricante() != null) {
				Fabricante fabricante = fabricanteRepo.findByNitAndEmpresa(searchDTO.getFabricante().getNit(), empresa)
						.orElseThrow(() -> new ResourceNotFoundException("Fabricante", "nit", searchDTO.getFabricante().getNit()));
				predicates.add(criteryBuilder.equal(root.get("fabricante"), fabricante));
			}
			if(searchDTO.getFamilia() != null) {
				predicates.add(criteryBuilder.equal(root.get("familia"), searchDTO.getFamilia()));
			}
			if(searchDTO.getTipo() != null) {
				predicates.add(criteryBuilder.equal(root.get("tipo"), searchDTO.getTipo()));
			}
			if(searchDTO.getEstado() != null) {				
				predicates.add(criteryBuilder.equal(root.get("estado"), searchDTO.getEstado()));
			}
			if(searchDTO.getEmpresa() != null) {
				predicates.add(criteryBuilder.equal(root.get("empresa"), searchDTO.getEmpresa()));
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
			if(searchDTO.getMedidas() != null && !searchDTO.getMedidas().isEmpty()) {
				predicates.add(criteryBuilder.like(root.get("medidas"), "%"+searchDTO.getMedidas()+"%"));
			}
			if(searchDTO.getReviso() != null) {
				predicates.add(criteryBuilder.equal(root.get("reviso"), searchDTO.getReviso()));
			}
			if(searchDTO.getEstaActivo() == null) {
				predicates.add(criteryBuilder.isTrue(root.get("estaActivo").as(Boolean.class)));				
			}
			
			
			query.orderBy(criteryBuilder.desc(root.get("descripcion")));
			
			return criteryBuilder.and(predicates.toArray(new Predicate[0]));
		};
	}
		
	public Specification<Producto> getProductosActivos(String letras, Empresa empresa){
		return (root, query, criteryBuilder) ->{
			List<Predicate> predicates = new ArrayList<>();
			Predicate predicateDecripcion = criteryBuilder.like(root.get("descripcion"), "%"+letras+ "%");
			//predicates.add(criteryBuilder.like(root.get("descripcion"), "%"+letras+ "%"));
			Predicate predicateCodigoPieza = criteryBuilder.like(root.get("codigoPieza"), "%"+letras+ "%");
			//predicates.add(criteryBuilder.like(root.get("codigoPieza"), "%"+letras+ "%"));
			predicates.add(criteryBuilder.or(predicateDecripcion, predicateCodigoPieza));
			predicates.add(criteryBuilder.equal(root.get("empresa"), empresa));

			predicates.add(criteryBuilder.isTrue(root.get("estaActivo").as(Boolean.class)));
			
			
			return criteryBuilder.and(predicates.toArray(new Predicate[0]));
		};
	}

	public Specification<Producto> getVerificacion(String orden, String filtro, Empresa empresa) {
		return (root, query, criteryBuilder) ->{
			
			List<Predicate> predicates = new ArrayList<>();
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
			predicates.add(criteryBuilder.isTrue(root.get("estaActivo").as(Boolean.class)));
						
			return criteryBuilder.and(predicates.toArray(new Predicate[0]));
		};
	} 
}
