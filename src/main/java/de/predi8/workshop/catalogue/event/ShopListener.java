package de.predi8.workshop.catalogue.event;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.predi8.workshop.catalogue.domain.Article;
import de.predi8.workshop.catalogue.repository.ArticleRepository;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

@Service
public class ShopListener {
	private final ObjectMapper mapper;
	private final ArticleRepository articleRepo;
	private final NullAwareBeanUtilsBean beanUtils;

	public ShopListener(ObjectMapper mapper, ArticleRepository articleRepo, NullAwareBeanUtilsBean beanUtils) {
		this.mapper = mapper;
		this.articleRepo = articleRepo;
		this.beanUtils = beanUtils;
	}

	@KafkaListener(topics = "shop")
	public void listen(Operation op) throws Exception {
		if (!op.getBo().equals("article")) return;

		op.logReceive();

		Article article = mapper.convertValue(op.getObject(), Article.class);

		switch (op.getAction()) {
			case "create":
				articleRepo.save(article);

				break;
			case "update":
				Article old = articleRepo.getOne(article.getUuid());

				beanUtils.copyProperties(old, article);

				articleRepo.save(old);

				break;
			case "delete":
				articleRepo.delete(article);
		}
	}
}