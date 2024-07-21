package org.nauman.app.security.providers;

import java.util.Collections;

import org.nauman.app.security.model.JwtAuthenticationToken;
import org.nauman.app.utils.JwtUtils;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class JwtAuthenticationProvider implements AuthenticationProvider {

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		
		Object jwtToken = authentication.getPrincipal();
		
		if (jwtToken != null && JwtUtils.validateJwt(jwtToken.toString())) {
			/*
			 * this constructor creates authentication object which is valid. (constructor with authorities parameter)
			 * Creating authentication object with this constructor will set the authenticated flag to true.
			 * AuthorizationFilter will check this authenticated flag to verify if token object is valid.
			 */
			authentication = new JwtAuthenticationToken(JwtUtils.getSubject(jwtToken.toString()), null, Collections.emptyList());
		} else {
			/*
			 * this constructor creates authentication object which is invalid. (constructor without authorities parameter)
			 * Creating authentication object with this constructor will set the authenticated flag to false.
			 * AuthorizationFilter will check this authenticated flag to verify if token object is valid.
			 */
			authentication = new JwtAuthenticationToken(null, null);
		}
		
		return authentication;
	}

	/*
	 * Authentication Manager calls this method to check if this provider can
	 * authenticate the authentication object
	 * 
	 */
	@Override
	public boolean supports(Class<?> authentication) {
		return JwtAuthenticationToken.class.equals(authentication);
	}

}
