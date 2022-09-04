package org.nnn4eu.hfische.blablahelp.blablahelpbackend.userdata;

import lombok.RequiredArgsConstructor;
import org.nnn4eu.hfische.blablahelp.blablahelpbackend.account.AccountService;
import org.nnn4eu.hfische.blablahelp.blablahelpbackend.shared.model.Address;
import org.nnn4eu.hfische.blablahelp.blablahelpbackend.shared.model.AddressWrap;
import org.nnn4eu.hfische.blablahelp.blablahelpbackend.shared.model.EAddressType;
import org.nnn4eu.hfische.blablahelp.blablahelpbackend.userdata.model.Offer;
import org.nnn4eu.hfische.blablahelp.blablahelpbackend.userdata.model.UserData;
import org.nnn4eu.hfische.blablahelp.blablahelpbackend.userdata.web.model.OfferResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.function.Predicate;
import java.util.stream.Collectors;

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

    public List<Offer> findOffersByIsExpired(boolean b) {
        List<Offer> offers=offerRepo.findByIsExpired(b);

        checkForExpired(offers,0);

        offers=offerRepo.saveAll(offers);
        return offers.stream().filter(a->b==a.isExpired())
                .sorted(Comparator.comparingLong(Offer::getTimeFrom))
                .collect(Collectors.toList());
    }

    public List<OfferResponse> findPublicOffersByIsExpired(boolean b){
        List<OfferResponse> offers = new ArrayList<>();
        findOffersByIsExpired(b).forEach(a->{
            String name=accountService.findAccountById(a.getAccountId()).getFirstname();
            UserData userData = findUserDataById(a.getAccountId());
            OfferResponse response=new OfferResponse(
                    name,
                    a.getShopAddress().city(),
                    a.getShopname(),
                    Instant.ofEpochMilli(a.getShoppingDay()),
                    userData.getMotto(),
                    userData.getShoppingCancellation(),
                    userData.getShoppingCount(),
                    userData.getShoppingRating()
            );
            offers.add(response);
        });
        return offers;
    }

    public void deleteOfferByOfferId(String accountId,String offerId) {
        Offer toDelete =offerRepo.findById(offerId).orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND));
        if(!toDelete.getAccountId().equals(accountId)) throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        if(toDelete.isBooked())throw new IllegalArgumentException("can't be deletead as there are associated bookings");
        offerRepo.delete(toDelete);
    }

    @Transactional
    public List<Offer> findOffersByAccountIdAndIsExpired(String accountId, boolean b) {
        List<Offer> offers=offerRepo.findByAccountIdAndIsExpired(accountId,b);
        checkForExpired(offers,0);
        offerRepo.saveAll(offers);
        deleteAllExpiredAndNotBooked(offers);

        Predicate<Offer> showEvenIfExpired=(a)->
                (!b)? !a.isExpired() || (a.isExpired() && a.isBooked() && (!a.isCanceled() || !a.isReviewed())):
                                        (a.isExpired() && a.isBooked()) && (a.isCanceled() || a.isReviewed());

        return offers.stream().filter(showEvenIfExpired::test)
                .sorted(Comparator.comparingLong(Offer::getTimeFrom))
                .collect(Collectors.toList());
    }

    private void deleteAllExpiredAndNotBooked(List<Offer> offers) {
        List<Offer> offersToDelete=offers.stream()
                .filter(a->a.isExpired() && !a.isBooked())
                .collect(Collectors.toList());
        offerRepo.deleteAll(offersToDelete);
    }

    private void checkForExpired(List<Offer> offers, int daysInBetween) {
        offers.forEach(a->{
            Instant now = Instant.now();//TODO think about expiration.. should it be a day with .truncatedTo(ChronoUnit.DAYS)?
            Instant day = Instant.ofEpochMilli(a.getTimeFrom());
            if(Duration.between(now,day).toDays()< daysInBetween){
                a.setExpired(true);
            }
        });
    }


}
