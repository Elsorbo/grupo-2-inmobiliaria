package com.istb.app.repository;

import com.istb.app.entity.Fotos;

import org.springframework.data.jpa.repository.JpaRepository;

public interface FotosRepository 
	extends JpaRepository<Fotos, Integer> {
    
}
