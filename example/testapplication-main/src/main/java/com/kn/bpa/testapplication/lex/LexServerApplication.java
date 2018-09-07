package com.kn.bpa.testapplication.lex;

import org.camunda.bpm.spring.boot.starter.annotation.EnableProcessApplication;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

import com.kn.bpa.lex.server.LexServerConfiguration;
import com.kn.bpa.testapplication.jms.DockerJmsAutoConfiguration;

import lombok.extern.slf4j.Slf4j;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * Plain old simple spring boot application that uses the camunda-springboot starter to run camunda process engine.
 */
@SpringBootApplication
@EnableProcessApplication("lex-server")
@EnableSwagger2
@Slf4j
@Import({
  LexServerConfiguration.class,
  DockerJmsAutoConfiguration.class })
public class LexServerApplication {

  public static void main(final String... args) {
    SpringApplication.run(LexServerApplication.class, args);
  }
}
