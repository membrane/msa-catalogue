package de.predi8.workshop.catalogue.api;

import de.predi8.workshop.catalogue.domain.Article;
import de.predi8.workshop.catalogue.error.NotFoundException;
import de.predi8.workshop.catalogue.event.Operation;
import de.predi8.workshop.catalogue.repository.ArticleRepository;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/articles")
public class CatalogueRestController {
	private ArticleRepository repo;
	private KafkaTemplate<String, Operation> kafka;

	public CatalogueRestController(ArticleRepository repo, KafkaTemplate<String, Operation> kafka) {
		this.repo = repo;
		this.kafka = kafka;
	}

	@GetMapping
	public List<Article> index() {
		return repo.findAll();
	}

	@GetMapping("/{id}")
	public Article index(@PathVariable String id) {
		Article article = repo.getOne(id);

		if (article == null) {
			throw new NotFoundException();
		}

		return article;
	}

	@GetMapping("/count")
	public long count() {
		return repo.count();
	}
}