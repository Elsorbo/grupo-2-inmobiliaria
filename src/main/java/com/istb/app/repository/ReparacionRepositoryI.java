
package com.istb.app.repository;

import java.util.List;

import com.istb.app.entity.Reparacion;
import com.istb.app.util.enums.EstadoReparacion;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ReparacionRepositoryI 
	extends JpaRepository<Reparacion, Integer> {

	public List<Reparacion> findAllByOrderByFechaCreacionDesc();
	
	public List<Reparacion> findByEstadoOrderByFechaCreacionDesc(EstadoReparacion estado);

	public List<Reparacion> findByArrendatario_Usuario_Usuario(String usuario);

	public List<Reparacion> findByArrendatario_Usuario_UsuarioOrderByFechaCreacionDesc(String usuario);
	
}
