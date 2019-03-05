package de.predi8.catalogue.api;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.predi8.catalogue.error.NotFoundException;
import de.predi8.catalogue.event.Operation;
import de.predi8.catalogue.model.Article;
import de.predi8.catalogue.repository.ArticleRepository;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import org.springframework.data.redis.core.StringRedisTemplate;


import java.math.BigDecimal;
import java.util.List;

import static java.util.UUID.randomUUID;
import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static org.springframework.http.ResponseEntity.created;

@RestController
@RequestMapping("/articles")
public class CatalogueRestController {

	private ArticleRepository repo;
	private KafkaTemplate<String, Operation> kafka;
	final private ObjectMapper mapper;

	final private StringRedisTemplate redis;

	public CatalogueRestController(ArticleRepository repo, KafkaTemplate<String, Operation> kafka, ObjectMapper mapper, StringRedisTemplate redis) {
		this.repo = repo;
		this.kafka = kafka;
		this.mapper = mapper;
		this.redis = redis;
	}

	@GetMapping
	public List<Article> index() {
		return (List<Article>) repo.findAll();
	}

	@GetMapping("/{id}")
	public Article get( @PathVariable String id) {
		return getArticle(id);
	}

	@DeleteMapping("/{id}")
	public void delete( @PathVariable String id) {

		Article article = getArticle(id);

		Operation op = new Operation("article", "remove", mapper.valueToTree(article));

		kafka.send(new ProducerRecord<>("shop", op));
	}

	@PutMapping("/{id}")
	public void change( @PathVariable String id,  @RequestBody Article article) {
		getArticle(id);

		article.setUuid(id);
		repo.save( article);
	}

	@PatchMapping("/{id}")
	public void patch( @PathVariable String id,  @RequestBody JsonNode json) {
		Article old = getArticle(id);

		// JSON 3 Zustände: kein Attribut, null, Wert
		if ( json.has("price")) {
			if (json.hasNonNull("price")) {
				old.setPrice( new BigDecimal( json.get("price").asDouble()));
			} else {
				old.setPrice( null);
			}
		}

		if ( json.has("name"))
			old.setName( json.get("name").asText());

		repo.save( old);
	}

	@PostMapping
	public ResponseEntity<Article> create( @RequestBody Article article, UriComponentsBuilder builder) throws Exception {

		ValueOperations<String, String> ops = redis.opsForValue();
		Integer id = Math.toIntExact(ops.increment("articleId"));



		String uuid = randomUUID().toString();
		article.setUuid(id + "");

		Operation op = new Operation("article", "upsert", mapper.valueToTree(article));

	 	kafka.send(new ProducerRecord<>("shop", op)).get(200, MILLISECONDS);

	 	//ValueOperations<String, String> ops = redis.opsForValue();
	 	//ops.set("Foo","42");



	 	return created( builder.path("/articles/" + uuid).build().toUri()).body(article);
	}

	@GetMapping("/count")
	public long count() {
		return repo.count();
	}

	private Article getArticle(@PathVariable String id) {
		return repo.findById(id).orElseThrow(() -> {
			return new NotFoundException();
		});
	}
}