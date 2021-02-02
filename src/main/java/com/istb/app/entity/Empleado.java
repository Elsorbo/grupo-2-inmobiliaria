package com.istb.app.entity;

import java.io.Serializable;
import java.util.Collection;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
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
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "empleados")
public class Empleado implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	@Size(min = 9, max = 10, message = "El teléfono debe no más de 10 dígitos.")
	private String telefono;
	
	@OneToOne
	@JoinColumn(name = "usuario_id")
	@JsonIgnoreProperties({"empleado", "arrendatario"})
	private Usuario usuario;
	
	@OneToMany(mappedBy = "empleado", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JsonIgnoreProperties({"empleado"})
	@OrderBy("id desc")
	private Collection<Arrendatario> arrendatarios;
	
	@ManyToMany
    @JoinTable(name = "empleado_inmueble", 
    joinColumns = @JoinColumn(name = "empleado_id", referencedColumnName = "id"), 
    inverseJoinColumns = @JoinColumn(name = "inmueble_id", referencedColumnName = "id"))
	private Collection<Inmueble> inmuebles;
}
