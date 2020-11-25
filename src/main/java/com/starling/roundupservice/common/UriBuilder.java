package com.starling.roundupservice.common;

import java.util.UUID;

public class UriBuilder
{
    public static String createSavingsGoalCreationUri(final String accountUid)
    {
        return String.format("/account/%s/savings-goals", accountUid);
    }

    public static String createTransactionFeedUri(final String accountUid,
                                                  final String categoryUid,
                                                  final String minTransactionTimestamp,
                                                  final String maxTransactionTimestamp)
    {
        return String.format("/account/%s/category/%s/category/transactions-between?minTransactionTimestamp=%s&?maxTransactionTimestamp=%s",
                accountUid, categoryUid, minTransactionTimestamp, maxTransactionTimestamp);
    }

    public static String createSavingsDepositUri(final String accountUid,
                                                 final String savingsGoalUid)
    {
        return String.format("/account/%s/savings-goals/%s/add-money/%s", accountUid, savingsGoalUid, UUID.randomUUID());
    }

    public static String createFundConfirmationUri(final String accountUid, final int amount)
    {
        return String.format("/accounts/%s/confirmation-of-funds?=targetAmountInMinorUnits=%s", accountUid, amount);
    }
}
