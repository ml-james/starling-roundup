package com.starling.roundupservice.dao;

import com.starling.roundupservice.common.account.roundup.repository.RoundupAccountRepository;
import com.starling.roundupservice.common.account.roundup.repository.RoundupStateRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
public class RoundupStateRepositoryDaoTest
{
    private static final String ROUNDUP_UID = "roundupUid";

    @Autowired
    private RoundupStateRepository roundupStateRepository;
    @Autowired
    private RoundupAccountRepository roundupAccountRepository;

    @BeforeEach
    void setup()
    {
        var accountUid = "accountUid";
        var categoryUid = "categoryUid";
        var accountUidCurrency = "gbp";
        var savingsGoalUid = "savingsGoalUid";
        var maximumRoundup = 10;
        var roundupFactor = 2;
        roundupAccountRepository.save(ROUNDUP_UID, accountUid, categoryUid, accountUidCurrency, savingsGoalUid, maximumRoundup, roundupFactor);
    }

    @Test
    void shouldInsertRoundupAccount()
    {
        var transferUid = "transferUid";
        var state = "TRANSFERRED";
        var weekEnd = "2020-06-15T00:00:00.000+01:00";
        roundupStateRepository.save(ROUNDUP_UID, transferUid, state, weekEnd);
        var result = roundupStateRepository.findByRoundupUidAndWeekEnd(ROUNDUP_UID, weekEnd);
        assertTrue(result.isPresent());
    }
}
