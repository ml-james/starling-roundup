package com.starling.roundupservice.common.transactions;

import com.starling.roundupservice.common.transactions.domain.TransactionMapping;
import java.util.Optional;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface TransactionRepository extends CrudRepository<TransactionMapping, Integer> {

  @Query("select * from dbo.roundup_value where roundup_uid = :roundupUid and week_end = :weekEnd")
  Optional<TransactionMapping> findTransactionRoundup(@Param("roundupUid") String roundupUid, @Param("weekEnd") String weekEnd);

}
