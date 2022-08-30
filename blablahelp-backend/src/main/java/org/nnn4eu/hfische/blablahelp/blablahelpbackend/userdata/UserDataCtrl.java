package org.nnn4eu.hfische.blablahelp.blablahelpbackend.userdata;

import lombok.RequiredArgsConstructor;
import org.nnn4eu.hfische.blablahelp.blablahelpbackend.config.UrlMapping;
import org.nnn4eu.hfische.blablahelp.blablahelpbackend.shop.ShopService;
import org.nnn4eu.hfische.blablahelp.blablahelpbackend.shop.web.model.ShopNameList;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotBlank;

@RequiredArgsConstructor
@RestController
@RequestMapping(UrlMapping.USERDATA)
public class UserDataCtrl {
    private final UserDataRepo userDataRepo;

    @GetMapping(path = "/{accountId}")
    public ResponseEntity<UserData> getUserData(@NotBlank @PathVariable String accountId) {
        UserData userData=userDataRepo.findById(accountId).orElse(new UserData(accountId));
        return  new ResponseEntity<>(userData, HttpStatus.OK);
    }
}
