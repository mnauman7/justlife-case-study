package org.nauman.app.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 *  Configuration class created to configure Spring MVC settings
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
	
	/**
	 * This method is allowing Cors for all origins in spring mvc
	 */
	@Override
	public void addCorsMappings(CorsRegistry registry) {
		registry.addMapping("/**")
            .allowedOrigins("*")
            .allowedMethods("HEAD", "GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS");
	}
}
