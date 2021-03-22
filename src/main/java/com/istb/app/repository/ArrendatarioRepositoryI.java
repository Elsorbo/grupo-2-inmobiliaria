
package com.istb.app.repository;

import java.util.List;
import java.util.Optional;

import com.istb.app.entity.Arrendatario;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArrendatarioRepositoryI 
	extends JpaRepository<Arrendatario, Integer> {

	Optional<Arrendatario> findByUsuario_Usuario(String usuario);
	
	List<Arrendatario> findAllByEmpleado_Usuario_Usuario(String usuario);

	Page<Arrendatario> findByEmpleado_Usuario_Usuario(
		String usuario, Pageable paginator);
		
	Page<Arrendatario> findAllByEmpleado_Usuario_Usuario(
		String usuario, Pageable paginator);
	
}
