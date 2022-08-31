package org.nnn4eu.hfische.blablahelp.blablahelpbackend.userdata.web;

import lombok.RequiredArgsConstructor;
import org.nnn4eu.hfische.blablahelp.blablahelpbackend.config.UrlMapping;
import org.nnn4eu.hfische.blablahelp.blablahelpbackend.userdata.UserDataService;
import org.nnn4eu.hfische.blablahelp.blablahelpbackend.userdata.model.Offer;
import org.nnn4eu.hfische.blablahelp.blablahelpbackend.userdata.model.UserData;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping(UrlMapping.USERDATA)
public class UserDataCtrl {
    private final UserDataService userDataService;

    @GetMapping(path = "/{accountId}")
    public ResponseEntity<UserData> getUserData(@NotBlank @PathVariable String accountId) {
        UserData userData=userDataService.findUserDataById(accountId);
        return  new ResponseEntity<>(userData, HttpStatus.OK);
    }

    @GetMapping(path = "/{accountId}/offers")
    public ResponseEntity<List<Offer>> getUserOffers(@NotBlank @PathVariable String accountId) {
        List<Offer> offers=userDataService.findOffersByAccountId(accountId);
        return  new ResponseEntity<>(offers, HttpStatus.OK);
    }

    @PostMapping(path = "/{accountId}/newOffer")
    public ResponseEntity<Offer> createNewOffer(@NotBlank @PathVariable String accountId,
                                                @NotNull @RequestBody @Valid Offer newOffer
    ) {
        if(!newOffer.getAccountId().equals(accountId))
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        Offer offer=userDataService.saveNewOffer(newOffer);
        return  new ResponseEntity<>(offer, HttpStatus.CREATED);
    }
}
