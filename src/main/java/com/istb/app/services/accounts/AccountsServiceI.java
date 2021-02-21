
package com.istb.app.services.accounts;

import com.istb.app.entity.Arrendatario;
import com.istb.app.entity.Empleado;

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
	public Arrendatario createTenantAccount(Arrendatario user);

	/**
	 * Create a new employee account
	 * @param user User details
	 * @return
	 */
	public Empleado createEmployeeAccount(Empleado user);
	
}
