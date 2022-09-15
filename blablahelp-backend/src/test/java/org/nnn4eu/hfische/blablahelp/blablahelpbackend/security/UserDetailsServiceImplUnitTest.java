package org.nnn4eu.hfische.blablahelp.blablahelpbackend.security;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.nnn4eu.hfische.blablahelp.blablahelpbackend.account.Account;
import org.nnn4eu.hfische.blablahelp.blablahelpbackend.account.AccountService;
import org.nnn4eu.hfische.blablahelp.blablahelpbackend.userdata.web.CreateData;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class UserDetailsServiceImplUnitTest {

    private final AccountService accountService = mock(AccountService.class);
    private final UserDetailsServiceImpl impl = new UserDetailsServiceImpl(accountService);

    @Test
    void testLoadUserByName() {
        Account account = CreateData.createAccount();
        Optional<Account> opt = Optional.of(account);
        when(accountService.findAccountByEmail(account.getEmail())).thenReturn(opt);
        UserDetails actual = impl.loadUserByUsername(account.getEmail());
        Assertions.assertEquals(account, actual);
    }
}