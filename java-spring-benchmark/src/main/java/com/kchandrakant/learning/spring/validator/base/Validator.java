package com.kchandrakant.learning.spring.validator.base;

public interface Validator<T> {
    T validate(T data);
}
