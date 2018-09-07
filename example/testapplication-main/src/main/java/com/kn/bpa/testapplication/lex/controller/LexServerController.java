package com.kn.bpa.testapplication.lex.controller;

import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.variable.VariableMap;
import org.camunda.bpm.engine.variable.impl.VariableMapImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiParam;

@RestController
@RequestMapping("/lex")
public class LexServerController {

  private final RuntimeService runtimeService;

  @Autowired
  public LexServerController(final RuntimeService runtimeService) {
    this.runtimeService = runtimeService;
  }

  @RequestMapping(value = "/startGlobalProcess", method = RequestMethod.GET)
  public ResponseEntity<?> startLexProcess(
    @ApiParam(required = true) //
    @RequestParam(value = "businessId") final String businessId,
    @ApiParam(required = true, defaultValue = "DEKN_DEHAM_AE") //
    @RequestParam(value = "office") final String office) {

    final VariableMap map = new VariableMapImpl();
    map.put("office", office);
    runtimeService.startProcessInstanceByKey("AE0000_GlobalLexProcess", businessId, map);

    return new ResponseEntity(HttpStatus.OK);
  }

}
