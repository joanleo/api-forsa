package com.prueba.entity;

import java.util.List;
import java.util.Objects;

import javax.persistence.CascadeType;
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
@Table(name = "m_tipos")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class TipoActivo {
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE,
    		generator = "seq_tipo_activo")
	@SequenceGenerator(name = "seq_tipo_activo", allocationSize = 10)
	@Column(name="nidtipo")
	private Long id;
	
	@Column(name="vcnombre")
	private String nombre;
	
	@JsonIgnore
    @OneToMany(mappedBy = "tipo", cascade = CascadeType.ALL)
    private List<Producto> productos;
	
	@JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "vcnitempresa", referencedColumnName = "vcnitempresa")
    private Empresa empresa;
	
	@Column(name = "bestaActivo", columnDefinition="BOOLEAN NOT NULL DEFAULT 1")
    private Boolean estaActivo = true;

	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "nidfamilia", referencedColumnName = "nidfamilia",  nullable = false)
    private Familia familia;

	public TipoActivo() {
		super();
	}

	public TipoActivo(String nombre, Empresa empresa, Familia familia) {
		super();
		this.nombre = nombre;
		this.empresa = empresa;
		this.familia = familia;
	}

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

	public List<Producto> getProductos() {
		return productos;
	}

	public void setProductos(List<Producto> productos) {
		this.productos = productos;
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

	@Override
	public int hashCode() {
		return Objects.hash(empresa, estaActivo, familia, id, nombre, productos);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TipoActivo other = (TipoActivo) obj;
		return Objects.equals(empresa, other.empresa) && Objects.equals(estaActivo, other.estaActivo)
				&& Objects.equals(familia, other.familia) && Objects.equals(id, other.id)
				&& Objects.equals(nombre, other.nombre) && Objects.equals(productos, other.productos);
	}

}
