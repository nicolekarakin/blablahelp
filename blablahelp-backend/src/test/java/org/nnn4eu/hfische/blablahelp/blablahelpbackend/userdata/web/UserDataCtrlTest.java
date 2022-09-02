package org.nnn4eu.hfische.blablahelp.blablahelpbackend.userdata.web;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.nnn4eu.hfische.blablahelp.blablahelpbackend.account.Account;
import org.nnn4eu.hfische.blablahelp.blablahelpbackend.account.AccountService;
import org.nnn4eu.hfische.blablahelp.blablahelpbackend.account.ERole;
import org.nnn4eu.hfische.blablahelp.blablahelpbackend.config.UrlMapping;
import org.nnn4eu.hfische.blablahelp.blablahelpbackend.shared.model.AddressWrap;
import org.nnn4eu.hfische.blablahelp.blablahelpbackend.shared.model.EAddressType;
import org.nnn4eu.hfische.blablahelp.blablahelpbackend.userdata.UserDataService;
import org.nnn4eu.hfische.blablahelp.blablahelpbackend.userdata.model.Offer;
import org.nnn4eu.hfische.blablahelp.blablahelpbackend.userdata.model.UserData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class UserDataCtrlTest {

    private Account account=createAccount();
    private final String id= "97a6c939-0919-44f5-99b3-1df1543c7427";
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    UserDataService userDataService;
    @Autowired
    AccountService accountService;

    @WithMockUser(authorities = {"BASIC"})
    @DirtiesContext
    @Test
    void getUserData_throw() throws Exception {
        UserData userData=new UserData(id);
        mockMvc.perform(
                        get(UrlMapping.USERDATA+"/"+id))
                .andExpect(status().isConflict());
    }


    @WithMockUser(authorities = {"BASIC"})
    @DirtiesContext
    @Test
    void getUserData() throws Exception {
        String accountId= account.getId();
        accountService.saveNew(account);
        UserData userData=new UserData(accountId);
        userData.setVersion(0L);
        mockMvc.perform(
                get(UrlMapping.USERDATA+"/"+accountId))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(userData)));

        Offer offer=createOffer(accountId);
        offer=createNewOfferTest(offer);

        AddressWrap wrap=new AddressWrap(EAddressType.PRIVATE, offer.getDestinationAddress());

        MvcResult result=  mockMvc.perform(
                        get(UrlMapping.USERDATA+"/"+accountId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.usedAddresses").isArray())
                .andExpect(jsonPath("$.usedAddresses", hasSize(1)))
                .andReturn();
        String actualStr = result.getResponse().getContentAsString(StandardCharsets.UTF_8);
        UserData actual=objectMapper.readValue(actualStr,UserData.class);
        assertThat(actual.getUsedAddresses().toArray()[0]).isEqualTo(wrap);

        getOffersTest(offer);
    }

    private void getOffersTest(Offer offer) throws Exception {
        String accountId=offer.getAccountId();
        MvcResult result=  mockMvc.perform(
                        get(UrlMapping.USERDATA+"/"+accountId+"/offers"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$", hasSize(1)))
                .andReturn();

        String actualStr = result.getResponse().getContentAsString(StandardCharsets.UTF_8);
        List<Offer> actual=objectMapper.readValue(actualStr, new TypeReference<>() {
            @Override
            public Type getType() {
                return super.getType();
            }
        });

        assertThat(actual.get(0)).isEqualTo(offer);
    }
    private Account createAccount() {
        return new Account(
                new BCryptPasswordEncoder().encode("blafr22"),
                "frank@gmail.de", "frank", "Berlin",
                Set.of(ERole.BASIC),
                true
        );
    }

    Offer createNewOfferTest(Offer offer) throws Exception {
        String accountId=offer.getAccountId();

        MvcResult mvcResult = mockMvc.perform(
                        post(UrlMapping.USERDATA+"/"+accountId+"/newOffer")
                                .contentType(MediaType.APPLICATION_JSON).characterEncoding(StandardCharsets.UTF_8)
                                .accept(MediaType.APPLICATION_JSON).characterEncoding(StandardCharsets.UTF_8)
                                .content(objectMapper.writeValueAsString(offer))
                                .with(user(account))
                                .with(csrf())
                )
                .andExpect(status().isCreated())
                .andReturn();
        String actualStr = mvcResult.getResponse().getContentAsString(StandardCharsets.UTF_8);
        Offer actual = objectMapper.readValue(actualStr, Offer.class);

        assertThat(actual.getDestinationAddress()).isEqualTo(offer.getDestinationAddress());
        assertThat(actual.getOfferId()).isNotNull();
        return actual;
    }

    private Offer createOffer(String accountId) throws JsonProcessingException {
        String str=
        """
        {
                "accountId":"%s",
                "shoppingDay":1661896800000,
                "timeFrom":1661911200000,
                "timeTo":1661926500000,
                "city":"münchen",
                "shopname":"LIDL",
                "shopAddress":{
                "street":"Balanstraße 188",
                    "zip":"81549",
                    "city":"München",
                    "loc":null
        },
            "destinationAddress":{
                "city":"München",
                 "country":"DE",
                 "street":"Kaspar-Spät-Straße 20 A",
                 "zip":"81549",
                 "type":"PRIVATE"
        },
            "maxMitshoppers":1,
                "maxDrinks":1,
                "maxArticles":10,
                "maxDistanceKm":1,
                "priceOffer":"0"
        }
        """;
        String jsonString=str.formatted(accountId);
        return objectMapper.readValue(jsonString,Offer.class);
    }
}
