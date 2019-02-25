package de.predi8.catalogue;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static com.fasterxml.jackson.databind.SerializationFeature.INDENT_OUTPUT;

@Configuration
public class Config {

    @Bean
    ObjectMapper mapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.addMixIn(Object.class, IgnoreConfig.class);
        mapper.enable( INDENT_OUTPUT);
        return mapper;
    }

    @JsonIgnoreProperties(value = {"hibernateLazyInitializer"}, ignoreUnknown = true)
    private abstract class IgnoreConfig { }
}
