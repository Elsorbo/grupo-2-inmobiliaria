
package com.istb.app.controller.dashboard;

import java.util.HashMap;
import java.time.LocalDate;

import com.istb.app.repository.ArrendatarioRepositoryI;
import com.istb.app.repository.NotificacionesRepositoryI;
import com.istb.app.util.AccountUtils;
import com.istb.app.util.ControllerUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class NotificacionController {

	@Autowired
	private ArrendatarioRepositoryI arrendatarioRepository;
	
	@Autowired
	public NotificacionesRepositoryI notificationRepository;
	
	@GetMapping("/notificaciones")
	public String getNotificacionesDesalojo(Model attributes, 
		Authentication account) {

		if( !AccountUtils.hasRole(account, "ARRENDATARIO") ) {
		
			attributes.addAttribute("sectionTitle", "notificaciones para arrendatarios"); 
			attributes.addAttribute("fechaActual", LocalDate.now());
			attributes.addAttribute("arrendatarios", arrendatarioRepository.findAll());
			
		} else {
			
			attributes.addAttribute("sectionTitle", "notificaciones");
			attributes.addAttribute("notificaciones", notificationRepository
				.findByArrendatario_Usuario_Usuario(account.getName()));
			
		}
		
		return "notificacion";
		
	}

	@GetMapping("/notificacion")
	public ResponseEntity<?> getNotifications() {
	
		return ControllerUtils.getJSONOkResponse( notificationRepository.findAll() );
	
	}

	@PostMapping("/notificacion")
	public ResponseEntity<?> addNotification() {
	
		return ControllerUtils.getJSONOkResponse( new HashMap<>() );
	
	}

}
