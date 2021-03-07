package com.starling.roundupservice.features.saveroundup;

import com.starling.roundupservice.common.exception.ClientException;
import com.starling.roundupservice.common.exception.ServerException;
import com.starling.roundupservice.save.SaveRoundupRequest;
import com.starling.roundupservice.save.SaveRoundupResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@AllArgsConstructor
@Slf4j
public class SaveRoundupResource
{
    private final SaveRoundupService saveRoundupService;

    @PutMapping(path = "/saveRoundupGoal/accountUid/{accountUid}", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<SaveRoundupResponse> saveRoundupGoal(@PathVariable("accountUid") final String accountUid,
                                                                 @RequestBody final SaveRoundupRequest creationRequest,
                                                                 @RequestHeader("Authorization") String bearerToken)
    {
        try
        {
            var saveRoundupResponse = saveRoundupService.saveRoundupGoal(creationRequest, accountUid, bearerToken);
            return ResponseEntity.ok(saveRoundupResponse);
        }
        catch (ClientException e)
        {
            log.error("There was an error in your request for {}", accountUid, e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(SaveRoundupResponse.builder().errors(e.getError()).build());
        }
        catch (ServerException e)
        {
            log.error("There was an error on our side processing your request for account {}", accountUid, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(SaveRoundupResponse.builder().errors(e.getError()).build());
        }
        catch (Exception e)
        {
            log.error("There was an unexpected error processing the request for account {}", accountUid, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(SaveRoundupResponse.builder()
                    .errors(new HashMap<>() {{ put("Something unexpected went wrong:", e.getMessage()); }}).build());
        }
    }
}
