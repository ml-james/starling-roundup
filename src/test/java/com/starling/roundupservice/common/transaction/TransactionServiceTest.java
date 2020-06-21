package com.starling.roundupservice.common.transaction;

import static com.starling.roundupservice.TestConstants.DEFAULT_WEEK_END;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

import com.starling.roundupservice.common.account.roundup.RoundupAccountMapping;
import com.starling.roundupservice.common.account.roundup.RoundupStateService;
import com.starling.roundupservice.common.exception.ClientException;
import com.starling.roundupservice.common.savingsgoal.deposit.Money;
import java.util.Arrays;
import org.joda.time.DateTimeUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

public class TransactionServiceTest {

  private static final int ROUND_UP_FACTOR_1 = 1;
  private static final int ROUND_UP_FACTOR_2 = 2;
  private static final int ROUND_UP_MAXIMUM_100 = 100;
  private static final int TRANSACTION_ROUND_UP = 81;

  @Mock
  private TransactionProvider transactionProvider;
  @Mock
  private RoundupStateService roundupStateService;

  private TransactionService transactionService;
  private RoundupAccountMapping account;
  private Roundup result;

  @Before
  public void setup() {
    initMocks(this);
    transactionService = new TransactionService(transactionProvider, roundupStateService);
    DateTimeUtils.setCurrentMillisFixed(1592731346192L);
  }

  @Test
  public void roundupDueTakeMaximum() {
    givenRoundupAccountMapping(ROUND_UP_FACTOR_2, ROUND_UP_MAXIMUM_100);
    givenRoundupDue();
    givenTransactionProviderReturnsTransactions();
    whenGetLatestRoundupCalled();
    thenRoundupCorrect(ROUND_UP_MAXIMUM_100);
  }

  @Test
  public void roundupDueTakeRoundup() {
    givenRoundupAccountMapping(ROUND_UP_FACTOR_1, ROUND_UP_MAXIMUM_100);
    givenRoundupDue();
    givenTransactionProviderReturnsTransactions();
    whenGetLatestRoundupCalled();
    thenRoundupCorrect(TRANSACTION_ROUND_UP);
  }

  @Test(expected = ClientException.class)
  public void roundupNotDue() {
    givenRoundupAccountMapping(ROUND_UP_FACTOR_1, ROUND_UP_MAXIMUM_100);
    givenRoundupNotDue();
    whenGetLatestRoundupCalled();
  }

  private void givenRoundupAccountMapping(int roundupFactor, int roundupMaximum) {
    account = RoundupAccountMapping.builder()
        .roundupFactor(roundupFactor)
        .maximumRoundup(roundupMaximum)
        .build();
  }

  private void givenRoundupDue() {
    when(roundupStateService.isRoundupDue(anyInt(), anyString())).thenReturn(true);
  }

  private void givenRoundupNotDue() {
    when(roundupStateService.isRoundupDue(anyInt(), anyString())).thenReturn(false);
  }

  private void givenTransactionProviderReturnsTransactions() {
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
    when(transactionProvider.retrieveTransactionsInWindow(any(), any())).thenReturn(new FeedItems(transactions));
  }

  private void whenGetLatestRoundupCalled() {
    result = transactionService.getLatestRoundup(account);
  }

  private void thenRoundupCorrect(int amount) {
    assertEquals(amount, result.roundupAmount);
    assertEquals(DEFAULT_WEEK_END, result.weekEnd);
  }
}
