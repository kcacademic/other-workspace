package com.kchandrakant.learning;

import io.reactivex.Observable;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.IntegerDeserializer;
import org.apache.kafka.common.serialization.IntegerSerializer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;
import reactor.kafka.receiver.KafkaReceiver;
import reactor.kafka.receiver.ReceiverOptions;
import reactor.kafka.receiver.ReceiverRecord;
import reactor.kafka.sender.KafkaSender;
import reactor.kafka.sender.SenderOptions;
import reactor.kafka.sender.SenderRecord;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/")
public class Controller {

    @Autowired
    PersonRepository personRepository;

    Scheduler scheduler = Schedulers.newBoundedElastic(5, 10, "MyThreadGroup");

    @GetMapping("/webclient")
    public Mono<String> getIndexWebClient() {
        return WebClient.create("http://localhost:8080/index").get()
                .retrieve()
                .bodyToMono(String.class)
                .subscribeOn(scheduler)
                .publishOn(scheduler)
                .doOnNext(s -> printThreads());
    }

    @GetMapping("/rxjava")
    public Observable<String> getIndexRxJava() {
        return Observable
                .fromIterable(Arrays.asList("Kumar", "Chandrakant"))
                .map(s -> s.toUpperCase())
                .observeOn(io.reactivex.schedulers.Schedulers.trampoline())
                .doOnComplete(this::printThreads);
    }

    @GetMapping("/mongodb")
    public Flux<Person> getIndexMongo() {
        return personRepository.findAll().doOnComplete(this::printThreads);
    }

    @GetMapping("kafka")
    public Flux<String> getIndexKafka() {
        Map<String, Object> producerProps = new HashMap<>();
        producerProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        producerProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, IntegerSerializer.class);
        producerProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        SenderOptions<Integer, String> senderOptions = SenderOptions.create(producerProps);
        KafkaSender<Integer, String> sender = KafkaSender.create(senderOptions);
        Flux<SenderRecord<Integer, String, Integer>> outboundFlux = Flux
                .range(1, 10)
                .map(i -> SenderRecord.create(new ProducerRecord<>("reactive-test", i, "Message_" + i), i));
        sender.send(outboundFlux).subscribe();

        Map<String, Object> consumerProps = new HashMap<>();
        consumerProps.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        consumerProps.put(ConsumerConfig.CLIENT_ID_CONFIG, "my-consumer");
        consumerProps.put(ConsumerConfig.GROUP_ID_CONFIG, "my-group");
        consumerProps.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, IntegerDeserializer.class);
        consumerProps.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        consumerProps.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        ReceiverOptions<Integer, String> receiverOptions = ReceiverOptions.create(consumerProps);
        receiverOptions.subscription(Collections.singleton("reactive-test"));
        KafkaReceiver<Integer, String> receiver = KafkaReceiver.create(receiverOptions);
        Flux<ReceiverRecord<Integer, String>> inboundFlux = receiver.receive();
        inboundFlux
                .doOnComplete(this::printThreads)
                .subscribe(r -> {
                    System.out.printf("Received message: %s\n", r.value());
                    r.receiverOffset().acknowledge();
                });
        return Flux.just("Hello World!");
    }

    @GetMapping("/index")
    public Mono<String> getIndex() {
        return Mono.just("Hello World!");
    }

    public void printThreads() {
        System.out.println("START");
        Thread.getAllStackTraces().keySet().stream()
                .forEach(t ->
                        System.out.printf("%-20s \t %s \t %d \t %s\n", t.getName(), t.getState(), t.getPriority(), t.isDaemon() ? "Daemon" : "Normal"));
        System.out.println("END");
    }
}
