package com.kchandrakant.learning.controller;

import java.util.Arrays;
import java.util.Collections;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import io.opencensus.stats.Aggregation;
import io.opencensus.stats.Aggregation.Distribution;
import io.opencensus.stats.BucketBoundaries;
import io.opencensus.stats.Measure.MeasureDouble;
import io.opencensus.stats.Stats;
import io.opencensus.stats.StatsRecorder;
import io.opencensus.stats.View;
import io.opencensus.stats.View.Name;
import io.opencensus.stats.ViewManager;
import io.opencensus.tags.TagKey;
import reactor.core.publisher.Flux;

@RestController
public class MyController {

	@GetMapping("/")
	public Flux<String> getAll() {
		handleOpenCensusMetrics();
		return Flux.just("Hello", "world!").name("mymetric").metrics();
	}

	private void handleOpenCensusMetrics() {
		
		MeasureDouble M_LATENCY_MS = MeasureDouble.create("latency", "The latency in milliseconds", "ms");
		StatsRecorder STATS_RECORDER = Stats.getStatsRecorder();
		STATS_RECORDER.newMeasureMap().put(M_LATENCY_MS, 17.0).record();

		Aggregation latencyDistribution = Distribution
				.create(BucketBoundaries.create(Arrays.asList(0.0, 25.0, 100.0, 200.0, 400.0, 800.0, 10000.0)));
		TagKey KEY_METHOD = TagKey.create("method");
		View view = View.create(Name.create("myapp/latency"), "The distribution of the latencies", M_LATENCY_MS,
				latencyDistribution, Collections.singletonList(KEY_METHOD));

		ViewManager manager = Stats.getViewManager();
		manager.registerView(view);

	}

}
