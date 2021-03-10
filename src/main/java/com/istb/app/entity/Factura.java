
package com.istb.app.entity;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Collection;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "facturas")
public class Factura implements Serializable {

	private static final long serialVersionUID = 4286844219230991867L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	private Double total;
	
	@Column(name = "fecha_admision")
	private LocalDate fechaAdmision;
	
	private String descripcion;
	
	@ManyToOne
	@JsonIgnoreProperties({"facturas", "reparaciones", "notificaciones"})
	private Arrendatario arrendatario;
	
	@OneToMany(mappedBy = "factura", cascade = CascadeType.ALL)
	@JsonIgnoreProperties({"factura"})
	private Collection<DetalleFactura> detalles;

	@Override
	public String toString() {
		
		return String.format("[Factura: %s]", 
			this.arrendatario.getUsuario().getUsuario());

	}

}
