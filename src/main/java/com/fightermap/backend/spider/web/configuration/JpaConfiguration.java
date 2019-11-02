package com.fightermap.backend.spider.web.configuration;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.util.Optional;

/**
 * @author zengqk
 */
@EnableJpaAuditing
@Configuration
public class JpaConfiguration {

    private final ApplicationContext applicationContext;

    public JpaConfiguration(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Bean
    public AuditorAware<String> auditorAware() {
        //todo 实现用户审计
        return () -> {
            return Optional.ofNullable("spider");
        };
    }
}
