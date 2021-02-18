
package com.istb.app.entity;

import java.util.Collection;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnore;

import org.springframework.security.core.GrantedAuthority;

import lombok.Data;

@Data
@Entity
@Table(name = "roles")
public class Role implements GrantedAuthority {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	@Size(min = 3, max = 255, message = "El rol debe ser mayor a 3 y menor a 255 caracteres.")
	@NotEmpty(message = "El nombre del rol es obligatorio.")
	private String nombre;

	@JsonIgnore
	@ManyToMany(mappedBy = "roles", fetch = FetchType.EAGER)
	private Collection<Usuario> usuarios;

	public Role() {}

	@Override
	public String getAuthority() {
	
		return this.nombre;
	
	}

	@Override
	public String toString() {

		return this.nombre;
		
	}

}
