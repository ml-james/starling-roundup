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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@Slf4j
public class CreationResource {

  private final CreationService creationService;

  @PutMapping(path = "/createRoundupGoal/accountUid/{accountUid}/defaultCategoryUid/{defaultCategoryUid/currency/{currency}",
      consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
  public ResponseEntity<RoundupCreationResponse> createRoundupGoal(@PathVariable("accountUid") final String accountUid,
      @PathVariable("defaultCategoryUid") final String defaultCategoryUid, @PathVariable("currency") final String currency,
      @RequestBody final RoundupCreationRequest creationRequest) {

    try {
      var savingsGoalUid = creationService.createRoundupGoal(creationRequest, accountUid, defaultCategoryUid, currency);
      return ResponseEntity.ok(RoundupCreationResponse.builder().roundupSavingsGoalUid(savingsGoalUid).build());
    } catch (ClientException e) {
      log.error("There was an error in your request for {}", accountUid, e);
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(RoundupCreationResponse.builder().error(e.getError()).build());
    } catch (ServerException e) {
      log.error("There was an error on our side processing your request for account {}", accountUid, e);
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(RoundupCreationResponse.builder().error(e.getError()).build());
    } catch (Exception e) {
      log.error("There was an unexpected error processing the request for account {}", accountUid, e);
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
  }
}
