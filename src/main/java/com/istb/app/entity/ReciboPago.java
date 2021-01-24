package com.istb.app.entity;

import java.io.Serializable;
import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.Table;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "recibo_pagos")
public class ReciboPago implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	@Column(name = "url_imagen")
	private String urlImagen;
	
	@Column(name = "nombre_imagen")
	private String nombreImagen;
	
	@Column(name = "fecha_creacion", updatable = false)
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private LocalDateTime fechaCreacion;
	
	@Column(name = "periodo_pago")
	private String periodoPago;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JsonIgnoreProperties({"recibos"})
	private Arrendatario arrendatario;
	
	@PrePersist
	public void preCreated () {
		this.fechaCreacion = LocalDateTime.now();
	}
}
