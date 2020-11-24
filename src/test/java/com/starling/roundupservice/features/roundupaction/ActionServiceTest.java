package com.starling.roundupservice.features.roundupaction;

import static com.starling.roundupservice.TestConstants.DEFAULT_ACCOUNT_UID;
import static com.starling.roundupservice.TestConstants.DEFAULT_ROUNDUP;
import static com.starling.roundupservice.TestConstants.DEFAULT_ROUNDUP_UID;
import static com.starling.roundupservice.TestConstants.DEFAULT_TRANSFER_UID;
import static com.starling.roundupservice.TestConstants.DEFAULT_WEEK_END;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

import com.starling.roundupservice.action.RoundupActionResponse;
import com.starling.roundupservice.action.State;
import com.starling.roundupservice.common.account.fundconfirmation.FundConfirmationService;
import com.starling.roundupservice.common.account.roundup.RoundupAccountMapping;
import com.starling.roundupservice.common.account.roundup.RoundupAccountService;
import com.starling.roundupservice.common.account.roundup.RoundupStateService;
import com.starling.roundupservice.common.exception.ClientException;
import com.starling.roundupservice.common.savingsgoal.deposit.SavingsGoalDepositService;
import com.starling.roundupservice.common.transaction.Roundup;
import com.starling.roundupservice.common.transaction.TransactionService;

import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

public class ActionServiceTest
{

    @Mock
    private RoundupAccountService roundupAccountService;
    @Mock
    private TransactionService transactionService;
    @Mock
    private FundConfirmationService fundConfirmationService;
    @Mock
    private SavingsGoalDepositService savingsGoalDepositService;
    @Mock
    private RoundupStateService roundupStateService;

    private RoundupActionResponse result;
    private ActionService actionService;

    @Before
    public void setup()
    {
        initMocks(this);
        actionService = new ActionService(roundupAccountService, transactionService, fundConfirmationService, savingsGoalDepositService, roundupStateService);
    }

    @Test(expected = ClientException.class)
    public void roundupAccountNotFound()
    {
        givenAccountDoesNotExist();
        whenPerformRoundupCalled();
    }

    @Test
    public void roundupTransferred()
    {
        givenAccountExists();
        givenTransactionServiceReturnsRoundup();
        givenSufficientFunds();
        givenSavingsGoalDepositSuccessful();
        whenPerformRoundupCalled();
        thenSufficientFunds();
    }

    @Test
    public void roundupInsufficientFunds()
    {
        givenAccountExists();
        givenTransactionServiceReturnsRoundup();
        givenInsufficientFunds();
        whenPerformRoundupCalled();
        thenInsufficientFunds();
    }

    private void givenAccountExists()
    {
        when(roundupAccountService.retrieveRoundupAccount(anyString())).thenReturn(
                Optional.of(RoundupAccountMapping.builder().roundupUid(DEFAULT_ROUNDUP_UID).build()));
    }

    private void givenAccountDoesNotExist()
    {
        when(roundupAccountService.retrieveRoundupAccount(anyString())).thenReturn(
                Optional.empty());
    }

    private void givenTransactionServiceReturnsRoundup()
    {
        when(transactionService.getLatestRoundup(any(), anyString())).thenReturn(Roundup.builder().roundupAmount(DEFAULT_ROUNDUP).weekEnd(DEFAULT_WEEK_END).build());
    }

    private void givenSufficientFunds()
    {
        when(fundConfirmationService.sufficientFunds(anyString(), anyInt(), "")).thenReturn(true);
    }

    private void givenInsufficientFunds()
    {
        when(fundConfirmationService.sufficientFunds(anyString(), anyInt(), "")).thenReturn(false);
    }

    private void givenSavingsGoalDepositSuccessful()
    {
        when(savingsGoalDepositService.deposit(any(), anyInt(), "")).thenReturn(DEFAULT_TRANSFER_UID);
    }

    private void whenPerformRoundupCalled()
    {
        result = actionService.performRoundup(DEFAULT_ACCOUNT_UID, "");
    }

    private void thenSufficientFunds()
    {
        assertEquals(State.TRANSFERRED, result.getRoundUpState());
    }

    private void thenInsufficientFunds()
    {
        assertEquals(State.INSUFFICIENT_FUNDS, result.getRoundUpState());
    }
}
