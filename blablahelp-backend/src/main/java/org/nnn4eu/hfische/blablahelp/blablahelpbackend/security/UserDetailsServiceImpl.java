package org.nnn4eu.hfische.blablahelp.blablahelpbackend.security;

import lombok.RequiredArgsConstructor;
import org.nnn4eu.hfische.blablahelp.blablahelpbackend.account.Account;
import org.nnn4eu.hfische.blablahelp.blablahelpbackend.account.AccountService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
class UserDetailsServiceImpl implements UserDetailsService {

    private final AccountService accountService;

    @Override
    public UserDetails loadUserByUsername(String name) throws UsernameNotFoundException {
        return accountService.findAccountByEmail(name)
                .orElseThrow(()->new UsernameNotFoundException("No user with name: " + name));

    }

}
