package com.kchandrakant.learning.demo;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.Optional;
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

import com.datastax.spark.connector.japi.CassandraRow;
import com.datastax.spark.connector.japi.rdd.CassandraTableScanJavaRDD;

import static com.datastax.spark.connector.japi.CassandraJavaUtil.javaFunctions;
import static com.datastax.spark.connector.japi.CassandraJavaUtil.mapToRow;

import scala.Tuple2;

public class SimpleAppSaveToCassandraWithCheckpoint {
	
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

		Collection<String> topics = Arrays.asList("votes");

		SparkConf sparkConf = new SparkConf();
		sparkConf.setMaster("local[2]");
		sparkConf.setAppName("SimpleAppSaveToCassandraWithCheckpoint");
		sparkConf.set("spark.cassandra.connection.host", "127.0.0.1");

		JavaStreamingContext streamingContext = new JavaStreamingContext(sparkConf, Durations.seconds(1));
		
		sparkContext = streamingContext.sparkContext();
		
		streamingContext.checkpoint("./.checkpoint");

		JavaInputDStream<ConsumerRecord<String, String>> messages = KafkaUtils.createDirectStream(streamingContext,
				LocationStrategies.PreferConsistent(),
				ConsumerStrategies.<String, String>Subscribe(topics, kafkaParams));

		JavaDStream<String> lines = messages.map(record -> (record.value()));

		JavaPairDStream<String, Integer> counts = lines
				.mapToPair((PairFunction<String, String, Integer>) s -> new Tuple2<>(s, 1))
				.reduceByKey((Function2<Integer, Integer, Integer>) (i1, i2) -> i1 + i2);
		
		Function3<String, Optional<Integer>, State<Integer>, Tuple2<String, Integer>> mappingFunc = 
				(word, one, state) -> {
					int sum = one.orElse(0) + (state.exists() ? state.get() : 0);
					Tuple2<String, Integer> output = new Tuple2<>(word, sum);
					state.update(sum);
					return output;
				};

		JavaPairRDD<String, Integer> initialRDD = JavaPairRDD.fromJavaRDD(streamingContext.sparkContext().emptyRDD());
		
		JavaMapWithStateDStream<String, Integer, Integer, Tuple2<String, Integer>> cumulativeCount = counts
				.mapWithState(StateSpec.function(mappingFunc).initialState(initialRDD));

		cumulativeCount.foreachRDD(new VoidFunction<JavaRDD<Tuple2<String,Integer>>>() {
			private static final long serialVersionUID = 1L;

			@Override
			public void call(JavaRDD<Tuple2<String,Integer>> javaRdd) throws Exception {
				List<Tuple2<String,Integer>> wordCountList = javaRdd.collect();
				for (Tuple2<String,Integer> tuple : wordCountList) {
					CassandraTableScanJavaRDD<CassandraRow> previousWords = javaFunctions(streamingContext.sparkContext())
							.cassandraTable("vocabulary", "words").where("word = '" + tuple + "'");
					Integer oldWords = 0;
					if (previousWords.count() > 0) {
						oldWords = previousWords.first().getInt("count");
					}
					Integer newWords = oldWords + tuple._2;
					List<Word> words = Arrays.asList(new Word(tuple._1, newWords));
					JavaRDD<Word> rdd = sparkContext.parallelize(words);

					javaFunctions(rdd).writerBuilder("vocabulary", "words", mapToRow(Word.class)).saveToCassandra();
				}
			}
		});

		cumulativeCount.print();

		streamingContext.start();
		streamingContext.awaitTermination();
	}

}
