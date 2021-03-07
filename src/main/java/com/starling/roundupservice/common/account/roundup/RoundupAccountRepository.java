package com.starling.roundupservice.common.account.roundup;

import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface RoundupAccountRepository extends CrudRepository<RoundupAccountMapping, Integer>
{
    @Query("select * from dbo.roundup_account where account_uid = :accountUid")
    Optional<RoundupAccountMapping> findById(@Param("accountUid") String accountUid);

    @Modifying
    @Query("insert into dbo.roundup_account values (:roundupUid, :accountUid, :categoryUid, :accountUidCurrency, :savingsGoalUid, :roundupGoal, :maximumRoundup, :roundupFactor)")
    void save(@Param("roundupUid") String roundupUid,
              @Param("accountUid") String accountUid,
              @Param("categoryUid") String categoryUid,
              @Param("accountUidCurrency") String accountUidCurrency,
              @Param("savingsGoalUid") String savingsGoalUid,
              @Param("roundupGoal") int roundupGoal,
              @Param("maximumRoundup") int maximumRoundUp,
              @Param("roundupFactor") int roundupFactor);

    @Modifying
    @Query("update dbo.roundup_account set roundup_goal = :roundupGoal, maximum_roundup= :maximumRoundup, roundup_factor = :roundupFactor where account_uid = :accountUid")
    void updateRoundupAccount(@Param("accountUid") String accountUid,
                              @Param("roundupGoal") int roundupGoal,
                              @Param("maximumRoundup") int maximumRoundup,
                              @Param("roundupFactor") int roundupFactor);
}
