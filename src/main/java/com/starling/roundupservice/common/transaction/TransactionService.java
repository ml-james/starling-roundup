package com.starling.roundupservice.common.transaction;

import com.starling.roundupservice.common.account.roundup.RoundupAccountMapping;
import com.starling.roundupservice.common.account.roundup.RoundupStateService;
import com.starling.roundupservice.common.exception.ClientException;
import lombok.RequiredArgsConstructor;
import org.joda.time.DateTime;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TransactionService {

  private static final String DIRECTION_OUT = "OUT";
  private static final String STATUS_SETTLED = "SETTLED";

  private final TransactionProvider transactionProvider;
  private final RoundupStateService roundupStateService;

  public Roundup getLatestRoundup(RoundupAccountMapping roundUpAccount) {

    var transactionWindow = getTransactionWindow();

    if (!roundupStateService.isRoundupDue(roundUpAccount.getRoundupUid(), transactionWindow.maxTransactionTimestamp)) {
      throw new ClientException("Roundup requirability: ", String
          .format("a roundup is not required at this time, we have already rounded up the transactions for the account %s, week end %s",
              roundUpAccount.getAccountUid(), transactionWindow.maxTransactionTimestamp));
    }

    var transactions = transactionProvider.retrieveTransactionsInWindow(roundUpAccount, transactionWindow);
    return calculateRoundup(transactions, roundUpAccount, transactionWindow.maxTransactionTimestamp);
  }

  private TransactionTimestamps getTransactionWindow() {

    return TransactionTimestamps.builder()
        .minTransactionTimestamp(new DateTime().minusWeeks(1).withDayOfWeek(1).withTimeAtStartOfDay().toString())
        .maxTransactionTimestamp(new DateTime().withDayOfWeek(1).withTimeAtStartOfDay().toString())
        .build();
  }

  private Roundup calculateRoundup(final FeedItems feedItems, final RoundupAccountMapping roundupAccountMapping, final String weekEnd) {

    var feedSum = feedItems.getFeedItems().stream()
        .filter(x -> x.getDirection().equals(DIRECTION_OUT))
        .filter(x -> x.getStatus().equals(STATUS_SETTLED))
        .map(FeedItem::getAmount)
        .map(x -> 100 - (x.getMinorUnits() % 100))
        .mapToInt(Integer::intValue)
        .sum();

    var roundupAmount = Math.min(feedSum * roundupAccountMapping.getRoundupFactor(), roundupAccountMapping.getMaximumRoundup());

    return Roundup.builder()
        .roundupAmount(roundupAmount)
        .weekEnd(weekEnd)
        .build();
  }
}
