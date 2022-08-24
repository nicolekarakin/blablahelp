package org.nnn4eu.hfische.blablahelp.blablahelpbackend.account;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.time.Instant;
import java.util.List;

public interface AccountRepo extends MongoRepository<Account, String> {
    List<Account> findByEmail(String email);

    List<Account> findByAuthoritiesIn(List<SimpleGrantedAuthority> granted);
}
