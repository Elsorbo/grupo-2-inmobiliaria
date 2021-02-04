
package com.istb.app.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {

	@GetMapping("/")
	public String main(Model attributes) {

		return "index";

	}
	
	@GetMapping("/dashboard")
	public String dashboard(Model attributes) {

		attributes.addAttribute("sectionTitle", "dashboard");
		
		return "dashboard";
		
	}
	
	@GetMapping("/inmuebles")
	public String getInmueble(Model attributes) {

		attributes.addAttribute("sectionTitle", "inmuebles");
		
		return "inmueble";
		
	}
	
	@GetMapping("/reparaciones")
	public String getReparaciones(Model attributes) {

		attributes.addAttribute("sectionTitle", "reparaciones");
		
		return "reparacion";
		
	}

	@GetMapping("/facturas")
	public String getFacturas(Model attributes) {

		attributes.addAttribute("sectionTitle", "facturas");
		
		return "factura";
		
	}

	@GetMapping("/desalojo")
	public String getNotificacionesDesalojo(Model attributes) {

		attributes.addAttribute("sectionTitle", "notificaiones de desalojo");
		
		return "desalojo";
		
	}

	@GetMapping("/empleados")
	public String getEmpleados(Model attributes) {

		attributes.addAttribute("sectionTitle", "empleados");
		
		return "empleado";
		
	}

	@GetMapping("/recibos")
	public String getRecibosPago(Model attributes) {

		attributes.addAttribute("sectionTitle", "recibos");
		
		return "recibo";
		
	}

	@GetMapping("/arrendatarios")
	public String getArrendatarios(Model attributes) {

		attributes.addAttribute("sectionTitle", "arrendatarios");
		
		return "arrendatario";
		
	}

}
