package com.kchandrakant.learning;

import static org.assertj.core.api.Assertions.assertThat;

import com.kchandrakant.learning.service.MyKotlinService;
import com.kchandrakant.learning.service.MyKotlinService;
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
