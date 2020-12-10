package com.sapient.benchmark.spring;

import org.junit.jupiter.api.BeforeEach;
import org.openjdk.jmh.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@SpringBootTest
@State(Scope.Benchmark)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MICROSECONDS)
public class SomeBenchmarkTests extends AbstractBenchmark {

    Map<String, Object> map = new HashMap<>();

    private static ApplicationContext applicationContext;

    @Autowired
    void setApplicationContext(ApplicationContext applicationContext) {
        SomeBenchmarkTests.applicationContext = applicationContext;
    }

    @BeforeEach
    public void setUp() {
        map.put("someDomain", applicationContext.getBean("someDomain"));
    }

    @Benchmark
    public void getFromMapBenchmark() {
        map.get("someDomain");
    }

    @Benchmark
    public void getFromApplicationContextBenchmark() {
        applicationContext.getBean("someDomain");
    }

}
