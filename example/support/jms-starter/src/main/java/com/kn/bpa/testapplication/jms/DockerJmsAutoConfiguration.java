package com.kn.bpa.testapplication.jms;

import java.util.Arrays;

import javax.jms.ConnectionFactory;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.RedeliveryPolicy;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.jms.DefaultJmsListenerContainerFactoryConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.config.JmsListenerContainerFactory;
import org.springframework.util.ErrorHandler;

import lombok.extern.slf4j.Slf4j;

/**
 * Used in testapp-core and testapp-lex to configure JMS against tha docker-compose activemq.
 */
@Configuration
@EnableJms
@Slf4j
public class DockerJmsAutoConfiguration {

  // for the testapplication example, we use a simple broker configuration: one queue for each direction of communication.
  // in real life, we would have a more complex fan-out configuration that routes messages based on their DestinationId (message-header)
  public static final String QUEUE_PLATFORM_TO_LEX = "platform-to-lex";

  public static final String SELECTOR_LEX_PROCESS_STARTED = "lexProcessStarted";
  public static final String SELECTOR_LEX_PROCESS_CANCELED = "lexProcessCanceled";
  public static final String SELECTOR_LEX_TASK_COMPLETED = "lexTaskCompleted";

  public static final String JMS_PROPERTY_COMMAND_TYPE = "commandType";

  @Value("${spring.activemq.broker-url}")
  private String brokerUrl;

  @Value("${spring.activemq.user}")
  private String brokerUser;

  @Value("${spring.activemq.password}")
  private String brokerPassword;

  @Bean
  public ActiveMQConnectionFactory activeMQConnectionFactory() {
    final ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory();
    connectionFactory.setBrokerURL(brokerUrl);
    connectionFactory.setUserName(brokerUser);
    connectionFactory.setPassword(brokerPassword);
    // allow message instances to be classes in this package
    connectionFactory.setTrustedPackages(Arrays.asList("com.kn.bpa"));

    final RedeliveryPolicy redeliveryPolicy = new RedeliveryPolicy();
    redeliveryPolicy.setMaximumRedeliveries(0);
    connectionFactory.setRedeliveryPolicy(redeliveryPolicy);

    return connectionFactory;
  }

  @Bean
  public JmsListenerContainerFactory<?> jmsListenerContainerFactory(
    final ConnectionFactory connectionFactory,
    final DefaultJmsListenerContainerFactoryConfigurer configurer,
    @Qualifier("jmsErrorHandler") final ErrorHandler jmsErrorHandler) {
    final DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
    // This provides all boot's default to this factory, including the message converter
    configurer.configure(factory, connectionFactory);
    // You could still override some of Boot's default if necessary.
    factory.setErrorHandler(jmsErrorHandler);
    factory.setPubSubDomain(false); // false = queue, true = topic
    factory.setConcurrency("1-1"); // lower and upper limits for concurrent sessions/consumers

    return factory;
  }

  @Bean
  @Qualifier("jmsErrorHandler")
  public ErrorHandler errorHandler() {
    return Throwable::printStackTrace;
  }
}
