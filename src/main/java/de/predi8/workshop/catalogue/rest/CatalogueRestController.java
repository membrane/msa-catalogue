package de.predi8.workshop.catalogue.rest;

import de.predi8.workshop.catalogue.dto.Article;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class CatalogueRestController {
	private final List<Article> articles;

	@RequestMapping("/articles")
	@GetMapping
	public List<Article> index() {
		return articles;
	}
}