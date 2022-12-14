package org.nnn4eu.hfische.blablahelp.blablahelpbackend.userdata.web;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.nnn4eu.hfische.blablahelp.blablahelpbackend.account.Account;
import org.nnn4eu.hfische.blablahelp.blablahelpbackend.account.AccountService;
import org.nnn4eu.hfische.blablahelp.blablahelpbackend.config.UrlMapping;
import org.nnn4eu.hfische.blablahelp.blablahelpbackend.geo.GeoService;
import org.nnn4eu.hfische.blablahelp.blablahelpbackend.shared.model.Address;
import org.nnn4eu.hfische.blablahelp.blablahelpbackend.shared.model.AddressWrap;
import org.nnn4eu.hfische.blablahelp.blablahelpbackend.shared.model.EAddressType;
import org.nnn4eu.hfische.blablahelp.blablahelpbackend.userdata.UserDataService;
import org.nnn4eu.hfische.blablahelp.blablahelpbackend.userdata.model.Offer;
import org.nnn4eu.hfische.blablahelp.blablahelpbackend.userdata.model.UserData;
import org.nnn4eu.hfische.blablahelp.blablahelpbackend.userdata.web.model.CreateInquiryResponse;
import org.nnn4eu.hfische.blablahelp.blablahelpbackend.userdata.web.model.MitshopperInquiryRecord;
import org.nnn4eu.hfische.blablahelp.blablahelpbackend.userdata.web.model.OfferSearchRequest;
import org.nnn4eu.hfische.blablahelp.blablahelpbackend.userdata.web.model.SearchOfferResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class UserDataCtrlTest {

    private final Account account = CreateData.createAccount();
    private final String id = "97a6c939-0919-44f5-99b3-1df1543c7427";
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    UserDataService userDataService;
    @Autowired
    AccountService accountService;
    @MockBean
    GeoService geoService;

    @WithMockUser(authorities = {"BASIC"})
    @DirtiesContext
    @Test
    void getUserData_throw() throws Exception {
        UserData userData = new UserData(id, "whoever");
        mockMvc.perform(
                        get(UrlMapping.USERDATA + "/" + id))
                .andExpect(status().isConflict());
    }


    @WithMockUser(authorities = {"BASIC"})
    @DirtiesContext
    @Test
    void getUserData() throws Exception {
        String accountId = account.getId();
        accountService.saveNew(account);
        UserData userData = new UserData(accountId, account.getFirstname());
        userData.setVersion(0L);
        mockMvc.perform(
                        get(UrlMapping.USERDATA + "/" + accountId))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(userData)));

        Offer offer = CreateData.createOffer(accountId);
        offer = createNewOfferTest(offer);

        AddressWrap wrap = new AddressWrap(EAddressType.PRIVATE, offer.getDestinationAddress());

        MvcResult result = mockMvc.perform(
                        get(UrlMapping.USERDATA + "/" + accountId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.usedAddresses").isArray())
                .andExpect(jsonPath("$.usedAddresses", hasSize(1)))
                .andReturn();
        String actualStr = result.getResponse().getContentAsString(StandardCharsets.UTF_8);
        UserData actual = objectMapper.readValue(actualStr, UserData.class);
        AddressWrap actualWrap = (AddressWrap) actual.getUsedAddresses().toArray()[0];
        assertThat(actualWrap.address().getStreet()).isEqualTo(wrap.address().getStreet());

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

    Offer createNewOfferTest(Offer offer) throws Exception {
        String accountId = offer.getAccountId();
        GeoJsonPoint point = new GeoJsonPoint(11.53559, 48.121563);
        when(geoService.getCoordinatesForAddress(any(Address.class))).thenReturn(point);

        MvcResult mvcResult = mockMvc.perform(
                        post(UrlMapping.USERDATA + "/" + accountId + "/newOffer")
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

        assertThat(actual.getDestinationAddress().getStreet()).isEqualTo(offer.getDestinationAddress().getStreet());
        assertThat(actual.getOfferId()).isNotNull();
        return actual;
    }


    @WithMockUser(authorities = {"BASIC"})
    @DirtiesContext
    @Test
    void deleteOffer() throws Exception {
        String accountId= account.getId();
        accountService.saveNew(account);
        Offer offer=CreateData.createOffer(accountId);
        offer.setAccountId(accountId);
        MvcResult mvcResult = mockMvc.perform(
                        post(UrlMapping.USERDATA+"/"+accountId+"/newOffer")
                                .contentType(MediaType.APPLICATION_JSON).characterEncoding(StandardCharsets.UTF_8)
                                .accept(MediaType.APPLICATION_JSON).characterEncoding(StandardCharsets.UTF_8)
                                .content(objectMapper.writeValueAsString(offer))
                                .with(user(account))
                                .with(csrf())
                )
                .andExpect(status().isCreated()).andReturn();

        String actualStr = mvcResult.getResponse().getContentAsString(StandardCharsets.UTF_8);
        Offer actual = objectMapper.readValue(actualStr, Offer.class);

        mockMvc.perform(
                        delete(UrlMapping.USERDATA+"/"+accountId+"/offers/"+actual.getOfferId())
                                .with(csrf()))
                .andExpect(status().isNoContent());

        mockMvc.perform(
                        get(UrlMapping.USERDATA + "/" + accountId + "/offers"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$", hasSize(0)));

    }

    @WithMockUser(authorities = {"BASIC"})
    @DirtiesContext
    @Test
    void createInquiry() throws Exception {
        String accountId = account.getId();
        accountService.saveNew(account);

        Offer offer = CreateData.createOffer(accountId);
        offer = createNewOfferTest(offer);

        String mitshopperId = UUID.randomUUID().toString();
        MitshopperInquiryRecord inquiryRecord = CreateData.createMitshopperInquiryRecord(offer.getOfferId(),
                "MitshopperName", mitshopperId, "My favourite shopping list");


        MvcResult mvcResult = mockMvc.perform(
                        post(UrlMapping.USERDATA + "/createInquiry")
                                .contentType(MediaType.APPLICATION_JSON).characterEncoding(StandardCharsets.UTF_8)
                                .accept(MediaType.APPLICATION_JSON).characterEncoding(StandardCharsets.UTF_8)
                                .content(objectMapper.writeValueAsString(inquiryRecord))
                                .with(user(account))
                                .with(csrf())
                )
                .andExpect(status().isCreated()).andReturn();

        String actualStr = mvcResult.getResponse().getContentAsString(StandardCharsets.UTF_8);
        CreateInquiryResponse actual = objectMapper.readValue(actualStr, CreateInquiryResponse.class);

        Assertions.assertEquals(offer.getOfferId(), actual.getOffer().offerId());
        Assertions.assertEquals("My favourite shopping list", actual.getInquiry().shoppingList().getTitle());

        MitshopperInquiryRecord inquiry = actual.getInquiry();
        Assertions.assertEquals(offer.getOfferId(), inquiry.offerId());
        Assertions.assertEquals(mitshopperId, inquiry.mitshopperAccountId());
        Assertions.assertEquals(inquiryRecord.shoppingList().getProducts().size(),
                inquiry.shoppingList().getProducts().size());
    }

    @WithMockUser(authorities = {"BASIC"})
    @DirtiesContext
    @Test
    void searchOffers() throws Exception {

        OfferSearchRequest offerSearchRequest = new OfferSearchRequest(UUID.randomUUID().toString(),
                CreateData.createAddressWithLocNoth(), "Arni");

        MvcResult mvcResult = mockMvc.perform(
                        post(UrlMapping.USERDATA + "/search")
                                .contentType(MediaType.APPLICATION_JSON).characterEncoding(StandardCharsets.UTF_8)

                                .accept(MediaType.APPLICATION_JSON).characterEncoding(StandardCharsets.UTF_8)
                                .content(objectMapper.writeValueAsString(offerSearchRequest))
                                .with(user(account))
                                .with(csrf())
                )
                .andExpect(status().isOk()).andReturn();

        String actualStr = mvcResult.getResponse().getContentAsString(StandardCharsets.UTF_8);
        List<SearchOfferResponse> actual = objectMapper.readValue(actualStr, new TypeReference<>() {
        });
        Assertions.assertEquals(0, actual.size());
    }

    @WithMockUser(authorities = {"BASIC"})
    @DirtiesContext
    @Test
    void deleteInquiry() throws Exception {
        mockMvc.perform(
                        delete(UrlMapping.USERDATA + "/" + UUID.randomUUID().toString()
                                + "/inquiries/" + UUID.randomUUID().toString())
                                .with(csrf()))
                .andExpect(status().isNotFound());

    }

    @WithMockUser(authorities = {"BASIC"})
    @DirtiesContext
    @Test
    void getUserInquiries() throws Exception {
        String accountId = UUID.randomUUID().toString();
        mockMvc.perform(
                        get(UrlMapping.USERDATA + "/" + accountId + "/inquiries"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$", hasSize(0)))
        ;
    }
}

