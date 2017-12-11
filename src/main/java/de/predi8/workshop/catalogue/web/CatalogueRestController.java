package de.predi8.workshop.catalogue.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.predi8.workshop.catalogue.dto.Article;
import de.predi8.workshop.catalogue.error.NotFoundException;
import de.predi8.workshop.catalogue.event.Operation;
import de.predi8.workshop.catalogue.event.ShopListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@RestController
@RequestMapping("articles/")
public class CatalogueRestController {

	private final Logger log = LoggerFactory.getLogger(ShopListener.class);

	private Map<String, Article> articles;

	private ObjectMapper mapper;

	private KafkaTemplate<String, Operation> kafka;

	@Autowired
	public CatalogueRestController(Map<String, Article> articles, ObjectMapper objectMapper, KafkaTemplate<String, Operation> kafka) {
		this.articles = articles;
		this.mapper = objectMapper;
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
	public ResponseEntity<Article> createArticle(@RequestBody Article article) throws Exception {

		article.setUuid(UUID.randomUUID().toString());

		Operation op = new Operation("article", "create", mapper.valueToTree(article));

		op.logSend();

		kafka.send("shop", op).get(100, TimeUnit.MILLISECONDS);

		return ResponseEntity.created(URI.create("todo")).build();
	}

	@DeleteMapping("/{id}")
	public void delete(@PathVariable String id) throws InterruptedException, ExecutionException, TimeoutException {

		Article article = articles.get(id);

		if (article == null) {
			throw new NotFoundException();
		}

		Operation op = new Operation("article", "delete", mapper.valueToTree(article));

		op.logSend();

		kafka.send("shop", op).get(100, TimeUnit.MILLISECONDS);
	}
}