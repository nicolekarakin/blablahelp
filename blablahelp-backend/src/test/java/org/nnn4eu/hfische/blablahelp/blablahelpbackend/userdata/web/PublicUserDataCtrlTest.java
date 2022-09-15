package org.nnn4eu.hfische.blablahelp.blablahelpbackend.userdata.web;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.nnn4eu.hfische.blablahelp.blablahelpbackend.account.Account;
import org.nnn4eu.hfische.blablahelp.blablahelpbackend.account.AccountService;
import org.nnn4eu.hfische.blablahelp.blablahelpbackend.config.UrlMapping;
import org.nnn4eu.hfische.blablahelp.blablahelpbackend.geo.GeoService;
import org.nnn4eu.hfische.blablahelp.blablahelpbackend.shared.model.Address;
import org.nnn4eu.hfische.blablahelp.blablahelpbackend.userdata.UserDataService;
import org.nnn4eu.hfische.blablahelp.blablahelpbackend.userdata.model.Offer;
import org.nnn4eu.hfische.blablahelp.blablahelpbackend.userdata.model.UserData;
import org.nnn4eu.hfische.blablahelp.blablahelpbackend.userdata.web.model.OfferPublicResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class PublicUserDataCtrlTest {
    private final Account
            account = CreateData.createAccount();
    @Autowired
    AccountService accountService;
    @Autowired
    UserDataService userDataService;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    GeoService geoService;

    @DirtiesContext
    @Test
    void getUserOffers() throws Exception {
        String accountId = account.getId();
        accountService.saveNew(account);
        UserData userData = userDataService.findUserDataById(accountId);

        GeoJsonPoint point = new GeoJsonPoint(11.53559, 48.121563);
        when(geoService.getCoordinatesForAddress(any(Address.class))).thenReturn(point);

        Offer offer1 = CreateData.createOffer(accountId);
        offer1 = userDataService.saveNewOffer(offer1);

        Offer offer2 = CreateData.createOffer(accountId);
        offer2 = CreateData.changeDate(offer2, LocalDate.now().plusDays(20));
        offer2.setShopname("my shop");
        offer2 = userDataService.saveNewOffer(offer2);

        MvcResult result = mockMvc.perform(
                        get(UrlMapping.PUBLIC + "/offers"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$", hasSize(2)))
                .andReturn();

        String actualStr = result.getResponse().getContentAsString(StandardCharsets.UTF_8);
        List<OfferPublicResponse> actual = objectMapper.readValue(actualStr, new TypeReference<>() {
        });

        assertThat(actual.get(0).firstname()).isEqualTo(account.getFirstname());
        assertThat(actual.get(1).shopname()).isEqualTo(offer2.getShopname()).isEqualTo("my shop");
        assertThat(actual.get(1).shopCity()).isEqualTo(offer2.getShopAddress().getCity());
        assertThat(actual.get(0).shoppingCount()).isEqualTo(userData.getShoppingCount());
        assertThat(actual.get(0).shoppingRating()).isEqualTo(userData.getShoppingRating());

        instantTest();

        Long offer2Day = offer2.getShoppingDay();
        Instant offerResponse=Instant.ofEpochMilli(offer2Day);
        Long offerResponseLong=offerResponse.toEpochMilli();
        assertThat(offerResponseLong).isEqualTo(offer2Day);
        assertThat(actual.get(1).shoppingDay().toEpochMilli()).isEqualTo(offer2Day);

        Offer offer3=CreateData.createOffer(accountId);
        Long day = LocalDate.of(2021,10,10).atStartOfDay().toInstant(ZoneOffset.UTC).toEpochMilli();
        Long from = LocalDate.of(2021,10,10).atTime(16,30).toInstant(ZoneOffset.UTC).toEpochMilli();
        Long to = LocalDate.of(2021,10,10).atTime(20,30).toInstant(ZoneOffset.UTC).toEpochMilli();
        offer3.setShoppingDay(day);
        offer3.setTimeFrom(from);
        offer3.setTimeTo(to);
        offer3=userDataService.saveNewOffer(offer3);
        oldOfferSetToExpiredAndFound(offer3.getOfferId());
    }

    public void instantTest(){
        Instant now= Instant.now();
        Long nowL=now.toEpochMilli();
        Instant nowLI=Instant.ofEpochMilli(nowL);
        assertThat(nowLI.toEpochMilli()).isEqualTo(nowL);
        assertThat(now).isNotEqualTo(nowLI);
    }
    public void oldOfferSetToExpiredAndFound(String offerId){

        List<Offer> oldOffers=userDataService.findOffersByIsExpired(false);
        assertThat(oldOffers).hasSize(2);
        assertThat(oldOffers.get(0).getOfferId()).isNotEqualTo(offerId);
        assertThat(oldOffers.get(1).getOfferId()).isNotEqualTo(offerId);
    }
}
