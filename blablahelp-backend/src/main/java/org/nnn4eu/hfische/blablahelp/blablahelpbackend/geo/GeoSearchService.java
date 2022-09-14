package org.nnn4eu.hfische.blablahelp.blablahelpbackend.geo;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.nnn4eu.hfische.blablahelp.blablahelpbackend.shared.model.AddressWrap;
import org.nnn4eu.hfische.blablahelp.blablahelpbackend.shared.model.EAddressType;
import org.nnn4eu.hfische.blablahelp.blablahelpbackend.userdata.UserDataRepo;
import org.nnn4eu.hfische.blablahelp.blablahelpbackend.userdata.model.MitshopperInquiry;
import org.nnn4eu.hfische.blablahelp.blablahelpbackend.userdata.model.Offer;
import org.nnn4eu.hfische.blablahelp.blablahelpbackend.userdata.model.UserData;
import org.nnn4eu.hfische.blablahelp.blablahelpbackend.userdata.web.model.OfferSearchRequest;
import org.nnn4eu.hfische.blablahelp.blablahelpbackend.userdata.web.model.SearchOfferResponse;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Slf4j
@Service
public class GeoSearchService {

    private final MongoTemplate mongoTemplate;
    private final GeoService geoService;
    private final UserDataRepo userDataRepo;


    public Criteria findMatchingNotExpiredOffersCriteria(GeoJsonPoint p) {
        Criteria criteria = new Criteria();
        return criteria.and("mpolygon").intersects(p)
                .and("isExpired").is(false)
                .and("isFullyBooked").is(false);
    }

    public List<Offer> findMatchingNotExpiredOffers(GeoJsonPoint p) {
        Query query = new Query();
        query.with(Sort.by(Sort.Direction.DESC, "timeFrom"));
        Criteria criteria = findMatchingNotExpiredOffersCriteria(p);
        query.addCriteria(criteria);
        return mongoTemplate.find(query, Offer.class);
    }

    public List<Offer> findMatchingNotExpiredOffers(GeoJsonPoint p, String accountId) {
        Query query = new Query();
        query.with(Sort.by(Sort.Direction.ASC, "timeFrom"));
        Criteria criteria = findMatchingNotExpiredOffersCriteria(p);
        criteria.and("accountId").ne(accountId);
        query.addCriteria(criteria);
        return mongoTemplate.find(query, Offer.class);
    }

    public List<SearchOfferResponse> getMatchesForMitshopper(OfferSearchRequest request) {
        GeoJsonPoint point;
        if (request.address().getLoc() == null) {
            point = geoService.getCoordinatesForAddress(request.address());
            request.address().setLoc(point);
            AddressWrap addressWrap = new AddressWrap(EAddressType.PRIVATE, request.address());
            updateUsedAddressesUserData(request.accountId(), addressWrap, request.firstname());
        } else {
            point = request.address().getLoc();
        }

        List<Offer> offers = findMatchingNotExpiredOffers(point, request.accountId());
        //TODO(@nicolekarakin) update isExpired field like it is done in getOffers request
        List<Offer> filteredOffers = offers.stream().filter(a -> {
            List<String> ids = a.getInquiries().stream().map(MitshopperInquiry::getMitshopperAccountId).toList();
            return !ids.contains(request.accountId());
        }).toList();

        return filteredOffers.stream().map(a -> {
            UserData userData = userDataRepo.findById(a.getAccountId())
                    .orElseThrow(() -> new IllegalArgumentException("no account with this id found"));
            return SearchOfferResponse.from(a, userData);
        }).toList();
    }

    private void updateUsedAddressesUserData(String accountId, AddressWrap addressWrap, String firstname) {
        UserData userData = userDataRepo.findById(accountId)
                .orElseGet(() -> userDataRepo.save(new UserData(accountId, firstname)));
        userData.getUsedAddresses().add(addressWrap);
        userDataRepo.save(userData);
    }
}
