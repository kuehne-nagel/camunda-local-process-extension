package com.kn.bpa.lex.server;

import com.kn.bpa.lex.communication.commands.CancelLexProcessCommand;
import com.kn.bpa.lex.communication.commands.CompleteLexTaskCommand;
import com.kn.bpa.lex.communication.commands.StartLexProcessCommand;
import com.kn.bpa.lex.communication.events.LexProcessCanceledEvent;
import com.kn.bpa.lex.communication.events.LexProcessCompletedEvent;
import com.kn.bpa.lex.communication.events.LexTaskCanceledEvent;
import com.kn.bpa.lex.communication.events.LexTaskCreatedEvent;

/**
 * Applications that want to serve as a master for lex process applications must implement this interface.
 * They must be able to send the commands to the lex engine (start process, complete task), and react on events
 * sent back from the lex instance (taskCreated, processCompleted).
 * <p>
 * Typically, the implementation will publish/subscribe to some kind of message broker (JMS).
 */
public interface LexServerService {

  void startLexProcess(final StartLexProcessCommand command);

  void cancelLexProcess(final CancelLexProcessCommand command);

  void completeLexTask(final CompleteLexTaskCommand command);

  void onLexTaskCanceled(final LexTaskCanceledEvent event);

  void onLexTaskCreated(final LexTaskCreatedEvent event);

  /**
   * If still active Lex-tasks exist for the completed lex-process an exception is thrown
   */
  void onLexProcessCompleted(final LexProcessCompletedEvent event);

  void onLexProcessCanceled(final LexProcessCanceledEvent event);
}
