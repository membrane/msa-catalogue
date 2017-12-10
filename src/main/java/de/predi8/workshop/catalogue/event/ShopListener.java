package de.predi8.workshop.catalogue.event;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.predi8.workshop.catalogue.dto.Article;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.PartitionOffset;
import org.springframework.kafka.annotation.TopicPartition;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Map;

@Service
public class ShopListener {

	private final Logger log = LoggerFactory.getLogger(ShopListener.class);

	private final ObjectMapper mapper;
	private final Map<String, Article> articles;

	public ShopListener(ObjectMapper mapper, Map<String, Article> articles) {
		this.mapper = mapper;
		this.articles = articles;
	}

	@KafkaListener(id = "stock-listener",
			topicPartitions =
					{ @TopicPartition(topic = "shop",
							partitionOffsets = @PartitionOffset(partition = "0", initialOffset = "0"))})
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
				articles.put(article.getUuid(), article);
				break;
			case "delete":
				articles.remove(article.getUuid());
		}
	}
}