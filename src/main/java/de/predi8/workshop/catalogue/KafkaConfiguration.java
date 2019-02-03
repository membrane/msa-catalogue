package de.predi8.workshop.catalogue;


import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.ser.std.JsonValueSerializer;
import de.predi8.workshop.catalogue.event.Operation;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.KafkaAdmin;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;

@EnableKafka
@Configuration
public class KafkaConfiguration {


	@Bean
	public ConsumerFactory<String, Operation> consumerFactory(KafkaProperties props) {

		ObjectMapper mapper = new ObjectMapper();
		mapper.enable(SerializationFeature.INDENT_OUTPUT);

		return new DefaultKafkaConsumerFactory<>( props.buildConsumerProperties(),
				new StringDeserializer(),
				new JsonDeserializer<>(Operation.class, mapper, false));
	}


	@Bean
	public ConcurrentKafkaListenerContainerFactory<String, Operation> kafkaListenerContainerFactory(ConsumerFactory<String, Operation> cf) {

		ConcurrentKafkaListenerContainerFactory<String, Operation> fac = new ConcurrentKafkaListenerContainerFactory<>();
		fac.setConsumerFactory(cf);
		fac.setConcurrency(1); // Wichtig für die Reihenfolge
		return fac;
	}


}