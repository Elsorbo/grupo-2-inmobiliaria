
package com.istb.app.repository;

import java.util.List;
import java.util.Optional;

import com.istb.app.entity.Notificacion;

import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificacionesRepositoryI 
	extends JpaRepository<Notificacion, Integer> {
	
	List<Notificacion> findAllByOrderByFechaGeneracionDesc();
	Optional<Notificacion> findByArrendatario_Usuario_Usuario(String usuario);
	List<Notificacion> findAllByArrendatario_Usuario_UsuarioOrderByFechaGeneracionDesc(
		String usuario);
	
}
