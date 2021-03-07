package com.starling.roundupservice.common.transaction;

import com.starling.roundupservice.common.ClockService;
import com.starling.roundupservice.common.Money;
import com.starling.roundupservice.common.account.roundup.RoundupAccountMapping;
import com.starling.roundupservice.common.account.roundup.RoundupStateService;
import com.starling.roundupservice.common.exception.ClientException;
import com.starling.roundupservice.common.starlingapi.StarlingApiProvider;
import org.joda.time.DateTime;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.util.Arrays;

import static com.starling.roundupservice.TestConstants.DEFAULT_WEEK_END;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class TransactionServiceTest
{
    private static final int ROUND_UP_FACTOR_1 = 1;
    private static final int ROUND_UP_FACTOR_2 = 2;
    private static final int ROUND_UP_MAXIMUM_100 = 100;
    private static final int TRANSACTION_ROUND_UP = 81;
    private static final String ROUNDUP_UID = "roundupUid";

    @Mock
    private StarlingApiProvider transactionProvider;
    @Mock
    private RoundupStateService roundupStateService;
    @Mock
    private ClockService clockService;

    private TransactionService transactionService;
    private RoundupAccountMapping account;
    private Roundup result;

    @BeforeEach
    public void setup()
    {
        initMocks(this);
        transactionService = new TransactionService(roundupStateService, transactionProvider, clockService);
        when(clockService.getCurrentDateTime()).thenReturn(new DateTime(1592731346192L));
    }

    @Test
    void roundupDueTakeMaximum()
    {
        account = RoundupAccountMapping.builder()
                .roundupUid(ROUNDUP_UID)
                .roundupFactor(ROUND_UP_FACTOR_2)
                .maximumRoundup(ROUND_UP_MAXIMUM_100)
                .build();
        var transactionOne = FeedItem.builder()
                .direction("OUT")
                .status("SETTLED")
                .amount(Money.builder().minorUnits(976).build())
                .build();
        var transactionTwo = FeedItem.builder()
                .direction("IN")
                .status("SETTLED")
                .amount(Money.builder().minorUnits(590).build())
                .build();
        var transactionThree = FeedItem.builder()
                .direction("IN")
                .status("PENDING")
                .amount(Money.builder().minorUnits(9345).build())
                .build();
        var transactionFour = FeedItem.builder()
                .direction("OUT")
                .status("SETTLED")
                .amount(Money.builder().minorUnits(10043).build())
                .build();
        var transactions = Arrays.asList(transactionOne, transactionTwo, transactionThree, transactionFour);

        when(roundupStateService.isRoundupDue(anyString(), anyString())).thenReturn(true);
        when(transactionProvider.queryStarlingAPI(any(), any(), any(), any(), any())).thenReturn(new FeedItems(transactions));
        result = transactionService.getLatestRoundup(account, "");

        Assertions.assertEquals(ROUND_UP_MAXIMUM_100, result.roundupAmount);
        Assertions.assertEquals(DEFAULT_WEEK_END, result.weekEnd);
    }

    @Test
    void roundupDueTakeRoundup()
    {
        account = RoundupAccountMapping.builder()
                .roundupUid(ROUNDUP_UID)
                .roundupFactor(ROUND_UP_FACTOR_1)
                .maximumRoundup(ROUND_UP_MAXIMUM_100)
                .build();
        var transactionOne = FeedItem.builder()
                .direction("OUT")
                .status("SETTLED")
                .amount(Money.builder().minorUnits(976).build())
                .build();
        var transactionTwo = FeedItem.builder()
                .direction("IN")
                .status("SETTLED")
                .amount(Money.builder().minorUnits(590).build())
                .build();
        var transactionThree = FeedItem.builder()
                .direction("IN")
                .status("PENDING")
                .amount(Money.builder().minorUnits(9345).build())
                .build();
        var transactionFour = FeedItem.builder()
                .direction("OUT")
                .status("SETTLED")
                .amount(Money.builder().minorUnits(10043).build())
                .build();
        var transactions = Arrays.asList(transactionOne, transactionTwo, transactionThree, transactionFour);

        when(roundupStateService.isRoundupDue(anyString(), anyString())).thenReturn(true);
        when(transactionProvider.queryStarlingAPI(any(), any(), any(), any(), any())).thenReturn(new FeedItems(transactions));
        result = transactionService.getLatestRoundup(account, "");

        Assertions.assertEquals(TRANSACTION_ROUND_UP, result.roundupAmount);
        Assertions.assertEquals(DEFAULT_WEEK_END, result.weekEnd);
    }

    @Test
    void roundupNotDue()
    {
        account = RoundupAccountMapping.builder()
                .roundupUid(ROUNDUP_UID)
                .roundupFactor(ROUND_UP_FACTOR_1)
                .maximumRoundup(ROUND_UP_MAXIMUM_100)
                .build();

        when(roundupStateService.isRoundupDue(anyString(), anyString())).thenReturn(false);

        assertThrows(ClientException.class, () -> transactionService.getLatestRoundup(account, ""));
    }
}
