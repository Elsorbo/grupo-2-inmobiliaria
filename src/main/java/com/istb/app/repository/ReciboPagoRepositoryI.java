
package com.istb.app.repository;

import java.util.List;

import com.istb.app.entity.ReciboPago;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ReciboPagoRepositoryI 
	extends JpaRepository<ReciboPago, Integer> {

	List<ReciboPago> findByArrendatario_Usuario_Id(int id);
	List<ReciboPago> findAllByOrderByFechaCreacionDesc();
	List<ReciboPago> findByArrendatario_Usuario_Usuario(String id);
	
}
