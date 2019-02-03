package de.predi8.workshop.catalogue.event;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.predi8.workshop.catalogue.repository.ArticleRepository;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

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

	@KafkaListener(topics = "shop" )
	public void listen(Operation op) {
		System.out.println(op);
	}
}