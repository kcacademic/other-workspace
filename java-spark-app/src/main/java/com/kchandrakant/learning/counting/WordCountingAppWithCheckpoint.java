package com.kchandrakant.learning.counting;

import static com.datastax.spark.connector.japi.CassandraJavaUtil.javaFunctions;
import static com.datastax.spark.connector.japi.CassandraJavaUtil.mapToRow;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.Optional;
import org.apache.spark.api.java.function.FlatMapFunction;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.api.java.function.Function2;
import org.apache.spark.api.java.function.Function3;
import org.apache.spark.api.java.function.PairFunction;
import org.apache.spark.api.java.function.VoidFunction;
import org.apache.spark.streaming.Durations;
import org.apache.spark.streaming.State;
import org.apache.spark.streaming.StateSpec;
import org.apache.spark.streaming.api.java.JavaDStream;
import org.apache.spark.streaming.api.java.JavaInputDStream;
import org.apache.spark.streaming.api.java.JavaMapWithStateDStream;
import org.apache.spark.streaming.api.java.JavaPairDStream;
import org.apache.spark.streaming.api.java.JavaStreamingContext;
import org.apache.spark.streaming.kafka010.ConsumerStrategies;
import org.apache.spark.streaming.kafka010.KafkaUtils;
import org.apache.spark.streaming.kafka010.LocationStrategies;

import com.kchandrakant.learning.demo.Word;

import scala.Tuple2;

public class WordCountingAppWithCheckpoint {
	
	public static JavaSparkContext sparkContext;

	public static void main(String[] args) throws InterruptedException {
		
		Logger.getLogger("org").setLevel(Level.OFF);
		Logger.getLogger("akka").setLevel(Level.OFF);

		Map<String, Object> kafkaParams = new HashMap<>();
		kafkaParams.put("bootstrap.servers", "localhost:9092");
		kafkaParams.put("key.deserializer", StringDeserializer.class);
		kafkaParams.put("value.deserializer", StringDeserializer.class);
		kafkaParams.put("group.id", "use_a_separate_group_id_for_each_stream");
		kafkaParams.put("auto.offset.reset", "latest");
		kafkaParams.put("enable.auto.commit", false);

		Collection<String> topics = Arrays.asList("twitter");

		SparkConf sparkConf = new SparkConf();
		sparkConf.setMaster("local[2]");
		sparkConf.setAppName("WordCountingAppWithCheckpoint");
		sparkConf.set("spark.cassandra.connection.host", "127.0.0.1");

		JavaStreamingContext streamingContext = new JavaStreamingContext(sparkConf, Durations.seconds(1));
		
		sparkContext = streamingContext.sparkContext();
		
		streamingContext.checkpoint("./.checkpoint");

		JavaInputDStream<ConsumerRecord<String, String>> messages = KafkaUtils.createDirectStream(
				streamingContext,
				LocationStrategies.PreferConsistent(),
				ConsumerStrategies.<String, String>Subscribe(topics, kafkaParams));
		
		JavaPairDStream<String, String> results = messages.mapToPair(
				(PairFunction<ConsumerRecord<String, String>, String, String>) 
					record -> new Tuple2<>(record.key(), record.value())
				);
		
		JavaDStream<String> lines = results.map(
				(Function<Tuple2<String, String>, String>)
					tuple -> tuple._2
				);
		
		JavaDStream<String> words = lines.flatMap(
				(FlatMapFunction<String, String>)
					str -> Arrays.asList(str.split("\\s+")).iterator()
				);
		
		JavaPairDStream<String, Integer> wordCounts = words.mapToPair(
				(PairFunction<String, String, Integer>)
					str -> new Tuple2<>(str, 1)
				).reduceByKey(
						(Function2<Integer, Integer, Integer>)
							(i1, i2) -> i1 + i2
						);
				
		JavaMapWithStateDStream<String, Integer, Integer, Tuple2<String, Integer>> cumulativeWordCounts = wordCounts
				.mapWithState(StateSpec.function(
						(Function3<String, Optional<Integer>, State<Integer>, Tuple2<String, Integer>>)
							(word, one, state) -> {
								int sum = one.orElse(0) + (state.exists() ? state.get() : 0);
								Tuple2<String, Integer> output = new Tuple2<>(word, sum);
								state.update(sum);
								return output;
							}	
						));
		
		Properties kafkaOutboundParams = new Properties();
		kafkaOutboundParams.put("bootstrap.servers", "localhost:9092");
		kafkaOutboundParams.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
		kafkaOutboundParams.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
		kafkaOutboundParams.put("acks", "1");
		kafkaOutboundParams.put("retries", "3");
		kafkaOutboundParams.put("linger.ms", 5);
		
		cumulativeWordCounts.foreachRDD(
				(VoidFunction<JavaRDD<Tuple2<String,Integer>>>)
					javaRdd -> {
						List<Tuple2<String,Integer>> wordCountList = javaRdd.collect();
				        for (Tuple2<String,Integer> tuple : wordCountList) {
				             KafkaProducer<String, String> producer = new KafkaProducer<String, String>(kafkaOutboundParams);
				             producer.send(new ProducerRecord<String, String>("summary", tuple._1 + " : " + tuple._2));
				             producer.close();
				             
				             List<Word> wordList = Arrays.asList(new Word(tuple._1, tuple._2));
				             JavaRDD<Word> rdd = sparkContext.parallelize(wordList);
				             javaFunctions(rdd).writerBuilder("vocabulary", "words", mapToRow(Word.class)).saveToCassandra();
				        }
					}
				);
		
		streamingContext.start();
		streamingContext.awaitTermination();
	}
	
}
