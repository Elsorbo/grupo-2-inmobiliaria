
package com.istb.app.repository;

import java.util.List;

import com.istb.app.entity.Inmueble;

import org.springframework.data.jpa.repository.JpaRepository;

public interface InmuebleRepositoryI 
	extends JpaRepository<Inmueble, Integer> {

	List<Inmueble> findByLocalidad(String localidad);

	List<Inmueble> findByAlquilado(boolean alquilado);

	List<Inmueble> findByAlquiladoOrderByIdAsc(boolean alquilado);
	
}
