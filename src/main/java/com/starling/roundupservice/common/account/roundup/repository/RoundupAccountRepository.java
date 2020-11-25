package com.starling.roundupservice.common.account.roundup.repository;

import java.util.Optional;

import com.starling.roundupservice.common.account.roundup.domain.RoundupAccountMapping;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface RoundupAccountRepository extends CrudRepository<RoundupAccountMapping, Integer>
{
    @Query("select * from dbo.roundup_account where account_uid = :accountUid")
    Optional<RoundupAccountMapping> findById(@Param("accountUid") String accountUid);
}