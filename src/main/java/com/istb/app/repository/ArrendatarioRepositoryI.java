
package com.istb.app.repository;

import com.istb.app.entity.Arrendatario;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ArrendatarioRepositoryI 
	extends JpaRepository<Arrendatario, Integer> {
	
}
