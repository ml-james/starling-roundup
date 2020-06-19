package com.starling.roundupservice.features.roundupaction;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import com.starling.roundupservice.action.RoundupActionResponse;
import com.starling.roundupservice.creation.RoundupCreationResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@Slf4j
public class ActionResource {

  private final ActionService actionService;

  @PutMapping(path = "/roundupAction/account/{accountUid}", produces = APPLICATION_JSON_VALUE)
  public ResponseEntity<RoundupActionResponse> roundupAccount(@PathVariable("accountUid") String accountUid) {

    var roundupActionResponse = actionService.performRoundup(accountUid);
    return ResponseEntity.ok(roundupActionResponse);
  }
}
