
package com.istb.app.repository;

import java.util.List;

import com.istb.app.entity.ReciboPago;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReciboPagoRepositoryI 
	extends JpaRepository<ReciboPago, Integer> {

	List<ReciboPago> findByArrendatario_Usuario_Id(int id);

	List<ReciboPago> findAllByOrderByFechaCreacionDesc();

	List<ReciboPago> findByArrendatario_Usuario_Usuario(String usuario);

	Page<ReciboPago> findAllByFacturadoOrderByFechaCreacionDesc(
		boolean estaFacturado, Pageable paginator);
	
	List<ReciboPago> findAllByFacturadoAndArrendatario_IdOrderByFechaCreacionDesc(
		boolean estaFacturado, int arrendatarioId);

	Page<ReciboPago> findByFacturadoAndArrendatario_Empleado_Usuario_UsuarioOrderByFechaCreacionDesc(
		boolean estaFacturado, String empleado, Pageable paginator);
	
}
