package com.istb.app.util.enums;

public enum EstadoReparacion {
	
	/**
	 * {@enum Sugerida}.
	 * 
	 */
	SUGERIDA("Sugerida"), 
	
	
	/**
	 * {@enum Enviada}.
	 * 
	 */
	ENVIADA("Enviada"),
	
	/**
	 * {@enum Enviada}.
	 * 
	 */
	ACEPTADA("Aceptada");
	
	private final String value;
	
	private EstadoReparacion (String value) {
		this.value = value;
	}
	
	public String value() {
		return this.value;
	}
}
