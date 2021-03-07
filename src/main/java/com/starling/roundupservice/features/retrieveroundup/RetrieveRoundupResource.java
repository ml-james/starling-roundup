package com.starling.roundupservice.features.retrieveroundup;

import com.starling.roundupservice.common.exception.ClientException;
import com.starling.roundupservice.common.exception.ServerException;
import com.starling.roundupservice.retrieve.RetrieveRoundupResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@AllArgsConstructor
@Slf4j
public class RetrieveRoundupResource
{
    private final RetrieveRoundupService retrieveRoundupService;

    @GetMapping(path = "/retrieveRoundupGoal/accountUid/{accountUid}", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<RetrieveRoundupResponse> retrieveRoundupGoal(@PathVariable("accountUid") final String accountUid,
                                                                       @RequestHeader("Authorization") String bearerToken)
    {
        try
        {
            var roundupInfo = retrieveRoundupService.retrieveRoundup(accountUid, bearerToken);
            return ResponseEntity.status(HttpStatus.OK).body(roundupInfo);
        }
        catch (ClientException e)
        {
            log.error("There was an error in your request for roundup retrieval for account {}", accountUid, e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(RetrieveRoundupResponse.builder().errors(e.getError()).build());
        }
        catch (ServerException e)
        {
            log.error("There was an error on our side processing your request for roundup retrieval for account {}", accountUid, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(RetrieveRoundupResponse.builder().errors(e.getError()).build());
        }
        catch (Exception e)
        {
            log.error("There was an unexpected error processing the request for roundup account retrieval for account {}", accountUid, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(RetrieveRoundupResponse.builder()
                    .errors(new HashMap<>() {{ put("Something unexpected went wrong:", e.getMessage()); }}).build());
        }
    }
}
