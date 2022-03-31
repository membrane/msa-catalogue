package de.predic8.catalogue;

import de.predic8.catalogue.event.NullAwareBeanUtilsBean;
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