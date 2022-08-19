package org.nnn4eu.hfische.blablahelp.blablahelpbackend.account;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.time.Instant;
import java.util.List;

public interface AccountRepo extends MongoRepository<Account, String> {
    List<Account> findByUsername(String username);

    @Query("{username:'?0'}")
    Account findUserByUsername(String username);

    @Query(value = "{}", fields = "{username : 1}")
    List<Account> findUsernameAndId();

    @Query(value = "{}", fields = "{_id : 0}")
    List<Account> findUsernameAndFirstnameExcludeId();

    @Query("{ 'username' : ?0 }")
    List<Account> findAccountsByUsername(String username);

    @Query("{ 'createdAt' : { $gt: ?0, $lt: ?1 } }")
    List<Account> findAccountsByCreatedAtBetween(Instant ageGT, Instant ageLT);

    @Query("{ 'username' : { $regex: ?0 } }")
    List<Account> findAccountsByRegexpFirstname(String regexp);

    List<Account> findByFirstname(String firstname);


}
