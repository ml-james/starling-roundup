package com.starling.roundupservice.features.roundupcreation;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import com.starling.roundupservice.creation.RoundupCreationRequest;
import com.starling.roundupservice.creation.RoundupCreationResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

    var savingsGoalUid = creationService.createRoundupGoal(creationRequest);
    return ResponseEntity.ok(new RoundupCreationResponse(savingsGoalUid));
  }
}
