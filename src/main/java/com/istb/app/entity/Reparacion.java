package com.istb.app.entity;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.istb.app.util.enums.EstadoReparacion;

import lombok.Data;

@Data
@Entity
@Table(name = "reparaciones")
public class Reparacion implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	@NotNull(message = "Se requiere una descripción")
	private String descripcion;
	
	@Column(name = "monto_estimado")
	@NotNull(message = "Se requiere una estimación del monto.")
	private Double montoEstimado;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "estado", length = 12)
	private EstadoReparacion estado;
	
	@ManyToOne
	@JsonIgnoreProperties({"reparaciones"})
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

	@Override
	public String toString() {

		return String.format("[Reparacion: %d]", this.id);

	}

}
