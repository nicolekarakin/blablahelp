package org.nnn4eu.hfische.blablahelp.blablahelpbackend.userdata;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.nnn4eu.hfische.blablahelp.blablahelpbackend.account.Account;
import org.nnn4eu.hfische.blablahelp.blablahelpbackend.account.AccountService;
import org.nnn4eu.hfische.blablahelp.blablahelpbackend.geo.GeoService;
import org.nnn4eu.hfische.blablahelp.blablahelpbackend.userdata.model.MitshopperInquiry;
import org.nnn4eu.hfische.blablahelp.blablahelpbackend.userdata.model.Offer;
import org.nnn4eu.hfische.blablahelp.blablahelpbackend.userdata.model.UserData;
import org.nnn4eu.hfische.blablahelp.blablahelpbackend.userdata.web.CreateData;
import org.nnn4eu.hfische.blablahelp.blablahelpbackend.userdata.web.model.CreateInquiryResponse;
import org.nnn4eu.hfische.blablahelp.blablahelpbackend.userdata.web.model.MitshopperInquiryRecord;

import java.util.*;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class UserDataServiceUnitTest {

    @Mock
    private UserDataRepo userDataRepo;
    @Mock
    private OfferRepo offerRepo;
    @Mock
    private AccountService accountService;
    @Mock
    private GeoService geoService;

    @InjectMocks
    private UserDataService userDataService;

    @Test
    void findInquiriesByAccountIdAndIsExpired() throws JsonProcessingException {
        Account account = CreateData.createAccount();
        String accountId = account.getId();

        Offer offer1 = CreateData.createOffer(accountId);
        offer1.setOfferId(UUID.randomUUID().toString());
        Offer offer2 = CreateData.createOffer(accountId);
        offer2.setOfferId(UUID.randomUUID().toString());
        offer2.setExpired(true);
        Offer offer3 = CreateData.createOffer(accountId);
        offer3.setOfferId(UUID.randomUUID().toString());

        String mitshopperAccountId = UUID.randomUUID().toString();
        MitshopperInquiryRecord inquiryRecord = CreateData.createMitshopperInquiryRecord(
                offer3.getOfferId(), "MitshopperName", mitshopperAccountId, "Title");
        MitshopperInquiry inquiry = MitshopperInquiry.from(inquiryRecord);

        offer3.addInquiry(inquiry);
        Set<Offer> offers = new HashSet<>(Arrays.asList(offer3));

        when(accountService.accountExistsById(anyString())).thenReturn(true);
        List<Offer> offersNotExpired = new ArrayList<>(Arrays.asList(offer1, offer3));

        when(accountService.findAccountById(anyString())).thenReturn(account);
        UserData userData = CreateData.createUserData(accountId, account.getFirstname());
        when(userDataRepo.findById(accountId)).thenReturn(Optional.of(userData));

        when(offerRepo.findAllByInquiries_MitshopperAccountIdEquals(anyString()))
                .thenReturn(offers);

        List<CreateInquiryResponse> result =
                userDataService.findInquiriesByAccountIdAndIsExpired(mitshopperAccountId, false);

        String id0 = result.get(0).getInquiry().mitshopperAccountId();
        Assertions.assertEquals(id0, mitshopperAccountId);
        String id1 = result.get(0).getInquiry().offerId();
        Assertions.assertEquals(id1, offer3.getOfferId());
        String id3 = result.get(0).getOffer().offerId();
        Assertions.assertEquals(id3, offer3.getOfferId());
    }
}