package org.nauman.app.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
	
	/*
	 * Adding cors origin for spring web mvc
	 * */
	@Override
	public void addCorsMappings(CorsRegistry registry) {
		registry.addMapping("/**")
            .allowedOrigins("*")
            .allowedMethods("HEAD", "GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS");
	}
}
