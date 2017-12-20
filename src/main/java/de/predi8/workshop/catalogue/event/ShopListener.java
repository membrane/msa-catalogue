package de.predi8.workshop.catalogue.event;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.predi8.workshop.catalogue.domain.Article;
import de.predi8.workshop.catalogue.repository.ArticleRepository;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class ShopListener {
	private final ObjectMapper mapper;
	private final ArticleRepository articleRepository;

	public ShopListener(ObjectMapper mapper, ArticleRepository articleRepository) {
		this.mapper = mapper;
		this.articleRepository = articleRepository;
	}

	@KafkaListener(topics = "shop")
	public void listen(String payload) throws IOException {
		Operation op = mapper.readValue(payload, Operation.class);

		if (!op.getBo().equals("article")) {
			return;
		}

		op.logReceive();

		Article article = mapper.convertValue(op.getObject(), Article.class);

		switch (op.getAction()) {
			case "create":
			case "update":
				articleRepository.save(article);
				//articles.put(article.getUuid(), article);
				break;
			case "delete":
				articleRepository.delete(article);
				// articles.remove(article.getUuid());
		}
	}
}