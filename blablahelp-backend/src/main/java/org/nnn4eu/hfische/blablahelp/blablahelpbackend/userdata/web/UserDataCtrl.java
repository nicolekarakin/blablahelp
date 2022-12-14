package org.nnn4eu.hfische.blablahelp.blablahelpbackend.userdata.web;

import lombok.RequiredArgsConstructor;
import org.nnn4eu.hfische.blablahelp.blablahelpbackend.config.UrlMapping;
import org.nnn4eu.hfische.blablahelp.blablahelpbackend.geo.GeoSearchService;
import org.nnn4eu.hfische.blablahelp.blablahelpbackend.userdata.UserDataService;
import org.nnn4eu.hfische.blablahelp.blablahelpbackend.userdata.model.Offer;
import org.nnn4eu.hfische.blablahelp.blablahelpbackend.userdata.model.UserData;
import org.nnn4eu.hfische.blablahelp.blablahelpbackend.userdata.web.model.CreateInquiryResponse;
import org.nnn4eu.hfische.blablahelp.blablahelpbackend.userdata.web.model.MitshopperInquiryRecord;
import org.nnn4eu.hfische.blablahelp.blablahelpbackend.userdata.web.model.OfferSearchRequest;
import org.nnn4eu.hfische.blablahelp.blablahelpbackend.userdata.web.model.SearchOfferResponse;
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
    private final GeoSearchService geoSearchService;

    @GetMapping(path = "/{accountId}")
    public ResponseEntity<UserData> getUserData(@NotBlank @PathVariable String accountId) {
        UserData userData = userDataService.findUserDataById(accountId);
        return new ResponseEntity<>(userData, HttpStatus.OK);
    }

    @GetMapping(path = "/{accountId}/offers")
    public ResponseEntity<List<Offer>> getUserOffers(@NotBlank @PathVariable String accountId) {
        List<Offer> offers = userDataService.findOffersByAccountIdAndIsExpired(accountId, false);
        return new ResponseEntity<>(offers, HttpStatus.OK);
    }

    @PostMapping(path = "/{accountId}/newOffer")
    public ResponseEntity<Offer> createNewOffer(@NotBlank @PathVariable String accountId,
                                                @NotNull @RequestBody @Valid Offer newOffer
    ) {
        if (!newOffer.getAccountId().equals(accountId))
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        Offer offer = userDataService.saveNewOffer(newOffer);
        return new ResponseEntity<>(offer, HttpStatus.CREATED);
    }

    @DeleteMapping(path = "/{accountId}/offers/{offerId}")
    public ResponseEntity<List<Offer>> deleteUserOffer(@NotBlank @PathVariable String accountId,
                                                       @NotBlank @PathVariable String offerId) {

        userDataService.deleteOfferByOfferId(accountId, offerId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping(path = "/search")
    public ResponseEntity<List<SearchOfferResponse>> searchOffers(@NotBlank @RequestBody OfferSearchRequest request) {
        List<SearchOfferResponse> offers = geoSearchService.getMatchesForMitshopper(request);
        return new ResponseEntity<>(offers, HttpStatus.OK);
    }

    @PostMapping(path = "/createInquiry")
    public ResponseEntity<CreateInquiryResponse> createInquiry(@NotBlank @RequestBody MitshopperInquiryRecord inquiry) {
        CreateInquiryResponse createInquiryResponse = userDataService.createMitshopperInquiry(inquiry);
        return new ResponseEntity<>(createInquiryResponse, HttpStatus.CREATED);
    }

    @DeleteMapping(path = "/{mitshopperAccountId}/inquiries/{offerId}")
    public ResponseEntity<CreateInquiryResponse> deleteInquiry(@NotBlank @PathVariable String mitshopperAccountId,
                                                               @NotBlank @PathVariable String offerId) {
        userDataService.deleteMitshopperInquiry(mitshopperAccountId, offerId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @GetMapping(path = "/{accountId}/inquiries")
    public ResponseEntity<List<CreateInquiryResponse>> getUserInquiries(@NotBlank @PathVariable String accountId) {
        List<CreateInquiryResponse> inquiries = userDataService.findInquiriesByAccountIdAndIsExpired(accountId, false);
        return new ResponseEntity<>(inquiries, HttpStatus.OK);
    }
}
