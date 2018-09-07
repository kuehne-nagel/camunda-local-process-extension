package com.kn.bpa.testapplication.lex.service;

import static com.kn.bpa.testapplication.jms.DockerJmsAutoConfiguration.SELECTOR_LEX_PROCESS_CANCELED;
import static com.kn.bpa.testapplication.jms.DockerJmsAutoConfiguration.SELECTOR_LEX_PROCESS_STARTED;
import static com.kn.bpa.testapplication.jms.DockerJmsAutoConfiguration.SELECTOR_LEX_TASK_COMPLETED;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.kn.bpa.lex.communication.commands.CancelLexProcessCommand;
import com.kn.bpa.lex.communication.commands.CompleteLexTaskCommand;
import com.kn.bpa.lex.communication.commands.StartLexProcessCommand;
import com.kn.bpa.lex.communication.events.LexProcessCanceledEvent;
import com.kn.bpa.lex.communication.events.LexProcessCompletedEvent;
import com.kn.bpa.lex.communication.events.LexTaskCanceledEvent;
import com.kn.bpa.lex.communication.events.LexTaskCreatedEvent;
import com.kn.bpa.lex.server.LexServerService;
import com.kn.bpa.lex.server.handler.LexProcessCanceledHandler;
import com.kn.bpa.lex.server.handler.LexProcessCompletedHandler;
import com.kn.bpa.lex.server.handler.LexTaskCanceledHandler;
import com.kn.bpa.lex.server.handler.LexTaskCreatedHandler;

import lombok.extern.slf4j.Slf4j;

@Service
@RestController()
@RequestMapping("/lex")
@Slf4j
public class LexService implements LexServerService {

  private final PublishJmsLexCommandService publishLexCommandJmsService;
  private final LexTaskCreatedHandler lexTaskCreatedHandler;
  private final LexProcessCompletedHandler lexProcessCompletedHandler;
  private final LexProcessCanceledHandler lexProcessCanceledHandler;
  private final LexTaskCanceledHandler lexTaskCanceledHandler;

  @Autowired
  public LexService(
    final PublishJmsLexCommandService publishLexCommandJmsService,
    final LexTaskCreatedHandler lexTaskCreatedHandler,
    final LexProcessCompletedHandler lexProcessCompletedHandler,
    final LexProcessCanceledHandler lexProcessCanceledHandler,
    final LexTaskCanceledHandler lexTaskCanceledHandler) {
    this.publishLexCommandJmsService = publishLexCommandJmsService;
    this.lexTaskCreatedHandler = lexTaskCreatedHandler;
    this.lexProcessCompletedHandler = lexProcessCompletedHandler;
    this.lexProcessCanceledHandler = lexProcessCanceledHandler;
    this.lexTaskCanceledHandler = lexTaskCanceledHandler;
  }

  /**
   * Converts the StartLexProcessCommand into Json and puts it to the jms queue of <code>DockerJmsAutoConfiguration.QUEUE_PLATFORM_TO_LEX</code>
   */
  @Override
  public void startLexProcess(final StartLexProcessCommand command) {
    log.info("starting lex process: {}", command);
    publishLexCommandJmsService.send(command, SELECTOR_LEX_PROCESS_STARTED);
  }

  /**
   * Converts the CompleteLexTaskCommand into Json and puts it to the jms queue of <code>DockerJmsAutoConfiguration.QUEUE_PLATFORM_TO_LEX</code>
   */
  @Override
  public void completeLexTask(final CompleteLexTaskCommand command) {
    log.info("completing lex task: {}", command);
    publishLexCommandJmsService.send(command, SELECTOR_LEX_TASK_COMPLETED);
  }

  @Override
  public void cancelLexProcess(final CancelLexProcessCommand command) {
    log.info("cancel lex task: {}", command);
    publishLexCommandJmsService.send(command, SELECTOR_LEX_PROCESS_CANCELED);
  }

  /**
   * Helper method which takes the jms body as json string and maps it into an LexTaskCreatedEvent
   *
   * @param event ... The LexTaskCreatedEvent in json format
   */
  @RequestMapping(value = "/task/created", method = RequestMethod.PUT)
  public void onLexTaskCreated(@RequestBody final String event) {
    this.onLexTaskCreated(LexTaskCreatedEvent.fromJson(event));
  }

  @Override
  public void onLexTaskCreated(final LexTaskCreatedEvent event) {
    log.info("starting lex task proxy: {}", event);
    lexTaskCreatedHandler.accept(event);
  }

  @RequestMapping(value = "/task/canceled", method = RequestMethod.PUT)
  public void onLexTaskCanceled(@RequestBody final String event) {
    this.onLexTaskCanceled(LexTaskCanceledEvent.fromJson(event));
  }

  @Override
  public void onLexTaskCanceled(final LexTaskCanceledEvent event) {
    log.info("cancel lex task proxy: {}", event);
    lexTaskCanceledHandler.accept(event);
  }

  /**
   * Helper method which takes the jms body as json string and maps it into an LexProcessCompletedEvent
   *
   * @param event ... The LexProcessCompletedEvent in json format
   */
  @RequestMapping(value = "/process/completed", method = RequestMethod.PUT)
  public void onLexProcessCompleted(@RequestBody final String event) {
    this.onLexProcessCompleted(LexProcessCompletedEvent.fromJson(event));
  }

  @Override
  public void onLexProcessCompleted(final LexProcessCompletedEvent event) {
    lexProcessCompletedHandler.accept(event);
  }

  @RequestMapping(value = "/process/canceled", method = RequestMethod.PUT)
  public void onLexProcessCanceled(@RequestBody final String event) {
    this.onLexProcessCanceled(LexProcessCanceledEvent.fromJson(event));
  }

  @Override
  public void onLexProcessCanceled(final LexProcessCanceledEvent event) {
    lexProcessCanceledHandler.accept(event);
  }
}
