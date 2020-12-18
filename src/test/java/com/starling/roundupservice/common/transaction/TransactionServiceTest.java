package com.starling.roundupservice.common.transaction;

import com.starling.roundupservice.common.starlingapi.StarlingApiProvider;
import com.starling.roundupservice.common.account.roundup.RoundupAccountMapping;
import com.starling.roundupservice.common.account.roundup.RoundupStateService;
import com.starling.roundupservice.common.exception.ClientException;
import com.starling.roundupservice.common.Money;
import org.joda.time.DateTimeUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.util.Arrays;

import static com.starling.roundupservice.TestConstants.DEFAULT_WEEK_END;
import static org.junit.Assert.assertEquals;
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

    private TransactionService transactionService;
    private RoundupAccountMapping account;
    private Roundup result;

    @Before
    public void setup()
    {
        initMocks(this);
        transactionService = new TransactionService(roundupStateService, transactionProvider);
        DateTimeUtils.setCurrentMillisFixed(1592731346192L);
    }

    @Test
    public void roundupDueTakeMaximum()
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

        assertEquals(ROUND_UP_MAXIMUM_100, result.roundupAmount);
        assertEquals(DEFAULT_WEEK_END, result.weekEnd);
    }

    @Test
    public void roundupDueTakeRoundup()
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

        assertEquals(TRANSACTION_ROUND_UP, result.roundupAmount);
        assertEquals(DEFAULT_WEEK_END, result.weekEnd);
    }

    @Test(expected = ClientException.class)
    public void roundupNotDue()
    {
        account = RoundupAccountMapping.builder()
                .roundupUid(ROUNDUP_UID)
                .roundupFactor(ROUND_UP_FACTOR_1)
                .maximumRoundup(ROUND_UP_MAXIMUM_100)
                .build();

        when(roundupStateService.isRoundupDue(anyString(), anyString())).thenReturn(false);

        result = transactionService.getLatestRoundup(account, "");
    }

}
