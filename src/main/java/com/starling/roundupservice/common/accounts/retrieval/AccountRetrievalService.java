package com.starling.roundupservice.common.accounts.retrieval;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AccountRetrievalService {

  private final AccountRetrievalProvider accountRetrievalProvider;

  public Account getAccountInformation(String accountUid) {

    var allAccounts = accountRetrievalProvider.retrieveAccounts();

    return allAccounts.getAccounts().stream()
        .filter(x -> x.getAccountUid().equals(accountUid))
        .findFirst()
        .orElseThrow();
  }
}
