package org.nnn4eu.hfische.blablahelp.blablahelpbackend.account;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class AccountService {
    private final AccountRepo accountRepo;

    public Account findAccountById(String id) {
        return accountRepo
                .findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Account with id: " + id + " not found"));
    }

    public Optional<Account> findAccountByEmail(String email) {
        List<Account> accounts = accountRepo.findByEmail(email);
        if (accounts.isEmpty()) return Optional.empty();
        else if (accounts.size() == 1) return Optional.of(accounts.get(0));
        else {
            String ids = accounts.stream().map(Account::getId).reduce("", (id, s) -> s + ", " + id);
            log.error("username should be unique, but query returned: " + ids);
            return Optional.empty();
        }
    }

    public Account save(Account account) {
        if (accountRepo.findById(account.getId()).isEmpty())
            return accountRepo.save(account);
        else throw new IllegalArgumentException("account with username: " + account.getEmail() + " already in db");
    }

    public Long count() {
        return accountRepo.count();
    }

    public List<Account> getBasicAccounts(Set<ERole> roles) {
        return accountRepo.findByAuthoritiesIn(getGranted(roles));
    }

    private List<SimpleGrantedAuthority> getGranted(Set<ERole> roles) {
        return roles.stream().map(a -> new SimpleGrantedAuthority(a.name()))
                .toList();
    }
}
