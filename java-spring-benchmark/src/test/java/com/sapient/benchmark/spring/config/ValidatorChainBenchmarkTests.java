package com.sapient.benchmark.spring.config;

import com.sapient.benchmark.spring.AbstractBenchmark;
import org.openjdk.jmh.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.TimeUnit;

@SpringBootTest
@State(Scope.Benchmark)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MICROSECONDS)
public class ValidatorChainBenchmarkTests extends AbstractBenchmark {

    private static ValidationChain validationChain;

    @Autowired
    void setValidationConfiguration(ValidationChain validationChain) {
        ValidatorChainBenchmarkTests.validationChain = validationChain;
    }

    @Benchmark
    public void getFromMapBenchmark() {
        validationChain.getValidators("payment-order", "VIO", "initiationDay");
    }

    @Benchmark
    public void getFromContextBenchmark() {
        validationChain.getValidatorsFromContext("payment-order", "VIO", "initiationDay");
    }

    @Benchmark
    public void getFromFlatMapBenchmark() {
        validationChain.getValidatorsFromFlatCache("payment-order", "VIO", "initiationDay");
    }

    @Benchmark
    public void getFromNestedMapBenchmark() {
        validationChain.getValidatorsFromNestedCache("payment-order", "VIO", "initiationDay");
    }

}
