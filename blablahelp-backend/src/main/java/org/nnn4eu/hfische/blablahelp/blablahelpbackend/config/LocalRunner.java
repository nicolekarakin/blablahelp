package org.nnn4eu.hfische.blablahelp.blablahelpbackend.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.nnn4eu.hfische.blablahelp.blablahelpbackend.account.Account;
import org.nnn4eu.hfische.blablahelp.blablahelpbackend.account.AccountService;
import org.nnn4eu.hfische.blablahelp.blablahelpbackend.account.ERole;
import org.nnn4eu.hfische.blablahelp.blablahelpbackend.product.DummyProductCreator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Slf4j
@Component
@RequiredArgsConstructor
@Profile("dev | local")
public class LocalRunner implements ApplicationRunner {
    private final AccountService accountService;
    private final PasswordEncoder passwordEncoder;

    private final DummyProductCreator dummyProductCreator;

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

        dummyProductCreator.createProducts();
        Optional<Account> accountOpt = accountService.findAccountByEmail("anna@gmail.de");
        if (accountOpt.isPresent()) {
            String annaId = accountOpt.get().getId();
            dummyProductCreator.addDummyShoppingList("Gesundesessen", annaId);
            dummyProductCreator.addDummyShoppingList("Lieblingsessen", annaId);
            dummyProductCreator.addDummyShoppingList("Grundnahrungsmittel", annaId);
        }
    }


    private List<Account> testMongoDb() {
        List<Account> accounts = new ArrayList<>();

        final String email1 = "frank@gmail.de";
        accounts.add(
                accountService.findAccountByEmail(email1).orElseGet(() ->
                        accountService.saveNew(
                                new Account(
                                        passwordEncoder.encode("blafr22"),
                                        email1, "frank", "Berlin",
                                        Set.of(ERole.ADMIN),
                                        true
                                )))
        );

        final String email2 = "anna@gmail.de";
        accounts.add(
                accountService.findAccountByEmail(email2).orElseGet(() ->
                        accountService.saveNew(
                                new Account(
                                        passwordEncoder.encode("blaan22"),
                                        email2, "anna", "M??nchen",
                                        Set.of(ERole.BASIC),
                                        true
                                )))
        );

        final String email3 = "annafrank@gmail.de";
        accounts.add(
                accountService.findAccountByEmail(email3).orElseGet(() ->
                        accountService.saveNew(
                                new Account(
                                        passwordEncoder.encode("annafrank"),
                                        email3, "annafrank", "Hulm",
                                        Set.of(ERole.ADMIN, ERole.BASIC),
                                        true
                                )))
        );

        return accounts;
    }

}

