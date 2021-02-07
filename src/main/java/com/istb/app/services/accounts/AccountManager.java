
package com.istb.app.services.accounts;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.istb.app.entity.Arrendatario;
import com.istb.app.entity.Empleado;
import com.istb.app.entity.Role;
import com.istb.app.entity.Usuario;
import com.istb.app.repository.ArrendatarioRepositoryI;
import com.istb.app.repository.EmpleadoRepositoryI;
import com.istb.app.repository.RoleRepositoryI;
import com.istb.app.repository.UsuarioRepositoryI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AccountManager implements AccountsServiceI {
	
	@Autowired
	private UsuarioRepositoryI userManager;
	
	@Autowired
	private EmpleadoRepositoryI employeeManager;

	@Autowired
	private ArrendatarioRepositoryI tenantManager;

	@Autowired
	private RoleRepositoryI roleManager;

	public Empleado createEmployeeAccount(Usuario user) {

		Empleado employee = new Empleado(user, "");
		
		user.setEstado(true);
		addRole(user, Arrays.asList("EMPLEADO"));
		
		userManager.save(user);
		employeeManager.save(employee);

		return employee;

	};
	
	@Override
	public Arrendatario createTenantAccount(Usuario user) {
	
		Arrendatario tenant = new Arrendatario(user);
		user.setEstado(true);
		addRole(user, Arrays.asList("ARRENDATARIO"));
		
		userManager.save(user);
		tenantManager.save(tenant);

		return tenant;
	
	}

	private void addRole(Usuario user, List<String> roles) {

		List<Role> authorities = new ArrayList<>();
		
		roles.forEach( rol -> authorities.add( 
			roleManager.findByNombre("ROLE_".concat(rol)) )
		);

		authorities.add( roleManager.findByNombre("ROLE_USUARIO") );

		user.setRoles(authorities);

	}

}
