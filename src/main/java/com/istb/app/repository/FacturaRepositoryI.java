
package com.istb.app.repository;

import java.util.List;

import com.istb.app.entity.Factura;

import org.springframework.data.jpa.repository.JpaRepository;

public interface FacturaRepositoryI 
	extends JpaRepository<Factura, Integer> {

	List<Factura> findByArrendatario_Usuario_UsuarioOrderByFechaAdmisionDesc(
		String usuario);
	
}
