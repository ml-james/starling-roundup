package com.starling.roundupservice.common.roundup;

import com.starling.roundupservice.common.account.roundup.RoundupAccountRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
public class RoundupAccountRepositoryDaoTest
{
    @Autowired
    private RoundupAccountRepository roundupAccountRepository;

    @Test
    void shouldRetrieveRoundupAccount()
    {
        var result = roundupAccountRepository.findById("b2191626-c67c-4a4b-aef9-3b1b80b65fdc");

        assertTrue(result.isPresent());
        assertEquals(result.get().getRoundupUid(), "a2191626-c67c-4a4b-aef9-3b1b80b65fdc");
        assertEquals(result.get().getAccountUid(), "b2191626-c67c-4a4b-aef9-3b1b80b65fdc");
        assertEquals(result.get().getCategoryUid(), "3116d935-426c-4af1-8593-da6c5d169ce5");
        assertEquals(result.get().getAccountUidCurrency(), "GBP");
        assertEquals(result.get().getSavingsGoalUid(), "50c71ed0-e776-4966-9a92-b00fd2f43ae3");
        assertEquals(result.get().getRoundupGoal(), 500);
        assertEquals(result.get().getMaximumRoundup(), 50);
        assertEquals(result.get().getRoundupFactor(), 2);
    }

    @Test
    void shouldInsertRoundupAccount()
    {
        var accountUid = "accountUid1";
        roundupAccountRepository.save("roundupUid1",
                accountUid,
                "categoryUid",
                "USD",
                "savingsGoalUid",
                500,
                10,
                1);

        var result = roundupAccountRepository.findById(accountUid);

        assertTrue(result.isPresent());
        assertEquals(result.get().getRoundupUid(), "roundupUid1");
        assertEquals(result.get().getAccountUid(), accountUid);
        assertEquals(result.get().getCategoryUid(), "categoryUid");
        assertEquals(result.get().getAccountUidCurrency(), "USD");
        assertEquals(result.get().getSavingsGoalUid(), "savingsGoalUid");
        assertEquals(result.get().getRoundupGoal(), 500);
        assertEquals(result.get().getMaximumRoundup(), 10);
        assertEquals(result.get().getRoundupFactor(), 1);
    }

    @Test
    void shouldUpdateRoundupAccount()
    {
        var accountUid = "accountUid2";
        roundupAccountRepository.save("roundupUid2",
                accountUid,
                "categoryUid",
                "USD",
                "savingsGoalUid",
                500,
                10,
                1);

        var result = roundupAccountRepository.findById(accountUid);

        assertEquals(result.get().getRoundupGoal(), 500);
        assertEquals(result.get().getMaximumRoundup(), 10);
        assertEquals(result.get().getRoundupFactor(), 1);

        var newRoundupGoal = 1000;
        var newMaximumRoundup = 20;
        var newRoundupFactor = 2;

        roundupAccountRepository.updateRoundupAccount(accountUid, newRoundupGoal, newMaximumRoundup, newRoundupFactor);

        var updatedResult = roundupAccountRepository.findById(accountUid);

        assertEquals(updatedResult.get().getRoundupGoal(), newRoundupGoal);
        assertEquals(updatedResult.get().getMaximumRoundup(), newMaximumRoundup);
        assertEquals(updatedResult.get().getRoundupFactor(), newRoundupFactor);
    }

}
