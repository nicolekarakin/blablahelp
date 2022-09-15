package org.nnn4eu.hfische.blablahelp.blablahelpbackend.account;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.nnn4eu.hfische.blablahelp.blablahelpbackend.userdata.web.CreateData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

@Slf4j
@DataMongoTest
@Import(value = AccountService.class)
@ExtendWith(SpringExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class AccountServiceTest {
    @Autowired
    private AccountRepo accountRepo;
    @Autowired
    private AccountService accountService;

    @Test
    void findAccountById() {
        Account account = CreateData.createAccount();
        Account expected = accountRepo.save(account);
        Account actual = accountService.findAccountById(expected.getId());
        Assertions.assertEquals(expected.getId(), actual.getId());
    }

    @Test
    void findAccountByEmail() {
        Account account = CreateData.createAccount();
        Account expected = accountRepo.save(account);
        Optional<Account> actual = accountService.findAccountByEmail(expected.getEmail());
        Assertions.assertEquals(expected, actual.get());
    }

    @Test
    void saveNew() {
        Account account = CreateData.createAccount();
        Account actual = accountService.saveNew(account);
        Assertions.assertEquals(account, actual);
    }

    @Test
    void saveNew_throw() {
        Account account = CreateData.createAccount();
        accountService.saveNew(account);
        Account account2 = CreateData.createAccount();

        Throwable exception = Assertions.assertThrows(IllegalArgumentException.class,
                () -> accountService.saveNew(account2));
        Assertions.assertEquals("account with email: " + account.getEmail() + " already in db", exception.getMessage());

    }


}