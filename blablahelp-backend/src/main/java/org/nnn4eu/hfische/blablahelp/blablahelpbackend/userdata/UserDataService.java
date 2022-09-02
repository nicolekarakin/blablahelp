package org.nnn4eu.hfische.blablahelp.blablahelpbackend.userdata;

import lombok.RequiredArgsConstructor;
import org.nnn4eu.hfische.blablahelp.blablahelpbackend.account.AccountService;
import org.nnn4eu.hfische.blablahelp.blablahelpbackend.shared.model.Address;
import org.nnn4eu.hfische.blablahelp.blablahelpbackend.shared.model.AddressWrap;
import org.nnn4eu.hfische.blablahelp.blablahelpbackend.shared.model.EAddressType;
import org.nnn4eu.hfische.blablahelp.blablahelpbackend.userdata.model.Offer;
import org.nnn4eu.hfische.blablahelp.blablahelpbackend.userdata.model.UserData;
import org.springframework.stereotype.Service;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserDataService {
    private final UserDataRepo userDataRepo;
    private final OfferRepo offerRepo;
    private final AccountService accountService;

    public UserData findUserDataById(@NotBlank String accountId){
        boolean ok=accountService.accountExistsById(accountId);
        if(!ok)throw new IllegalArgumentException("account with given id doesn't exist");
        return userDataRepo.findById(accountId).orElseGet(()->userDataRepo.save(new UserData(accountId)));
    }

    public AddressWrap addNewUsedAddress(Address address, String accountId){
        UserData userData=findUserDataById(accountId);
        AddressWrap destination=new AddressWrap(EAddressType.PRIVATE,address);
        userData.getUsedAddresses().add(destination);
        userDataRepo.save(userData);
        return destination;
    }
    public Offer saveNewOffer(@NotBlank @Valid Offer newOffer) {
        newOffer.setOfferId(UUID.randomUUID().toString());
        addNewUsedAddress(newOffer.getDestinationAddress(), newOffer.getAccountId());
        return offerRepo.save(newOffer);
    }

    public List<Offer> findOffersByAccountId(String accountId) {
        return offerRepo.findByAccountId(accountId);
    }
}
