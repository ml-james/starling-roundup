package com.starling.roundupservice.common.account.roundup;

import static com.starling.roundupservice.TestConstants.DEFAULT_ROUNDUP_UID;
import static com.starling.roundupservice.TestConstants.DEFAULT_TRANSFER_UID;
import static com.starling.roundupservice.TestConstants.DEFAULT_WEEK_END;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import com.starling.roundupservice.action.State;

import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@RunWith(SpringRunner.class)
@SpringBootTest
public class RoundupAccountRepositoryTest
{

    private static final int ALTERNATIVE_ROUNDUP_UID = 2;

    @Autowired
    private RoundupStateRepository roundupStateRepository;

    private Optional<RoundupStateMapping> result;

    @Test
    public void retrieveFromRepository()
    {
        whenRetrieveFromRepository(DEFAULT_ROUNDUP_UID);
        thenResultPresent();
    }

    @Test
    public void retrieveFromRepositoryEmpty()
    {
        whenRetrieveFromRepository(ALTERNATIVE_ROUNDUP_UID);
        thenResultEmpty();
    }

    private void whenRetrieveFromRepository(int roundupUid)
    {
        result = roundupStateRepository.findByRoundupUidAndWeekEnd(roundupUid, DEFAULT_WEEK_END);
    }

    private void thenResultPresent()
    {
        assertTrue(result.isPresent());
        assertEquals(DEFAULT_ROUNDUP_UID, result.get().roundupUid);
        assertEquals(DEFAULT_WEEK_END, result.get().weekEnd);
        assertEquals(DEFAULT_TRANSFER_UID, result.get().transferUid);
        assertEquals(State.TRANSFERRED.toString(), result.get().state);
    }

    private void thenResultEmpty()
    {
        assertTrue(result.isEmpty());
    }
}
