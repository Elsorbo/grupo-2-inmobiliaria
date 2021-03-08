package com.istb.app.entity;

import java.io.Serializable;
import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Data;

@Data
@Entity
@Table(name = "notificaciones")
public class Notificacion implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	@NotEmpty(message = "Se requiere un título para la notificación")
	private String titulo;
	
	@NotEmpty(message = "Es necesario indicar los detalles de la notificación")
	private String detalles;
	
	@Column(name = "fecha_generacion")
	private LocalDate fechaGeneracion;
	
	@Column(name = "fecha_limite")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate fechaLimite;
	
	@ManyToOne
	@JsonIgnoreProperties({"notificaciones"})
	private Arrendatario arrendatario;
	
	@PrePersist
	public void preCreated () {
		
		this.fechaGeneracion = LocalDate.now();
		
		if( this.fechaLimite == null ) { 
			this.fechaLimite = LocalDate.now(); }
	
	}

	@Override
	public String toString() {

		return String.format("[Notificacion: %s - %s]", 
			this.arrendatario.getUsuario().getUsuario(),
			this.titulo );
		
	}

}
