package com.kn.bpa.testapplication.lex.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.kn.bpa.lex.communication.commands.CompleteLexTaskCommand;
import com.kn.bpa.lex.server.LexServerService;

import io.swagger.annotations.ApiParam;

@RestController
@RequestMapping("/lex-task-command")
public class LexTaskCommandController {

  private final LexServerService lexServerService;

  public LexTaskCommandController(final LexServerService lexServerService) {
    this.lexServerService = lexServerService;
  }

  @Transactional
  @RequestMapping(value = "/completeLexTask", method = RequestMethod.GET)
  public ResponseEntity<?> completeLexTask(
    @ApiParam(required = true) //
    @RequestParam(value = "taskId") final String taskId,
    @ApiParam(required = true) //
    @RequestParam(value = "applicationId") final String applicationId) {

    lexServerService.completeLexTask(CompleteLexTaskCommand.of(
      taskId,
      applicationId));

    return new ResponseEntity(HttpStatus.OK);
  }
}
