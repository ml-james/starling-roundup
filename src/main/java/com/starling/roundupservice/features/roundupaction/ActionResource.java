package com.starling.roundupservice.features.roundupaction;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import com.starling.roundupservice.action.RoundupActionResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
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

    try {
      var roundupActionResponse = actionService.performRoundup(accountUid);
      return ResponseEntity.ok(roundupActionResponse);
    } catch (NoRoundupRequiredException e) {
      log.error("The weekly roundup has already been completed {}", accountUid);
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    } catch (NoRoundupGoalFoundException e) {
      log.error("There is no round up goal associated with the account {}", accountUid);
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    } catch (Exception e) {
      log.error("An error occurred trying to perform round up action", e);
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
  }
}
