
package com.istb.app.entity;

import java.io.Serializable;
import java.util.Collection;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

@Data
@Entity
@Table(name = "arrendatarios")
public class Arrendatario implements Serializable  {
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	@OneToOne
	@JoinColumn(name = "usuario_id")
	@JsonIgnoreProperties({"arrendatario", "empleado"})
	private Usuario usuario;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JsonIgnoreProperties({"arrendatarios"})
	@JoinColumn(name = "empleado_id")
	private Empleado empleado;
	
	@ManyToMany
    @JoinTable(name = "arrendatario_inmueble", 
    joinColumns = @JoinColumn(name = "arrendatario_id", referencedColumnName = "id"), 
    inverseJoinColumns = @JoinColumn(name = "inmueble_id", referencedColumnName = "id"))
	private Collection<Inmueble> inmuebles;
	
	@OneToMany(mappedBy = "arrendatario", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JsonIgnoreProperties({"arrendatario"})
	private Collection<Reparacion> reparaciones;
	
	@OneToMany(mappedBy = "arrendatario", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JsonIgnoreProperties({"arrendatario"})
	private Collection<ReciboPago> recibos;
	
	@OneToMany(mappedBy = "arrendatario", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JsonIgnoreProperties({"arrendatario"})
	private Collection<Notificacion> notificaciones;
	
	@OneToMany(mappedBy = "arrendatario", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JsonIgnoreProperties({"arrendatario"})
	private Collection<Factura> facturas;

	public Arrendatario(Usuario user) {

		this.usuario = user;
		
	}

	@Override
	public String toString() {

		return "[Arrendatario: ]";

	}

}
