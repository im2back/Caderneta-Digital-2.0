package com.github.im2back.stockms.infra.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.github.im2back.stockms.exceptions.feingexceptions.CustomErrorDecoder;

import feign.codec.ErrorDecoder;

@Configuration
public class FeignClientConfiguration {

    @Bean
    public ErrorDecoder errorDecoder() {
        return new CustomErrorDecoder();
    }
}