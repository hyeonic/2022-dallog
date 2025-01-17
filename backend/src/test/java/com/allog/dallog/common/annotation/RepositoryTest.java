package com.allog.dallog.common.annotation;

import com.allog.dallog.global.config.JpaConfig;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

@DataJpaTest
@Import(JpaConfig.class)
public class RepositoryTest {
}
