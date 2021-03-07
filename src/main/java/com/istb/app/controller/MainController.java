
package com.istb.app.controller;

import java.time.LocalDate;

import com.istb.app.util.AccountUtils;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {

	@GetMapping("/")
	public String mainPage(Model attributes) {

		return "index";

	}
	
	@GetMapping("/dashboard")
	public String dashboard(Model attributes, Authentication account) {
		
		attributes.addAttribute("sectionTitle", "dashboard");
		
		if(AccountUtils.hasRole(account, "ADMINISTRADOR")) {
			return "redirect:/empleados"; }
		
		if(AccountUtils.hasRole(account, "EMPLEADO")) {
			return "redirect:/arrendatarios"; }

		if(AccountUtils.hasRole(account, "ARRENDATARIO")) {
			return "redirect:/recibos"; }

		return "dashboard";
		
	}
	
	@GetMapping("/facturas")
	public String getFacturas(Model attributes) {

		attributes.addAttribute("sectionTitle", "facturas");
		
		return "factura";
		
	}

	@GetMapping("/recibos")
	public String getRecibosPago(Model attributes) {

		attributes.addAttribute("sectionTitle", "recibos");
		
		return "recibo";
		
	}
		
}
