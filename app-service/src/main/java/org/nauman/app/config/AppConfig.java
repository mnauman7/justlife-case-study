package org.nauman.app.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Main configuration class. Use this class to define beans.
 */
@Configuration
public class AppConfig {

    /**
     * @return this model mapper object is used to map entity classes to DTO classes
     */
    @Bean
    public ModelMapper getModelMapper() {
        return new ModelMapper();
    }
}
