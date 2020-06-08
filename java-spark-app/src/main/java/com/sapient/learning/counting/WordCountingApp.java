package com.sapient.learning.counting;

import static com.datastax.spark.connector.japi.CassandraJavaUtil.javaFunctions;
import static com.datastax.spark.connector.japi.CassandraJavaUtil.mapToRow;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
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
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.function.FlatMapFunction;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.api.java.function.Function2;
import org.apache.spark.api.java.function.PairFunction;
import org.apache.spark.api.java.function.VoidFunction;
import org.apache.spark.streaming.Durations;
import org.apache.spark.streaming.api.java.JavaDStream;
import org.apache.spark.streaming.api.java.JavaInputDStream;
import org.apache.spark.streaming.api.java.JavaPairDStream;
import org.apache.spark.streaming.api.java.JavaStreamingContext;
import org.apache.spark.streaming.kafka010.ConsumerStrategies;
import org.apache.spark.streaming.kafka010.KafkaUtils;
import org.apache.spark.streaming.kafka010.LocationStrategies;

import com.sapient.learning.demo.Word;

import scala.Tuple2;

public class WordCountingApp {

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
		sparkConf.setAppName("WordCountingApp");
		sparkConf.set("spark.cassandra.connection.host", "127.0.0.1");

		JavaStreamingContext streamingContext = new JavaStreamingContext(sparkConf, Durations.seconds(1));

		JavaInputDStream<ConsumerRecord<String, String>> messages = KafkaUtils.createDirectStream(streamingContext,
				LocationStrategies.PreferConsistent(),
				ConsumerStrategies.<String, String>Subscribe(topics, kafkaParams));
		
		
		JavaPairDStream<String, String> results = messages.mapToPair(
				new PairFunction<ConsumerRecord<String, String>, String, String>() {
			private static final long serialVersionUID = 1L;
			@Override
		    public Tuple2<String, String> call(ConsumerRecord<String, String> record) {
		        return new Tuple2<>(record.key(), record.value());
		    }
		});
		
		JavaDStream<String> lines = results.map(
				new Function<Tuple2<String, String>, String>() {
			private static final long serialVersionUID = 1L;
			@Override
			public String call(Tuple2<String, String> tuple2) {
				return tuple2._2();
			}
		});
		
		JavaDStream<String> words = lines.flatMap(
				new FlatMapFunction<String, String>() {
			private static final long serialVersionUID = 1L;
		    @Override
		    public Iterator<String> call(String x) {
		    	System.out.println("Line retrieved: " + x);
		        return Arrays.asList(x.split("\\s+")).iterator();
		    }
		});
		
		JavaPairDStream<String, Integer> wordCounts = words.mapToPair(
				new PairFunction<String, String, Integer>() {
			private static final long serialVersionUID = 1L;
		    @Override
		    public Tuple2<String, Integer> call(String s) {
		    	System.out.println("Word to count: " + s);
		        return new Tuple2<>(s, 1);
		    }
		}).reduceByKey(new Function2<Integer, Integer, Integer>() {
			private static final long serialVersionUID = 1L;
		    @Override
		    public Integer call(Integer i1, Integer i2) {
		    	System.out.println("Count with reduceByKey: " + i1 + i2);
		        return i1 + i2;
		    }
		});
		
		Properties kafkaOutboundParams = new Properties();
		kafkaOutboundParams.put("bootstrap.servers", "localhost:9092");
		kafkaOutboundParams.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
		kafkaOutboundParams.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
		kafkaOutboundParams.put("acks", "1");
		kafkaOutboundParams.put("retries", "3");
		kafkaOutboundParams.put("linger.ms", 5);
		
		wordCounts.foreachRDD(new VoidFunction<JavaPairRDD<String, Integer>>() {
			private static final long serialVersionUID = 1L;
			@Override
		    public void call(JavaPairRDD<String, Integer> javaRdd) throws Exception {
		        Map<String, Integer> wordCountMap = javaRdd.collectAsMap();
		        for (String key : wordCountMap.keySet()) {
		             KafkaProducer<String, String> producer = new KafkaProducer<String, String>(kafkaOutboundParams);
		             producer.send(new ProducerRecord<String, String>("summary", key + " : " + wordCountMap.get(key)));
		             producer.close();
		             
		             List<Word> words = Arrays.asList(new Word(key, wordCountMap.get(key)));
		             JavaRDD<Word> rdd = streamingContext.sparkContext().parallelize(words);
		             javaFunctions(rdd).writerBuilder("vocabulary", "words", mapToRow(Word.class)).saveToCassandra();
		        }
		    }
		});
		
		streamingContext.start();
		streamingContext.awaitTermination();
	}
	
}
