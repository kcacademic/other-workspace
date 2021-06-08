package com.kchandrakant.learning.spring;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.kchandrakant.learning.spring.config.ValidationChain;

@SpringBootTest
public class BasicTests {

    @Autowired
    ValidationChain validationConfiguration;

    @Test
    public void testConfigurationGetters() {

        System.out.println(validationConfiguration.getValidators("payment-order", "VIO", "initiationDay"));
        System.out.println(validationConfiguration.getValidatorsFromContext("payment-order", "VIO", "initiationDay"));
        System.out.println(validationConfiguration.getValidatorsFromFlatCache("payment-order", "VIO", "initiationDay"));
        System.out.println(validationConfiguration.getValidatorsFromNestedCache("payment-order", "VIO", "initiationDay"));

    }
}
