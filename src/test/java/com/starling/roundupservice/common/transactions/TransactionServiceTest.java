package com.starling.roundupservice.common.transactions;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

import com.starling.roundupservice.common.account.roundup.RoundupAccountMapping;
import com.starling.roundupservice.common.savingsgoal.deposit.Money;
import java.util.ArrayList;
import org.joda.time.DateTimeUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

public class TransactionServiceTest {

  private static final int ROUNDUP_FACTOR = 2;
  private static final int ROUNDUP_MAXIMUM = 100;

  @Mock
  private TransactionProvider transactionProvider;

  private TransactionService transactionService;
  private RoundupAccountMapping account;
  private int result;

  @Before
  public void setup() {
    initMocks(this);
    DateTimeUtils.setCurrentMillisFixed(1592731346192L);
//    transactionService = new TransactionService(transactionProvider);
  }

  @Test
  public void testTransactionWindow() {
    givenRoundupAccountMapping();
    givenTransactionProviderReturnsTransactions();
//    whenGetLatestRoundupCalled();
  }

  private void givenRoundupAccountMapping() {
    account = RoundupAccountMapping.builder()
        .roundupFactor(ROUNDUP_FACTOR)
        .maximumRoundup(ROUNDUP_MAXIMUM)
        .build();
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
    var transactions = new ArrayList<FeedItem>();
    transactions.add(transactionOne);
    transactions.add(transactionTwo);
    transactions.add(transactionThree);
    transactions.add(transactionFour);
    when(transactionProvider.retrieveTransactionsInWindow(any(), any())).thenReturn(new FeedItems(transactions));
  }

//  private void whenGetLatestRoundupCalled() {
//    result = transactionService.getLatestRoundup(account);
//  }
}
