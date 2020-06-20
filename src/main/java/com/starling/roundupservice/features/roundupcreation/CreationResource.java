package com.starling.roundupservice.features.roundupcreation;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import com.starling.roundupservice.common.exception.ClientException;
import com.starling.roundupservice.common.exception.ServerException;
import com.starling.roundupservice.creation.RoundupCreationRequest;
import com.starling.roundupservice.creation.RoundupCreationResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@Slf4j
public class CreationResource {

  private final CreationService creationService;

  @PutMapping(path = "/createRoundupGoal", produces = APPLICATION_JSON_VALUE)
  public ResponseEntity<RoundupCreationResponse> createRoundupGoal(@RequestBody RoundupCreationRequest creationRequest) {

    try {
      var savingsGoalUid = creationService.createRoundupGoal(creationRequest);
      return ResponseEntity.ok(new RoundupCreationResponse(savingsGoalUid));
    } catch (ClientException ce) {
      log.error("Client error on create roundup goal: {}", creationRequest.getAccountUid());
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    } catch (ServerException se) {
      log.error("Server error on create roundup goal: {}", creationRequest.getAccountUid());
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
  }
}
