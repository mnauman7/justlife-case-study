package org.nauman.app.security.model;

import java.util.Collection;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

/**
 * Authentication object which is used by JwtAuthenticationFilter to let AuthenticationFilter know if
 * Jwt validation was successful or not
 */
public class JwtAuthenticationToken extends UsernamePasswordAuthenticationToken {

	public JwtAuthenticationToken(Object principal, Object credentials, 
			Collection<? extends GrantedAuthority> authorities) {
		// this constructor sets the authenticated flag to true so created token object is valid
		super(principal, credentials, authorities);
	}
	
	public JwtAuthenticationToken(Object principal, Object credentials) {
		// this constructor sets the authenticated flag to false so created token object will be invalid
		// and will fail AuthorizationFilter check
		super(principal, credentials);
	}

	public JwtAuthenticationToken(Object principal) {
		super(principal, null);
	}

	public JwtAuthenticationToken() {
		super(null, null);
	}
}
