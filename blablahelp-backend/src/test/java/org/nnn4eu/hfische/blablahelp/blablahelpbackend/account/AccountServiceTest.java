package org.nnn4eu.hfische.blablahelp.blablahelpbackend.account;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class AccountServiceTest {
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
        when(accountRepo.findById(id)).thenThrow(generalEx);
        Throwable exception = Assertions.assertThrows(ResponseStatusException.class, () -> {
            throw generalEx;
        });
        Assertions.assertEquals("404 NOT_FOUND \"Account with id: " + id + " not found\"", exception.getMessage());
        Assertions.assertThrowsExactly(ResponseStatusException.class, () -> accountRepo.findById(id));
    }

    @Test
    void findAccountByUsername() {
        Account expected = new Account("pass1234", "Rosy@");
        List<Account> expectedList = List.of(expected);
        String username = expected.getUsername();
        when(accountRepo.findByUsername(username)).thenReturn(expectedList);
        Optional<Account> actual = accountService.findAccountByUsername(username);
        Assertions.assertEquals(Optional.of(expected), actual);
    }

    @Test
    void save() {
        Account expected = new Account("pass1234", "Rosy@");
        when(accountRepo.save(expected)).thenReturn(expected);
        Account actual = accountService.save(expected);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void count() {
        Long expected = 0L;
        when(accountRepo.count()).thenReturn(expected);
        Long actual = accountService.count();
        Assertions.assertEquals(expected, actual);
    }
}