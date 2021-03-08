
package com.istb.app.repository;

import java.util.Optional;

import com.istb.app.entity.Arrendatario;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ArrendatarioRepositoryI 
	extends JpaRepository<Arrendatario, Integer> {

	Optional<Arrendatario> findByUsuario_Usuario(String usuario);
		
}
