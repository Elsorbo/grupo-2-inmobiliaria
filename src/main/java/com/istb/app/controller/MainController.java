
package com.istb.app.controller;

import java.util.List;
import java.util.stream.Collectors;

import com.istb.app.repository.EmpleadoRepositoryI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {

	@Autowired
	private EmpleadoRepositoryI employeeManager;

	@GetMapping("/")
	public String main(Model attributes) {

		return "index";

	}
	
	@GetMapping("/dashboard")
	public String dashboard(Model attributes, Authentication account) {
		
		List<String> roles = account.getAuthorities()
			.stream()
			.map( rol -> rol.getAuthority().replace("ROLE_", "") )
			.collect(Collectors.toList());
		
		attributes.addAttribute("sectionTitle", "dashboard");
		
		if(roles.contains("ADMINISTRADOR")) {
			return "redirect:/empleados"; }
		
		if(roles.contains("EMPLEADO")) {
			return "redirect:/inmuebles"; }
		
		if(roles.contains("ADMINISTRADOR")) {
			return "redirect:/facturas"; }

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
		attributes.addAttribute("paginator", 
			employeeManager.findAll( PageRequest.of(0, 5) ));
		
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
