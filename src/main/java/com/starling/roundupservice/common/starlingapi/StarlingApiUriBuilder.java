package com.starling.roundupservice.common.starlingapi;

import java.util.UUID;

public class StarlingApiUriBuilder
{
    public static String buildAccountRetrievalUri()
    {
        return "/accounts";
    }

    public static String buildSavingsGoalCreationUri(final String accountUid)
    {
        return String.format("/account/%s/savings-goals", accountUid);
    }

    public static String buildSavingsGoalUpdateUri(final String accountUid, final String savingsGoalUid)
    {
        return String.format("/account/%s/savings-goals/%s", accountUid, savingsGoalUid);
    }

    public static String buildTransactionFeedUri(final String accountUid,
                                                 final String categoryUid,
                                                 final String minTransactionTimestamp,
                                                 final String maxTransactionTimestamp)
    {
        return String.format("/account/%s/category/%s/transactions-between?minTransactionTimestamp=%s&?maxTransactionTimestamp=%s",
                accountUid, categoryUid, minTransactionTimestamp, maxTransactionTimestamp);
    }

    public static String buildFundConfirmationUri(final String accountUid, final int amount)
    {
        return String.format("/accounts/%s/confirmation-of-funds?targetAmountInMinorUnits=%s", accountUid, amount);
    }

    public static String buildSavingsDepositUri(final String accountUid,
                                                final String savingsGoalUid)
    {
        return String.format("/account/%s/savings-goals/%s/add-money/%s", accountUid, savingsGoalUid, UUID.randomUUID());
    }

    public static String buildSavingsGoalRetrievalUri(final String accountUid,
                                                      final String savingsGoalUid)
    {
        return String.format("/account/%s/savings-goals/%s", accountUid, savingsGoalUid);
    }
}
