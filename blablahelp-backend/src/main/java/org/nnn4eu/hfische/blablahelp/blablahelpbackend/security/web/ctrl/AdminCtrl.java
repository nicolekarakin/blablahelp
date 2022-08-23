package org.nnn4eu.hfische.blablahelp.blablahelpbackend.security.web.ctrl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.nnn4eu.hfische.blablahelp.blablahelpbackend.account.Account;
import org.nnn4eu.hfische.blablahelp.blablahelpbackend.account.AccountService;
import org.nnn4eu.hfische.blablahelp.blablahelpbackend.account.ERole;
import org.nnn4eu.hfische.blablahelp.blablahelpbackend.config.UrlMapping;
import org.nnn4eu.hfische.blablahelp.blablahelpbackend.shared.model.Address;
import org.nnn4eu.hfische.blablahelp.blablahelpbackend.shop.ShopList;
import org.nnn4eu.hfische.blablahelp.blablahelpbackend.shop.ShopService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import java.io.IOException;
import java.util.List;
import java.util.Set;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(UrlMapping.ADMIN)
public class AdminCtrl {

    private final AccountService accountService;
    private final ShopService shopService;

    @GetMapping
    public ResponseEntity<String> getAdminHome() {
        return new ResponseEntity<>("You are in admin home!", HttpStatus.OK);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/accounts")
    public ResponseEntity<List<Account>> getBasicAccounts() {
        return ResponseEntity.ok(accountService.getBasicAccounts(Set.of(ERole.BASIC)));
    }

    @PostMapping(value = "/uploadJson")
    public ResponseEntity<Void> uploadFile(@RequestParam("mfile") MultipartFile mFile,
                                           @RequestParam("name") @NotBlank String name) {

        if (processFile(mFile, name)) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    boolean processFile(MultipartFile file, String name) {
        if (!file.isEmpty()) {
            byte[] bytes = new byte[0];
            try {
                bytes = file.getBytes();
                ObjectMapper objectMapper = new ObjectMapper();
                String json = new String(bytes);
                log.debug("json received:\n{}", json);
                Set<Address> addresses = objectMapper.readValue(json, new TypeReference<>() {
                });
                ShopList shopList = shopService.addAddresses(name, addresses);
                log.debug("ShopList with id {} has {} addresses", name, shopList.getAddresses().size());
                return true;
            } catch (IOException e) {
                log.error(e.getMessage());
                return false;
            }

        } else {
            log.debug("You upload an empty file");
            return false;
        }
    }

}
