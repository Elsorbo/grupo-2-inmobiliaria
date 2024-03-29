
package com.istb.app.entity;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

@Data
@Entity
@Table(name = "inmuebles")
public class Inmueble implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	private String titulo;
	
	private String descripcion;
	
	private long area;
	
	private boolean alquilado;
	
	private boolean comercializado;

	private long precio;

	private String localidad;

	@Column(name = "fecha_creacion", updatable = false)
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private LocalDateTime fechaCreacion;
	
	@Column(name = "fecha_actualizacion")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private LocalDateTime fechaActualizacion;
	
	@ManyToMany(mappedBy = "inmuebles", fetch = FetchType.EAGER)
	@JsonIgnoreProperties({"inmuebles", "arrendatarios"})
	private Set<Empleado> empleados;
	
	@ManyToMany(mappedBy = "inmuebles")
	@JsonIgnoreProperties({"inmuebles", "reparaciones", "empleado"})
	private Collection<Arrendatario> arrendatarios;
	
	@NotEmpty(message = "Se necesita al menos una foto para el inmueble")
	@OneToMany(mappedBy = "inmueble", cascade = CascadeType.ALL)
	@JsonIgnoreProperties({"inmueble"})
	private Collection<Fotos> fotos;
	
	@ManyToMany
	@JoinTable(
		name="inmueble_servicio",
		joinColumns =  @JoinColumn(name = "inmueble_id", nullable = false),
		inverseJoinColumns = @JoinColumn(name = "servicio_id", nullable = false)
	)
	@JsonIgnoreProperties({"inmuebles"})
	private Collection<Servicio> servicios;
	
	@PrePersist
	public void preCreated () {
		this.fechaCreacion = LocalDateTime.now();
		this.fechaActualizacion = LocalDateTime.now();
	}

	@PreUpdate
	public void preUpdated () {
		this.fechaActualizacion = LocalDateTime.now();
	}

	@Override
	public String toString() {

		return String.format("[Inmueble: %s]", this.titulo);

	}

}
