package com.sapient.benchmark.spring.validator.base;

public interface Validator<T> {
    T validate(T data);
}
