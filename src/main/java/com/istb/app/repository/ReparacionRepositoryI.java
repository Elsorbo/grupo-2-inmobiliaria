
package com.istb.app.repository;

import java.util.List;

import com.istb.app.entity.Reparacion;
import com.istb.app.util.enums.EstadoReparacion;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ReparacionRepositoryI 
	extends JpaRepository<Reparacion, Integer> {

	List<Reparacion> findAllByOrderByFechaCreacionDesc();
	
	List<Reparacion> findByEstadoOrderByFechaCreacionDesc(EstadoReparacion estado);

	List<Reparacion> findByEstadoAndArrendatario_Empleado_Usuario_UsuarioOrderByFechaCreacionDesc(
		EstadoReparacion estado, String empleado);

	List<Reparacion> findByArrendatario_Usuario_Usuario(String usuario);

	List<Reparacion> findByArrendatario_Usuario_UsuarioOrderByFechaCreacionDesc(String usuario);
	
}
