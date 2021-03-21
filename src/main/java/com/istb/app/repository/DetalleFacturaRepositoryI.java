
package com.istb.app.repository;

import com.istb.app.entity.DetalleFactura;

import org.springframework.data.jpa.repository.JpaRepository;

public interface DetalleFacturaRepositoryI 
	extends JpaRepository<DetalleFactura, Integer> {
    
}
