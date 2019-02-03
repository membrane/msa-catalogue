package de.predi8.workshop.catalogue.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.predi8.workshop.catalogue.domain.Article;
import de.predi8.workshop.catalogue.error.NotFoundException;
import de.predi8.workshop.catalogue.event.Operation;
import de.predi8.workshop.catalogue.repository.ArticleRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.concurrent.TimeUnit;

import static java.util.UUID.randomUUID;
import static java.util.concurrent.TimeUnit.MILLISECONDS;

@RestController
@RequestMapping("/articles")
public class CatalogueRestController {

	private ArticleRepository repo;
	private KafkaTemplate<String, Operation> kafka;
	final private ObjectMapper mapper;

	public CatalogueRestController(ArticleRepository repo, KafkaTemplate<String, Operation> kafka, ObjectMapper mapper) {
		this.repo = repo;
		this.kafka = kafka;
		this.mapper = mapper;
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

	@PostMapping
	public ResponseEntity<Article> createArticle(@RequestBody Article article, UriComponentsBuilder builder) throws Exception {
		String id = randomUUID().toString();
		article.setUuid(id);

		Operation op = new Operation("article", "create", mapper.valueToTree(article));

		op.logSend();

		kafka.send("shop", op).get(100, MILLISECONDS);

		return ResponseEntity.created(builder.path("/articles/{id}").buildAndExpand(id).toUri()).build();
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable String id) throws Exception {
		Article article = repo.getOne(id);

		if (article == null) {
			throw new NotFoundException();
		}

		Operation op = new Operation("article", "delete", mapper.valueToTree(article));

		op.logSend();

		kafka.send("shop", op).get(100, MILLISECONDS);

		return ResponseEntity.accepted().build();
	}

	@PutMapping("/{id}")
	public ResponseEntity<Void> update(@PathVariable String id, @RequestBody Article article) throws Exception {
		article.setUuid(id);

		Operation op = new Operation("article", "update", mapper.valueToTree(article));

		op.logSend();

		kafka.send("shop", op).get(100, MILLISECONDS);

		return ResponseEntity.accepted().build();
	}

	@GetMapping("/count")
	public long count() {
		return repo.count();
	}
}