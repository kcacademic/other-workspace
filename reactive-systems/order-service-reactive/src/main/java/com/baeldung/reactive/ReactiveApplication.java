package com.baeldung.reactive;

import java.io.IOException;

import org.bson.types.ObjectId;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.codec.json.Jackson2JsonDecoder;
import org.springframework.http.codec.json.Jackson2JsonEncoder;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.reactive.function.client.ExchangeStrategies;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.module.SimpleModule;

@SpringBootApplication
public class ReactiveApplication {

    public static void main(String[] args) {
        SpringApplication.run(ReactiveApplication.class, args);
    }

    @Component
    public class ObjectIdSerializer extends JsonSerializer<ObjectId> {
        @Override
        public void serialize(ObjectId value, JsonGenerator jgen, SerializerProvider provider) throws IOException, JsonProcessingException {
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

    @Bean
    public ExchangeStrategies customExchangeStrategies() {
        return ExchangeStrategies.builder()
            .codecs(configurer -> {
                configurer.defaultCodecs()
                    .jackson2JsonDecoder(new Jackson2JsonDecoder(new CustomObjectMapper()));
                configurer.defaultCodecs()
                    .jackson2JsonEncoder(new Jackson2JsonEncoder(new CustomObjectMapper()));
            })
            .build();
    }

    @ControllerAdvice
    public class ExceptionController {
        @ExceptionHandler(value = RuntimeException.class)
        public ResponseEntity<Object> exception(RuntimeException exception) {
            return new ResponseEntity<>(exception.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
