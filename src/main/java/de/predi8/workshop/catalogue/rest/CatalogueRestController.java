package de.predi8.workshop.catalogue.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.predi8.workshop.catalogue.dto.Article;
import de.predi8.workshop.catalogue.error.NotFoundException;
import de.predi8.workshop.catalogue.event.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("articles/")
public class CatalogueRestController {

	private Map<String, Article> articles;

	private ObjectMapper objectMapper;

	private KafkaTemplate<String, Operation> kafka;

	@Autowired
	public CatalogueRestController(Map<String, Article> articles, ObjectMapper objectMapper, KafkaTemplate<String, Operation> kafka) {
		this.articles = articles;
		this.objectMapper = objectMapper;
		this.kafka = kafka;
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

	@PostMapping
	public ResponseEntity<Article> createArticle(@RequestBody Article article) throws JsonProcessingException {

		article.setUuid(UUID.randomUUID().toString());

		kafka.send("shop", new Operation("article", "create", objectMapper.valueToTree(article)));


		return ResponseEntity.created(URI.create("fff")).build();
	}

	@DeleteMapping("/{id}")
	public void delete(@PathVariable String id) {
		Article article = articles.get(id);

		if (article == null) {
			throw new NotFoundException();
		}

		kafka.send("shop", new Operation("article", "delete", objectMapper.valueToTree(article)));
	}
}