package org.nnn4eu.hfische.blablahelp.blablahelpbackend.userdata;

import org.nnn4eu.hfische.blablahelp.blablahelpbackend.userdata.model.Offer;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OfferRepo extends MongoRepository<Offer, String> {

    List<Offer> findByAccountId(String accountId);

    List<Offer> findByIsExpired(boolean b);

    List<Offer> findByAccountIdAndIsExpired(String accountId, boolean b);

    @Query(value = "{'shopAddress.loc' :{'$geoWithin': { '$centerSphere' : [ [ ?0, ?1], ?2 ] }}}")
    List<Offer> findByShopAddrWithinCircle(@Param("lng") Double lng, @Param("lat") Double lat, @Param("dist") Double dist);
}
