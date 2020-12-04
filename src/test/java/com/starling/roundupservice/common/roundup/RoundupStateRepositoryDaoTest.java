package com.starling.roundupservice.common.roundup;

import com.starling.roundupservice.common.account.roundup.repository.RoundupAccountRepository;
import com.starling.roundupservice.common.account.roundup.repository.RoundupStateRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
public class RoundupStateRepositoryDaoTest
{
    private static final String ROUNDUP_UID = "roundupUid";

    @Autowired
    private RoundupStateRepository roundupStateRepository;
    @Autowired
    private RoundupAccountRepository roundupAccountRepository;

    @Test
    void shouldRetrieveRoundupState()
    {
        var result = roundupStateRepository.findByRoundupUidAndWeekEnd("a2191626-c67c-4a4b-aef9-3b1b80b65fdc", "2020-06-15T00:00:00.000+01:00");

        assertTrue(result.isPresent());
        assertEquals(result.get().getRoundupUid(), "a2191626-c67c-4a4b-aef9-3b1b80b65fdc");
        assertEquals(result.get().getTransferUid(), "b2191626-c67c-4a4b-aef9-3b1b80b65fdc");
        assertEquals(result.get().getState(), "TRANSFERRED");
        assertEquals(result.get().getWeekEnd(), "2020-06-15T00:00:00.000+01:00");
    }

    @Test
    void shouldInsertRoundupState()
    {
        roundupAccountRepository.save(ROUNDUP_UID,
                "accountUid",
                "categoryUid",
                "gbp",
                "savingsGoalUid",
                10,
                2);

        var weekEnd = "2020-06-15T00:00:00.000+01:00";
        roundupStateRepository.save(ROUNDUP_UID, "transferUid", "TRANSFERRED", weekEnd);

        var result = roundupStateRepository.findByRoundupUidAndWeekEnd(ROUNDUP_UID, weekEnd);

        assertTrue(result.isPresent());
        assertEquals(result.get().getRoundupUid(), ROUNDUP_UID);
        assertEquals(result.get().getTransferUid(), "transferUid");
        assertEquals(result.get().getState(), "TRANSFERRED");
        assertEquals(result.get().getWeekEnd(), weekEnd);
    }
}
