package org.nnn4eu.hfische.blablahelp.blablahelpbackend.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.nnn4eu.hfische.blablahelp.blablahelpbackend.account.Account;
import org.nnn4eu.hfische.blablahelp.blablahelpbackend.account.AccountService;
import org.nnn4eu.hfische.blablahelp.blablahelpbackend.account.ERole;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Slf4j
@Component
@RequiredArgsConstructor
@Profile("dev | local")
public class LocalRunner implements ApplicationRunner {
    private final AccountService accountService;
    private final PasswordEncoder passwordEncoder;

    @Value("${spring.data.mongodb.uri}")
    private String mongoUri;

    @Value("${spring.data.mongodb.database}")
    private String databaseName;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        log.info("Runner in db=====================================" + accountService.count());
        List<Account> accounts = testMongoDb();
        log.info("Runner saved=====================================" + accounts.size());
        log.info("Runner in db=====================================" + accountService.count());
    }


    private List<Account> testMongoDb() {
        List<Account> accounts = new ArrayList<>();

        accounts.add(
                accountService.findAccountByUsername("frank@").orElseGet(() ->
                        accountService.save(
                                new Account(
                                        passwordEncoder.encode("frank123"),
                                        "frank@", "frank",
                                        Set.of(ERole.ADMIN),
                                        true
                                )))
        );
        accounts.add(
                accountService.findAccountByUsername("anna@").orElseGet(() ->
                        accountService.save(
                                new Account(
                                        passwordEncoder.encode("anna123"),
                                        "anna@", "anna",
                                        Set.of(ERole.BASIC),
                                        true
                                )))
        );

        accounts.add(
                accountService.findAccountByUsername("annafrank@").orElseGet(() ->
                        accountService.save(
                                new Account(
                                        passwordEncoder.encode("annafrank"),
                                        "annafrank@", "annafrank",
                                        Set.of(ERole.ADMIN, ERole.BASIC),
                                        true
                                )))
        );
        return accounts;
    }

}

