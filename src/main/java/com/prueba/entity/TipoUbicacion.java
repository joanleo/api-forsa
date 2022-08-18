package com.prueba.entity;

import java.util.List;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name = "m_tipos_ubicacion")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class TipoUbicacion {
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE,
    		generator = "seq_tipo_ubicacion")
	@SequenceGenerator(name = "seq_tipo_ubicacion", allocationSize = 10)
	@Column(name = "nidtipo_ubicacion")
	private Long id;
	
	@Column(name = "vcnombre")
	private String nombre;
	
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "vcnitempresa", referencedColumnName = "vcnitempresa")
    private Empresa empresa;
	
	@Column(name = "bestaActivo", columnDefinition="BOOLEAN NOT NULL DEFAULT 1")
	private Boolean estaActivo=true;
	
	@JsonIgnore
	@OneToMany(mappedBy = "tipo")
	private List<Ubicacion> ubicaciones;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public Empresa getEmpresa() {
		return empresa;
	}

	public void setEmpresa(Empresa empresa) {
		this.empresa = empresa;
	}

	public Boolean getEstaActivo() {
		return estaActivo;
	}

	public void setEstaActivo(Boolean estaActivo) {
		this.estaActivo = estaActivo;
	}	

	public List<Ubicacion> getUbicaciones() {
		return ubicaciones;
	}

	public void setUbicaciones(List<Ubicacion> ubicaciones) {
		this.ubicaciones = ubicaciones;
	}

	
	
	
	
	public TipoUbicacion() {
		super();
	}

	public TipoUbicacion(Long id, String nombre, Empresa empresa, Boolean estaActivo) {
		super();
		this.id = id;
		this.nombre = nombre;
		this.empresa = empresa;
		this.estaActivo = estaActivo;
	}

	@Override
	public int hashCode() {
		return Objects.hash(empresa, estaActivo, id, nombre, ubicaciones);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TipoUbicacion other = (TipoUbicacion) obj;
		return Objects.equals(empresa, other.empresa) && Objects.equals(estaActivo, other.estaActivo)
				&& Objects.equals(id, other.id) && Objects.equals(nombre, other.nombre)
				&& Objects.equals(ubicaciones, other.ubicaciones);
	}

	

}
