
package com.istb.app.controller.dashboard;

import javax.transaction.Transactional;
import javax.validation.Valid;

import java.time.LocalDate;

import com.istb.app.entity.Notificacion;
import com.istb.app.repository.ArrendatarioRepositoryI;
import com.istb.app.repository.NotificacionesRepositoryI;
import com.istb.app.services.dashboard.NotificacionService;
import com.istb.app.util.AccountUtils;
import com.istb.app.util.ControllerUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
public class NotificacionController {

	@Autowired
	public NotificacionService notificacionManager;

	@Autowired
	private ArrendatarioRepositoryI arrendatarioRepository;
	
	@Autowired
	public NotificacionesRepositoryI notificationRepository;
	
	@GetMapping("/notificaciones")
	@Transactional
	public String getNotificacionesDesalojo(
		Model attributes, Authentication account) {

		if( !AccountUtils.hasRole(account, "ARRENDATARIO") ) {
		
			attributes.addAttribute("sectionTitle", 
				"notificaciones para arrendatarios"); 
			attributes.addAttribute("fechaActual", LocalDate.now());
			
			if( AccountUtils.hasRole(account, "ADMINISTRADOR") ) {
				attributes.addAttribute("arrendatarios", 
					arrendatarioRepository.findAll()); }
			else { 
				attributes.addAttribute("arrendatarios", 
					arrendatarioRepository.findAllByEmpleado_Usuario_Usuario(
						account.getName())); }
			
		} else {
			
			attributes.addAttribute("sectionTitle", "notificaciones");
			attributes.addAttribute("notificaciones", notificationRepository
				.findAllByArrendatario_Usuario_Usuario(account.getName()));
			
		}
		
		return "notificacion";
		
	}

	@GetMapping("/notificacion")
	@Transactional
	public ResponseEntity<?> getNotifications() {
	
		return ControllerUtils.getJSONOkResponse( notificationRepository.findAll() );
	
	}

	@PostMapping("/notificacion")
	@Transactional
	public ResponseEntity<?> addNotification(
		@Valid @RequestBody Notificacion notification, BindingResult bindObjt) {
	
		if( bindObjt.hasErrors() ) { 
			return ControllerUtils.getJSONBindErrors(bindObjt); }

		return ControllerUtils.getJSONOkResponse( 
			notificacionManager.addNotification(notification) );
	
	}

}
