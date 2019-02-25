package de.predi8.catalogue;

import de.predi8.catalogue.event.NullAwareBeanUtilsBean;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import static org.springframework.boot.SpringApplication.run;

@SpringBootApplication
public class CatalogueApplication {

	@Bean
	public NullAwareBeanUtilsBean beanUtils() {
		return new NullAwareBeanUtilsBean();
	}

	public static void main(String[] args) {
		run(CatalogueApplication.class, args);
	}
}