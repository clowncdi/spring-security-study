package com.example.springsecuritystudy.helper;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@ActiveProfiles(value = "test")
@Transactional
public class TestConfig {
}
