package de.predi8.catalogue;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.predi8.catalogue.event.Operation;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;

import static com.fasterxml.jackson.databind.SerializationFeature.INDENT_OUTPUT;

@EnableKafka
@Configuration
public class KafkaConfiguration {


	/**
	 * Do not use headers! Otherwise the class info in the headers will be used
	 *
	 * @param props
	 * @return
	 */
	@Bean
	public ConsumerFactory<String, Operation> consumerFactory(KafkaProperties props) {
		return new DefaultKafkaConsumerFactory<>( props.buildConsumerProperties(),
				new StringDeserializer(),
				new JsonDeserializer<>(Operation.class, false));
	}


	@Bean
	public ProducerFactory<Object, Object> producerFactory( KafkaProperties props) {

		return new DefaultKafkaProducerFactory<>(props.buildProducerProperties(), new StringSerializer(), (JsonSerializer) getJSONSerializer());
	}

	private JsonSerializer<Operation> getJSONSerializer() {
		return new JsonSerializer<Operation>(getObjectMapper());
	}

	/**
	 * Custom mapper with identation
	 *
	 */
	private ObjectMapper getObjectMapper() {
		ObjectMapper mapper = new ObjectMapper();
		mapper.enable( INDENT_OUTPUT);
		return mapper;
	}


	@Bean
	public ConcurrentKafkaListenerContainerFactory<String, Operation> kafkaListenerContainerFactory(ConsumerFactory<String, Operation> cf) {

		ConcurrentKafkaListenerContainerFactory<String, Operation> fac = new ConcurrentKafkaListenerContainerFactory<>();
		fac.setConsumerFactory(cf);
		fac.setConcurrency(1); // Important for message processing order
		return fac;
	}


}