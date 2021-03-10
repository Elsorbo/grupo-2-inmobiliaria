package com.istb.app.entity;

import java.io.Serializable;
import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

@Data
@Entity
@Table(name = "recibo_pagos")
public class ReciboPago implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@NotEmpty(message = "Se requiere la imagen")
	@Column(name = "url_imagen")
	private String urlImagen;
	
	@NotEmpty(message = "Por favor seleccione una imagen")
	@Column(name = "nombre_imagen")
	private String nombreImagen;
	
	@Column(name = "fecha_creacion", updatable = false)
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private LocalDateTime fechaCreacion;
	
	@NotEmpty(message = "Se requiere el periodo de pago")
	@Column(name = "periodo_pago")
	private String periodoPago;
	
	@Column(name = "facturado")
	boolean facturado;

	@ManyToOne
	@JsonIgnoreProperties({"recibos", "reparaciones", "notificaciones"})
	private Arrendatario arrendatario;
	
	@PrePersist
	public void preCreated () {
		
		this.fechaCreacion = LocalDateTime.now();
		this.periodoPago = String.format("%s del %s", 
			this.periodoPago, this.fechaCreacion.getYear());

	}

	@Override
	public String toString() {

		return String.format("[Recibo de pago: %s]", 
			this.arrendatario.getUsuario().getApellidos());
	}

}
