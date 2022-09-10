package org.nnn4eu.hfische.blablahelp.blablahelpbackend.userdata.web;

import lombok.RequiredArgsConstructor;
import org.nnn4eu.hfische.blablahelp.blablahelpbackend.config.UrlMapping;
import org.nnn4eu.hfische.blablahelp.blablahelpbackend.userdata.UserDataService;
import org.nnn4eu.hfische.blablahelp.blablahelpbackend.userdata.web.model.OfferPublicResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping(UrlMapping.PUBLIC)
public class PublicUserDataCtrl {
    private final UserDataService userDataService;

    @GetMapping(path = "/offers")
    public ResponseEntity<List<OfferPublicResponse>> getUserOffers() {
        List<OfferPublicResponse> offers = userDataService.findPublicOffersByIsExpired(false);
        return new ResponseEntity<>(offers, HttpStatus.OK);
    }


}
