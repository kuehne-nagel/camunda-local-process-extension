package com.kn.bpa.lex.client;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(excludeFilters = @ComponentScan.Filter(Configuration.class))
public class LexClientConfiguration {

}
