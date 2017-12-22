package de.predi8.workshop.catalogue.event;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.predi8.workshop.catalogue.domain.Article;
import de.predi8.workshop.catalogue.repository.ArticleRepository;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import static org.springframework.beans.BeanUtils.copyProperties;

@Service
public class ShopListener {
	private final ObjectMapper mapper;
	private final ArticleRepository articleRepository;
	private final NullAwareBeanUtilsBean beanUtils;

	public ShopListener(ObjectMapper mapper, ArticleRepository articleRepository, NullAwareBeanUtilsBean beanUtils) {
		this.mapper = mapper;
		this.articleRepository = articleRepository;
		this.beanUtils = beanUtils;
	}

	@KafkaListener(topics = "shop")
	public void listen(Operation op) throws IOException, InvocationTargetException, IllegalAccessException {
		if (!op.getBo().equals("article")) return;

		op.logReceive();

		Article article = mapper.convertValue(op.getObject(), Article.class);

		switch (op.getAction()) {
			case "create":
				articleRepository.save(article);

				break;
			case "update":
				Article old = articleRepository.findOne(article.getUuid());

				beanUtils.copyProperties(old, article);

				articleRepository.save(old);

				break;
			case "delete":
				articleRepository.delete(article);
		}
	}
}