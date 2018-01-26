package de.predi8.workshop.catalogue.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.predi8.workshop.catalogue.domain.Article;
import de.predi8.workshop.catalogue.error.NotFoundException;
import de.predi8.workshop.catalogue.event.Operation;
import de.predi8.workshop.catalogue.repository.ArticleRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@RestController
@RequestMapping("/articles")
public class CatalogueRestController {
	private ArticleRepository articleRepository;
	private KafkaTemplate<String, Operation> kafka;

	public CatalogueRestController(ArticleRepository articleRepository, KafkaTemplate<String, Operation> kafka) {
		this.articleRepository = articleRepository;
		this.kafka = kafka;
	}

	@GetMapping
	public List<Article> index() {
		return articleRepository.findAll();
	}

	@GetMapping("/{id}")
	public Article index(@PathVariable String id) {
		Article article = articleRepository.findOne(id);

		if (article == null) {
			throw new NotFoundException();
		}

		return article;
	}

	@GetMapping("/count")
	public long count() {
		return articleRepository.count();
	}
}