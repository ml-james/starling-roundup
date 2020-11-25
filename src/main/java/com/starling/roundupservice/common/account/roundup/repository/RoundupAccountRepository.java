package com.starling.roundupservice.common.account.roundup.repository;

import java.util.Optional;

import com.starling.roundupservice.common.account.roundup.domain.RoundupAccountMapping;
import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface RoundupAccountRepository extends CrudRepository<RoundupAccountMapping, Integer>
{
    @Query("select * from dbo.roundup_account where account_uid = :accountUid")
    Optional<RoundupAccountMapping> findById(@Param("accountUid") String accountUid);

    @Modifying
    @Query("insert into dbo.roundup_account values (:roundupUid, :accountUid, :categoryUid, :accountUidCurrency, :savingsGoalUid, :maximumRoundup, :roundupFactor)")
    void save(@Param("roundupUid") String roundupUid,
              @Param("accountUid") String accountUid,
              @Param("categoryUid") String categoryUid,
              @Param("accountUidCurrency") String accountUidCurrency,
              @Param("savingsGoalUid") String savingsGoalUid,
              @Param("maximumRoundup") int maximumRoundUp,
              @Param("roundupFactor") int roundupFactor);
}
