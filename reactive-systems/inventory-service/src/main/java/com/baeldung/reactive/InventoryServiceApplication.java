package com.baeldung.reactive;

import java.io.IOException;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.MongoTransactionManager;
import org.springframework.data.repository.init.Jackson2RepositoryPopulatorFactoryBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.baeldung.reactive.repository.ProductRepository;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.module.SimpleModule;

@SpringBootApplication
public class InventoryServiceApplication {

	@Autowired
	ProductRepository productRepository;

	public static void main(String[] args) {
		SpringApplication.run(InventoryServiceApplication.class, args);

	}

	@Bean
	public Jackson2RepositoryPopulatorFactoryBean getRespositoryPopulator() {
		Jackson2RepositoryPopulatorFactoryBean factory = new Jackson2RepositoryPopulatorFactoryBean();
		factory.setResources(new Resource[] { new ClassPathResource("data.json") });
		return factory;
	}
	
    @Bean
    MongoTransactionManager transactionManager(MongoDatabaseFactory dbFactory) {
        return new MongoTransactionManager(dbFactory);
    }

	@Component
	public class ObjectIdSerializer extends JsonSerializer<ObjectId> {

		@Override
		public void serialize(ObjectId value, JsonGenerator jgen, SerializerProvider provider)
				throws IOException, JsonProcessingException {
			jgen.writeString(value.toString());
		}
	}

	@Component
	public class CustomObjectMapper extends ObjectMapper {
		private static final long serialVersionUID = 1L;

		public CustomObjectMapper() {
			SimpleModule module = new SimpleModule("ObjectIdmodule");
			module.addSerializer(ObjectId.class, new ObjectIdSerializer());
			this.registerModule(module);
		}

	}

	@ControllerAdvice
	public class ProductExceptionController {
		@ExceptionHandler(value = RuntimeException.class)
		public ResponseEntity<Object> exception(RuntimeException exception) {
			return new ResponseEntity<>(exception.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

}
