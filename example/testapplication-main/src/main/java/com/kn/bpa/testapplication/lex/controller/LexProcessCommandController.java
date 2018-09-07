package com.kn.bpa.testapplication.lex.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.kn.bpa.lex.communication.commands.CancelLexProcessCommand;
import com.kn.bpa.lex.communication.commands.StartLexProcessCommand;
import com.kn.bpa.lex.server.LexServerService;

import io.swagger.annotations.ApiParam;

@RestController
@RequestMapping("/lex-processes-command")
public class LexProcessCommandController {

  private final LexServerService lexServerService;

  @Autowired
  public LexProcessCommandController(final LexServerService lexServerService) {
    this.lexServerService = lexServerService;
  }

  @Transactional
  @RequestMapping(value = "/cancelLexProcess", method = RequestMethod.GET)
  public ResponseEntity<?> cancelLexProcess(
    @ApiParam(required = true) //
    @RequestParam(value = "businessId") final String businessId,
    @ApiParam //
    @RequestParam(value = "cancellationReason", required = false) final String cancellationReason,
    @ApiParam(required = true) //
    @RequestParam(value = "clientApplicationId") final String clientApplicationId,
    @ApiParam(required = true) //
    @RequestParam(value = "lexBridgeProcessInstanceId") final String lexBridgeProcessInstanceId) {

    lexServerService.cancelLexProcess(CancelLexProcessCommand.builder()
      .businessObjectIdentifier(businessId)
      .cancellationReason(cancellationReason)
      .clientApplicationId(clientApplicationId)
      .lexBridgeProcessInstanceId(lexBridgeProcessInstanceId)
      .build());

    return new ResponseEntity(HttpStatus.OK);
  }

  @Transactional
  @RequestMapping(value = "/startLexProcessCommand", method = RequestMethod.GET)
  public ResponseEntity<?> startLexProcess(
    @ApiParam(required = true) //
    @RequestParam(value = "businessId") final String businessId,
    @ApiParam(required = true) //
    @RequestParam(value = "clientApplicationId") final String clientApplicationId,
    @ApiParam(required = true) //
    @RequestParam(value = "lexBridgeProcessInstanceId") final String lexBridgeProcessInstanceId,
    @ApiParam(required = true) //
    @RequestParam(value = "taskTypeId") final String taskTypeId) {

    lexServerService.startLexProcess(StartLexProcessCommand.builder()
      .businessObjectIdentifier(businessId)
      .clientApplicationId(clientApplicationId)
      .lexBridgeProcessInstanceId(lexBridgeProcessInstanceId)
      .taskTypeId(taskTypeId)
      .build());

    return new ResponseEntity(HttpStatus.OK);
  }
}
