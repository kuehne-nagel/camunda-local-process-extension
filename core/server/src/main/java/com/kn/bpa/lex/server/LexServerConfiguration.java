package com.kn.bpa.lex.server;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(excludeFilters = @ComponentScan.Filter(Configuration.class))
public class LexServerConfiguration {
}
