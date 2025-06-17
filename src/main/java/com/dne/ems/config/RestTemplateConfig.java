package com.dne.ems.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * 配置类，提供 RestTemplate Bean
 */
@Configuration
public class RestTemplateConfig {

    /**
     * 创建并配置 RestTemplate Bean
     * @return 配置好的 RestTemplate 实例
     */
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
} 