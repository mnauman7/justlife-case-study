package org.nauman.app.security.filters;

import java.io.IOException;

import org.nauman.app.config.SecurityConfig;
import org.nauman.app.security.model.JwtAuthenticationToken;
import org.nauman.app.utils.JwtUtils;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import io.micrometer.common.util.StringUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

	private AuthenticationManager authManager;
	
	
	
	public JwtAuthenticationFilter(AuthenticationManager authManager) {
		super();
		this.authManager = authManager;
	}
	

	@Override
	protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
	    AntPathMatcher pathMatcher = new AntPathMatcher();
	    return SecurityConfig.SPRING_SECURITY_ALLOWED_URLS_LIST.stream()
	        .anyMatch(p -> pathMatcher.match(p, request.getServletPath()));
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		
		String jwtToken = JwtUtils.getTokenFromRequest(request);
		
		if (StringUtils.isBlank(jwtToken)) {
			/*
			 * if token is blank then continue without validating the token.
			 * NOT setting the authentication object in security context means that this filter failed to
			 * authenticate the token. AuthorizationFilter will not be able to find
			 * authentication object in the security context and will fail the request (unless
			 * some other filter validates the request and sets the authentication object in security context)
			 */
			filterChain.doFilter(request, response);
			return;
		}
		
		JwtAuthenticationToken jwtAuthToken = new JwtAuthenticationToken(jwtToken);
		
		/*
		 * will use authManager implementation provided by spring security (Bean created
		 * in securityConfigs), authManager will scan available auth providers and choose
		 * appropriate auth provider to authenticate the token.
		 * If provider is able to validate the token then it will set authentication object 
		 * in security context
		 */
		Authentication authentication = authManager.authenticate(jwtAuthToken);
		
		if (authentication.isAuthenticated()) {
			/*
			 * AuthorizationFilter will take authentication object from this context and
			 * check the authentication.isAuthenticated() flag to verify if token validated
			 * successfully
			 */
			SecurityContextHolder.getContext().setAuthentication(authentication);
		}
		
		/*
		 * NOTES:
		 * AuthorizationFilter will authenticate the request only if following conditions are met:
		 * 1). Authentication object is present in security context
		 * 2). Authentication object in security context has authenticated flag set to true
		 * 
		 * If one of the above conditions is not true then AuthorizationFilter will throw access denied exception
		 * and request will fail
		 */
		
		filterChain.doFilter(request, response);
	}	

}
