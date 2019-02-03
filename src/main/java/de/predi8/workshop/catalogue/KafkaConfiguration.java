package de.predi8.workshop.catalogue;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import de.predi8.workshop.catalogue.event.Operation;
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
	public ConsumerFactory<String, Operation> consumerFactory( KafkaProperties props) {
		return new DefaultKafkaConsumerFactory<>( props.buildConsumerProperties(),
				new StringDeserializer(),
				new JsonDeserializer<>(Operation.class, false));
	}

	/**
	 * Custom mapper with identation
	 *
	 * @param props
	 * @return
	 */
	@Bean
	public ProducerFactory<Object, Object> producerFactory( KafkaProperties props) {

		ObjectMapper mapper = new ObjectMapper();
		mapper.enable(SerializationFeature.INDENT_OUTPUT);

		JsonSerializer ser =  new JsonSerializer<Operation>(mapper);

		return new DefaultKafkaProducerFactory<>(props.buildProducerProperties(), new StringSerializer(),ser);
	}


	@Bean
	public ConcurrentKafkaListenerContainerFactory<String, Operation> kafkaListenerContainerFactory(ConsumerFactory<String, Operation> cf) {

		ConcurrentKafkaListenerContainerFactory<String, Operation> fac = new ConcurrentKafkaListenerContainerFactory<>();
		fac.setConsumerFactory(cf);
		fac.setConcurrency(1); // Important for message processing order
		return fac;
	}


}