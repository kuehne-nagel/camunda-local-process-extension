package com.kn.bpa.testapplication.lex.service;

import static com.kn.bpa.testapplication.jms.DockerJmsAutoConfiguration.JMS_PROPERTY_COMMAND_TYPE;

import java.util.LinkedList;
import java.util.List;
import java.util.function.Supplier;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessagePostProcessor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionSynchronizationAdapter;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import com.kn.bpa.lex.communication.commands.LexCommand;
import com.kn.bpa.testapplication.jms.DockerJmsAutoConfiguration;

@Service
public class PublishJmsLexCommandService {

  private final JmsTemplate jmsTemplate;

  @Autowired
  public PublishJmsLexCommandService(final JmsTemplate jmsTemplate) {
    this.jmsTemplate = jmsTemplate;
  }

  /**
   * Sending a jms message to a lex application after committing the spring transaction!
   *
   * @param lexCommand  represents the body of the message
   * @param commandType is used by the receiver to resolve the commandType.
   */
  void send(final LexCommand lexCommand, final String commandType) {
    final AfterCommitSynchronization es = currentSynchronization(AfterCommitSynchronization.class, AfterCommitSynchronization::new);
    es.publish(() -> jmsTemplate.convertAndSend(
      DockerJmsAutoConfiguration.QUEUE_PLATFORM_TO_LEX,
      lexCommand.toJson(),
      setCommandTypeMessageProperty(commandType)));
  }

  private MessagePostProcessor setCommandTypeMessageProperty(final String commandType) {
    return message -> {
      message.setStringProperty(JMS_PROPERTY_COMMAND_TYPE, commandType);
      return message;
    };
  }

  /**
   * This is needed to make sure that the lex client starts working AFTER lex server has completed his transaction!
   **/
  class AfterCommitSynchronization extends TransactionSynchronizationAdapter {

    private final List<Runnable> commands = new LinkedList<>();

    @Override
    public void afterCommit() {
      commands.forEach(Runnable::run);
    }

    void publish(final Runnable runnable) {
      commands.add(runnable);
    }
  }

  public <T extends TransactionSynchronizationAdapter> T currentSynchronization(final Class<T> clazz, final Supplier<T> constructor) {
    return TransactionSynchronizationManager.getSynchronizations().stream()
      .filter(clazz::isInstance)
      .map(clazz::cast)
      .findFirst()
      .orElseGet(() -> {
        final T e = constructor.get();
        TransactionSynchronizationManager.registerSynchronization(e);
        return e;
      });
  }
}
