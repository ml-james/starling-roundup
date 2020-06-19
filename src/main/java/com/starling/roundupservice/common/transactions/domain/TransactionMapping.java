package com.starling.roundupservice.common.transactions.domain;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Builder
@Data
@Table("dbo.roundup_mapping")
public class TransactionMapping {

  @Id
  String roundup_uid;
  int roundUpValue;
  String weekEnd;

}
