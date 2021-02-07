
package com.istb.app.entity;

import java.io.Serializable;
import java.util.Collection;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Servicio
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="servicios")
public class Servicio implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@NotEmpty(message = "Necesita otorgar un nombre al servicio")
	private String nombre;

	@ManyToMany(mappedBy = "servicios", fetch = FetchType.LAZY)
	private Collection<Inmueble> inmuebles;
	
}
