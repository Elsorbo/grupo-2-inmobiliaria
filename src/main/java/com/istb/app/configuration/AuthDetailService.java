
package com.istb.app.configuration;

import java.util.ArrayList;
import java.util.List;

import com.istb.app.entity.Role;
import com.istb.app.entity.Usuario;
import com.istb.app.repository.UsuarioRepositoryI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AuthDetailService implements UserDetailsService {

	@Autowired
	private UsuarioRepositoryI userRepository;

	@Override
	public UserDetails loadUserByUsername(String username) 
		throws UsernameNotFoundException {

		Usuario currentUser = userRepository.findByUsuario(username);

		if(currentUser != null) {

			List<GrantedAuthority> roles = new ArrayList<>();
			for (Role rol : currentUser.getRoles()) {
				roles.add(rol); }

			return ( 
				new User(currentUser.getUsuario(), currentUser.getContrasena(),
					(currentUser.getEstado() != null) ? currentUser.getEstado(): false,
					true, true, true, roles)
			);

		}
		
		throw new UsernameNotFoundException("Invalid user or password");

	}

}
