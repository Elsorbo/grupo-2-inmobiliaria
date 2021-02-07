
package com.istb.app.controller;

import java.security.Principal;

import com.istb.app.entity.Usuario;
import com.istb.app.services.accounts.AccountsServiceI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;

@RestController
@RequestMapping("/accounts")
public class AccountController {
	
	@Autowired
	private AccountsServiceI accountService;

	@GetMapping("/empleados")
	public String getEmployees(Usuario newUser) {

		return "empleado";

	}

	@PostMapping("/empleados")
	public ResponseEntity<?> addNewEmployee(@RequestBody Usuario newUser) {

		return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON)
			.body(accountService.createEmployeeAccount(newUser));

	}
	
	@PostMapping("/arrendatarios")
	public ResponseEntity<?> addNewTenant(@RequestBody Usuario newUser) {

		return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON)
			.body(accountService.createTenantAccount(newUser));

	}
	
	@GetMapping("/login")
	public RedirectView login(
		@RequestParam(required = false) String error,
		@RequestParam(required = false) String logout, 
		Principal principal) {

		return (new RedirectView("/"));

	}
	
}
