package com.istb.app.entity;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Collection;

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

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "usuarios")
public class Usuario implements Serializable {
	 
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	@ManyToMany
    @JoinTable(name = "role_usuario", 
    joinColumns = @JoinColumn(name = "usuario_id", referencedColumnName = "id"), 
    inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id")) 
	private Collection<Role> roles;
	
	@NotEmpty(message = "El campo cédula es requerido.")
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
	
	@NotEmpty(message = "La contraseña es requerida.")
	private String contrasena;
	
	private Boolean estado;
	
	@OneToOne(mappedBy = "usuario", fetch = FetchType.LAZY)
	@JsonIgnoreProperties({"usuario"})
	private Empleado empleado;
	
	@OneToOne(mappedBy = "usuario", fetch = FetchType.LAZY)
	@JsonIgnoreProperties({"usuario"})
	private Arrendatario arrendatario;
	
	@Column(name = "fecha_creacion", updatable = false)
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private LocalDateTime fechaCreacion;
	
	@Column(name = "fecha_actualizacion")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private LocalDateTime fechaActualizacion;
	
	@PrePersist
	public void preCreated () {
		this.fechaCreacion = LocalDateTime.now();
		this.fechaActualizacion = LocalDateTime.now();
	}

	@PreUpdate
	public void preUpdated () {
		this.fechaActualizacion = LocalDateTime.now();
	}
}
