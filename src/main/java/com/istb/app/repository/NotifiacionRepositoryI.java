
package com.istb.app.repository;

import java.util.List;

import com.istb.app.entity.Notificacion;

import org.springframework.data.jpa.repository.JpaRepository;

public interface NotifiacionRepositoryI 
	extends JpaRepository<Notificacion, Integer> {

	List<Notificacion> findByArrendatario_Usuario_Usuario(String usuario);
		
}
