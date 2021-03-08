
package com.istb.app.services.dashboard;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import javax.transaction.Transactional;

import com.istb.app.entity.Arrendatario;
import com.istb.app.entity.Notificacion;
import com.istb.app.repository.ArrendatarioRepositoryI;
import com.istb.app.repository.NotificacionesRepositoryI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NotificacionService {

	@Autowired
	private ArrendatarioRepositoryI arrendatarioRepository;

	@Autowired
	private NotificacionesRepositoryI notificationRepository;

	@Transactional
	public Map<String, Object> addNotification(Notificacion notification) {

		Map<String, Object> data = new HashMap<>();
		Optional<Arrendatario> storedTenant = arrendatarioRepository.findById(
			notification.getArrendatario().getId() );

		if( storedTenant.isPresent() ) {
		
			notification.setArrendatario( storedTenant.get() );

			notificationRepository.save(notification);
			data.put("notificacion", notification);
		
		} else {
			data.put("error", "No se pudo enviar la notificación"); }

		return data;

	}
	
}
