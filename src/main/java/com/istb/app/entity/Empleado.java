
package com.istb.app.entity;

import java.io.Serializable;
import java.util.Collection;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.validation.Valid;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "empleados")
public class Empleado implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	@Size(min = 0, max = 10, message = "El teléfonon no debe tener más de 10 dígitos.")
	private String telefono;

	@Valid
	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "usuario_id")
	@JsonIgnoreProperties(value = {"empleado", "arrendatario"}, allowSetters = true)
	private Usuario usuario;
	
	@OneToMany(mappedBy = "empleado", cascade = CascadeType.ALL)
	@JsonIgnoreProperties({"empleado", "inmuebles", "reparaciones"})
	@OrderBy("id desc")
	private Collection<Arrendatario> arrendatarios;
	
	@ManyToMany
    @JoinTable(name = "empleado_inmueble", 
    	joinColumns = @JoinColumn(name = "empleado_id", referencedColumnName = "id"), 
    	inverseJoinColumns = @JoinColumn(name = "inmueble_id", referencedColumnName = "id")
	)
	@JsonIgnoreProperties({"empleados", "arrendatarios"})
	private Collection<Inmueble> inmuebles;
	
	public Empleado() {}
	
	public Empleado(Usuario user, String telefono) {

		this.usuario = user;
		this.telefono = telefono;

	}

	@Override
	public String toString() {

		return this.telefono;
		
	}

}
