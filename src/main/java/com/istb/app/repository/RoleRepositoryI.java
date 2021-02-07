
package com.istb.app.repository;

import com.istb.app.entity.Role;

import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepositoryI 
	extends JpaRepository<Role, Integer> {

	public Role findByNombre(String username);
		
}
