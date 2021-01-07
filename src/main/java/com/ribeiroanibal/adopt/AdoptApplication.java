package com.ribeiroanibal.adopt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ribeiroanibal.adopt.util.ObjectMapperCustom;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class AdoptApplication {
    public static final String API_BASIC_PATH = "/api";
    public static final String API_AUTH = API_BASIC_PATH + "/auth";
    public static final String API_PETS = API_BASIC_PATH + "/pets";
    public static final String API_PETS_ITEM = API_PETS + "/{id}";

    public static void main(final String[] args) {
        SpringApplication.run(AdoptApplication.class, args);
    }

    @Bean
    public ObjectMapper objectMapper() {
        return ObjectMapperCustom.customMapper();
    }
}
