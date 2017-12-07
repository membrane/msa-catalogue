package de.predi8.workshop.catalogue;

import de.predi8.workshop.catalogue.dto.Article;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.annotation.EnableKafka;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@EnableKafka
@EnableDiscoveryClient
@SpringBootApplication
public class CatalogueApplication {

	@Bean
	public Map<String,Article> articles() {
		return new ConcurrentHashMap<>();
	}

	public static void main(String[] args) {
		SpringApplication.run(CatalogueApplication.class, args);
	}

}