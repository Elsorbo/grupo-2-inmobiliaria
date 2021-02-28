
package com.istb.app.repository;

import com.istb.app.entity.Servicio;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ServicioRepositoryI 
	extends JpaRepository<Servicio, Integer> {
    
}
