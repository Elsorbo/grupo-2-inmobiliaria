package com.istb.app.entity;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "detalle_facturas")
public class DetalleFactura implements Serializable {

	private static final long serialVersionUID = 4171317251529112340L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	private String concepto;
	
	private Double monto;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JsonIgnoreProperties({"detalles"})
	private Factura factura;
}
