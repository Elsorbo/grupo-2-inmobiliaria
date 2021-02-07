
package com.istb.app.services.accounts;

import com.istb.app.entity.Arrendatario;
import com.istb.app.entity.Empleado;
import com.istb.app.entity.Usuario;

/**
 * AccountsServiceI
 * Manage users accounts
 */
public interface AccountsServiceI {

	/**
	 * Create a new Tenant account
	 * @param user User details
	 * @return
	 */
	public Arrendatario createTenantAccount(Usuario user);

	/**
	 * Create a new employee account
	 * @param user User details
	 * @return
	 */
	public Empleado createEmployeeAccount(Usuario user);
	
}