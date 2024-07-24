package org.nauman.app.config;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.nauman.app.security.filters.JwtAuthenticationFilter;
import org.nauman.app.security.providers.JwtAuthenticationProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

/**
 * Purpose of this class is to setup spring security filter chain and to define spring security configurations.
 */
@Configuration
@EnableWebSecurity(debug = true)
public class SecurityConfig {
	
	private static final String[] SPRING_SECURITY_ALLOWED_URLS = new String[] {"/auth/login"};
	
	public static final List<String> SPRING_SECURITY_ALLOWED_URLS_LIST =
		      Collections.unmodifiableList(Arrays.asList(SPRING_SECURITY_ALLOWED_URLS));
	
    /**
     * @param http
     * @param jwtAuthenticationFilter
     * @return Creating our own filter chain so spring security uses this filter chain instead of creating its default one
     * @throws Exception
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, JwtAuthenticationFilter jwtAuthenticationFilter) throws Exception {
        return http
        		.csrf(AbstractHttpConfigurer::disable) //removing csrf filter
        		.cors(Customizer.withDefaults())
        	    .authorizeHttpRequests(requests -> requests.requestMatchers(SPRING_SECURITY_ALLOWED_URLS).permitAll()
        	    		.anyRequest().authenticated())
        		.addFilterAfter(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
        		.sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        		.build();
    }
    

    /**
     * @param tokenAuthProvider this authProvider is used by auth manager to validate jwt tokens
     * @return creating auth manager which is used by our JWT filter
     */
    @Bean
    public AuthenticationManager authManager(JwtAuthenticationProvider tokenAuthProvider) {
        return new ProviderManager(List.of(tokenAuthProvider));
    }


    /**
     * @return Creating corsFilter bean with our settings so spring security uses this bean instead of its default one
     */
    @Bean
    public CorsFilter corsFilter() {
      UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
      CorsConfiguration config = new CorsConfiguration();
      config.addAllowedOrigin("*");

      config.addAllowedHeader("*");

      config.addAllowedMethod("OPTIONS");
      config.addAllowedMethod("GET");
      config.addAllowedMethod("POST");
      config.addAllowedMethod("PUT");
      config.addAllowedMethod("DELETE");

      // cache preflight request
      config.setMaxAge(4 * 60 * 60l);

      source.registerCorsConfiguration("/**", config);
      return new CorsFilter(source);
    }
    
}
