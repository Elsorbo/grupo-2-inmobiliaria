
package com.istb.app.util;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.Authentication;

/**
 * AccountUtils
 */
public class AccountUtils {

	public static boolean hasRole(Authentication account, String role) {

		List<String> roles = account.getAuthorities().stream()
			.map( rol -> rol.getAuthority().replace("ROLE_", "") )
			.collect(Collectors.toList());

		return roles.contains(role);

	}
	
}
