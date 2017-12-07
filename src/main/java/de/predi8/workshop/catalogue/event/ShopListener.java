package de.predi8.workshop.catalogue.event;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.predi8.workshop.catalogue.dto.Article;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.PartitionOffset;
import org.springframework.kafka.annotation.TopicPartition;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Map;

@Service
public class ShopListener {
	private final ObjectMapper objectMapper;
	private final Map<String, Article> articles;

	public ShopListener(ObjectMapper objectMapper, Map<String, Article> articles) {
		this.objectMapper = objectMapper;
		this.articles = articles;
	}

	@KafkaListener(id = "stock-listener",
			topicPartitions =
					{ @TopicPartition(topic = "shop",
							partitionOffsets = @PartitionOffset(partition = "0", initialOffset = "0"))})
	public void listen(String payload) throws IOException {
		Operation operation = objectMapper.readValue(payload, Operation.class);

		if (!operation.getType().equals("article")) {
			return;
		}

		System.out.println("operation = " + operation);

		Article article = objectMapper.convertValue(operation.getObject(), Article.class);

		switch (operation.getAction()) {
			case "create":
			case "update":
				articles.put(article.getUuid(), article);
				break;
			case "delete":
				articles.remove(article.getUuid());
		}
	}
}