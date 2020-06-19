package com.starling.roundup.common.accounts.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Builder(toBuilder = true)
@RequiredArgsConstructor
@Getter
public class Account {

  private final String accountUid;
  private final String defaultCategory;
  private final String currency;
  private final String dateTime;
}
