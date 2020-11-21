package com.starling.roundupservice.features.roundupaction;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import com.starling.roundupservice.action.RoundupActionResponse;
import com.starling.roundupservice.common.exception.ClientException;
import com.starling.roundupservice.common.exception.ServerException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@Slf4j
public class ActionResource
{

    private final ActionService actionService;

    @PostMapping(path = "/roundupAction/accountUid/{accountUid}", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<RoundupActionResponse> roundupAccount(@PathVariable("accountUid") String accountUid)
    {

        try
        {
            var roundupActionResponse = actionService.performRoundup(accountUid);
            return ResponseEntity.ok(roundupActionResponse);
        }
        catch (ClientException e)
        {
            log.error("There was an error in your request for {}", accountUid, e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(RoundupActionResponse.builder().error(e.getError()).build());
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
