package com.starling.roundupservice.common.account.retrieval;

import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Builder(toBuilder = true)
@RequiredArgsConstructor
@Getter
public class Accounts {

  private final List<Account> accounts;

}
