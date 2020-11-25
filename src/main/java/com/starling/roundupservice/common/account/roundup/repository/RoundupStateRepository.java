package com.starling.roundupservice.common.account.roundup.repository;

import java.util.Optional;

import com.starling.roundupservice.common.account.roundup.domain.RoundupStateMapping;
import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface RoundupStateRepository extends CrudRepository<RoundupStateMapping, Integer>
{
    @Query("select * from dbo.roundup_state where roundup_uid = :roundupUid and week_end = :weekEnd")
    Optional<RoundupStateMapping> findByRoundupUidAndWeekEnd(@Param("roundupUid") String roundupUid, @Param("weekEnd") String weekEnd);

    @Modifying
    @Query("insert into dbo.roundup_state values (:roundupUid, :transferUid, :state, :weekEnd)")
    void save(@Param("roundupUid") String roundupUid,
              @Param("transferUid") String transferUid,
              @Param("state") String state,
              @Param("weekEnd") String weekEnd);
}
