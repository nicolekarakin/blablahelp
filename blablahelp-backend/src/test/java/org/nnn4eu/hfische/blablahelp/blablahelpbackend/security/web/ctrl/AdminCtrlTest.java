package org.nnn4eu.hfische.blablahelp.blablahelpbackend.security.web.ctrl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.nnn4eu.hfische.blablahelp.blablahelpbackend.account.AccountService;
import org.nnn4eu.hfische.blablahelp.blablahelpbackend.shared.model.Address;
import org.nnn4eu.hfische.blablahelp.blablahelpbackend.shop.ShopList;
import org.nnn4eu.hfische.blablahelp.blablahelpbackend.shop.ShopService;
import org.springframework.mock.web.MockMultipartFile;

import java.util.Set;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class AdminCtrlTest {

    private final AccountService accountService = mock(AccountService.class);
    private final ShopService shopService = mock(ShopService.class);

    AdminCtrl adminCtrl = new AdminCtrl(accountService, shopService);
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void processFile_exeption() {
        String json = "[{\nstreet\": \"Merkurstraße 24\"";
        String name = "en-DE_name";
        MockMultipartFile jsonFile = new MockMultipartFile("myjson", "", "application/json",
                json.getBytes());
        boolean ok = adminCtrl.processFile(jsonFile, name);
        Assertions.assertFalse(ok);

    }

    @Test
    void processFile() throws JsonProcessingException {
        String json = "[{\"street\": \"Merkurstraße 24\"}]";
        String name = "en-DE_name";
        MockMultipartFile jsonFile = new MockMultipartFile("myjson", "", "application/json",
                json.getBytes());
        Set<Address> addresses = objectMapper.readValue(json, new TypeReference<>() {
        });
        ShopList shopList = new ShopList(name, addresses);
        when(shopService.addAddresses(name, addresses)).thenReturn(shopList);

        boolean ok = adminCtrl.processFile(jsonFile, name);
        Assertions.assertTrue(ok);
    }
}