package com.allog.dallog.common.annotation;

import com.allog.dallog.common.DatabaseCleaner;
import com.allog.dallog.common.config.OAuthConfig;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = OAuthConfig.class)
public class ServiceTest {

    @Autowired
    private DatabaseCleaner databaseCleaner;

    @BeforeEach
    void setUp() {
        databaseCleaner.execute();
    }
}
