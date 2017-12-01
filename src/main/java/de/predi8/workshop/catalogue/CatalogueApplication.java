package de.predi8.workshop.catalogue;

import de.predi8.workshop.catalogue.dto.Article;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

@EnableDiscoveryClient
@SpringBootApplication
public class CatalogueApplication {
	@Bean
	public List<Article> articles() {
		return new CopyOnWriteArrayList<>();
	}

	public static void main(String[] args) {
		SpringApplication.run(CatalogueApplication.class, args);
	}
}