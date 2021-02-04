
package com.istb.app.controller;

import java.security.Principal;

import com.istb.app.entity.Usuario;
import com.istb.app.repository.UsuarioRepositoryI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;

@Controller
public class Settings {
	
	@Autowired
	private UsuarioRepositoryI userManager;

	@GetMapping("/perfil")
	public String profile(Principal account, Model attributes) {

		attributes.addAttribute("account", 
			userManager.findByUsuario(account.getName()));

		return "perfil";

	}
	
	@PutMapping("/perfil")
	public ResponseEntity<?> updateProfile(Usuario account) {

		// To do..

		return ResponseEntity.ok()
			.contentType(MediaType.APPLICATION_JSON)
			.body(null);

	}
	
}
