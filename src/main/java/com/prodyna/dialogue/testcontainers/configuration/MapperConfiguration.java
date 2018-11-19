package com.prodyna.dialogue.testcontainers.configuration;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MapperConfiguration {

    @Bean
    public ModelMapper mapper() {

        return new ModelMapper();
    }
}
