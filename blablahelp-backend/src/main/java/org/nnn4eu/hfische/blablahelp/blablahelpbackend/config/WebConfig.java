package org.nnn4eu.hfische.blablahelp.blablahelpbackend.config;

import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.geo.GeoJsonModule;

//@EnableWebMvc
@Configuration
public class WebConfig {


    @Bean
    public Jackson2ObjectMapperBuilderCustomizer customizeJson() {
        return builder ->
                builder.modules(new GeoJsonModule(), new Jdk8Module(), new JavaTimeModule());

    }

}