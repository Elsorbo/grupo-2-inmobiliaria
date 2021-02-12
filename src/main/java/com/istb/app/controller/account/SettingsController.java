
package com.istb.app.controller.account;

import java.security.Principal;

import javax.transaction.Transactional;

import com.istb.app.entity.Usuario;
import com.istb.app.models.FileUpload;
import com.istb.app.repository.UsuarioRepositoryI;
import com.istb.app.services.firebase.FirebaseStrategy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.multipart.MultipartFile;

@Controller
public class SettingsController {
	
	@Autowired
	private UsuarioRepositoryI userManager;

	@Autowired
	private FirebaseStrategy fileManager;

	@GetMapping("/perfil")
	@Transactional
	public String profile(Principal account, Model attributes) {

		attributes.addAttribute("account", (account != null) ? 
			userManager.findByUsuario(account.getName()) : new Usuario());

		return "perfil";

	}
	
	@PutMapping("/perfil")
	@Transactional
	public ResponseEntity<?> updateProfile(@RequestBody Usuario account) {

		Usuario userAccount = userManager.findByUsuario(account.getUsuario());

		if(userAccount != null) {
		
			userAccount.setCedula(account.getCedula());
			userAccount.setNombres(account.getNombres());
			userAccount.setApellidos(account.getApellidos());
			userAccount.setCorreo(account.getCorreo());
			userAccount.setDescripcion(account.getDescripcion());

			userManager.save(userAccount);
			
		}

		return ResponseEntity.ok()
			.contentType(MediaType.APPLICATION_JSON)
			.body(account);

	}

	@PutMapping("/updatepic")
	@Transactional
	public ResponseEntity<?> updatePicture(MultipartFile picture, Principal account) 
		throws Exception {

		Usuario user = userManager.findByUsuario(account.getName());
		String previousName = user.getNombreImagenPerfil();
		FileUpload img = fileManager.uploadFile(picture);

		if(img.getSize() > 0) {

			fileManager.deleteFile( previousName != null ? previousName : "");
			user.setNombreImagenPerfil(img.getName());
			user.setUrlImagenPerfil(img.getUrl());
			
		}

		return ResponseEntity.ok()
			.contentType(MediaType.APPLICATION_JSON)
			.body(img);

	}
	
}
