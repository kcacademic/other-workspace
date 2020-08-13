package com.sapient.learning.application;

import static org.assertj.core.api.Assertions.assertThat;

import com.sapient.learning.service.MyKotlinService;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class ApplicationTest {

    @Autowired
    private MyKotlinService myKotlinService;

    @Test
    public void contextLoads() {
        assertThat(myKotlinService.message()).isNotNull();
    }

}
