package com.starling.roundupservice.common.transactions;

import com.starling.roundupservice.common.accounts.roundup.RoundupAccountMapping;
import com.starling.roundupservice.common.transactions.domain.FeedItems;
import com.starling.roundupservice.common.transactions.domain.TransactionTimestamps;
import lombok.RequiredArgsConstructor;
import org.joda.time.DateTime;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TransactionsService {

  private final TransactionRepository transactionRepository;
  private final TransactionProvider transactionProvider;

  public int getRoundupValue(RoundupAccountMapping roundUpAccount) {

    var transactionTimestamps = getTransactionTimestamps();
    var transaction = transactionRepository
        .findTransactionRoundup(roundUpAccount.getSavingsGoalUid(), transactionTimestamps.getMaxTransactionTimestamp());

    if (transaction.isPresent()) {
      return transaction.get().getRoundUpValue();
    }

    var transactions = transactionProvider.retrieveTransactionsInWindow(roundUpAccount.getAccountUid(), transactionTimestamps);
    var transactionRoundup = calculateRoundup(transactions, roundUpAccount);



  }

  private TransactionTimestamps getTransactionTimestamps() {

    return TransactionTimestamps.builder()
        .minTransactionTimestamp(new DateTime().minusWeeks(1).withDayOfWeek(0).toString())
        .maxTransactionTimestamp(new DateTime().minusWeeks(1).withDayOfWeek(7).toString())
        .build();
  }

  private int calculateRoundup(final FeedItems feedItems, final RoundupAccountMapping roundupAccountMapping) {



  }
}
