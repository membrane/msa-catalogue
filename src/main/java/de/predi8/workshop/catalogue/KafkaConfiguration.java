package de.predi8.workshop.catalogue;


import de.predi8.workshop.catalogue.event.Operation;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;

@EnableKafka
@Configuration
public class KafkaConfiguration {
	@Bean
	public ConsumerFactory<String, Operation> consumerFactory(KafkaProperties kafkaProperties) {
		return new DefaultKafkaConsumerFactory<>(kafkaProperties.buildConsumerProperties(), new StringDeserializer(), new JsonDeserializer<>(Operation.class));
	}

	@Bean
	public ConcurrentKafkaListenerContainerFactory<String, Operation> kafkaListenerContainerFactory(ConsumerFactory<String, Operation> consumerFactory) {
		ConcurrentKafkaListenerContainerFactory<String, Operation> factory = new ConcurrentKafkaListenerContainerFactory<>();
		factory.setConsumerFactory(consumerFactory);

		return factory;
	}
}