package com.kn.bpa.lex.client;

import com.kn.bpa.lex.communication.commands.CancelLexProcessCommand;
import com.kn.bpa.lex.communication.commands.CompleteLexTaskCommand;
import com.kn.bpa.lex.communication.commands.StartLexProcessCommand;
import com.kn.bpa.lex.communication.events.LexProcessCanceledEvent;
import com.kn.bpa.lex.communication.events.LexProcessCompletedEvent;
import com.kn.bpa.lex.communication.events.LexTaskCanceledEvent;
import com.kn.bpa.lex.communication.events.LexTaskCreatedEvent;

public interface LexClientService {

  void handleStartLexProcess(final StartLexProcessCommand command);

  void handleLexTaskCompleted(final CompleteLexTaskCommand command);

  void handleLexProcessCancellation(final CancelLexProcessCommand command);

  void notifyLexTaskCreated(final LexTaskCreatedEvent event);

  void notifyLexTaskCanceled(final LexTaskCanceledEvent event);

  void notifyLexProcessCanceled(final LexProcessCanceledEvent event);

  void notifyLexProcessCompleted(final LexProcessCompletedEvent event);
}
