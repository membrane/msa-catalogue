package de.predi8.workshop.catalogue.event;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.predi8.workshop.catalogue.domain.Article;
import de.predi8.workshop.catalogue.repository.ArticleRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;

import static org.springframework.beans.BeanUtils.copyProperties;

@Service
public class ShopListener {
	private final ObjectMapper mapper;
	private final ArticleRepository articleRepository;

	public ShopListener(ObjectMapper mapper, ArticleRepository articleRepository) {
		this.mapper = mapper;
		this.articleRepository = articleRepository;
	}

	@KafkaListener(topics = "shop")
	public void listen(String payload) throws IOException, InvocationTargetException, IllegalAccessException {
		Operation op = mapper.readValue(payload, Operation.class);

		if (!op.getBo().equals("article")) { return;
		}

		op.logReceive();

		Article article = mapper.convertValue(op.getObject(), Article.class);

		switch (op.getAction()) {
			case "create":
				articleRepository.save(article);
				break;
			case "update":
				Article old = articleRepository.findOne(article.getUuid());

				new NullAwareBeanUtilsBean().copyProperties(old, article);

				articleRepository.save(old);
				break;
			case "delete":
				articleRepository.delete(article);
		}
	}
}