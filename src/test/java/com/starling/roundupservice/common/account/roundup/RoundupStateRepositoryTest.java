package com.starling.roundupservice.common.account.roundup;

import static com.starling.roundupservice.TestConstants.DEFAULT_ACCOUNT_UID;
import static com.starling.roundupservice.TestConstants.DEFAULT_CATEGORY_UID;
import static com.starling.roundupservice.TestConstants.DEFAULT_CURRENCY;
import static com.starling.roundupservice.TestConstants.DEFAULT_ROUNDUP_UID;
import static org.junit.jupiter.api.Assertions.*;

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
public class RoundupStateRepositoryTest {

  private static final String ALTERNATIVE_ACCOUNT_UID = "32423432";

  private Optional<RoundupAccountMapping> result;

  @Autowired
  private RoundupAccountRepository roundupAccountRepository;

  @Test
  public void retrieveRoundupAccount() {
    whenRetrieveAccountCalled(DEFAULT_ACCOUNT_UID);
    thenAccountReturned();
  }

  @Test
  public void retrieveMissingAccount() {
    whenRetrieveAccountCalled(ALTERNATIVE_ACCOUNT_UID);
    thenAccountEmpty();
  }
  private void whenRetrieveAccountCalled(String accountUid) {
    result = roundupAccountRepository.findById(accountUid);
  }

  private void thenAccountReturned() {
    assertTrue(result.isPresent());
    assertEquals(DEFAULT_ROUNDUP_UID, result.get().roundupUid);
    assertEquals(DEFAULT_ACCOUNT_UID, result.get().accountUid);
    assertEquals(DEFAULT_CATEGORY_UID, result.get().categoryUid);
    assertEquals(DEFAULT_CURRENCY, result.get().accountUidCurrency);
  }

  private void thenAccountEmpty() {
    assertTrue(result.isEmpty());
  }

}
