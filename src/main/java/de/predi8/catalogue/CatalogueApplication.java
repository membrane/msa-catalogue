package de.predi8.catalogue;

import de.predi8.catalogue.event.NullAwareBeanUtilsBean;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;

import java.util.HashMap;
import java.util.Map;

import static org.springframework.boot.SpringApplication.run;

@EnableDiscoveryClient
@SpringBootApplication
public class CatalogueApplication {

	@Bean
	public NullAwareBeanUtilsBean beanUtils() {
		return new NullAwareBeanUtilsBean();
	}

	public static void main(String[] args) {
		run( CatalogueApplication.class, args);
	}
}