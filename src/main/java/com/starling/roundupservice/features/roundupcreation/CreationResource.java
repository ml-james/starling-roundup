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
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@Slf4j
public class CreationResource
{
    private final CreationService creationService;

    @PutMapping(path = "/createRoundupGoal/accountUid/{accountUid}/",
            consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<RoundupCreationResponse> createRoundupGoal(@PathVariable("accountUid") final String accountUid,
                                                                     @RequestBody final RoundupCreationRequest creationRequest,
                                                                     @RequestHeader("Authorization") String bearerToken)
    {
        try
        {
            var savingsGoalUid = creationService.createRoundupGoal(creationRequest, accountUid, bearerToken);
            return ResponseEntity.ok(RoundupCreationResponse.builder().roundupSavingsGoalUid(savingsGoalUid).build());
        }
        catch (ClientException e)
        {
            log.error("There was an error in your request for {}", accountUid, e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(RoundupCreationResponse.builder().error(e.getError()).build());
        }
        catch (ServerException e)
        {
            log.error("There was an error on our side processing your request for account {}", accountUid, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(RoundupCreationResponse.builder().error(e.getError()).build());
        }
        catch (Exception e)
        {
            log.error("There was an unexpected error processing the request for account {}", accountUid, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
