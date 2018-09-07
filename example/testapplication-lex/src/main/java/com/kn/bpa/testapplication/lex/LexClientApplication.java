package com.kn.bpa.testapplication.lex;

import org.camunda.bpm.spring.boot.starter.annotation.EnableProcessApplication;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.web.client.RestTemplate;

import com.kn.bpa.lex.client.LexClientConfiguration;
import com.kn.bpa.testapplication.jms.DockerJmsAutoConfiguration;

import lombok.extern.slf4j.Slf4j;

/**
 * Plain old simple spring boot application that uses the camunda-springboot starter to run camunda process engine.
 */
@SpringBootApplication
@EnableProcessApplication("lex-client")
@Slf4j
@Import({
  LexClientConfiguration.class,
  DockerJmsAutoConfiguration.class })
public class LexClientApplication {

  @Value("${lex.server.url}")
  private String endpointUrl;

  @Bean
  RestTemplate restTemplate() {
    return new RestTemplateBuilder().rootUri(endpointUrl).build();
  }

  public static void main(final String... args) {
    SpringApplication.run(LexClientApplication.class, args);
  }
}
