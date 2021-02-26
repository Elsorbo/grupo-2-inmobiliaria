
package com.istb.app.repository;

import com.istb.app.entity.Empleado;

import org.springframework.data.jpa.repository.JpaRepository;

public interface EmpleadoRepositoryI 
	extends JpaRepository<Empleado, Integer> {

	public Empleado findByUsuario_Usuario(String usuario);
		
}
