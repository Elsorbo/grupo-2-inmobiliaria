
package com.istb.app.entity;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Collection;

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
import javax.persistence.OneToOne;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

import lombok.Data;

@Data
@Entity
@Table(name = "usuarios")
public class Usuario implements Serializable {
	 
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	@ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "role_usuario", 
    	joinColumns = @JoinColumn(name = "usuario_id", referencedColumnName = "id"), 
    	inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id"))
	@JsonIgnoreProperties({"usuarios"})
	private Collection<Role> roles;
	
	@Pattern(
		regexp="^([0-1][0-9]|2[0-4])([0-5])([0-9]{6})([0-9])$", 
		message="El campo cédula es invalido.")
	private String cedula;
	
	@NotEmpty(message = "Los nombres son requeridos.")
	private String nombres;
	
	@NotEmpty(message = "Los apellidos son requeridos.")
	private String apellidos;
	
	@Column(name = "url_imagen_perfil")
	private String urlImagenPerfil;
	
	@Column(name = "nombre_imagen_perfil")
	private String nombreImagenPerfil;
	
	private String usuario;
	
	@NotEmpty(message = "El correo electrónico es requerido.")
	@Email(message = "El correo no tiene un formato válido.")
	private String correo;
	
	// @NotEmpty(message = "La contraseña es requerida.")
	@JsonProperty(access = Access.WRITE_ONLY)
	private String contrasena;
	
	private Boolean estado;
	
	@OneToOne(mappedBy = "usuario", cascade = CascadeType.ALL)
	@JsonIgnoreProperties(value = {"usuario"}, allowSetters = true)
	private Empleado empleado;
	
	@OneToOne(mappedBy = "usuario")
	@JsonIgnoreProperties(value = {"usuario", "empleado", "inmuebles", "reparaciones"}, allowSetters = true)
	private Arrendatario arrendatario;
	
	@Column(name = "fecha_creacion", updatable = false)
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private LocalDateTime fechaCreacion;
	
	@Column(name = "fecha_actualizacion")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private LocalDateTime fechaActualizacion;
	
	private String descripcion;

	public Usuario(){}

	@PrePersist
	public void preCreated () {
		this.fechaCreacion = LocalDateTime.now();
		this.fechaActualizacion = LocalDateTime.now();
	}

	@PreUpdate
	public void preUpdated () {
		this.fechaActualizacion = LocalDateTime.now();
	}

	public void setContrasena(String newPassword) {

		this.contrasena = newPassword;
		
	}

	@Override
	public String toString() {

		return String.format("[Usuario: %s - %s]", this.usuario, this.cedula);
		
	}

}
