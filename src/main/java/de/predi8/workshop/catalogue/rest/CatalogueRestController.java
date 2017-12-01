package de.predi8.workshop.catalogue.rest;

import de.predi8.workshop.catalogue.dto.Article;
import de.predi8.workshop.catalogue.error.NotFoundException;
import org.springframework.http.RequestEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RequestMapping("/articles")
@RestController
public class CatalogueRestController {
	private final Map<String, Article> articles;

	public CatalogueRestController(Map<String, Article> articles) {
		this.articles = articles;
	}

	@GetMapping
	public Map<String, Article> index() {
		return articles;
	}

	@GetMapping("/{id}")
	public Article index(@PathVariable String id) {
		Article article = articles.get(id);

		if (article == null) {
			throw new NotFoundException();
		}

		return article;
	}
}