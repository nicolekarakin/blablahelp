package org.nnn4eu.hfische.blablahelp.blablahelpbackend.geo;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.nnn4eu.hfische.blablahelp.blablahelpbackend.account.Account;
import org.nnn4eu.hfische.blablahelp.blablahelpbackend.userdata.UserDataRepo;
import org.nnn4eu.hfische.blablahelp.blablahelpbackend.userdata.model.Offer;
import org.nnn4eu.hfische.blablahelp.blablahelpbackend.userdata.model.UserData;
import org.nnn4eu.hfische.blablahelp.blablahelpbackend.userdata.web.CreateData;
import org.nnn4eu.hfische.blablahelp.blablahelpbackend.userdata.web.model.OfferSearchRequest;
import org.nnn4eu.hfische.blablahelp.blablahelpbackend.userdata.web.model.SearchOfferResponse;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class GeoSearchServiceUnitTest {
    private final MongoTemplate mongoTemplate = mock(MongoTemplate.class);
    private final GeoService geoService = mock(GeoService.class);
    private final UserDataRepo userDataRepo = mock(UserDataRepo.class);
    private final GeoSearchService geoSearchService = new GeoSearchService(mongoTemplate,
            geoService, userDataRepo);

    @Test
    void getMatchesForMitshopper() throws JsonProcessingException {
        Account shopper1 = CreateData.createAccount();
        UserData userData1 = CreateData.createUserData(shopper1.getId(), shopper1.getFirstname());
        Account shopper2 = CreateData.createAccount();
        UserData userData2 = CreateData.createUserData(shopper2.getId(), shopper2.getFirstname());
        Account account = CreateData.createAccount();
        List<SearchOfferResponse> expected = new ArrayList<>();
        OfferSearchRequest input = new OfferSearchRequest(account.getId(),
                CreateData.createAddressWithLocSouth(), account.getFirstname());

        Offer offer1 = CreateData.createOffer(shopper1.getId());
        offer1.setOfferId(UUID.randomUUID().toString());
        Offer offer2 = CreateData.createOffer(shopper2.getId());
        offer2.setOfferId(UUID.randomUUID().toString());

        SearchOfferResponse res1 = CreateData
                .createSearchOfferResponse(offer1, userData1);
        SearchOfferResponse res2 = CreateData
                .createSearchOfferResponse(offer2, userData2);

        when(mongoTemplate.find(any(Query.class), eq(Offer.class))).thenReturn(List.of(offer1, offer2));
        when(userDataRepo.findById(shopper1.getId())).thenReturn(Optional.of(userData1));
        when(userDataRepo.findById(shopper2.getId())).thenReturn(Optional.of(userData2));

        List<SearchOfferResponse> actual = geoSearchService.getMatchesForMitshopper(input);

        Assertions.assertEquals(List.of(res1, res2), actual);

    }
}