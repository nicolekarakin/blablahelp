package org.nnn4eu.hfische.blablahelp.blablahelpbackend.userdata;

import org.nnn4eu.hfische.blablahelp.blablahelpbackend.userdata.model.Offer;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface OfferRepo extends MongoRepository<Offer, String> {

    List<Offer> findByAccountId(String accountId);

    List<Offer> findByIsExpired(boolean b);

    List<Offer> findByAccountIdAndIsExpired(String accountId, boolean b);
}
