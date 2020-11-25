package com.starling.roundupservice.common.transaction;

import com.starling.roundupservice.common.StarlingAPIProvider;
import com.starling.roundupservice.common.UriBuilder;
import com.starling.roundupservice.common.account.roundup.domain.RoundupAccountMapping;
import com.starling.roundupservice.common.account.roundup.service.RoundupStateService;
import com.starling.roundupservice.common.exception.ClientException;
import com.starling.roundupservice.common.transaction.domain.FeedItem;
import com.starling.roundupservice.common.transaction.domain.FeedItems;
import com.starling.roundupservice.common.transaction.domain.Roundup;
import com.starling.roundupservice.common.transaction.domain.TransactionTimestamps;
import lombok.RequiredArgsConstructor;
import org.joda.time.DateTime;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TransactionService
{
    private static final String DIRECTION_OUT = "OUT";
    private static final String STATUS_SETTLED = "SETTLED";

    private final RoundupStateService roundupStateService;
    private final StarlingAPIProvider starlingAPIProvider;

    public Roundup getLatestRoundup(final RoundupAccountMapping roundUpAccount, final String bearerToken)
    {
        var transactionWindow = TransactionTimestamps.builder()
                .minTransactionTimestamp(new DateTime().minusWeeks(1).withDayOfWeek(1).withTimeAtStartOfDay().toString())
                .maxTransactionTimestamp(new DateTime().withDayOfWeek(1).withTimeAtStartOfDay().toString())
                .build();;

        if (!roundupStateService.isRoundupDue(roundUpAccount.getRoundupUid(), transactionWindow.maxTransactionTimestamp))
        {
            throw new ClientException("Roundup requirability: ", String
                    .format("a roundup is not required at this time, we have already rounded up the transactions for the account %s, week end %s", roundUpAccount.getAccountUid(),
                            transactionWindow.maxTransactionTimestamp));
        }

        var uri = UriBuilder.buildTransactionFeedUri(roundUpAccount.getAccountUid(), roundUpAccount.getCategoryUid(), transactionWindow.minTransactionTimestamp, transactionWindow.maxTransactionTimestamp);
        var transactions = starlingAPIProvider.queryStarlingAPI(uri, bearerToken, HttpMethod.POST, new Object(), FeedItems.class);

        return Roundup.builder()
                .roundupAmount(calculateRoundup(transactions, roundUpAccount))
                .weekEnd(transactionWindow.maxTransactionTimestamp)
                .build();
    }

    private int calculateRoundup(final FeedItems feedItems, final RoundupAccountMapping roundupAccountMapping)
    {
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
