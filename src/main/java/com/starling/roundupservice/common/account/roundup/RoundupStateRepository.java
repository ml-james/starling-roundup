package com.starling.roundupservice.common.account.roundup;

import java.util.Optional;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface RoundupStateRepository extends CrudRepository<RoundupStateMapping, Integer>
{

    @Query("select * from dbo.roundup_state where roundup_uid = :roundupUid and week_end = :weekEnd")
    Optional<RoundupStateMapping> findByRoundupUidAndWeekEnd(@Param("roundupUid") int roundupUid,
                                                             @Param("weekEnd") String weekEnd);
}
