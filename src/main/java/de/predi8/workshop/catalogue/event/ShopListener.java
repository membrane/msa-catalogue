package de.predi8.workshop.catalogue.event;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.predi8.workshop.catalogue.dto.Article;
import org.slf4j.Logger;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class ShopListener {
	private static final Logger log = org.slf4j.LoggerFactory.getLogger(ShopListener.class);

	private final ObjectMapper objectMapper;
	private final List<Article> articles;

	public ShopListener(ObjectMapper objectMapper, List<Article> articles) {
		this.objectMapper = objectMapper;
		this.articles = articles;
	}

	@KafkaListener(topics = "shop")
	public void listen(String playload) throws IOException {
		Operation operation = objectMapper.readValue(playload, Operation.class);

		if (!operation.getType().equals("article")) {
			return;
		}

		if (!operation.getAction().equals("create")) {
			return;
		}

		articles.add(objectMapper.convertValue(operation.getObject(), Article.class));
	}
}