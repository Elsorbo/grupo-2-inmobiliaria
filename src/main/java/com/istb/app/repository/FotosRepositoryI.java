
package com.istb.app.repository;

import com.istb.app.entity.Fotos;

import org.springframework.data.jpa.repository.JpaRepository;

public interface FotosRepositoryI 
	extends JpaRepository<Fotos, Integer> {
    
}
