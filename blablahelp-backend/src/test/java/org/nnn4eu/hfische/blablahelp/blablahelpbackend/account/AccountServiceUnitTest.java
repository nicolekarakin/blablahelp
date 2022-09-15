package org.nnn4eu.hfische.blablahelp.blablahelpbackend.account;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.nnn4eu.hfische.blablahelp.blablahelpbackend.userdata.web.CreateData;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@Slf4j
class AccountServiceUnitTest {
    private final AccountRepo accountRepo = mock(AccountRepo.class);
    private final AccountService accountService = new AccountService(accountRepo);

    @Test
    void findAccountById() {
        Account expected = new Account("pass1234", "Rosy@");
        String id = expected.getId();
        when(accountRepo.findById(id)).thenReturn(Optional.of(expected));
        Account actual = accountService.findAccountById(id);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void findAccountById_throw() {
        String id = UUID.randomUUID().toString();
        final Exception generalEx = new ResponseStatusException(HttpStatus.NOT_FOUND, "Account with id: " + id + " not found");
        when(accountRepo.findById(id)).thenReturn(Optional.empty());
        Throwable exception = Assertions.assertThrows(ResponseStatusException.class, () -> {
            throw generalEx;
        });
        Assertions.assertEquals("404 NOT_FOUND \"Account with id: " + id + " not found\"", exception.getMessage());
        Assertions.assertThrowsExactly(ResponseStatusException.class, () -> accountService.findAccountById(id));
    }

    @Test
    void findAccountByEmail() {
        Account expected = new Account("pass1234", "Rosy@");
        List<Account> expectedList = List.of(expected);
        String username = expected.getEmail();
        when(accountRepo.findByEmail(username)).thenReturn(expectedList);
        Optional<Account> actual = accountService.findAccountByEmail(username);
        Assertions.assertEquals(Optional.of(expected), actual);
    }

    @Test
    void findAccountByEmail_throw() {
        Account expected1 = new Account("pass1234", "Rosy@");
        Account expected2 = new Account("pass1234", "Rosy@");
        List<Account> expectedList = List.of(expected1, expected2);
        String username = expected1.getEmail();
        when(accountRepo.findByEmail(username)).thenReturn(expectedList);

        String ids = expected1.getId() + ", " + expected2.getId();

        Throwable exception = Assertions.assertThrows(IllegalStateException.class, () -> {
            throw new IllegalStateException("username should be unique, but query returned: " + ids);
        });
        Assertions.assertEquals("username should be unique, but query returned: " + ids, exception.getMessage());
        Assertions.assertThrowsExactly(ResponseStatusException.class, () -> accountService.findAccountById(username));


    }

    @Test
    void save() {
        Account expected = new Account("pass1234", "Rosy@");
        when(accountRepo.save(expected)).thenReturn(expected);
        Account actual = accountService.saveNew(expected);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void count() {
        Long expected = 0L;
        when(accountRepo.count()).thenReturn(expected);
        Long actual = accountService.count();
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void getBasicAccounts() {
        Account expected1 = CreateData.createAccount();
        Account expected2 = CreateData.createAccount();
        List<Account> expectedList = List.of(expected1, expected2);
        when(accountRepo.findByAuthoritiesIn(anyList())).thenReturn(expectedList);

        List<Account> actual = accountService.getBasicAccounts(Set.of(ERole.BASIC));
        Assertions.assertEquals(expectedList, actual);
    }
}
