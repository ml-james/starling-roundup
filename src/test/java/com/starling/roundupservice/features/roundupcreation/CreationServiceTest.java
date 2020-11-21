package com.starling.roundupservice.features.roundupcreation;

import static com.starling.roundupservice.TestConstants.DEFAULT_ACCOUNT_UID;
import static com.starling.roundupservice.TestConstants.DEFAULT_CATEGORY_UID;
import static com.starling.roundupservice.TestConstants.DEFAULT_CURRENCY;
import static com.starling.roundupservice.TestConstants.DEFAULT_SAVINGS_GOAL_UID;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

import com.starling.roundupservice.common.account.roundup.RoundupAccountMapping;
import com.starling.roundupservice.common.account.roundup.RoundupAccountService;
import com.starling.roundupservice.common.exception.ClientException;
import com.starling.roundupservice.common.savingsgoal.create.SavingsGoalCreationResponse;
import com.starling.roundupservice.common.savingsgoal.create.SavingsGoalCreationService;
import com.starling.roundupservice.creation.RoundupCreationRequest;

import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

public class CreationServiceTest
{

    @Mock
    private SavingsGoalCreationService savingsGoalCreationService;
    @Mock
    private RoundupAccountService roundupAccountService;

    private String result;
    private RoundupCreationRequest creationRequest;
    private CreationService creationService;

    @Before
    public void setup()
    {
        initMocks(this);
        creationService = new CreationService(savingsGoalCreationService, roundupAccountService);
    }

    @Test
    public void savingsGoalCreatedSuccessfully()
    {
        givenRoundupAccountEmptyOptional();
        givenSavingsGoalCreated();
        whenCreateRoundupGoalCalled();
        thenSavingsGoalUidReturned();
    }

    @Test(expected = ClientException.class)
    public void savingsGoalAlreadyExists()
    {
        givenRoundupAccount();
        whenCreateRoundupGoalCalled();
    }

    private void givenRoundupAccountEmptyOptional()
    {
        when(roundupAccountService.retrieveRoundupAccount(DEFAULT_ACCOUNT_UID)).thenReturn(Optional.empty());
    }

    private void givenSavingsGoalCreated()
    {
        when(savingsGoalCreationService.createSavingsGoal(creationRequest, DEFAULT_CURRENCY)).thenReturn(SavingsGoalCreationResponse.builder().savingsGoalUid(DEFAULT_SAVINGS_GOAL_UID).build());
    }

    private void givenRoundupAccount()
    {
        when(roundupAccountService.retrieveRoundupAccount(DEFAULT_ACCOUNT_UID)).thenReturn(Optional.of(RoundupAccountMapping.builder().build()));
    }

    private void whenCreateRoundupGoalCalled()
    {
        result = creationService.createRoundupGoal(creationRequest, DEFAULT_ACCOUNT_UID, DEFAULT_CATEGORY_UID, DEFAULT_CURRENCY);
    }

    private void thenSavingsGoalUidReturned()
    {
        assertEquals(DEFAULT_SAVINGS_GOAL_UID, result);
    }
}
