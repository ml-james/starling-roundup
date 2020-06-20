package com.starling.roundupservice.common.transactions;

import com.starling.roundupservice.common.account.roundup.RoundupAccountMapping;
import lombok.RequiredArgsConstructor;
import org.joda.time.DateTime;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TransactionService {

  private static final String DIRECTION_OUT = "OUT";
  private static final String STATUS_SETTLED = "SETTLED";

  private final TransactionProvider transactionProvider;

  public int getLatestRoundup(RoundupAccountMapping roundUpAccount) {

    var transactionWindow = getTransactionWindow();
    var transactions = transactionProvider.retrieveTransactionsInWindow(roundUpAccount, transactionWindow);
    return calculateRoundup(transactions, roundUpAccount);
  }

  private TransactionTimestamps getTransactionWindow() {

    return TransactionTimestamps.builder()
        .minTransactionTimestamp(new DateTime().minusWeeks(1).withDayOfWeek(0).toString())
        .maxTransactionTimestamp(new DateTime().minusWeeks(1).withDayOfWeek(7).toString())
        .build();
  }

  private int calculateRoundup(final FeedItems feedItems, final RoundupAccountMapping roundupAccountMapping) {

    var feedSum = feedItems.getFeedItems().stream()
        .filter(x -> x.getDirection().equals(DIRECTION_OUT))
        .filter(x -> x.getStatus().equals(STATUS_SETTLED))
        .map(FeedItem::getAmount)
        .map(x -> 100 - (x.getMinorUnits() % 100))
        .mapToInt(Integer::intValue)
        .sum();

    return Math.min(feedSum * roundupAccountMapping.getRoundupFactor(), roundupAccountMapping.getMaximumRoundup());

  }
}
