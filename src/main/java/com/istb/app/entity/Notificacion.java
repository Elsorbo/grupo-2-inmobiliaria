package com.istb.app.entity;

import java.io.Serializable;
import java.time.LocalDate;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

@Data
@Entity
@Table(name = "notificaciones")
public class Notificacion implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	private String titulo;
	
	@Lob
	private String detalles;
	
	@Column(name = "fecha_generacion")
	private LocalDate fechaGeneracion;
	
	@Column(name = "fecha_limite")
	private LocalDate fechaLimite;
	
	@ManyToOne
	@JsonIgnoreProperties({"notificaciones"})
	private Arrendatario arrendatario;

	@Override
	public String toString() {

		return String.format("[Notificacion: %s - %s]", 
			this.arrendatario.getUsuario().getUsuario(),
			this.titulo );
		
	}

}
