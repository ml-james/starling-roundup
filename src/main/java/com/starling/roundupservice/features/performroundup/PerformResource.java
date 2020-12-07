package com.starling.roundupservice.features.performroundup;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import com.starling.roundupservice.perform.PerformRoundupResponse;
import com.starling.roundupservice.common.exception.ClientException;
import com.starling.roundupservice.common.exception.ServerException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@Slf4j
public class PerformResource
{
    private final PerformService performService;

    @PostMapping(path = "/perform/accountUid/{accountUid}", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<PerformRoundupResponse> roundupAccount(@PathVariable("accountUid") String accountUid,
                                                                 @RequestHeader("Authorization") String bearerToken)
    {
        try
        {
            var roundupActionResponse = performService.performRoundup(accountUid, bearerToken);
            return ResponseEntity.ok(roundupActionResponse);
        }
        catch (ClientException e)
        {
            log.error("There was an error in your request for {}", accountUid, e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(PerformRoundupResponse.builder().error(e.getError()).build());
        }
        catch (ServerException e)
        {
            log.error("There was an error on our side processing your request for account {}", accountUid, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
        catch (Exception e)
        {
            log.error("There was an unexpected error processing the request for account {}", accountUid, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
