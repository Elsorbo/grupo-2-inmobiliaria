
package com.istb.app.repository;

import com.istb.app.entity.Usuario;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * UsuarioRepository
 */
public interface UsuarioRepositoryI 
	extends JpaRepository<Usuario, Integer> {

	public Usuario findByUsuario(String username);
	public Usuario findByCorreo(String username);

}
