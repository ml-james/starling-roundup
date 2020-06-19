package com.starling.roundupservice.common.accounts.retrieval;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Builder(toBuilder = true)
@RequiredArgsConstructor
@Getter
public class Account {

  private final String accountUid;
  private final String defaultCategoryUid;
  private final String currency;
  private final String dateTime;
}
