
package com.istb.app.repository;

import java.util.List;

import com.istb.app.entity.Inmueble;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InmuebleRepositoryI 
	extends JpaRepository<Inmueble, Integer> {

	List<Inmueble> findByLocalidad(String localidad);

	List<Inmueble> findByAlquilado(boolean alquilado);

	List<Inmueble> findByAlquiladoOrderByIdAsc(boolean alquilado);

	List<Inmueble> findAllByEmpleados_Usuario_UsuarioOrderById(String usuario);
	
	Page<Inmueble> findAllByEmpleados_Usuario_UsuarioOrderById(
		String usuario, Pageable paginator);
	
	List<Inmueble> findByAlquiladoAndEmpleados_Usuario_UsuarioOrderByIdAsc(
		boolean alquilado, String usuario);
	
	Page<Inmueble> findByEmpleados_Usuario_Usuario(String usuario, Pageable paginator);
	
}
