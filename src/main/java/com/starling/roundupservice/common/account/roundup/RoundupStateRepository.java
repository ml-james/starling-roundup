package com.starling.roundupservice.common.account.roundup;

import java.util.Optional;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface RoundupStateRepository extends CrudRepository<RoundupStateMapping, Integer> {

  @Query("select top 1 from dbo.roundup_state where roundup_uid = :roundupUid")
  Optional<RoundupStateMapping> findById(@Param("roundup_uid") String roundupUid);

  @Override
  <S extends RoundupStateMapping> S save(S entity);
}
